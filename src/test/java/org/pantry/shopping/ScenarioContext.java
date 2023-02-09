package org.pantry.shopping;

import org.pantry.shopping.cases.ShoppingCases;
import org.pantry.shopping.cases.ShoppingCasesFactory;
import org.pantry.shopping.cases.data.GatewaysFactory;
import org.pantry.shopping.controllers.shoppingcart.ShoppingCartController;
import org.pantry.shopping.controllers.shoppingcart.ShoppingCartControllerImpl;
import org.pantry.shopping.controllers.shoppingcart.responses.CartItemResponse;
import org.pantry.shopping.controllers.shoppingcart.responses.DelFromCartResponse;
import org.pantry.shopping.controllers.shoppingcart.responses.FetchToCartResponse;
import org.pantry.shopping.controllers.shoppingcart.responses.ReturnFromCartResponse;
import org.pantry.shopping.controllers.shoppinglist.ShoppingListController;
import org.pantry.shopping.controllers.shoppinglist.ShoppingListControllerImpl;
import org.pantry.shopping.controllers.shoppinglist.responses.AddToListResponse;
import org.pantry.shopping.controllers.shoppinglist.responses.DelFromListResponse;
import org.pantry.shopping.controllers.shoppinglist.responses.FetchFromListResponse;
import org.pantry.shopping.controllers.shoppinglist.responses.ListItemResponse;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ScenarioContext {
    private final VolatileCartGateway cart;
    private final VolatileListGateway list;

    AddToListResponse lastAddToListResponse;
    DelFromListResponse lastDelFromListResponse;
    FetchFromListResponse lastFetchFromListResponse;
    List<ListItemResponse> lastViewListResponse;

    DelFromCartResponse lastDelFromCartResponse;
    FetchToCartResponse lastFetchToCartResponse;
    ReturnFromCartResponse lastReturnFromCartResponse;
    List<CartItemResponse> lastViewCartResponse;

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
