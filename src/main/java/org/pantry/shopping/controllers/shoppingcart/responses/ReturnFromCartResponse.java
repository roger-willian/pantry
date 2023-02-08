package org.pantry.shopping.controllers.shoppingcart.responses;

import java.util.Optional;

public record ReturnFromCartResponse(StatusCode status, Optional<CartItemResponse> stillThere) {
    public enum StatusCode {
        OK_ALL,
        OK_SOME,
        TOO_MANY,
        NOT_FOUND,
        INVALID,
        ERROR
    }
}
