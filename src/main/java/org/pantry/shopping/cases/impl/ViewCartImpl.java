package org.pantry.shopping.cases.impl;

import org.pantry.shopping.cases.data.GatewaysFactory;
import org.pantry.shopping.cases.data.ShoppingCartGateway;
import org.pantry.shopping.cases.input.ViewCartRequest;
import org.pantry.shopping.cases.input.ViewCartUC;
import org.pantry.shopping.cases.output.CartItemResponse;
import org.pantry.shopping.entities.CartItem;

import java.util.List;

public class ViewCartImpl implements ViewCartUC {
    private final ShoppingCartGateway cart;

    public ViewCartImpl(GatewaysFactory databases) {
        this.cart = databases.getShoppingCartGateway();
    }
    public List<CartItemResponse> execute(ViewCartRequest req) {
        List<CartItem> items = cart.findAll();
        return items
                .stream()
                .map(it -> new CartItemResponse(it.id(), it.quantity(), it.unit(), it.name(), it.pricePerUnit(), it.expiration()))
                .toList();
    }
}
