package org.pantry.shopping.cases.input;

import org.pantry.shopping.cases.output.ReturnFromCartInternalResponse;

public interface ReturnFromCartUC {
    ReturnFromCartInternalResponse execute(ReturnFromCartInternalRequest request);
}
