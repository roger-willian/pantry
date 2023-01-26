package org.pantry.shopping.cases.output;

public record AddToListResponse(Double quantity, String unit, String name) {
    public static AddToListResponse empty() {
        return new AddToListResponse(0.0, "", "");
    }
}
