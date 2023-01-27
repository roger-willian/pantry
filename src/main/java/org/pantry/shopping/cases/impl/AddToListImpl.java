package org.pantry.shopping.cases.impl;

import org.pantry.shopping.cases.data.ShoppingListGateway;
import org.pantry.shopping.cases.data.GatewaysFactory;
import org.pantry.shopping.cases.input.AddToListRequest;
import org.pantry.shopping.cases.input.AddToListUC;
import org.pantry.shopping.cases.output.AddToListResponse;
import org.pantry.shopping.entities.ListItem;

public class AddToListImpl implements AddToListUC {
    private final ShoppingListGateway list;

    public AddToListImpl(GatewaysFactory gatewaysFactory) {
        this.list = gatewaysFactory.getShoppingListGateway();
    }

    @Override
    public AddToListResponse execute(AddToListRequest req) {
        ListItem savedItem;
        ListItem info = new ListItem(null, req.quantity(), req.unit(), req.name());
        if (list.existsSimilar(info))
            return increaseInList(info);
        else
            return insertIntoList(info);
    }

    private AddToListResponse insertIntoList(ListItem newItem) {
        try {
            list.addItem(newItem);
            return AddToListResponse.OK_NEW;
        } catch (Exception e) {
            return AddToListResponse.ERROR;
        }
    }

    private AddToListResponse increaseInList(ListItem increment) {
        try {
            ListItem alreadyThere = list.findSimilar(increment).orElseThrow();
            Double newQuantity = increment.quantity() + alreadyThere.quantity();
            ListItem increased = new ListItem(alreadyThere.id(), newQuantity, alreadyThere.unit(), alreadyThere.name());
            list.updateItem(increased);
            return AddToListResponse.OK_INCREASED;
        } catch (Exception e) {
            return AddToListResponse.ERROR;
        }
    }
}
