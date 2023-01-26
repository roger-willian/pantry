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
        CartItem savedItem;
        CartItem info = new CartItem(request.quantity(), request.unit(), request.name(), request.pricePerUnit(), request.expiration());
        if (cart.existsSimilar(info))
            savedItem = incrementItem(info);
        else
            savedItem = addItem(info);

        ListItem fetched = new ListItem(info.quantity(), info.unit(), info.name());
        if (list.existsSimilar(fetched))
            scratchFromList(fetched);

        return new FetchToCartResponse(savedItem.quantity(), savedItem.unit(), savedItem.name(), savedItem.pricePerUnit(), savedItem.expiration());
    }

    private void scratchFromList(ListItem fetched) {
        ListItem oldItem = list.findSimilar(fetched).orElseThrow();
        Double newQuantity = oldItem.quantity() - fetched.quantity();
        ListItem remainingItem = new ListItem(newQuantity, oldItem.unit(), oldItem.name());
        if (newQuantity <= 0.0) list.removeSimilar(fetched);
        else list.updateItem(remainingItem);
    }

    private CartItem incrementItem(CartItem increment) {
        CartItem oldItem = cart.findSimilar(increment).orElseThrow();
        Double newQuantity = oldItem.quantity() + increment.quantity();
        CartItem newItem = new CartItem(newQuantity, oldItem.unit(), oldItem.name(), oldItem.pricePerUnit(), oldItem.expiration());
        return cart.updateItem(newItem);
    }

    private CartItem addItem(CartItem newItem) {
        return cart.addItem(newItem);
    }
}
