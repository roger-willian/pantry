package org.pantry.shopping.cases;

import org.pantry.shopping.cases.ShoppingCasesFactory;
import org.pantry.shopping.cases.data.GatewaysFactory;
import org.pantry.shopping.cases.impl.*;
import org.pantry.shopping.cases.input.*;

public class ShoppingCases implements ShoppingCasesFactory {

    private final DelFromCartUC delFromCart;
    private final FetchToCartUC fetchToCart;
    private final ReturnFromCartUC returnFromCart;
    private final ViewCartUC viewCart;

    private final ViewListUC viewList;
    private final AddToListUC addToList;
    private final DelFromListUC delFromList;
    private final FetchFromListUC fetchFromList;

    public ShoppingCases(GatewaysFactory databases) {
        delFromCart = new DelFromCartImpl(databases);
        fetchToCart = new FetchToCartImpl(databases);
        returnFromCart = new ReturnFromCartImpl(databases);
        viewCart = new ViewCartImpl(databases);

        viewList = new ViewListImpl(databases);
        addToList = new AddToListImpl(databases);
        delFromList = new DelFromListImpl(databases);
        fetchFromList = new FetchFromListImpl(databases);
    }

    @Override
    public ViewListUC getViewListUC() {
        return viewList;
    }

    @Override
    public AddToListUC getAddToShoppingListUC() {
        return addToList;
    }

    @Override
    public DelFromListUC getDelFromShoppingListUC() {
        return delFromList;
    }

    @Override
    public ViewCartUC getViewCartUC() {
        return viewCart;
    }

    @Override
    public FetchToCartUC getFetchToCartUC() {
        return fetchToCart;
    }

    @Override
    public ReturnFromCartUC getReturnFromCartUC() {
        return returnFromCart;
    }

    @Override
    public FetchFromListUC getFetchFromListUC() {
        return fetchFromList;
    }

    @Override
    public DelFromCartUC getDelFromCartUC() {
        return delFromCart;
    }
}
