package org.pantry.shopping.cases.input;

import org.pantry.shopping.cases.output.FetchToCartResponse;

public interface FetchToCartUC {
    FetchToCartResponse execute(FetchToCartRequest request);
}
