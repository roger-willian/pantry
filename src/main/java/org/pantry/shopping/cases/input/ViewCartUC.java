package org.pantry.shopping.cases.input;

import org.pantry.shopping.cases.output.CartItemResponse;

import java.util.List;

public interface ViewCartUC {
    List<CartItemResponse> execute(ViewCartRequest request);
}
