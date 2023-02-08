package org.pantry.shopping.cases.input;

import org.pantry.shopping.cases.output.CartItemInternalResponse;

import java.util.List;

public interface ViewCartUC {
    List<CartItemInternalResponse> execute(ViewCartInternalRequest request);
}
