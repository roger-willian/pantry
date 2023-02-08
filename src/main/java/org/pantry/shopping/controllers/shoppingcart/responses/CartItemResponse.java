package org.pantry.shopping.controllers.shoppingcart.responses;

public record CartItemResponse(Long id, Double quantity, String unit, String name, Integer pricePerUnit, Integer expiration) {
}
