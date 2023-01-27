package org.pantry.shopping.cases.impl;

import org.pantry.shopping.cases.data.GatewaysFactory;
import org.pantry.shopping.cases.data.ShoppingCartGateway;
import org.pantry.shopping.cases.data.ShoppingListGateway;
import org.pantry.shopping.cases.input.FetchToCartRequest;
import org.pantry.shopping.cases.input.FetchToCartUC;
import org.pantry.shopping.cases.output.FetchToCartResponse;
import org.pantry.shopping.entities.CartItem;
import org.pantry.shopping.entities.ListItem;

public class FetchToCartImpl implements FetchToCartUC {
    ShoppingCartGateway cart;
    ShoppingListGateway list;

    public FetchToCartImpl(GatewaysFactory databases) {
        cart = databases.getShoppingCartGateway();
        list = databases.getShoppingListGateway();
    }

    @Override
    public FetchToCartResponse execute(FetchToCartRequest request) {
        CartItem toCart = new CartItem(null, request.quantity(), request.unit(), request.name(), request.pricePerUnit(), request.expiration());
        ListItem fromList = new ListItem(null, toCart.quantity(), toCart.unit(), toCart.name());
        return fetchAndScratchFromList(toCart, fromList);
    }

    private FetchToCartResponse fetchAndScratchFromList(CartItem toCart, ListItem fromList) {
        try {
            if (list.existsSimilar(fromList))
                scratchFromList(fromList);
            return fetch(toCart);
        } catch (Exception e) {
            e.printStackTrace();
            return FetchToCartResponse.ERROR;
        }
    }

    private void scratchFromList(ListItem fetched) {
        ListItem before = list.findSimilar(fetched).orElseThrow();
        Double newQuantity = before.quantity() - fetched.quantity();
        ListItem remainingItem = new ListItem(before.id(), newQuantity, before.unit(), before.name());
        if (newQuantity <= 0.0) list.removeById(before.id());
        else list.updateItem(remainingItem);
    }

    private FetchToCartResponse fetch(CartItem toCart) {
        if (cart.existsSimilar(toCart))
            return incrementItem(toCart);
        else
            return addItem(toCart);
    }

    private FetchToCartResponse incrementItem(CartItem increment) {
        CartItem alreadyThere = cart.findSimilar(increment).orElseThrow();
        Double newQuantity = alreadyThere.quantity() + increment.quantity();
        CartItem newItem = new CartItem(null, newQuantity, alreadyThere.unit(), alreadyThere.name(), alreadyThere.pricePerUnit(), alreadyThere.expiration());
        cart.updateItem(newItem);
        return FetchToCartResponse.OK_INCREASED;
    }

    private FetchToCartResponse addItem(CartItem newItem) {
        cart.addItem(newItem);
        return FetchToCartResponse.OK_NEW;
    }
}
