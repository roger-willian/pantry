package org.pantry.shopping.cases.impl;

import org.pantry.shopping.cases.data.ShoppingListGateway;
import org.pantry.shopping.cases.data.GatewaysFactory;
import org.pantry.shopping.cases.input.AddToListInternalRequest;
import org.pantry.shopping.cases.input.AddToListUC;
import org.pantry.shopping.cases.output.AddToListInternalResponse;
import org.pantry.shopping.cases.output.ListItemInternalResponse;
import org.pantry.shopping.entities.ListItem;

public class AddToListImpl implements AddToListUC {
    private final ShoppingListGateway list;

    public AddToListImpl(GatewaysFactory gatewaysFactory) {
        this.list = gatewaysFactory.getShoppingListGateway();
    }

    @Override
    public AddToListInternalResponse execute(AddToListInternalRequest request) {
        try {
            ListItem item = fromRequest(request);
            if (list.existsSimilar(item))
                return increaseInList(item);
            else
                return insertIntoList(item);
        } catch (Exception e) {
            return AddToListInternalResponse.error();
        }
    }

    private AddToListInternalResponse insertIntoList(ListItem newItem) {
        if (!newItem.isValid()) {
            return AddToListInternalResponse.invalid();
        } else {
            ListItem saved = list.addItem(newItem);
            ListItemInternalResponse response = toResponse(saved);
            return AddToListInternalResponse.okNew(response);
        }
    }

    private AddToListInternalResponse increaseInList(ListItem increment) {
        ListItem alreadyThere = list.findSimilar(increment).orElseThrow();
        double newQuantity = increment.quantity() + alreadyThere.quantity();
        ListItem increased = listItemFrom(alreadyThere, newQuantity);
        ListItem saved = list.updateItem(increased);
        ListItemInternalResponse response = toResponse(saved);
        return AddToListInternalResponse.okIncreased(response);
    }

    private ListItem fromRequest(AddToListInternalRequest request) {
        return new ListItem(null, request.quantity(), request.unit(), request.name());
    }

    private ListItemInternalResponse toResponse(ListItem item) {
        return new ListItemInternalResponse(item.id(), item.quantity(), item.unit(), item.name());
    }

    private ListItem listItemFrom(ListItem original, double newQuantity) {
        return new ListItem(original.id(), newQuantity, original.unit(), original.name());
    }
}
