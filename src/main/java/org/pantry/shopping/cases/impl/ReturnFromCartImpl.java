package org.pantry.shopping.cases.impl;

import org.pantry.shopping.cases.data.GatewaysFactory;
import org.pantry.shopping.cases.data.ShoppingCartGateway;
import org.pantry.shopping.cases.data.ShoppingListGateway;
import org.pantry.shopping.cases.input.ReturnFromCartInternalRequest;
import org.pantry.shopping.cases.input.ReturnFromCartUC;
import org.pantry.shopping.cases.output.CartItemInternalResponse;
import org.pantry.shopping.cases.output.ReturnFromCartInternalResponse;
import org.pantry.shopping.entities.CartItem;
import org.pantry.shopping.entities.ListItem;

import java.util.Optional;

public class ReturnFromCartImpl implements ReturnFromCartUC {

    private final ShoppingCartGateway cart;
    private final ShoppingListGateway list;

    public ReturnFromCartImpl(GatewaysFactory databases) {
        cart = databases.getShoppingCartGateway();
        list = databases.getShoppingListGateway();
    }

    @Override
    public ReturnFromCartInternalResponse execute(ReturnFromCartInternalRequest request) {
        Optional<CartItem> inCart = cart.findById(request.id());
        if (inCart.isEmpty()) return ReturnFromCartInternalResponse.notFound();

        CartItem fromCart = cartItemMerging(inCart.get(), request);
        ListItem toList = listItemMerging(fromCart, request);

        if (!fromCart.isValid() || !toList.isValid()) return ReturnFromCartInternalResponse.invalid();
        return removeAndAddToList(fromCart, toList);
    }

    ReturnFromCartInternalResponse removeAndAddToList(CartItem fromCart, ListItem toList) {
        try {
            ReturnFromCartInternalResponse response = remove(fromCart);
            if (response.status() != ReturnFromCartInternalResponse.StatusCode.TOO_MANY)
                addToList(toList);
            return response;
        } catch (Exception e) {
            return ReturnFromCartInternalResponse.error();
        }
    }

    private void addToList(ListItem toList) {
        if (list.existsSimilar(toList))
            increaseInList(toList);
        else
            insertIntoList(toList);
    }

    private void insertIntoList(ListItem newItem) {
        list.addItem(newItem);
    }

    private void increaseInList(ListItem returning) {
        ListItem alreadyThere = list.findSimilar(returning).orElseThrow();
        double newQuantity = returning.quantity() + alreadyThere.quantity();
        ListItem increased = listItemFrom(alreadyThere, newQuantity);
        list.updateItem(increased);
    }

    private ReturnFromCartInternalResponse remove(CartItem returned) {
        CartItem inCart = cart.findSimilar(returned).orElseThrow();
        double remaining = inCart.quantity() - returned.quantity();

        if (remaining < 0){
            CartItemInternalResponse response = cartResponseFrom(inCart);
            return ReturnFromCartInternalResponse.tooMany(response);
        } else if (remaining == 0) {
            return removeAll(inCart);
        } else {
            return leaveSome(remaining, inCart);
        }
    }

    private ReturnFromCartInternalResponse removeAll(CartItem returned) {
        cart.removeById(returned.id());
        return ReturnFromCartInternalResponse.okAll();
    }

    private ReturnFromCartInternalResponse leaveSome(double quantity, CartItem inCart) {
        CartItem leftInCart = cartItemFrom(inCart, quantity);
        CartItem updated = cart.updateItem(leftInCart);
        CartItemInternalResponse response = cartResponseFrom(updated);
        return ReturnFromCartInternalResponse.okSome(response);
    }

    private CartItem cartItemMerging(CartItem original, ReturnFromCartInternalRequest request) {
        return new CartItem(null, request.quantity(), original.unit(), original.name(), original.pricePerUnit(), original.expiration());
    }

    private ListItem listItemMerging(CartItem cartItem, ReturnFromCartInternalRequest request) {
        return new ListItem(null, request.quantity(), cartItem.unit(), cartItem.name());
    }

    private ListItem listItemFrom(ListItem original, double newQuantity) {
        return new ListItem(original.id(), newQuantity, original.unit(), original.name());
    }

    private CartItem cartItemFrom(CartItem original, double newQuantity) {
        return new CartItem(original.id(), newQuantity, original.unit(), original.name(), original.pricePerUnit(), original.expiration());
    }

    private CartItemInternalResponse cartResponseFrom(CartItem cartItem) {
        return new CartItemInternalResponse(cartItem.id(), cartItem.quantity(), cartItem.unit(), cartItem.name(), cartItem.pricePerUnit(), cartItem.expiration());
    }
}
