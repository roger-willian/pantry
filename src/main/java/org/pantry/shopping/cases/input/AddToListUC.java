package org.pantry.shopping.cases.input;

import org.pantry.shopping.cases.output.AddToListInternalResponse;

public interface AddToListUC {
    AddToListInternalResponse execute(AddToListInternalRequest item);
}
