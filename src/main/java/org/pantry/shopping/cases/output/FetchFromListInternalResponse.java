package org.pantry.shopping.cases.output;

import java.util.Optional;

public record FetchFromListInternalResponse(StatusCode status, Optional<ListItemInternalResponse> inList, Optional<CartItemInternalResponse> inCart) {
    public enum StatusCode {
        OK_ALL,
        OK_SOME,
        NOT_FOUND,
        ERROR,
        INVALID
    }

    public static FetchFromListInternalResponse okAll(CartItemInternalResponse inCart) {
        return new FetchFromListInternalResponse(StatusCode.OK_ALL, Optional.empty(), Optional.of(inCart));
    }

    public static FetchFromListInternalResponse okSome(ListItemInternalResponse inList, CartItemInternalResponse inCart) {
        return new FetchFromListInternalResponse(StatusCode.OK_SOME, Optional.of(inList), Optional.of(inCart));
    }

    public static FetchFromListInternalResponse notFound() {
        return new FetchFromListInternalResponse(StatusCode.NOT_FOUND, Optional.empty(), Optional.empty());
    }

    public static FetchFromListInternalResponse error() {
        return new FetchFromListInternalResponse(StatusCode.ERROR, Optional.empty(), Optional.empty());
    }

    public static FetchFromListInternalResponse invalid() {
        return new FetchFromListInternalResponse(StatusCode.INVALID, Optional.empty(), Optional.empty());
    }
}
