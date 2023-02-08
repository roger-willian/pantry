package org.pantry.shopping.cases.impl;

import org.pantry.shopping.cases.input.ViewListInternalRequest;
import org.pantry.shopping.cases.input.ViewListUC;
import org.pantry.shopping.cases.data.GatewaysFactory;
import org.pantry.shopping.cases.data.ShoppingListGateway;
import org.pantry.shopping.cases.output.ListItemInternalResponse;
import org.pantry.shopping.entities.ListItem;

import java.util.List;

public class ViewListImpl implements ViewListUC {
    private final ShoppingListGateway list;

    public ViewListImpl(GatewaysFactory databases) {
        this.list = databases.getShoppingListGateway();
    }
    public List<ListItemInternalResponse> execute(ViewListInternalRequest req) {
        List<ListItem> items = list.findAll();
        return items.stream().map(this::listResponseFrom).toList();
    }

    private ListItemInternalResponse listResponseFrom(ListItem listItem) {
        return new ListItemInternalResponse(listItem.id(), listItem.quantity(), listItem.unit(), listItem.name());
    }
}
