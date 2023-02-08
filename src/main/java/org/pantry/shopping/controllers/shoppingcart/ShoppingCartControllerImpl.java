package org.pantry.shopping.controllers.shoppingcart;

import org.pantry.shopping.cases.input.*;
import org.pantry.shopping.cases.output.CartItemInternalResponse;
import org.pantry.shopping.cases.output.DelFromCartInternalResponse;
import org.pantry.shopping.cases.output.FetchToCartInternalResponse;
import org.pantry.shopping.cases.output.ReturnFromCartInternalResponse;
import org.pantry.shopping.controllers.shoppingcart.presenters.DelFromCartPresenter;
import org.pantry.shopping.controllers.shoppingcart.presenters.FetchToCartPresenter;
import org.pantry.shopping.controllers.shoppingcart.presenters.ReturnFromCartPresenter;
import org.pantry.shopping.controllers.shoppingcart.presenters.ViewCartPresenter;
import org.pantry.shopping.controllers.shoppingcart.requests.DelFromCartRequest;
import org.pantry.shopping.controllers.shoppingcart.requests.FetchToCartRequest;
import org.pantry.shopping.controllers.shoppingcart.requests.ReturnFromCartRequest;
import org.pantry.shopping.controllers.shoppingcart.requests.ViewCartRequest;
import org.pantry.shopping.controllers.shoppingcart.responses.CartItemResponse;
import org.pantry.shopping.controllers.shoppingcart.responses.DelFromCartResponse;
import org.pantry.shopping.controllers.shoppingcart.responses.FetchToCartResponse;
import org.pantry.shopping.controllers.shoppingcart.responses.ReturnFromCartResponse;

import java.util.List;

public class ShoppingCartControllerImpl implements ShoppingCartController {

    private final DelFromCartUC delFromCart;
    private final FetchToCartUC fetchToCart;
    private final ReturnFromCartUC returnFromCart;
    private final ViewCartUC viewCart;

    public ShoppingCartControllerImpl(UCFactory cases) {
        delFromCart = cases.getDelFromCartUC();
        fetchToCart = cases.getFetchToCartUC();
        returnFromCart = cases.getReturnFromCartUC();
        viewCart = cases.getViewCartUC();
    }

    @Override
    public void delFromCart(DelFromCartRequest request, DelFromCartPresenter presenter) {
        DelFromCartInternalRequest internalRequest = internalRequestFrom(request);
        DelFromCartInternalResponse internalResponse = delFromCart.execute(internalRequest);
        DelFromCartResponse response = responseFrom(internalResponse);

        presenter.present(response);
    }

    private DelFromCartResponse responseFrom(DelFromCartInternalResponse internalResponse) {
        DelFromCartResponse.StatusCode status = switch (internalResponse.status()) {
            case OK_ALL -> DelFromCartResponse.StatusCode.OK_ALL;
            case OK_SOME -> DelFromCartResponse.StatusCode.OK_SOME;
            case NOT_FOUND -> DelFromCartResponse.StatusCode.NOT_FOUND;
            case INVALID -> DelFromCartResponse.StatusCode.INVALID;
            default -> DelFromCartResponse.StatusCode.ERROR;
        };

        return new DelFromCartResponse(status, internalResponse.stillThere().map(this::itemResponseFrom));
    }

    private DelFromCartInternalRequest internalRequestFrom(DelFromCartRequest request) {
        return new DelFromCartInternalRequest(request.id(), request.quantity());
    }

    @Override
    public void fetchToCart(FetchToCartRequest request, FetchToCartPresenter presenter) {
        FetchToCartInternalRequest internalRequest = internalRequestFrom(request);
        FetchToCartInternalResponse internalResponse = fetchToCart.execute(internalRequest);
        FetchToCartResponse response = responseFrom(internalResponse);

        presenter.present(response);
    }

    private FetchToCartResponse responseFrom(FetchToCartInternalResponse internalResponse) {
        FetchToCartResponse.StatusCode status = switch (internalResponse.status()) {
            case OK_ALL -> FetchToCartResponse.StatusCode.OK_ALL;
            case OK_SOME -> FetchToCartResponse.StatusCode.OK_SOME;
            case OK_NEW -> FetchToCartResponse.StatusCode.OK_NEW;
            case OK_INCREASED -> FetchToCartResponse.StatusCode.OK_INCREASED;
            case INVALID -> FetchToCartResponse.StatusCode.INVALID;
            default -> FetchToCartResponse.StatusCode.ERROR;
        };

        return new FetchToCartResponse(status, internalResponse.inCart().map(this::itemResponseFrom));
    }

    private FetchToCartInternalRequest internalRequestFrom(FetchToCartRequest request) {
        return new FetchToCartInternalRequest(request.quantity(), request.unit(), request.name(), request.pricePerUnit(), request.expiration());
    }

    @Override
    public void returnFromCart(ReturnFromCartRequest request, ReturnFromCartPresenter presenter) {
        ReturnFromCartInternalRequest internalRequest = internalRequestFrom(request);
        ReturnFromCartInternalResponse internalResponse = returnFromCart.execute(internalRequest);
        ReturnFromCartResponse response = responseFrom(internalResponse);

        presenter.present(response);
    }

    private ReturnFromCartResponse responseFrom(ReturnFromCartInternalResponse internalResponse) {
        ReturnFromCartResponse.StatusCode status = switch (internalResponse.status()) {
            case OK_ALL -> ReturnFromCartResponse.StatusCode.OK_ALL;
            case OK_SOME -> ReturnFromCartResponse.StatusCode.OK_SOME;
            case NOT_FOUND -> ReturnFromCartResponse.StatusCode.NOT_FOUND;
            case TOO_MANY -> ReturnFromCartResponse.StatusCode.TOO_MANY;
            case INVALID -> ReturnFromCartResponse.StatusCode.INVALID;
            default -> ReturnFromCartResponse.StatusCode.ERROR;
        };

        return new ReturnFromCartResponse(status, internalResponse.stillThere().map(this::itemResponseFrom));
    }

    private ReturnFromCartInternalRequest internalRequestFrom(ReturnFromCartRequest request) {
        return new ReturnFromCartInternalRequest(request.id(), request.quantity());
    }

    @Override
    public void viewCart(ViewCartRequest request, ViewCartPresenter presenter) {
        ViewCartInternalRequest internalRequest = internalRequestFrom(request);
        List<CartItemInternalResponse> internalResponse = viewCart.execute(internalRequest);
        List<CartItemResponse> response = internalResponse.stream().map(this::itemResponseFrom).toList();

        presenter.present(response);
    }

    private ViewCartInternalRequest internalRequestFrom(ViewCartRequest request) {
        return new ViewCartInternalRequest();
    }

    private CartItemResponse itemResponseFrom(CartItemInternalResponse item) {
        return new CartItemResponse(item.id(), item.quantity(), item.unit(), item.name(), item.pricePerUnit(), item.expiration());
    }
}
