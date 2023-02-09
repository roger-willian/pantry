package org.pantry.shopping;

import org.pantry.shopping.cases.ShoppingCases;
import org.pantry.shopping.cases.ShoppingCasesFactory;
import org.pantry.shopping.cases.data.GatewaysFactory;
import org.pantry.shopping.controllers.shoppingcart.ShoppingCartController;
import org.pantry.shopping.controllers.shoppingcart.ShoppingCartControllerImpl;
import org.pantry.shopping.controllers.shoppingcart.responses.*;
import org.pantry.shopping.controllers.shoppinglist.ShoppingListController;
import org.pantry.shopping.controllers.shoppinglist.ShoppingListControllerImpl;
import org.pantry.shopping.controllers.shoppinglist.responses.*;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ScenarioContext {
    private final VolatileCartGateway cart;
    private final VolatileListGateway list;

    AddToListResponse lastAddToListResponse;
    DelFromListResponse lastDelFromListResponse;
    FetchFromListResponse lastFetchFromListResponse;
    ViewListResponse lastViewListResponse;

    DelFromCartResponse lastDelFromCartResponse;
    FetchToCartResponse lastFetchToCartResponse;
    ReturnFromCartResponse lastReturnFromCartResponse;
    ViewCartResponse lastViewCartResponse;

    private final ShoppingCartController cartController;
    private final ShoppingListController listController;

    public ScenarioContext() {
        list = new VolatileListGateway();
        cart = new VolatileCartGateway();

        GatewaysFactory databases = mock(GatewaysFactory.class);
        when(databases.getShoppingListGateway()).thenReturn(list);
        when(databases.getShoppingCartGateway()).thenReturn(cart);

        ShoppingCasesFactory cases = new ShoppingCases(databases);

        cartController = new ShoppingCartControllerImpl(cases);
        listController = new ShoppingListControllerImpl(cases);
    }

    public VolatileListGateway getList() {
        return list;
    }

    public VolatileCartGateway getCart() {
        return cart;
    }

    public ShoppingCartController getCartController() {
        return cartController;
    }

    public ShoppingListController getListController() {
        return listController;
    }
}
