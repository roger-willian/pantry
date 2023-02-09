package org.pantry.shopping.controllers.shoppinglist.responses;

import java.util.List;

public record ViewListResponse(StatusCode status, List<ListItemResponse> items) {
    public enum StatusCode {
        OK,
        ERROR
    }
}
