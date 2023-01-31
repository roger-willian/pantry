package org.pantry.shopping.cases;

import io.cucumber.java.DataTableType;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.pantry.shopping.cases.impl.AddToListImpl;
import org.pantry.shopping.cases.impl.DelFromListImpl;
import org.pantry.shopping.cases.impl.ViewListImpl;
import org.pantry.shopping.cases.input.*;
import org.pantry.shopping.cases.output.AddToListResponse;
import org.pantry.shopping.cases.output.DelFromListResponse;
import org.pantry.shopping.cases.output.ListItemResponse;
import org.pantry.shopping.entities.ListItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ShoppingListSteps {
    private final VolatileListGateway list;
    private final AddToListUC addToList;
    private final DelFromListUC delFromList;
    private final ViewListUC viewList;

    private final ScenarioContext context;

    public ShoppingListSteps(ScenarioContext context) {
        this.context = context;
        list = context.getList();
        addToList = new AddToListImpl(context.getDatabases());
        delFromList = new DelFromListImpl(context.getDatabases());
        viewList = new ViewListImpl(context.getDatabases());
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
    public void i_add_qty_units_of_product_to_my_shopping_list(Double qty, String unit, String product) {
        AddToListRequest request = new AddToListRequest(qty, unit, product);
        context.lastAddToListResponse = addToList.execute(request);
    }

    @When("I delete the item with id {long} from my shopping list")
    public void i_delete_the_item_with_id_from_my_shopping_list(Long id) {
        DelFromListRequest request = new DelFromListRequest(id);
        context.lastDelFromListResponse = delFromList.execute(request);
    }

    @When("I look at my shopping list")
    public void iLookAtMyShoppingList() {
        ViewListRequest request = new ViewListRequest();
        context.lastViewListResponse = viewList.execute(request);
    }

    @Then("my shopping list should have {double} {string} of {string}")
    public void my_shopping_list_should_have_qty_units_of_product(Double qty, String unit, String product) {
        ListItem query = new ListItem(null, null, unit, product);
        Assertions.assertTrue(list.existsSimilar(query));
        ListItem inTheList = list.findSimilar(query).orElseThrow();
        Assertions.assertEquals(inTheList.quantity(), qty);
    }

    @Then("my shopping list should not have any {string} of {string}")
    public void myShoppingListShouldNotHaveAnyOf(String unit, String product) {
        ListItem query = new ListItem(null, null, unit, product);
        Assertions.assertFalse(list.existsSimilar(query));
    }

    @Then("the last Delete fom List response should be {string}")
    public void theLastDeleteFomListResponseShouldBe(String status) {
        Map<String, DelFromListResponse> expected = new HashMap<>();
        expected.put("OK", DelFromListResponse.OK);
        expected.put("ERROR", DelFromListResponse.ERROR);
        expected.put("NOT_FOUND", DelFromListResponse.NOT_FOUND);
        Assertions.assertEquals(context.lastDelFromListResponse, expected.get(status));
    }

    @Then("the last Add to List response should be {string}")
    public void theLastAddToListResponseShouldBe(String status) {
        Map<String, AddToListResponse> expected = new HashMap<>();
        expected.put("OK_NEW", AddToListResponse.OK_NEW);
        expected.put("OK_INCREASED", AddToListResponse.OK_INCREASED);
        expected.put("INVALID", AddToListResponse.INVALID);
        expected.put("ERROR", AddToListResponse.ERROR);
        Assertions.assertEquals(context.lastAddToListResponse, expected.get(status));
    }

    @Then("I should see {double} {string} of {string} in my shopping list")
    public void iShouldSeeOfInMyShoppingList(Double qty, String unit, String product) {
        boolean found = context.lastViewListResponse.stream().anyMatch(it->{
            if (!Objects.equals(it.quantity(), qty)) return false;
            if (!Objects.equals(it.unit(), unit)) return false;
            if (!Objects.equals(it.name(), product)) return false;
            return true;
        });
        Assertions.assertTrue(found);
    }

    @Then("I should see exactly {int} items in my shopping list, including:")
    public void iShouldSeeExactlyItemsInMyShoppingListIncluding(int size, List<ListItemResponse> items) {
        Assertions.assertEquals(size, context.lastViewListResponse.size());
        Assertions.assertTrue(context.lastViewListResponse.containsAll(items));
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

    @But("I should not see any item with id {long} in my shopping list")
    public void iShouldNotSeeAnyItemWithIdInMyShoppingList(long id) {
        Assertions.assertTrue(list.findById(id).isEmpty());
    }
}
