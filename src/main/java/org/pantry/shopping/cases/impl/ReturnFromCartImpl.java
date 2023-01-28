package org.pantry.shopping.cases.impl;

import org.pantry.shopping.cases.data.GatewaysFactory;
import org.pantry.shopping.cases.data.ShoppingCartGateway;
import org.pantry.shopping.cases.data.ShoppingListGateway;
import org.pantry.shopping.cases.input.ReturnFromCartRequest;
import org.pantry.shopping.cases.input.ReturnFromCartUC;
import org.pantry.shopping.cases.output.ReturnFromCartResponse;
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
    public ReturnFromCartResponse execute(ReturnFromCartRequest request) {
        Optional<CartItem> inCart = cart.findById(request.id());
        if (inCart.isEmpty()) return  ReturnFromCartResponse.NOT_FOUND;

        CartItem fromCart = new CartItem(null, request.quantity(), inCart.get().unit(), inCart.get().name(), inCart.get().pricePerUnit(), inCart.get().expiration());
        ListItem toList = new ListItem(null, request.quantity(), fromCart.unit(), fromCart.name());
        return leaveAndAddToList(fromCart, toList);
    }

    ReturnFromCartResponse leaveAndAddToList(CartItem fromCart, ListItem toList) {
        try {
            addToList(toList);
            return leave(fromCart);
        } catch (Exception e) {
            return ReturnFromCartResponse.ERROR;
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
        Double newQuantity = returning.quantity() + alreadyThere.quantity();
        ListItem increased = new ListItem(alreadyThere.id(), newQuantity, alreadyThere.unit(), alreadyThere.name());
        list.updateItem(increased);
    }

    private ReturnFromCartResponse leave(CartItem returned) {
        CartItem before = cart.findSimilar(returned).orElseThrow();
        Double remaining = before.quantity() - returned.quantity();

        if (remaining < 0){
            return ReturnFromCartResponse.TOO_MANY;
        } else if (remaining == 0) {
            cart.removeById(before.id());
            return ReturnFromCartResponse.OK_ALL;
        } else {
            CartItem remainingItem = new CartItem(before.id(), remaining, before.unit(), before.name(), before.pricePerUnit(), before.expiration());
            cart.updateItem(remainingItem);
            return ReturnFromCartResponse.OK_SOME;
        }
    }
}
