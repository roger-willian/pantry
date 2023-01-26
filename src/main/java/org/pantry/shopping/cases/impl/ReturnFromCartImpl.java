package org.pantry.shopping.cases.impl;

import org.pantry.shopping.cases.data.GatewaysFactory;
import org.pantry.shopping.cases.data.ShoppingCartGateway;
import org.pantry.shopping.cases.data.ShoppingListGateway;
import org.pantry.shopping.cases.input.ReturnFromCartRequest;
import org.pantry.shopping.cases.input.ReturnFromCartUC;
import org.pantry.shopping.cases.output.ReturnFromCartResponse;
import org.pantry.shopping.entities.CartItem;
import org.pantry.shopping.entities.ListItem;

public class ReturnFromCartImpl implements ReturnFromCartUC {

    private final ShoppingCartGateway cart;
    private final ShoppingListGateway list;

    public ReturnFromCartImpl(GatewaysFactory databases) {
        cart = databases.getShoppingCartGateway();
        list = databases.getShoppingListGateway();
    }

    @Override
    public ReturnFromCartResponse execute(ReturnFromCartRequest request) {
        ListItem savedItem;
        ListItem info = new ListItem(request.quantity(), request.unit(), request.name());
        if (list.existsSimilar(info))
            savedItem = increaseInList(info);
        else
            savedItem = insertIntoList(info);

        CartItem returned = new CartItem(request.quantity(), request.unit(), request.name(), request.pricePerUnit(), request.expiration());
        removeFromCart(returned);
        return new ReturnFromCartResponse();
    }

    private void removeFromCart(CartItem returned) {
        CartItem before = cart.findSimilar(returned).orElseThrow();
        Double remaining = before.quantity() - returned.quantity();
        if (remaining > 0.0){
            CartItem remainingItems = new CartItem(remaining, before.unit(), before.name(), before.pricePerUnit(), before.expiration());
            cart.updateItem(remainingItems);
        } else if (remaining == 0.0) {
            cart.removeSimilar(before);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private ListItem insertIntoList(ListItem newItem) {
        return list.addItem(newItem);
    }

    private ListItem increaseInList(ListItem returning) {
        ListItem alreadyThere = list.findSimilar(returning).orElseThrow();
        Double newQuantity = alreadyThere.quantity() + returning.quantity();
        ListItem increased = new ListItem(newQuantity, alreadyThere.unit(), alreadyThere.name());
        return list.updateItem(increased);
    }
}
