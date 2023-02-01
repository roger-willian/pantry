package org.pantry.shopping.cases;

import io.cucumber.java.DataTableType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.pantry.shopping.cases.impl.FetchFromListImpl;
import org.pantry.shopping.cases.impl.FetchToCartImpl;
import org.pantry.shopping.cases.impl.ReturnFromCartImpl;
import org.pantry.shopping.cases.impl.ViewCartImpl;
import org.pantry.shopping.cases.input.*;
import org.pantry.shopping.cases.output.CartItemResponse;
import org.pantry.shopping.cases.output.FetchFromListResponse;
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

    public ShoppingCartSteps(ScenarioContext context) {
        this.context = context;
        cart = context.getCart();
        viewCart = new ViewCartImpl(context.getDatabases());
        fetchToCart = new FetchToCartImpl(context.getDatabases());
        fetchFromList = new FetchFromListImpl(context.getDatabases());
        returnFromCart = new ReturnFromCartImpl(context.getDatabases());
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
        context.lastReturnFromCartResponse = returnFromCart.execute(request);
    }

    @When("I look at my shopping cart")
    public void iLookAtMyShoppingCart() {
        ViewCartRequest request = new ViewCartRequest();
        context.lastViewCartResponse = viewCart.execute(request);
    }

    @When("I fetch {double} {string} of {string} to my shopping cart, costing $ {double} per unit and expiring on {string}")
    public void iFetchOfToMyShoppingCartCostingPerUnitAndExpiringOn(Double qty, String unit, String product, Double price, String date) {
        Integer pricePerUnit = fromPrice(price);
        Integer expiration = fromDate(date);
        FetchToCartRequest request = new FetchToCartRequest(qty, unit, product, pricePerUnit, expiration);
        context.lastFetchToCartResponse = fetchToCart.execute(request);
    }

    @When("I fetch {double} units of the item with id {long} to my shopping cart, costing $ {double} per unit and expiring on {string}")
    public void iFetchUnitsOfTheItemWithIdToMyShoppingCartCosting$PerUnitAndExpiringOn(Double qty, Long id, Double price, String date) {
        Integer pricePerUnit = fromPrice(price);
        Integer expiration = fromDate(date);
        FetchFromListRequest request = new FetchFromListRequest(id, qty, pricePerUnit, expiration);
        context.lastFetchFromListResponse = fetchFromList.execute(request);
    }

    @Then("I should see exactly {int} items in my shopping cart, including:")
    public void iShouldSeeExactlyItemsInMyShoppingCartIncluding(int size, List<CartItemResponse> items) {
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

    @Then("the last Fetch from List response should be {string}")
    public void theLastFetchFromListResponseShouldBe(String response) {
        Map<String, FetchFromListResponse> expected = new HashMap<>();
        expected.put("OK_ALL", FetchFromListResponse.OK_ALL);
        expected.put("OK_SOME", FetchFromListResponse.OK_SOME);
        expected.put("NOT_FOUND", FetchFromListResponse.NOT_FOUND);
        expected.put("ERROR", FetchFromListResponse.ERROR);
        expected.put("INVALID", FetchFromListResponse.INVALID);
        Assertions.assertEquals(expected.get(response), context.lastFetchFromListResponse);
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
}
