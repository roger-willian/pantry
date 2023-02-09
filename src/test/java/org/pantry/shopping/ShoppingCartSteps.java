package org.pantry.shopping;

import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.pantry.shopping.controllers.shoppingcart.ShoppingCartController;
import org.pantry.shopping.controllers.shoppingcart.requests.DelFromCartRequest;
import org.pantry.shopping.controllers.shoppingcart.requests.FetchToCartRequest;
import org.pantry.shopping.controllers.shoppingcart.requests.ReturnFromCartRequest;
import org.pantry.shopping.controllers.shoppingcart.requests.ViewCartRequest;
import org.pantry.shopping.controllers.shoppingcart.responses.CartItemResponse;
import org.pantry.shopping.controllers.shoppingcart.responses.DelFromCartResponse;
import org.pantry.shopping.controllers.shoppingcart.responses.FetchToCartResponse;
import org.pantry.shopping.controllers.shoppingcart.responses.ReturnFromCartResponse;
import org.pantry.shopping.controllers.shoppinglist.responses.FetchFromListResponse;
import org.pantry.shopping.entities.CartItem;

import java.text.SimpleDateFormat;
import java.util.*;

public class ShoppingCartSteps {
    private final VolatileCartGateway cart;
    private final ScenarioContext context;
    private final ShoppingCartController controller;

    public ShoppingCartSteps(ScenarioContext context) {
        this.context = context;
        cart = context.getCart();
        controller = context.getCartController();
    }

    private Integer fromDate(String uiDate) {
        String dbDate;
        try {
            SimpleDateFormat uiFormat = new SimpleDateFormat("dd/MM/yyyy");
            uiFormat.setLenient(false);
            Date date = uiFormat.parse(uiDate);
            dbDate = CartItem.dateFormat.format(date);
        } catch (Exception e) {
            dbDate = uiDate.substring(6) + uiDate.substring(3,5) + uiDate.substring(0,2);
        }
        Integer expiration = Integer.parseInt(dbDate);
        return expiration;
    }

    private Integer fromPrice(Double uiPrice) {
        Integer pricePerUnit = (int) (uiPrice * 100);
        return pricePerUnit;
    }

    @DataTableType
    public CartItemResponse cartItemResponseTransformer(Map<String, String> entry) {
        Long id = Long.parseLong(entry.get("id"));
        Double qty = Double.parseDouble(entry.get("qty"));
        String unit = entry.get("unit");
        String product = entry.get("product");
        Integer price = (int) (Double.parseDouble(entry.get("price")) * 100);
        String date = entry.get("expiration");
        Integer expiration = fromDate(date);
        return new CartItemResponse(id, qty, unit, product, price, expiration);
    }

    @DataTableType
    public CartItem cartItemTransformer(Map<String, String> entry) {
        Long id = Long.parseLong(entry.get("id"));
        Double qty = Double.parseDouble(entry.get("qty"));
        String unit = entry.get("unit");
        String product = entry.get("product");
        Integer price = (int) (Double.parseDouble(entry.get("price")) * 100);
        String date = entry.get("expiration");
        Integer expiration = fromDate(date);
        return new CartItem(id, qty, unit, product, price, expiration);
    }

    @Given("I have the following items in my shopping cart:")
    public void iHaveTheFollowingItemsInMyShoppingCart(List<CartItem> items) {
        items.forEach(cart::addWithId);
    }

    @When("I return {double} units of the item with id {long} from the shopping cart to the shopping list")
    public void iReturnUnitsOfTheItemWithIdFromTheShoppingCartToTheShoppingList(Double qty, Long id) {
        ReturnFromCartRequest request = new ReturnFromCartRequest(id, qty);
        controller.returnFromCart(request, response -> context.lastReturnFromCartResponse = response);
    }

    @When("I look at my shopping cart")
    public void iLookAtMyShoppingCart() {
        ViewCartRequest request = new ViewCartRequest();
        controller.viewCart(request, response -> context.lastViewCartResponse = response);
    }

    @When("I fetch {double} {string} of {string} to my shopping cart, costing $ {double} per unit and expiring on {string}")
    public void iFetchOfToMyShoppingCartCostingPerUnitAndExpiringOn(Double qty, String unit, String product, Double price, String date) {
        Integer pricePerUnit = fromPrice(price);
        Integer expiration = fromDate(date);
        FetchToCartRequest request = new FetchToCartRequest(qty, unit, product, pricePerUnit, expiration);
        controller.fetchToCart(request, response -> context.lastFetchToCartResponse = response);
    }

    @When("I remove {double} units of the item with id {long} from the shopping cart")
    public void iRemoveUnitsOfTheItemWithIdFromTheShoppingCart(Double qty, Long id) {
        DelFromCartRequest request = new DelFromCartRequest(id, qty);
        controller.delFromCart(request, response -> context.lastDelFromCartResponse = response);
    }

