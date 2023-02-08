package org.pantry.shopping.cases.impl;

import org.pantry.shopping.cases.data.GatewaysFactory;
import org.pantry.shopping.cases.data.ShoppingListGateway;
import org.pantry.shopping.cases.input.DelFromListInternalRequest;
import org.pantry.shopping.cases.input.DelFromListUC;
import org.pantry.shopping.cases.output.DelFromListInternalResponse;

public class DelFromListImpl implements DelFromListUC {
    private final ShoppingListGateway list;

    public DelFromListImpl(GatewaysFactory gatewaysFactory) {
        this.list = gatewaysFactory.getShoppingListGateway();
    }

    @Override
    public DelFromListInternalResponse execute(DelFromListInternalRequest req) {
        if (list.findById(req.id()).isPresent())
            return removeFromList(req.id());
        else
            return DelFromListInternalResponse.notFound();
    }

    private DelFromListInternalResponse removeFromList(Long id) {
        try {
            list.removeById(id);
            return DelFromListInternalResponse.ok();
        } catch (Exception e) {
            return DelFromListInternalResponse.error();
        }
    }
}
