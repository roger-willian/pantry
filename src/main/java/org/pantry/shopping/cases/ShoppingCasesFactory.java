package org.pantry.shopping.cases;

import org.pantry.shopping.cases.input.*;

public interface ShoppingCasesFactory {
    ViewListUC getViewListUC();

    AddToListUC getAddToShoppingListUC();

    DelFromListUC getDelFromShoppingListUC();

    ViewCartUC getViewCartUC();

    FetchToCartUC getFetchToCartUC();

    ReturnFromCartUC getReturnFromCartUC();

    FetchFromListUC getFetchFromListUC();

    DelFromCartUC getDelFromCartUC();
}