    @Then("I should see exactly {int} items in my shopping cart, including:")
    public void iShouldSeeExactlyItemsInMyShoppingCartIncluding(int size, List<CartItemResponse> items) {
        Assertions.assertEquals(size, context.lastViewCartResponse.items().size());
        Assertions.assertTrue(context.lastViewCartResponse.items().containsAll(items));
    }

    @Then("my shopping cart should have exactly {int} items, including:")
    public void myShoppingCartShouldHaveExactlyItemsIncluding(Integer size, List<CartItem> items) {
        Assertions.assertEquals(size, cart.findAll().size());
        Assertions.assertTrue(cart.findAll().containsAll(items));
    }

    @Then("my shopping cart should have {double} {string} of {string}, costing $ {double} per unit and expiring on {string}")
    public void myShoppingCartShouldHaveOfCosting$PerUnitAndExpiringOn(Double qty, String unit, String product, Double price, String date) {
        Integer pricePerUnit = fromPrice(price);
        Integer expiration = fromDate(date);
        boolean found = cart.findAll().stream().anyMatch(it -> {
            if (!Objects.equals(it.quantity(), qty)) return false;
            if (!Objects.equals(it.unit(), unit)) return false;
            if (!Objects.equals(it.name(), product)) return false;
            if (!Objects.equals(it.pricePerUnit(), pricePerUnit)) return false;
            if (!Objects.equals(it.expiration(), expiration)) return false;

            return true;
        });
        Assertions.assertTrue(found);
    }

    @Then("my shopping cart should not have an item with id {long}")
    public void myShoppingCartShouldNotHaveAnItemWithIdLong(Long id) {
        Assertions.assertTrue(cart.findById(id).isEmpty());
    }

    @Then("the last Fetch from List response should be {string}")
    public void theLastFetchFromListResponseShouldBe(String response) {
        Map<String, FetchFromListResponse.StatusCode> expected = new HashMap<>();
        expected.put("OK_ALL", FetchFromListResponse.StatusCode.OK_ALL);
        expected.put("OK_SOME", FetchFromListResponse.StatusCode.OK_SOME);
        expected.put("NOT_FOUND", FetchFromListResponse.StatusCode.NOT_FOUND);
        expected.put("ERROR", FetchFromListResponse.StatusCode.ERROR);
        expected.put("INVALID", FetchFromListResponse.StatusCode.INVALID);
        Assertions.assertEquals(expected.get(response), context.lastFetchFromListResponse.status());
    }

    @Then("the last Fetch to Cart response should be {string}")
    public void theLastFetchToCartResponseShouldBe(String response) {
        Map<String, FetchToCartResponse.StatusCode> expected = new HashMap<>();
        expected.put("OK_ALL", FetchToCartResponse.StatusCode.OK_ALL);
        expected.put("OK_SOME", FetchToCartResponse.StatusCode.OK_SOME);
        expected.put("OK_INCREASED", FetchToCartResponse.StatusCode.OK_INCREASED);
        expected.put("OK_NEW", FetchToCartResponse.StatusCode.OK_NEW);
        expected.put("INVALID", FetchToCartResponse.StatusCode.INVALID);
        Assertions.assertEquals(expected.get(response), context.lastFetchToCartResponse.status());
    }

    @Then("the last Return from Cart response should be {string}")
    public void theLastReturnFromCartResponseShouldBe(String response) {
        Map<String, ReturnFromCartResponse.StatusCode> expected = new HashMap<>();
        expected.put("OK_ALL", ReturnFromCartResponse.StatusCode.OK_ALL);
        expected.put("OK_SOME", ReturnFromCartResponse.StatusCode.OK_SOME);
        expected.put("TOO_MANY", ReturnFromCartResponse.StatusCode.TOO_MANY);
        expected.put("NOT_FOUND", ReturnFromCartResponse.StatusCode.NOT_FOUND);
        expected.put("INVALID", ReturnFromCartResponse.StatusCode.INVALID);
        Assertions.assertEquals(expected.get(response), context.lastReturnFromCartResponse.status());
    }

    @Then("the last Delete fom Cart response should be {string}")
    public void theLastDeleteFomCartResponseShouldBe(String response) {
        Map<String, DelFromCartResponse.StatusCode> expected = new HashMap<>();
        expected.put("OK_ALL", DelFromCartResponse.StatusCode.OK_ALL);
        expected.put("OK_SOME", DelFromCartResponse.StatusCode.OK_SOME);
        expected.put("NOT_FOUND", DelFromCartResponse.StatusCode.NOT_FOUND);
        expected.put("INVALID", DelFromCartResponse.StatusCode.INVALID);
        expected.put("TOO_MANY", DelFromCartResponse.StatusCode.TOO_MANY);
        Assertions.assertEquals(expected.get(response), context.lastDelFromCartResponse.status());
    }
}
