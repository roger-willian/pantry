package org.pantry.shopping.controllers.shoppingcart.presenters;

import org.pantry.shopping.controllers.shoppingcart.responses.CartItemResponse;

import java.util.List;

public interface ViewCartPresenter {
    void present(List<CartItemResponse> response);
}
