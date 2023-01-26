package org.pantry.shopping.cases.impl;

import org.pantry.shopping.cases.data.GatewaysFactory;
import org.pantry.shopping.cases.data.ShoppingCartGateway;
import org.pantry.shopping.cases.input.ViewCartRequest;
import org.pantry.shopping.cases.input.ViewCartUC;
import org.pantry.shopping.cases.output.CartItemResponse;
import org.pantry.shopping.entities.CartItem;

import java.util.List;

public class ViewCartImpl implements ViewCartUC {
    private final ShoppingCartGateway db;

    public ViewCartImpl(GatewaysFactory databases) {
        this.db = databases.getShoppingCartGateway();
    }
    public List<CartItemResponse> execute(ViewCartRequest req) {
        List<CartItem> items = db.findAll();
        return items
                .stream()
                .map(it -> new CartItemResponse(it.quantity(), it.unit(), it.name(), it.pricePerUnit(), it.expiration()))
                .toList();
    }
}
