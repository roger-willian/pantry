package org.pantry.shopping.cases;

import org.pantry.shopping.cases.data.GatewaysFactory;
import org.pantry.shopping.cases.data.ShoppingCartGateway;
import org.pantry.shopping.cases.data.ShoppingListGateway;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ScenarioContext {
    private final GatewaysFactory databases;

    public ScenarioContext() {
        this.databases = mock(GatewaysFactory.class);
        ShoppingListGateway list = new VolatileListGateway();
        ShoppingCartGateway cart = new VolatileCartGateway();
        when(databases.getShoppingListGateway()).thenReturn(list);
        when(databases.getShoppingCartGateway()).thenReturn(cart);
    }

    public GatewaysFactory getDatabases() {
        return databases;
    }
}
