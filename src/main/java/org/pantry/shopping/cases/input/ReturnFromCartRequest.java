package org.pantry.shopping.cases.input;

public record ReturnFromCartRequest(Double quantity, String unit, String name, Integer pricePerUnit, Integer expiration) {
}
