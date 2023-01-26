package org.pantry.shopping.cases.output;

public record CartItemResponse(Double quantity, String unit, String name, Integer pricePerUnit, Integer expiration) {
}
