package org.pantry.shopping.cases.impl;

import org.pantry.shopping.cases.data.GatewaysFactory;
import org.pantry.shopping.cases.data.ShoppingListGateway;
import org.pantry.shopping.cases.input.DelFromListRequest;
import org.pantry.shopping.cases.input.DelFromListUC;
import org.pantry.shopping.cases.output.DelFromListResponse;
import org.pantry.shopping.entities.ListItem;

public class DelFromListImpl implements DelFromListUC {
    private final ShoppingListGateway db;

    public DelFromListImpl(GatewaysFactory gatewaysFactory) {
        this.db = gatewaysFactory.getShoppingListGateway();
    }

    @Override
    public DelFromListResponse execute(DelFromListRequest req) {
        ListItem remove = new ListItem(0.0, req.unit(), req.name());
        db.removeSimilar(remove);
        return new DelFromListResponse();
    }
}
