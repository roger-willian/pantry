package org.pantry.shopping.cases.output;

public record AddToShoppingListResponse(Double quantity, String unit, String name) {
    public static AddToShoppingListResponse empty() {
        return new AddToShoppingListResponse(0.0, "", "");
    }
}
