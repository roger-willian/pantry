package org.pantry.shopping.cases.input;

import org.pantry.shopping.cases.output.AddToListResponse;

public interface AddToListUC {
    AddToListResponse execute(AddToListRequest item);
}
