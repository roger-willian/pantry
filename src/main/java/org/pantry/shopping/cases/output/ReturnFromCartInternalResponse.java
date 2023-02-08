package org.pantry.shopping.cases.output;

import java.util.Optional;

public record ReturnFromCartInternalResponse(StatusCode status, Optional<CartItemInternalResponse> stillThere) {
    public enum StatusCode {
        OK_ALL,
        OK_SOME,
        TOO_MANY,
        NOT_FOUND,
        INVALID,
        ERROR
    }

    public static ReturnFromCartInternalResponse okAll() {
        return new ReturnFromCartInternalResponse(StatusCode.OK_ALL, Optional.empty());
    }

    public static ReturnFromCartInternalResponse okSome(CartItemInternalResponse stillThere) {
        return new ReturnFromCartInternalResponse(StatusCode.OK_SOME, Optional.of(stillThere));
    }

    public static ReturnFromCartInternalResponse tooMany(CartItemInternalResponse stillThere) {
        return new ReturnFromCartInternalResponse(StatusCode.TOO_MANY, Optional.of(stillThere));
    }

    public static ReturnFromCartInternalResponse notFound() {
        return new ReturnFromCartInternalResponse(StatusCode.NOT_FOUND, Optional.empty());
    }

    public static ReturnFromCartInternalResponse invalid() {
        return new ReturnFromCartInternalResponse(StatusCode.INVALID, Optional.empty());
    }

    public static ReturnFromCartInternalResponse error() {
        return new ReturnFromCartInternalResponse(StatusCode.ERROR, Optional.empty());
    }
}
