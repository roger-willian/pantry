package org.pantry.shopping.controllers;

import org.pantry.shopping.cases.input.*;
import org.pantry.shopping.cases.output.CartItemResponse;
import org.pantry.shopping.cases.output.FetchFromListResponse;
import org.pantry.shopping.cases.output.ListItemResponse;

import java.util.List;

public class ShoppingController {

    private final ListPresenter listPresenter;
    private final CartPresenter cartPresenter;
    private final UCFactory cases;

    public ShoppingController(UCFactory cases, PresentersFactory presenters) {
        this.cases = cases;
        this.listPresenter = presenters.getListPresenter();
        this.cartPresenter = presenters.getCartPreserter();
    }

    public void viewShoppingList() {
        ViewListUC uc = cases.getViewListUC();
        ViewListRequest request = new ViewListRequest();
        List<ListItemResponse> response = uc.execute(request);
        listPresenter.present(response);
    }

    public void addToShoppingList(Double quantity, String unit, String productName) {
        AddToListUC uc = cases.getAddToShoppingList();
        AddToListRequest request = new AddToListRequest(quantity, unit, productName);
        uc.execute(request);
    }

    public void delFromShoppingList(Long id) {
        DelFromListUC uc = cases.getDelFromShoppingList();
        DelFromListRequest request = new DelFromListRequest(id);
        uc.execute(request);
    }

    public void viewShoppingCart() {
        ViewCartUC uc = cases.getViewCartUC();
        ViewCartRequest request = new ViewCartRequest();
        List<CartItemResponse> response = uc.execute(request);
        cartPresenter.present(response);
    }

    public void fetchToShoppingCart(Double quantity, String unit, String name, Integer pricePerUnit, Integer expiration) {
        FetchToCartUC uc = cases.getFetchToCartUC();
        FetchToCartRequest request = new FetchToCartRequest(quantity, unit, name, pricePerUnit, expiration);
        uc.execute(request);
    }

    public void returnFromShoppingCart(Double quantity, String unit, String name, Integer pricePerUnit, Integer expiration) {
        ReturnFromCartUC uc = cases.getReturnFromCartUC();
        ReturnFromCartRequest request = new ReturnFromCartRequest(quantity, unit, name, pricePerUnit, expiration);
        uc.execute(request);
    }

    public void fetchFromShoppingList(Long id, Double quantity, Integer pricePerUnit, Integer expiration) {
        FetchFromListUC uc = cases.getFetchFromListUC();
        FetchFromListRequest request = new FetchFromListRequest(id, quantity, pricePerUnit, expiration);
        uc.execute(request);
    }
}
