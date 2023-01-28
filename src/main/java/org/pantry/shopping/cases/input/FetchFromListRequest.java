package org.pantry.shopping.cases.input;

public record FetchFromListRequest(Long id, Double quantity, Integer pricePerUnit, Integer expiration) {
}
