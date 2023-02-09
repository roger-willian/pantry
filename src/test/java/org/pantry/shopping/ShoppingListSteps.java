package org.pantry.shopping;

import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.pantry.shopping.controllers.shoppinglist.ShoppingListController;
import org.pantry.shopping.controllers.shoppinglist.requests.AddToListRequest;
import org.pantry.shopping.controllers.shoppinglist.requests.DelFromListRequest;
import org.pantry.shopping.controllers.shoppinglist.requests.FetchFromListRequest;
import org.pantry.shopping.controllers.shoppinglist.requests.ViewListRequest;
import org.pantry.shopping.controllers.shoppinglist.responses.AddToListResponse;
import org.pantry.shopping.controllers.shoppinglist.responses.DelFromListResponse;
import org.pantry.shopping.controllers.shoppinglist.responses.ListItemResponse;
import org.pantry.shopping.entities.CartItem;
import org.pantry.shopping.entities.ListItem;

import java.text.SimpleDateFormat;
import java.util.*;

public class ShoppingListSteps {
    private final VolatileListGateway list;
    private final ScenarioContext context;
    private final ShoppingListController controller;

    public ShoppingListSteps(ScenarioContext context) {
        this.context = context;
        list = context.getList();
        controller = context.getListController();
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
    public ListItemResponse listItemResponseTransformer(Map<String, String> entry) {
        return new ListItemResponse(Long.parseLong(entry.get("id")),
                Double.parseDouble(entry.get("qty")),
                entry.get("unit"),
                entry.get("product"));
    }

    @DataTableType
    public ListItem listItemTransformer(Map<String, String> entry) {
        return new ListItem(Long.parseLong(entry.get("id")),
                Double.parseDouble(entry.get("qty")),
                entry.get("unit"),
                entry.get("product"));
    }

    @Given("I have the following items in my shopping list:")
    public void theFollowingItemsAreInTheShoppingList(List<ListItem> items) {
        items.forEach(list::addItemWithId);
    }

    @When("I add {double} {string} of {string} to my shopping list")
    public void iAddOfToMyShoppingList(Double qty, String unit, String product) {
        AddToListRequest request = new AddToListRequest(qty, unit, product);
        controller.addToList(request, response -> context.lastAddToListResponse = response);
    }

    @When("I delete the item with id {long} from my shopping list")
    public void iDeleteTheItemWithIdFromMyShoppingList(Long id) {
        DelFromListRequest request = new DelFromListRequest(id);
        controller.delFromList(request, response -> context.lastDelFromListResponse = response);
    }

    @When("I look at my shopping list")
    public void iLookAtMyShoppingList() {
        ViewListRequest request = new ViewListRequest();
        controller.viewList(request, response -> context.lastViewListResponse = response);
    }

    @When("I fetch {double} units of the item with id {long} to my shopping cart, costing $ {double} per unit and expiring on {string}")
    public void iFetchUnitsOfTheItemWithIdToMyShoppingCartCosting$PerUnitAndExpiringOn(Double qty, Long id, Double price, String date) {
        Integer pricePerUnit = fromPrice(price);
        Integer expiration = fromDate(date);
        FetchFromListRequest request = new FetchFromListRequest(id, qty, pricePerUnit, expiration);
        controller.fetchFromList(request, response -> context.lastFetchFromListResponse = response);
    }

    @Then("my shopping list should have {double} {string} of {string}")
    public void myShoppingListShouldHaveOf(Double qty, String unit, String product) {
        ListItem query = new ListItem(null, null, unit, product);
        Assertions.assertTrue(list.existsSimilar(query));
        ListItem inTheList = list.findSimilar(query).orElseThrow();
        Assertions.assertEquals(qty, inTheList.quantity());
    }

    @Then("my shopping list should not have any {string} of {string}")
    public void myShoppingListShouldNotHaveAnyOf(String unit, String product) {
        ListItem query = new ListItem(null, null, unit, product);
        Assertions.assertFalse(list.existsSimilar(query));
    }

    @Then("I should see exactly {int} items in my shopping list, including:")
    public void iShouldSeeExactlyItemsInMyShoppingListIncluding(int size, List<ListItemResponse> items) {
        Assertions.assertEquals(size, context.lastViewListResponse.items().size());
        Assertions.assertTrue(context.lastViewListResponse.items().containsAll(items));
    }

    @Then("my shopping list should have exactly {int} items, including:")
    public void myShoppingListShouldHaveExactlyItemsIncluding(int size, List<ListItem> items) {
        Assertions.assertEquals(size, list.findAll().size());
        Assertions.assertTrue(list.findAll().containsAll(items));
    }

    @Then("my shopping list should not have an item with id {long}")
    public void myShoppingListShouldNotHaveAnItemWithId(long id) {
        Assertions.assertTrue(list.findById(id).isEmpty());
    }

    @Then("the last Delete fom List response should be {string}")
    public void theLastDeleteFomListResponseShouldBe(String status) {
        Map<String, DelFromListResponse.StatusCode> expected = new HashMap<>();
        expected.put("OK", DelFromListResponse.StatusCode.OK);
        expected.put("ERROR", DelFromListResponse.StatusCode.ERROR);
        expected.put("NOT_FOUND", DelFromListResponse.StatusCode.NOT_FOUND);
        Assertions.assertEquals(expected.get(status), context.lastDelFromListResponse.status());
    }

    @Then("the last Add to List response should be {string}")
    public void theLastAddToListResponseShouldBe(String status) {
        Map<String, AddToListResponse.StatusCode> expected = new HashMap<>();
        expected.put("OK_NEW", AddToListResponse.StatusCode.OK_NEW);
        expected.put("OK_INCREASED", AddToListResponse.StatusCode.OK_INCREASED);
        expected.put("INVALID", AddToListResponse.StatusCode.INVALID);
        expected.put("ERROR", AddToListResponse.StatusCode.ERROR);
        Assertions.assertEquals(expected.get(status), context.lastAddToListResponse.status());
    }
}
