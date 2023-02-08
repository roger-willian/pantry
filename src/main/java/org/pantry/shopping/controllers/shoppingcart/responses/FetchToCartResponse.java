package org.pantry.shopping.controllers.shoppingcart.responses;

import java.util.Optional;

public record FetchToCartResponse(StatusCode status, Optional<CartItemResponse> item) {
    public enum StatusCode {
        OK_NEW,
        OK_INCREASED,
        OK_ALL,
        OK_SOME,
        INVALID,
        ERROR
    }
}
