package org.pantry.shopping.controllers.shoppinglist.requests;

public record FetchFromListRequest(Long id, Double quantity, Integer pricePerUnit, Integer expiration) {
}
