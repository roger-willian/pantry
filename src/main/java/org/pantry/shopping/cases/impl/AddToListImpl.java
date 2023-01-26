package org.pantry.shopping.cases.impl;

import org.pantry.shopping.cases.data.ShoppingListGateway;
import org.pantry.shopping.cases.data.GatewaysFactory;
import org.pantry.shopping.cases.input.AddToListRequest;
import org.pantry.shopping.cases.input.AddToListUC;
import org.pantry.shopping.cases.output.AddToShoppingListResponse;
import org.pantry.shopping.entities.ListItem;

public class AddToListImpl implements AddToListUC {
    private final ShoppingListGateway db;

    public AddToListImpl(GatewaysFactory gatewaysFactory) {
        this.db = gatewaysFactory.getShoppingListGateway();
    }

    @Override
    public AddToShoppingListResponse execute(AddToListRequest req) {
        ListItem savedItem;
        ListItem info = new ListItem(req.quantity(), req.unit(), req.name());
        if (db.existsSimilar(info))
            savedItem = incrementItem(info);
        else
            savedItem = addNewItem(info);

        return new AddToShoppingListResponse(savedItem.quantity(), savedItem.unit(), savedItem.name());
    }

    private ListItem addNewItem(ListItem newItem) {
        return db.addItem(newItem);
    }

    private ListItem incrementItem(ListItem increment) {
        ListItem oldItem = db.findSimilar(increment).orElseThrow();
        Double newQuantity = increment.quantity() + oldItem.quantity();
        ListItem newItem = new ListItem(newQuantity, oldItem.unit(), oldItem.name());
        return db.updateItem(newItem);
    }
}
