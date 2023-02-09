package org.pantry.shopping.controllers.shoppingcart.responses;

import java.util.List;

public record ViewCartResponse(StatusCode status, List<CartItemResponse> items) {
    public enum StatusCode {
        OK,
        ERROR
    }
}
