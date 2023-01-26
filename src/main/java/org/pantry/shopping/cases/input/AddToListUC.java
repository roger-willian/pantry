package org.pantry.shopping.cases.input;

import org.pantry.shopping.cases.output.AddToShoppingListResponse;

public interface AddToListUC {
    AddToShoppingListResponse execute(AddToListRequest item);
}
