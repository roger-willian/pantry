package org.pantry.shopping.cases.output;

import java.util.List;

public record ViewListInternalResponse(StatusCode status, List<ListItemInternalResponse> items) {
    public enum StatusCode {
        OK,
        ERROR
    }

    public static ViewListInternalResponse ok(List<ListItemInternalResponse> items) {
        return new ViewListInternalResponse(StatusCode.OK, items);
    }

    public static ViewListInternalResponse error() {
        return new ViewListInternalResponse(StatusCode.ERROR, List.of());
    }
}
