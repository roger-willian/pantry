package org.pantry.shopping.cases.output;

import java.util.Optional;

public record AddToListInternalResponse(StatusCode status, Optional<ListItemInternalResponse> item) {

    public enum StatusCode {
        OK_NEW,
        OK_INCREASED,
        INVALID,
        ERROR
    }

    public static AddToListInternalResponse invalid() {
        return new AddToListInternalResponse(StatusCode.INVALID, Optional.empty());
    }

    public static AddToListInternalResponse okNew(ListItemInternalResponse item) {
        return new AddToListInternalResponse(StatusCode.OK_NEW, Optional.of(item));
    }

    public static AddToListInternalResponse okIncreased(ListItemInternalResponse item) {
        return new AddToListInternalResponse(StatusCode.OK_INCREASED, Optional.of(item));
    }

    public static AddToListInternalResponse error() {
        return new AddToListInternalResponse(StatusCode.ERROR, Optional.empty());
    }
}
