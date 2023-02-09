package org.pantry.shopping.cases.output;

import java.util.List;

public record ViewCartInternalResponse(StatusCode status, List<CartItemInternalResponse> items) {
    public enum StatusCode {
        OK,
        ERROR
    }

    public static ViewCartInternalResponse ok(List<CartItemInternalResponse> items) {
        return new ViewCartInternalResponse(StatusCode.OK, items);
    }

    public static ViewCartInternalResponse error() {
        return new ViewCartInternalResponse(StatusCode.ERROR, List.of());
    }
}
