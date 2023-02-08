package org.pantry.shopping.cases;

import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.pantry.shopping.cases.impl.*;
import org.pantry.shopping.cases.input.*;
import org.pantry.shopping.cases.output.*;
import org.pantry.shopping.entities.CartItem;

import java.text.SimpleDateFormat;
import java.util.*;

public class ShoppingCartSteps {
    private final VolatileCartGateway cart;

    private final ScenarioContext context;
    private final ViewCartUC viewCart;
    private final FetchToCartUC fetchToCart;
    private final FetchFromListUC fetchFromList;
    private final ReturnFromCartUC returnFromCart;
    private final DelFromCartUC delFromCart;

    public ShoppingCartSteps(ScenarioContext context) {
        this.context = context;
        cart = context.getCart();
        viewCart = new ViewCartImpl(context.getDatabases());
        fetchToCart = new FetchToCartImpl(context.getDatabases());
        fetchFromList = new FetchFromListImpl(context.getDatabases());
        returnFromCart = new ReturnFromCartImpl(context.getDatabases());
        delFromCart = new DelFromCartImpl(context.getDatabases());
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
    public CartItemInternalResponse cartItemResponseTransformer(Map<String, String> entry) {
        Long id = Long.parseLong(entry.get("id"));
        Double qty = Double.parseDouble(entry.get("qty"));
        String unit = entry.get("unit");
        String product = entry.get("product");
        Integer price = (int) (Double.parseDouble(entry.get("price")) * 100);
        String date = entry.get("expiration");
        Integer expiration = fromDate(date);
        return new CartItemInternalResponse(id, qty, unit, product, price, expiration);
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
        ReturnFromCartInternalRequest request = new ReturnFromCartInternalRequest(id, qty);
        context.lastReturnFromCartResponse = returnFromCart.execute(request);
    }

    @When("I look at my shopping cart")
    public void iLookAtMyShoppingCart() {
        ViewCartInternalRequest request = new ViewCartInternalRequest();
        context.lastViewCartResponse = viewCart.execute(request);
    }

    @When("I fetch {double} {string} of {string} to my shopping cart, costing $ {double} per unit and expiring on {string}")
    public void iFetchOfToMyShoppingCartCostingPerUnitAndExpiringOn(Double qty, String unit, String product, Double price, String date) {
        Integer pricePerUnit = fromPrice(price);
        Integer expiration = fromDate(date);
        FetchToCartInternalRequest request = new FetchToCartInternalRequest(qty, unit, product, pricePerUnit, expiration);
        context.lastFetchToCartResponse = fetchToCart.execute(request);
    }

    @When("I fetch {double} units of the item with id {long} to my shopping cart, costing $ {double} per unit and expiring on {string}")
    public void iFetchUnitsOfTheItemWithIdToMyShoppingCartCosting$PerUnitAndExpiringOn(Double qty, Long id, Double price, String date) {
        Integer pricePerUnit = fromPrice(price);
        Integer expiration = fromDate(date);
        FetchFromListInternalRequest request = new FetchFromListInternalRequest(id, qty, pricePerUnit, expiration);
        context.lastFetchFromListResponse = fetchFromList.execute(request);
    }

    @Then("I should see exactly {int} items in my shopping cart, including:")
    public void iShouldSeeExactlyItemsInMyShoppingCartIncluding(int size, List<CartItemInternalResponse> items) {
        Assertions.assertEquals(size, context.lastViewCartResponse.size());
        Assertions.assertTrue(context.lastViewCartResponse.containsAll(items));
    }

    @Then("I should see {double} {string} of {string} in my shopping cart, costing $ {double} per unit and expiring on {string}")
    public void iShouldSeeOfInMyShoppingCartCostingPerUnitAndExpiringOn(Double qty, String unit, String product, Double price, String date) {
        Integer pricePerUnit = fromPrice(price);
        Integer expiration = fromDate(date);
        boolean found = context.lastViewCartResponse.stream().anyMatch(it -> {
           if (!Objects.equals(it.quantity(), qty)) return false;
           if (!Objects.equals(it.unit(), unit)) return false;
           if (!Objects.equals(it.name(), product)) return false;
           if (!Objects.equals(it.pricePerUnit(), pricePerUnit)) return false;
           if (!Objects.equals(it.expiration(), expiration)) return false;

           return true;
        });
        Assertions.assertTrue(found);
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

    @When("I remove {double} units of the item with id {long} from the shopping cart")
    public void iRemoveUnitsOfTheItemWithIdFromTheShoppingCart(Double qty, Long id) {
        DelFromCartInternalRequest request = new DelFromCartInternalRequest(id, qty);
        context.lastDelFromCartResponse = delFromCart.execute(request);
    }

    @Then("the last Fetch from List response should be {string}")
    public void theLastFetchFromListResponseShouldBe(String response) {
        Map<String, FetchFromListInternalResponse.StatusCode> expected = new HashMap<>();
        expected.put("OK_ALL", FetchFromListInternalResponse.StatusCode.OK_ALL);
        expected.put("OK_SOME", FetchFromListInternalResponse.StatusCode.OK_SOME);
        expected.put("NOT_FOUND", FetchFromListInternalResponse.StatusCode.NOT_FOUND);
        expected.put("ERROR", FetchFromListInternalResponse.StatusCode.ERROR);
        expected.put("INVALID", FetchFromListInternalResponse.StatusCode.INVALID);
        Assertions.assertEquals(expected.get(response), context.lastFetchFromListResponse.status());
    }

    @Then("the last Fetch to Cart response should be {string}")
    public void theLastFetchToCartResponseShouldBe(String response) {
        Map<String, FetchToCartInternalResponse.StatusCode> expected = new HashMap<>();
        expected.put("OK_ALL", FetchToCartInternalResponse.StatusCode.OK_ALL);
        expected.put("OK_SOME", FetchToCartInternalResponse.StatusCode.OK_SOME);
        expected.put("OK_INCREASED", FetchToCartInternalResponse.StatusCode.OK_INCREASED);
        expected.put("OK_NEW", FetchToCartInternalResponse.StatusCode.OK_NEW);
        expected.put("INVALID", FetchToCartInternalResponse.StatusCode.INVALID);
        Assertions.assertEquals(expected.get(response), context.lastFetchToCartResponse.status());
    }

    @Then("the last Return from Cart response should be {string}")
    public void theLastReturnFromCartResponseShouldBe(String response) {
        Map<String, ReturnFromCartInternalResponse.StatusCode> expected = new HashMap<>();
        expected.put("OK_ALL", ReturnFromCartInternalResponse.StatusCode.OK_ALL);
        expected.put("OK_SOME", ReturnFromCartInternalResponse.StatusCode.OK_SOME);
        expected.put("TOO_MANY", ReturnFromCartInternalResponse.StatusCode.TOO_MANY);
        expected.put("NOT_FOUND", ReturnFromCartInternalResponse.StatusCode.NOT_FOUND);
        expected.put("INVALID", ReturnFromCartInternalResponse.StatusCode.INVALID);
        Assertions.assertEquals(expected.get(response), context.lastReturnFromCartResponse.status());
    }

    @Then("the last Delete fom Cart response should be {string}")
    public void theLastDeleteFomCartResponseShouldBe(String response) {
        Map<String, DelFromCartInternalResponse.StatusCode> expected = new HashMap<>();
        expected.put("OK_ALL", DelFromCartInternalResponse.StatusCode.OK_ALL);
        expected.put("OK_SOME", DelFromCartInternalResponse.StatusCode.OK_SOME);
        expected.put("NOT_FOUND", DelFromCartInternalResponse.StatusCode.NOT_FOUND);
        expected.put("INVALID", DelFromCartInternalResponse.StatusCode.INVALID);
        expected.put("TOO_MANY", DelFromCartInternalResponse.StatusCode.TOO_MANY);
        Assertions.assertEquals(expected.get(response), context.lastDelFromCartResponse.status());
    }
}
