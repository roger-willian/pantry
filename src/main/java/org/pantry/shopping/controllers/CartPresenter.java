package org.pantry.shopping.controllers;

import org.pantry.shopping.cases.output.CartItemResponse;

import java.util.List;

public interface CartPresenter {

    void present(List<CartItemResponse> items);
}
