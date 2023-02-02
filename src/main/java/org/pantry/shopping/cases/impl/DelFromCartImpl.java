package org.pantry.shopping.cases.impl;

import org.pantry.shopping.cases.data.GatewaysFactory;
import org.pantry.shopping.cases.data.ShoppingCartGateway;
import org.pantry.shopping.cases.input.DelFromCartRequest;
import org.pantry.shopping.cases.input.DelFromCartUC;
import org.pantry.shopping.cases.output.DelFromCartResponse;
import org.pantry.shopping.entities.CartItem;

import java.util.Optional;

public class DelFromCartImpl implements DelFromCartUC {
    private final ShoppingCartGateway cart;

    public DelFromCartImpl(GatewaysFactory databases) {
        cart = databases.getShoppingCartGateway();
    }

    @Override
    public DelFromCartResponse execute(DelFromCartRequest request) {
        Optional<CartItem> fromCart = cart.findById(request.id());
        if (request.quantity() <= 0D) return DelFromCartResponse.INVALID;
        if (fromCart.isEmpty()) return DelFromCartResponse.NOT_FOUND;
        else return removeFromCart(fromCart.get(), request.quantity());
    }

    private DelFromCartResponse removeFromCart(CartItem fromCart, Double quantity) {
        try {
            if (quantity > fromCart.quantity()) return DelFromCartResponse.TOO_MANY;
            else if (quantity.equals(fromCart.quantity())) return removeAll(fromCart);
            else return removeSome(fromCart, quantity);
        } catch (Exception e) {
            return DelFromCartResponse.ERROR;
        }
    }

    private DelFromCartResponse removeAll(CartItem fromCart) {
        cart.removeById(fromCart.id());
        return DelFromCartResponse.OK_ALL;
    }

    private DelFromCartResponse removeSome(CartItem fromCart, Double quantity) {
        Double remaining = fromCart.quantity() - quantity;
        CartItem decreased = new CartItem(fromCart.id(), remaining, fromCart.unit(), fromCart.name(), fromCart.pricePerUnit(), fromCart.expiration());
        cart.updateItem(decreased);
        return DelFromCartResponse.OK_SOME;
    }
}
