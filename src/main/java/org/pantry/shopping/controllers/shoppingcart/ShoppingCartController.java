package org.pantry.shopping.controllers.shoppingcart;

import org.pantry.shopping.controllers.shoppingcart.presenters.DelFromCartPresenter;
import org.pantry.shopping.controllers.shoppingcart.presenters.FetchToCartPresenter;
import org.pantry.shopping.controllers.shoppingcart.presenters.ReturnFromCartPresenter;
import org.pantry.shopping.controllers.shoppingcart.presenters.ViewCartPresenter;
import org.pantry.shopping.controllers.shoppingcart.requests.DelFromCartRequest;
import org.pantry.shopping.controllers.shoppingcart.requests.FetchToCartRequest;
import org.pantry.shopping.controllers.shoppingcart.requests.ReturnFromCartRequest;
import org.pantry.shopping.controllers.shoppingcart.requests.ViewCartRequest;

public interface ShoppingCartController {
    void delFromCart(DelFromCartRequest request, DelFromCartPresenter presenter);
    void fetchToCart(FetchToCartRequest request, FetchToCartPresenter presenter);
    void returnFromCart(ReturnFromCartRequest request, ReturnFromCartPresenter presenter);
    void viewCart(ViewCartRequest request, ViewCartPresenter presenter);
}
