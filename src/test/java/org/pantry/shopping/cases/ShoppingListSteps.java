package org.pantry.shopping.cases;

import io.cucumber.java.DataTableType;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.pantry.shopping.cases.impl.AddToListImpl;
import org.pantry.shopping.cases.impl.DelFromListImpl;
import org.pantry.shopping.cases.impl.ViewListImpl;
import org.pantry.shopping.cases.input.*;
import org.pantry.shopping.cases.output.AddToListInternalResponse;
import org.pantry.shopping.cases.output.DelFromListInternalResponse;
import org.pantry.shopping.cases.output.ListItemInternalResponse;
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
    public ListItemInternalResponse listItemResponseTransformer(Map<String, String> entry) {
        return new ListItemInternalResponse(Long.parseLong(entry.get("id")),
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
        AddToListInternalRequest request = new AddToListInternalRequest(qty, unit, product);
        context.lastAddToListResponse = addToList.execute(request);
    }

    @When("I delete the item with id {long} from my shopping list")
    public void i_delete_the_item_with_id_from_my_shopping_list(Long id) {
        DelFromListInternalRequest request = new DelFromListInternalRequest(id);
        context.lastDelFromListResponse = delFromList.execute(request);
    }

    @When("I look at my shopping list")
    public void iLookAtMyShoppingList() {
        ViewListInternalRequest request = new ViewListInternalRequest();
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
    public void iShouldSeeExactlyItemsInMyShoppingListIncluding(int size, List<ListItemInternalResponse> items) {
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

    @Then("I should not see any item with id {long} in my shopping list")
    public void iShouldNotSeeAnyItemWithIdInMyShoppingList(long id) {
        Assertions.assertTrue(list.findById(id).isEmpty());
    }

    @Then("the last Delete fom List response should be {string}")
    public void theLastDeleteFomListResponseShouldBe(String status) {
        Map<String, DelFromListInternalResponse.StatusCode> expected = new HashMap<>();
        expected.put("OK", DelFromListInternalResponse.StatusCode.OK);
        expected.put("ERROR", DelFromListInternalResponse.StatusCode.ERROR);
        expected.put("NOT_FOUND", DelFromListInternalResponse.StatusCode.NOT_FOUND);
        Assertions.assertEquals(expected.get(status), context.lastDelFromListResponse.status());
    }

    @Then("the last Add to List response should be {string}")
    public void theLastAddToListResponseShouldBe(String status) {
        Map<String, AddToListInternalResponse.StatusCode> expected = new HashMap<>();
        expected.put("OK_NEW", AddToListInternalResponse.StatusCode.OK_NEW);
        expected.put("OK_INCREASED", AddToListInternalResponse.StatusCode.OK_INCREASED);
        expected.put("INVALID", AddToListInternalResponse.StatusCode.INVALID);
        expected.put("ERROR", AddToListInternalResponse.StatusCode.ERROR);
        Assertions.assertEquals(expected.get(status), context.lastAddToListResponse.status());
    }
}
