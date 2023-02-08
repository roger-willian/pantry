package org.pantry.shopping.cases.impl;

import org.pantry.shopping.cases.data.GatewaysFactory;
import org.pantry.shopping.cases.data.ShoppingCartGateway;
import org.pantry.shopping.cases.input.DelFromCartInternalRequest;
import org.pantry.shopping.cases.input.DelFromCartUC;
import org.pantry.shopping.cases.output.CartItemInternalResponse;
import org.pantry.shopping.cases.output.DelFromCartInternalResponse;
import org.pantry.shopping.entities.CartItem;

import java.util.Optional;

public class DelFromCartImpl implements DelFromCartUC {
    private final ShoppingCartGateway cart;

    public DelFromCartImpl(GatewaysFactory databases) {
        cart = databases.getShoppingCartGateway();
    }

    @Override
    public DelFromCartInternalResponse execute(DelFromCartInternalRequest request) {
        try {
            Optional<CartItem> fromCart = cart.findById(request.id());
            if (request.quantity() <= 0D) return DelFromCartInternalResponse.invalid();
            if (fromCart.isEmpty()) return DelFromCartInternalResponse.notFound();
            else return removeFromCart(fromCart.get(), request.quantity());
        } catch (Exception e) {
            return DelFromCartInternalResponse.error();
        }
    }

    private DelFromCartInternalResponse removeFromCart(CartItem fromCart, double quantity) {
        if (quantity > fromCart.quantity()) {
            CartItemInternalResponse response = cartResponseFrom(fromCart);
            return DelFromCartInternalResponse.tooMany(response);
        } else if (quantity == fromCart.quantity()) {
            return removeAll(fromCart);
        } else {
            return removeSome(fromCart, quantity);
        }
    }

    private CartItemInternalResponse cartResponseFrom(CartItem fromCart) {
        return new CartItemInternalResponse(fromCart.id(), fromCart.quantity(), fromCart.unit(), fromCart.name(), fromCart.pricePerUnit(), fromCart.expiration());
    }

    private DelFromCartInternalResponse removeAll(CartItem fromCart) {
        cart.removeById(fromCart.id());
        return DelFromCartInternalResponse.okAll();
    }

    private DelFromCartInternalResponse removeSome(CartItem fromCart, Double quantity) {
        double remaining = fromCart.quantity() - quantity;
        CartItem decreased = cartItemFrom(fromCart, remaining);
        CartItem updated = cart.updateItem(decreased);
        CartItemInternalResponse response = cartResponseFrom(updated);
        return DelFromCartInternalResponse.okSome(response);
    }

    private CartItem cartItemFrom(CartItem original, double newQuantity) {
        return new CartItem(original.id(), newQuantity, original.unit(), original.name(), original.pricePerUnit(), original.expiration());
    }
}
