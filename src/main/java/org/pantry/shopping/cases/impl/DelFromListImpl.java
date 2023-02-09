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
        try {
            if (list.findById(req.id()).isPresent())
                return removeFromList(req.id());
            else
                return DelFromListInternalResponse.notFound();
        } catch (Exception e) {
            return DelFromListInternalResponse.error();
        }
    }

    private DelFromListInternalResponse removeFromList(Long id) {
        list.removeById(id);
        return DelFromListInternalResponse.ok();
    }
}
