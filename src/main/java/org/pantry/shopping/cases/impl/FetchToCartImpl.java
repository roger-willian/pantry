package org.pantry.shopping.cases.impl;

import org.pantry.shopping.cases.data.GatewaysFactory;
import org.pantry.shopping.cases.data.ShoppingCartGateway;
import org.pantry.shopping.cases.data.ShoppingListGateway;
import org.pantry.shopping.cases.input.FetchToCartInternalRequest;
import org.pantry.shopping.cases.input.FetchToCartUC;
import org.pantry.shopping.cases.output.CartItemInternalResponse;
import org.pantry.shopping.cases.output.FetchToCartInternalResponse;
import org.pantry.shopping.cases.output.ListItemInternalResponse;
import org.pantry.shopping.entities.CartItem;
import org.pantry.shopping.entities.ListItem;

import java.util.Optional;

public class FetchToCartImpl implements FetchToCartUC {
    protected ShoppingCartGateway cart;
    protected ShoppingListGateway list;

    public FetchToCartImpl(GatewaysFactory databases) {
        cart = databases.getShoppingCartGateway();
        list = databases.getShoppingListGateway();
    }

    @Override
    public FetchToCartInternalResponse execute(FetchToCartInternalRequest request) {
        try {
            CartItem toCart = cartItemFrom(request);
            ListItem fromList = listItemFrom(toCart);
            if (!toCart.isValid() || !fromList.isValid()) return FetchToCartInternalResponse.invalid();

            if (list.existsSimilar(fromList))
                return fetchAndScratchFromList(toCart, fromList);
            else
                return fetchOnly(toCart);
        } catch (Exception e) {
            return FetchToCartInternalResponse.error();
        }
    }

    protected FetchToCartInternalResponse fetchAndScratchFromList(CartItem toCart, ListItem fromList) {
        Optional<ListItemInternalResponse> inList = scratch(fromList);
        Optional<CartItemInternalResponse> inCart = fetchOnly(toCart).inCart();

        return inList.map(it -> FetchToCartInternalResponse.okSome(it, inCart.orElseThrow()))
                .orElseGet(() -> FetchToCartInternalResponse.okAll(inCart.orElseThrow()));
    }

    private Optional<ListItemInternalResponse> scratch(ListItem fromList) {
        ListItem inList = list.findSimilar(fromList).orElseThrow();
        double quantity = inList.quantity() - fromList.quantity();
        if (quantity <= 0D) {
            return scratchAll(inList);
        } else {
            return leaveSome(quantity, inList);
        }
    }

    private Optional<ListItemInternalResponse> scratchAll(ListItem inList) {
        list.removeById(inList.id());
        return Optional.empty();
    }

    private Optional<ListItemInternalResponse> leaveSome(double quantity, ListItem inList) {
        ListItem left = listItemFrom(inList, quantity);
        ListItem updated = list.updateItem(left);
        return Optional.of(listResponseFrom(updated));
    }

    private FetchToCartInternalResponse fetchOnly(CartItem toCart) {
        if (cart.existsSimilar(toCart)) {
            return increaseQuantity(toCart);
        } else {
            return addNewItem(toCart);
        }
    }

    private FetchToCartInternalResponse increaseQuantity(CartItem toCart) {
        CartItem inCart = cart.findSimilar(toCart).orElseThrow();
        double quantity = inCart.quantity() + toCart.quantity();
        CartItem increased = cartItemFrom(inCart, quantity);
        CartItem updated = cart.updateItem(increased);
        CartItemInternalResponse response = cartResponseFrom(updated);
        return FetchToCartInternalResponse.okIncreased(response);
    }

    private FetchToCartInternalResponse addNewItem(CartItem toCart) {
        CartItem added = cart.addItem(toCart);
        CartItemInternalResponse response = cartResponseFrom(added);
        return FetchToCartInternalResponse.okNew(response);
    }

    private CartItemInternalResponse cartResponseFrom(CartItem item) {
        return new CartItemInternalResponse(item.id(), item.quantity(), item.unit(), item.name(), item.pricePerUnit(), item.expiration());
    }

    private ListItemInternalResponse listResponseFrom(ListItem item) {
        return new ListItemInternalResponse(item.id(), item.quantity(), item.unit(), item.name());
    }

    private CartItem cartItemFrom(FetchToCartInternalRequest request) {
        return new CartItem(null, request.quantity(), request.unit(), request.name(), request.pricePerUnit(), request.expiration());
    }

    private ListItem listItemFrom(CartItem item) {
        return new ListItem(null, item.quantity(), item.unit(), item.name());
    }

    private CartItem cartItemFrom(CartItem original, double newQuantity) {
        return new CartItem(original.id(), newQuantity, original.unit(), original.name(), original.pricePerUnit(), original.expiration());
    }

    private ListItem listItemFrom(ListItem original, double quantity) {
        return  new ListItem(original.id(), quantity, original.unit(), original.name());
    }
}
