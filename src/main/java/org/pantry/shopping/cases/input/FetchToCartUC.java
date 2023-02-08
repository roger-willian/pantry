package org.pantry.shopping.cases.input;

import org.pantry.shopping.cases.output.FetchToCartInternalResponse;

public interface FetchToCartUC {
    FetchToCartInternalResponse execute(FetchToCartInternalRequest request);
}
