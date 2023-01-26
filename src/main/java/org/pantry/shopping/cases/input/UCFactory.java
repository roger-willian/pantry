package org.pantry.shopping.cases.input;

public interface UCFactory {
    ViewListUC getViewListUC();

    AddToListUC getAddToShoppingList();

    DelFromListUC getDelFromShoppingList();

    ViewCartUC getViewCartUC();

    FetchToCartUC getFetchToCartUC();
}
