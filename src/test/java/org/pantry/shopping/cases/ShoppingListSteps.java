package org.pantry.shopping.cases;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.pantry.shopping.cases.impl.AddToListImpl;
import org.pantry.shopping.cases.impl.DelFromListImpl;
import org.pantry.shopping.cases.input.AddToListRequest;
import org.pantry.shopping.cases.input.AddToListUC;
import org.pantry.shopping.cases.input.DelFromListRequest;
import org.pantry.shopping.cases.input.DelFromListUC;
import org.pantry.shopping.cases.output.AddToListResponse;
import org.pantry.shopping.cases.output.DelFromListResponse;
import org.pantry.shopping.entities.ListItem;

public class ShoppingListSteps {
    private final VolatileListGateway list;
    private final AddToListUC addToList;
    private final DelFromListUC delFromList;

    public ShoppingListSteps(ScenarioContext context) {
        list = context.getList();
        addToList = new AddToListImpl(context.getDatabases());
        delFromList = new DelFromListImpl(context.getDatabases());
    }

    @Given("I have {double} {string} of {string} in my shopping list")
    public void i_have_qty_units_of_product_in_my_shopping_list(Double qty, String unit, String product) {
        ListItem newItem = new ListItem(null, qty, unit, product);
        list.addItem(newItem);
    }

    @Given("I have an item with id {long} in my shopping list")
    public void i_have_an_item_with_id_n_in_my_shopping_list(Long id) {
        ListItem item = new ListItem(id, 1D, "units", "product");
        list.addItemWithId(item);
    }

    @When("I add {double} {string} of {string} to my shopping list")
    public void i_add_qty_units_of_product_to_my_shopping_list(Double qty, String unit, String product) {
        AddToListRequest request = new AddToListRequest(qty, unit, product);
        AddToListResponse response = addToList.execute(request);
    }

    @When("I delete the item with id {long} from my shopping list")
    public void i_delete_the_item_with_id_from_my_shopping_list(Long id) {
        DelFromListRequest request = new DelFromListRequest(id);
        DelFromListResponse response = delFromList.execute(request);
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

    @Then("I should not have any item with id {long} in my shopping list")
    public void i_should_not_have_any_item_with_id_in_my_shopping_list(Long id) {
        Assertions.assertTrue(list.findById(id).isEmpty());
    }

}
