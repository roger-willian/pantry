package org.pantry.shopping.cases.impl;

import org.pantry.shopping.cases.data.GatewaysFactory;
import org.pantry.shopping.cases.data.ShoppingCartGateway;
import org.pantry.shopping.cases.input.ViewCartInternalRequest;
import org.pantry.shopping.cases.input.ViewCartUC;
import org.pantry.shopping.cases.output.CartItemInternalResponse;
import org.pantry.shopping.entities.CartItem;

import java.util.List;

public class ViewCartImpl implements ViewCartUC {
    private final ShoppingCartGateway cart;

    public ViewCartImpl(GatewaysFactory databases) {
        this.cart = databases.getShoppingCartGateway();
    }
    public List<CartItemInternalResponse> execute(ViewCartInternalRequest req) {
        List<CartItem> items = cart.findAll();
        return items.stream().map(this::cartResponseFrom).toList();
    }

    private CartItemInternalResponse cartResponseFrom(CartItem cartitem) {
        return new CartItemInternalResponse(cartitem.id(), cartitem.quantity(), cartitem.unit(), cartitem.name(), cartitem.pricePerUnit(), cartitem.expiration());
    }
}
