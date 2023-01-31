package org.pantry.shopping.cases;

import io.cucumber.java.DataTableType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.pantry.shopping.cases.impl.FetchFromListImpl;
import org.pantry.shopping.cases.impl.FetchToCartImpl;
import org.pantry.shopping.cases.impl.ViewCartImpl;
import org.pantry.shopping.cases.input.*;
import org.pantry.shopping.cases.output.CartItemResponse;
import org.pantry.shopping.entities.CartItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ShoppingCartSteps {
    private final VolatileCartGateway cart;

    private final ScenarioContext context;
    private final ViewCartUC viewCart;
    private final FetchToCartUC fetchToCart;
    private final FetchFromListUC fetchFromList;

    public ShoppingCartSteps(ScenarioContext context) {
        this.context = context;
        cart = context.getCart();
        viewCart = new ViewCartImpl(context.getDatabases());
        fetchToCart = new FetchToCartImpl(context.getDatabases());
        fetchFromList = new FetchFromListImpl(context.getDatabases());
    }

    private Integer fromDate(String uiDate) {
        try {
            SimpleDateFormat uiFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat dbFormat = new SimpleDateFormat("yyyyMMdd");
            Date date = uiFormat.parse(uiDate);
            Integer dbDate = Integer.parseInt(dbFormat.format(date));
            return dbDate;
        } catch (Exception e) {
            return 0;
        }
    }

    @DataTableType
    public CartItemResponse cartItemResponseTransformer(Map<String, String> entry) {
        Long id = Long.parseLong(entry.get("id"));
        Double qty = Double.parseDouble(entry.get("qty"));
        String unit = entry.get("unit");
        String product = entry.get("product");
        Integer price = (int) (Double.parseDouble(entry.get("price")) * 100);
        String date = entry.get("expiration");
        String year = date.substring(6);
        String month = date.substring(3, 4);
        String day = date.substring(0, 1);
        Integer expiration = Integer.parseInt(year + month + day);
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
        String year = date.substring(6);
        String month = date.substring(3, 4);
        String day = date.substring(0, 1);
        Integer expiration = Integer.parseInt(year + month + day);
        return new CartItem(id, qty, unit, product, price, expiration);
    }

    @Given("I have the following items in my shopping cart:")
    public void iHaveTheFollowingItemsInMyShoppingCart(List<CartItem> items) {
        items.forEach(cart::addWithId);
    }

    @When("I look at my shopping cart")
    public void iLookAtMyShoppingCart() {
        ViewCartRequest request = new ViewCartRequest();
        context.lastViewCartResponse = viewCart.execute(request);
    }

    @Then("I should see exactly {int} items in my shopping cart, including:")
    public void iShouldSeeExactlyItemsInMyShoppingCartIncluding(int size, List<CartItemResponse> items) {
        Assertions.assertEquals(size, context.lastViewCartResponse.size());
        Assertions.assertTrue(context.lastViewCartResponse.containsAll(items));
    }

    @When("I fetch {double} {string} of {string} to my shopping cart, costing $ {double} per unit and expiring on {string}")
    public void iFetchOfToMyShoppingCartCostingPerUnitAndExpiringOn(Double qty, String unit, String product, Double price, String date) {
        Integer pricePerUnit = (int) (price * 100);
        Integer expiration = fromDate(date);
        FetchToCartRequest request = new FetchToCartRequest(qty, unit, product, pricePerUnit, expiration);
        context.lastFetchToCartResponse = fetchToCart.execute(request);
    }

    @And("I should see {double} {string} of {string} in my shopping cart, costing $ {double} per unit and expiring on {string}")
    public void iShouldSeeOfInMyShoppingCartCostingPerUnitAndExpiringOn(Double qty, String unit, String product, Double price, String date) {
        Integer pricePerUnit = (int) (price * 100);
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

    @When("I fetch {double} units of the item with id {long} to my shopping cart, costing $ {double} per unit and expiring on {string}")
    public void iFetchUnitsOfTheItemWithIdToMyShoppingCartCosting$PerUnitAndExpiringOn(Double qty, Long id, Double price, String date) {
        Integer pricePerUnit = (int) (price * 100);
        Integer expiration = fromDate(date);
        FetchFromListRequest request = new FetchFromListRequest(id, qty, pricePerUnit, expiration);
        context.lastFetchFromListResponse = fetchFromList.execute(request);
    }
}
