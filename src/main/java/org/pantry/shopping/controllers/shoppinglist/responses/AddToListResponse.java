package org.pantry.shopping.controllers.shoppinglist.responses;

import java.util.Optional;

public record AddToListResponse(StatusCode status, Optional<ListItemResponse> item) {
    public enum StatusCode {
        OK_NEW,
        OK_INCREASED,
        INVALID,
        ERROR
    }

    public static AddToListResponse okNew(ListItemResponse item) {
        return new AddToListResponse(StatusCode.OK_NEW, Optional.of(item));
    }

    public static AddToListResponse okIncreased(ListItemResponse item) {
        return new AddToListResponse(StatusCode.OK_INCREASED, Optional.of(item));
    }

    public static AddToListResponse invalid() {
        return new AddToListResponse(StatusCode.INVALID, Optional.empty());
    }

    public static AddToListResponse error() {
        return new AddToListResponse(StatusCode.ERROR, Optional.empty());
    }
}
