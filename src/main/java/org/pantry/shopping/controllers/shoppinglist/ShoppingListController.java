package org.pantry.shopping.controllers.shoppinglist;

import org.pantry.shopping.controllers.shoppinglist.presenters.AddToListPresenter;
import org.pantry.shopping.controllers.shoppinglist.presenters.DelFromListPresenter;
import org.pantry.shopping.controllers.shoppinglist.presenters.FetchFromListPresenter;
import org.pantry.shopping.controllers.shoppinglist.presenters.ViewListPresenter;
import org.pantry.shopping.controllers.shoppinglist.requests.AddToListRequest;
import org.pantry.shopping.controllers.shoppinglist.requests.DelFromListRequest;
import org.pantry.shopping.controllers.shoppinglist.requests.FetchFromListRequest;
import org.pantry.shopping.controllers.shoppinglist.requests.ViewListRequest;

public interface ShoppingListController {
    void addToList(AddToListRequest request, AddToListPresenter presenter);
    void delFromList(DelFromListRequest request, DelFromListPresenter presenter);
    void fetchFromList(FetchFromListRequest request, FetchFromListPresenter presenter);
    void viewList(ViewListRequest request, ViewListPresenter presenter);
}
