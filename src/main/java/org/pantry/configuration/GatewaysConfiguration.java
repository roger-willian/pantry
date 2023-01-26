package org.pantry.configuration;

import org.pantry.shopping.cases.data.ShoppingCartGateway;
import org.pantry.shopping.cases.data.ShoppingListGateway;
import org.pantry.shopping.cases.data.GatewaysFactory;
import org.pantry.shopping.databases.CSVCartGateway;
import org.pantry.shopping.databases.CSVListGateway;

public class GatewaysConfiguration implements GatewaysFactory {
    static ShoppingListGateway shoppingListDB = new CSVListGateway("shoppingList.csv");
    static ShoppingCartGateway shoppingCartDB = new CSVCartGateway("shoppingCart.csv");

    @Override
    public ShoppingListGateway getShoppingListGateway() {
        return GatewaysConfiguration.shoppingListDB;
    }

    @Override
    public ShoppingCartGateway getShoppingCartGateway() {
        return GatewaysConfiguration.shoppingCartDB;
    }
}
