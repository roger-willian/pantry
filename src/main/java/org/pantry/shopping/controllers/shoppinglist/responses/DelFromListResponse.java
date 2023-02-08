package org.pantry.shopping.controllers.shoppinglist.responses;

import java.util.Optional;

public record DelFromListResponse(StatusCode status, Optional<ListItemResponse> item) {
    public enum StatusCode {
        OK,
        NOT_FOUND,
        ERROR
    }
}
