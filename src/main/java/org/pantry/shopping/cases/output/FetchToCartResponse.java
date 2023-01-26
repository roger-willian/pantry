package org.pantry.shopping.cases.output;

public record FetchToCartResponse(Double quantity, String unit, String name, Integer integer, Integer expiration) {
}
