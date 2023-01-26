package org.pantry.configuration;

import org.pantry.shopping.cases.data.GatewaysFactory;
import org.pantry.shopping.cases.impl.*;
import org.pantry.shopping.cases.input.*;

public class UCConfiguration implements UCFactory {
    private final ViewListUC viewList;
    private final AddToListUC addToList;
    private final DelFromListUC delFromShoppingList;
    private final ViewCartUC viewCart;

    private final FetchToCartUC fetchToCart;

    public UCConfiguration(GatewaysFactory databaseConfig) {
        viewList = new ViewListImpl(databaseConfig);
        addToList = new AddToListImpl(databaseConfig);
        delFromShoppingList = new DelFromListImpl(databaseConfig);
        viewCart = new ViewCartImpl(databaseConfig);
        fetchToCart = new FetchToCartImpl(databaseConfig);
    }
    @Override
    public ViewListUC getViewListUC() {
        return viewList;
    }

    @Override
    public AddToListUC getAddToShoppingList() {
        return addToList;
    }

    @Override
    public DelFromListUC getDelFromShoppingList() {
        return delFromShoppingList;
    }

    @Override
    public ViewCartUC getViewCartUC() {
        return viewCart;
    }

    @Override
    public FetchToCartUC getFetchToCartUC() {
        return fetchToCart;
    }
}
