package org.pantry.shopping.cases.impl;

import org.pantry.shopping.cases.input.ViewListRequest;
import org.pantry.shopping.cases.input.ViewListUC;
import org.pantry.shopping.cases.data.GatewaysFactory;
import org.pantry.shopping.cases.data.ShoppingListGateway;
import org.pantry.shopping.cases.output.ListItemResponse;
import org.pantry.shopping.entities.ListItem;

import java.util.List;

public class ViewListImpl implements ViewListUC {
    private final ShoppingListGateway db;

    public ViewListImpl(GatewaysFactory databases) {
        this.db = databases.getShoppingListGateway();
    }
    public List<ListItemResponse> execute(ViewListRequest req) {
        List<ListItem> items = db.findAll();
        return items
                .stream()
                .map(it -> new ListItemResponse(it.quantity(), it.unit(), it.name()))
                .toList();
    }
}
