package org.pantry.shopping.controllers.shoppinglist.requests;

public record AddToListRequest(Double quantity, String unit, String name) {
}
