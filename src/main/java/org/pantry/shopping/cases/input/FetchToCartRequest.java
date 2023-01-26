package org.pantry.shopping.cases.input;

public record FetchToCartRequest(Double quantity, String unit, String name, Integer pricePerUnit, Integer expiration) {
}
