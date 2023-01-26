package org.pantry.shopping.presenters;

import java.util.List;

public record CartViewModel(List<CartItemViewModel> items, String totalPrice) {
}
