package org.pantry.shopping.controllers.shoppinglist;

import org.pantry.shopping.cases.ShoppingCasesFactory;
import org.pantry.shopping.cases.input.*;
import org.pantry.shopping.cases.output.AddToListInternalResponse;
import org.pantry.shopping.cases.output.DelFromListInternalResponse;
import org.pantry.shopping.cases.output.FetchFromListInternalResponse;
import org.pantry.shopping.cases.output.ListItemInternalResponse;
import org.pantry.shopping.controllers.shoppinglist.presenters.AddToListPresenter;
import org.pantry.shopping.controllers.shoppinglist.presenters.DelFromListPresenter;
import org.pantry.shopping.controllers.shoppinglist.presenters.FetchFromListPresenter;
import org.pantry.shopping.controllers.shoppinglist.presenters.ViewListPresenter;
import org.pantry.shopping.controllers.shoppinglist.requests.AddToListRequest;
import org.pantry.shopping.controllers.shoppinglist.requests.DelFromListRequest;
import org.pantry.shopping.controllers.shoppinglist.requests.FetchFromListRequest;
import org.pantry.shopping.controllers.shoppinglist.requests.ViewListRequest;
import org.pantry.shopping.controllers.shoppinglist.responses.AddToListResponse;
import org.pantry.shopping.controllers.shoppinglist.responses.DelFromListResponse;
import org.pantry.shopping.controllers.shoppinglist.responses.FetchFromListResponse;
import org.pantry.shopping.controllers.shoppinglist.responses.ListItemResponse;

import java.util.List;
import java.util.Optional;

public class ShoppingListControllerImpl implements ShoppingListController {

    private final ViewListUC viewList;
    private final AddToListUC addToList;
    private final DelFromListUC delFromList;
    private final FetchFromListUC fetchFromList;

    public ShoppingListControllerImpl(ShoppingCasesFactory cases) {
        viewList = cases.getViewListUC();
        addToList = cases.getAddToShoppingList();
        delFromList = cases.getDelFromShoppingList();
        fetchFromList = cases.getFetchFromListUC();
    }

    @Override
    public void addToList(AddToListRequest request, AddToListPresenter presenter) {
        AddToListInternalRequest internalRequest = internalRequestFrom(request);
        AddToListInternalResponse internalResponse = addToList.execute(internalRequest);
        AddToListResponse response = responseFrom(internalResponse);

        presenter.present(response);
    }

    @Override
    public void delFromList(DelFromListRequest request, DelFromListPresenter presenter) {
        DelFromListInternalRequest internalRequest = internalRequestFrom(request);
        DelFromListInternalResponse internalResponse = delFromList.execute(internalRequest);
        DelFromListResponse response = responseFrom(internalResponse);

        presenter.present(response);
    }

    @Override
    public void fetchFromList(FetchFromListRequest request, FetchFromListPresenter presenter) {
        FetchFromListInternalRequest internalRequest = internalRequestFrom(request);
        FetchFromListInternalResponse internalResponse = fetchFromList.execute(internalRequest);
        FetchFromListResponse response = responseFrom(internalResponse);

        presenter.present(response);
    }

    @Override
    public void viewList(ViewListRequest request, ViewListPresenter presenter) {
        ViewListInternalRequest internalRequest = new ViewListInternalRequest();
        List<ListItemInternalResponse> internalResponse = viewList.execute(internalRequest);
        List<ListItemResponse> response = internalResponse.stream().map(this::itemResponseFrom).toList();

        presenter.present(response);
    }

    private AddToListResponse responseFrom(AddToListInternalResponse internalResponse) {
        AddToListResponse.StatusCode status = switch (internalResponse.status()) {
            case OK_NEW -> AddToListResponse.StatusCode.OK_NEW;
            case OK_INCREASED ->  AddToListResponse.StatusCode.OK_INCREASED;
            case INVALID ->  AddToListResponse.StatusCode.INVALID;
            default -> AddToListResponse.StatusCode.ERROR;
        };

        return new AddToListResponse(status, internalResponse.item().map(this::itemResponseFrom));
    }

    private FetchFromListResponse responseFrom(FetchFromListInternalResponse internalResponse) {
        FetchFromListResponse.StatusCode status = switch (internalResponse.status()) {
            case OK_ALL -> FetchFromListResponse.StatusCode.OK_ALL;
            case OK_SOME -> FetchFromListResponse.StatusCode.OK_SOME;
            case NOT_FOUND -> FetchFromListResponse.StatusCode.NOT_FOUND;
            case INVALID -> FetchFromListResponse.StatusCode.INVALID;
            default -> FetchFromListResponse.StatusCode.ERROR;
        };

        return new FetchFromListResponse(status, internalResponse.inList().map(this::itemResponseFrom));
    }

    private DelFromListResponse responseFrom(DelFromListInternalResponse internalResponse) {
        DelFromListResponse.StatusCode status = switch (internalResponse.status()) {
            case OK -> DelFromListResponse.StatusCode.OK;
            case NOT_FOUND -> DelFromListResponse.StatusCode.NOT_FOUND;
            default -> DelFromListResponse.StatusCode.ERROR;
        };

        return new DelFromListResponse(status, Optional.empty());
    }

    private ListItemResponse itemResponseFrom(ListItemInternalResponse item) {
        return new ListItemResponse(item.id(), item.quantity(), item.unit(), item.name());
    }

    private AddToListInternalRequest internalRequestFrom(AddToListRequest request) {
        return new AddToListInternalRequest(request.quantity(), request.unit(), request.name());
    }

    private DelFromListInternalRequest internalRequestFrom(DelFromListRequest request) {
        return new DelFromListInternalRequest(request.id());
    }

    private FetchFromListInternalRequest internalRequestFrom(FetchFromListRequest request) {
        return new FetchFromListInternalRequest(request.id(), request.quantity(), request.pricePerUnit(), request.expiration());
    }
}
