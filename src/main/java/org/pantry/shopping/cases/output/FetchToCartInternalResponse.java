package org.pantry.shopping.cases.output;

import java.util.Optional;

public record FetchToCartInternalResponse(StatusCode status, Optional<ListItemInternalResponse> inList, Optional<CartItemInternalResponse> inCart) {
    public enum StatusCode {
        OK_NEW,
        OK_INCREASED,
        OK_ALL,
        OK_SOME,
        INVALID,
        ERROR
    }

    public static FetchToCartInternalResponse okNew(CartItemInternalResponse inCart) {
        return new FetchToCartInternalResponse(StatusCode.OK_NEW, Optional.empty(), Optional.of(inCart));
    }

    public static FetchToCartInternalResponse okIncreased(CartItemInternalResponse inCart) {
        return new FetchToCartInternalResponse(StatusCode.OK_INCREASED, Optional.empty(), Optional.of(inCart));
    }

    public static FetchToCartInternalResponse okAll(CartItemInternalResponse inCart) {
        return new FetchToCartInternalResponse(StatusCode.OK_ALL, Optional.empty(), Optional.of(inCart));
    }

    public static FetchToCartInternalResponse okSome(ListItemInternalResponse inList, CartItemInternalResponse inCart) {
        return new FetchToCartInternalResponse(StatusCode.OK_SOME, Optional.of(inList), Optional.of(inCart));
    }

    public static FetchToCartInternalResponse invalid() {
        return new FetchToCartInternalResponse(StatusCode.INVALID, Optional.empty(), Optional.empty());
    }

    public static FetchToCartInternalResponse error() {
        return new FetchToCartInternalResponse(StatusCode.ERROR, Optional.empty(), Optional.empty());
    }
}
