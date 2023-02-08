package org.pantry.shopping.controllers.shoppinglist.presenters;

import org.pantry.shopping.controllers.shoppinglist.responses.ListItemResponse;

import java.util.List;

public interface ViewListPresenter {
    void present(List<ListItemResponse> items);
}
