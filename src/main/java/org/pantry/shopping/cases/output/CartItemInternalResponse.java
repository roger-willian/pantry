package org.pantry.shopping.cases.output;

public record CartItemInternalResponse(Long id, Double quantity, String unit, String name, Integer pricePerUnit, Integer expiration) {
}
