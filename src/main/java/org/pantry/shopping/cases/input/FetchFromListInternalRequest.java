package org.pantry.shopping.cases.input;

public record FetchFromListInternalRequest(Long id, Double quantity, Integer pricePerUnit, Integer expiration) {
}
