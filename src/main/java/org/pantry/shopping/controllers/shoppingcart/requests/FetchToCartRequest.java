package org.pantry.shopping.controllers.shoppingcart.requests;

public record FetchToCartRequest(Double quantity, String unit, String name, Integer pricePerUnit, Integer expiration) {
}
