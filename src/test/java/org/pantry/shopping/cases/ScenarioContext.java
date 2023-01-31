package org.pantry.shopping.cases;

import org.pantry.shopping.cases.data.GatewaysFactory;
import org.pantry.shopping.cases.output.*;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ScenarioContext {
    private final GatewaysFactory databases;
    private final VolatileCartGateway cart;
    private final VolatileListGateway list;

    AddToListResponse lastAddToListResponse;
    DelFromListResponse lastDelFromListResponse;
    List<ListItemResponse> lastViewListResponse;

    List<CartItemResponse> lastViewCartResponse;
    FetchToCartResponse lastFetchToCartResponse;
    FetchFromListResponse lastFetchFromListResponse;


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
