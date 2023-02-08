package org.pantry.shopping.cases.input;

public record FetchToCartInternalRequest(Double quantity, String unit, String name, Integer pricePerUnit, Integer expiration) {
}
