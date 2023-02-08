package org.pantry.shopping.controllers.shoppinglist.responses;

import java.util.Optional;

public record FetchFromListResponse(StatusCode status, Optional<ListItemResponse> stillThere) {
    public enum StatusCode {
        OK_ALL,
        OK_SOME,
        NOT_FOUND,
        INVALID,
        ERROR
    }
}
