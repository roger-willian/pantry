package org.pantry.shopping.cases.impl;

import org.pantry.shopping.cases.data.GatewaysFactory;
import org.pantry.shopping.cases.data.ShoppingListGateway;
import org.pantry.shopping.cases.input.DelFromListRequest;
import org.pantry.shopping.cases.input.DelFromListUC;
import org.pantry.shopping.cases.output.DelFromListResponse;

public class DelFromListImpl implements DelFromListUC {
    private final ShoppingListGateway list;

    public DelFromListImpl(GatewaysFactory gatewaysFactory) {
        this.list = gatewaysFactory.getShoppingListGateway();
    }

    @Override
    public DelFromListResponse execute(DelFromListRequest req) {
        if (list.findById(req.id()).isPresent())
            return removeFromList(req.id());
        else
            return DelFromListResponse.NOT_FOUND;
    }

    private DelFromListResponse removeFromList(Long id) {
        try {
            list.removeById(id);
            return DelFromListResponse.OK;
        } catch (Exception e) {
            return DelFromListResponse.ERROR;
        }
    }
}
