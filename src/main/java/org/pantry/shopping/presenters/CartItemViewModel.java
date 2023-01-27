package org.pantry.shopping.presenters;

public record CartItemViewModel(
        String id,
        String name,
        String quantity,
        String unit,
        String pricePerUnit,
        String subtotal,
        String expiration) {
}
