package org.pantry.shopping.cases.input;

import org.pantry.shopping.cases.output.DelFromShoppingListResponse;

public interface DelFromListUC {
    DelFromShoppingListResponse execute(DelFromListRequest req);
}
