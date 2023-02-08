package org.pantry.shopping.controllers.shoppingcart.responses;

import java.util.Optional;

public record DelFromCartResponse(StatusCode status, Optional<CartItemResponse> stillThere) {
    public enum StatusCode {
        OK_ALL,
        OK_SOME,
        NOT_FOUND,
        INVALID,
        TOO_MANY,
        ERROR
    }
}
