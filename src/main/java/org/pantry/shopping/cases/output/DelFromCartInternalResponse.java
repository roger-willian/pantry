package org.pantry.shopping.cases.output;

import java.util.Optional;

public record DelFromCartInternalResponse(StatusCode status, Optional<CartItemInternalResponse> stillThere) {
    public enum StatusCode {
        OK_ALL,
        OK_SOME,
        NOT_FOUND,
        INVALID,
        TOO_MANY,
        ERROR
    }

    public static DelFromCartInternalResponse okAll() {
        return new DelFromCartInternalResponse(StatusCode.OK_ALL, Optional.empty());
    }

    public static DelFromCartInternalResponse okSome(CartItemInternalResponse stillThere) {
        return new DelFromCartInternalResponse(StatusCode.OK_SOME, Optional.of(stillThere));
    }

    public static DelFromCartInternalResponse notFound() {
        return new DelFromCartInternalResponse(StatusCode.NOT_FOUND, Optional.empty());
    }

    public static DelFromCartInternalResponse invalid() {
        return new DelFromCartInternalResponse(StatusCode.INVALID, Optional.empty());
    }

    public static DelFromCartInternalResponse tooMany(CartItemInternalResponse stillThere) {
        return new DelFromCartInternalResponse(StatusCode.TOO_MANY, Optional.of(stillThere));
    }

    public static DelFromCartInternalResponse error() {
        return new DelFromCartInternalResponse(StatusCode.ERROR, Optional.empty());
    }
}
