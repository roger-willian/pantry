package org.pantry.shopping.cases.impl;

import org.pantry.shopping.cases.data.GatewaysFactory;
import org.pantry.shopping.cases.input.FetchFromListRequest;
import org.pantry.shopping.cases.input.FetchFromListUC;
import org.pantry.shopping.cases.output.FetchFromListResponse;
import org.pantry.shopping.cases.output.FetchToCartResponse;
import org.pantry.shopping.entities.CartItem;
import org.pantry.shopping.entities.ListItem;

import java.util.Optional;

public class FetchFromListImpl extends FetchToCartImpl implements FetchFromListUC {

    public FetchFromListImpl(GatewaysFactory databases) {
        super(databases);
    }

    @Override
    public FetchFromListResponse execute(FetchFromListRequest request) {
        Optional<ListItem> inList = list.findById(request.id());
        if (inList.isEmpty()) return FetchFromListResponse.NOT_FOUND;
        ListItem fromList = new ListItem(null, request.quantity(), inList.get().unit(), inList.get().name());
        CartItem toCart = new CartItem(null, request.quantity(), fromList.unit(), fromList.name(), request.pricePerUnit(), request.expiration());

        FetchToCartResponse response = fetchAndScratchFromList(toCart, fromList);

        if (response == FetchToCartResponse.OK_ALL) return FetchFromListResponse.OK_ALL;
        else if (response == FetchToCartResponse.OK_SOME) return FetchFromListResponse.OK_SOME;
        else return FetchFromListResponse.ERROR;
    }
}
