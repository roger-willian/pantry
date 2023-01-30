package org.pantry.shopping.cases;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.pantry.shopping.cases.data.GatewaysFactory;
import org.pantry.shopping.cases.data.ShoppingListGateway;
import org.pantry.shopping.cases.impl.AddToListImpl;
import org.pantry.shopping.cases.input.AddToListRequest;
import org.pantry.shopping.cases.input.AddToListUC;
import org.pantry.shopping.cases.output.AddToListResponse;
import org.pantry.shopping.databases.CSVListGateway;
import org.pantry.shopping.entities.ListItem;

import java.io.File;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ShoppingListSteps {
    private final ShoppingListGateway list;
    private final AddToListUC addToList;

    public ShoppingListSteps(ScenarioContext context) {
        list = context.getDatabases().getShoppingListGateway();
        addToList = new AddToListImpl(context.getDatabases());
    }

    @Given("I have {double} {string} of {string} in my shopping list")
    public void i_have_qty_units_of_product_in_my_shopping_list(Double qty, String unit, String product) {
        ListItem newItem = new ListItem(null, qty, unit, product);
        list.addItem(newItem);
    }

    @When("I add {double} {string} of {string} to my shopping list")
    public void i_add_qty_units_of_product_to_my_shopping_list(Double qty, String unit, String product) {
        AddToListRequest request = new AddToListRequest(qty, unit, product);
        AddToListResponse response = addToList.execute(request);
    }

    @Then("my shopping list should have {double} {string} of {string}")
    public void my_shopping_list_should_have_qty_units_of_product(Double qty, String unit, String product) {
        ListItem query = new ListItem(null, null, unit, product);
        assert list.existsSimilar(query);
        ListItem inTheList = list.findSimilar(query).orElseThrow();
        assert inTheList.quantity().equals(qty);
    }

    @Then("my shopping list should not have any {string} of {string}")
    public void myShoppingListShouldNotHaveAnyOf(String unit, String product) {
        ListItem query = new ListItem(null, null, unit, product);
        assert !list.existsSimilar(query);
    }
}
