package org.pantry.shopping.cases.impl;

import org.pantry.shopping.cases.data.GatewaysFactory;
import org.pantry.shopping.cases.input.FetchFromListInternalRequest;
import org.pantry.shopping.cases.input.FetchFromListUC;
import org.pantry.shopping.cases.output.CartItemInternalResponse;
import org.pantry.shopping.cases.output.FetchFromListInternalResponse;
import org.pantry.shopping.cases.output.FetchToCartInternalResponse;
import org.pantry.shopping.cases.output.ListItemInternalResponse;
import org.pantry.shopping.entities.CartItem;
import org.pantry.shopping.entities.ListItem;

import java.util.Optional;

public class FetchFromListImpl extends FetchToCartImpl implements FetchFromListUC {

    public FetchFromListImpl(GatewaysFactory databases) {
        super(databases);
    }

    @Override
    public FetchFromListInternalResponse execute(FetchFromListInternalRequest request) {
        Optional<ListItem> inList = list.findById(request.id());
        if (inList.isEmpty()) return FetchFromListInternalResponse.notFound();

        ListItem fromList = listItemMerging(inList.get(), request);
        CartItem toCart = cartItemMerging(inList.get(), request);

        if (!fromList.isValid() || !toCart.isValid()) return FetchFromListInternalResponse.invalid();

        FetchToCartInternalResponse response = fetchAndScratchFromList(toCart, fromList);
        return responseFrom(response);
    }

    private FetchFromListInternalResponse responseFrom(FetchToCartInternalResponse response) {
        try {
            Optional<ListItemInternalResponse> inList = response.inList();
            Optional<CartItemInternalResponse> inCart = response.inCart();
            FetchToCartInternalResponse.StatusCode status = response.status();
            return switch (status) {
                case OK_ALL -> FetchFromListInternalResponse.okAll(inCart.orElseThrow());
                case OK_SOME -> FetchFromListInternalResponse.okSome(inList.orElseThrow(), inCart.orElseThrow());
                default -> FetchFromListInternalResponse.error();
            };
        } catch (Exception e) {
            return FetchFromListInternalResponse.error();
        }
    }

    private ListItem listItemMerging(ListItem original, FetchFromListInternalRequest request) {
        return new ListItem(null, request.quantity(), original.unit(), original.name());
    }

    private CartItem cartItemMerging(ListItem listItem, FetchFromListInternalRequest request) {
        return new CartItem(null, request.quantity(), listItem.unit(), listItem.name(), request.pricePerUnit(), request.expiration());
    }
}
