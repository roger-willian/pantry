package org.pantry.shopping.cases;

import org.pantry.shopping.cases.data.GatewaysFactory;
import org.pantry.shopping.cases.data.ShoppingCartGateway;
import org.pantry.shopping.cases.data.ShoppingListGateway;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ScenarioContext {
    private final GatewaysFactory databases;
    private final VolatileCartGateway cart;
    private final VolatileListGateway list;

    public ScenarioContext() {
        this.databases = mock(GatewaysFactory.class);
        list = new VolatileListGateway();
        cart = new VolatileCartGateway();
        when(databases.getShoppingListGateway()).thenReturn(list);
        when(databases.getShoppingCartGateway()).thenReturn(cart);
    }

    public GatewaysFactory getDatabases() {
        return databases;
    }

    public VolatileListGateway getList() {
        return list;
    }
    public VolatileCartGateway getCart() {
        return cart;
    }
}
