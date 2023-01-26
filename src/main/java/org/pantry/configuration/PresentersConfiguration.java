package org.pantry.configuration;

import org.pantry.shopping.controllers.CartPresenter;
import org.pantry.shopping.controllers.ListPresenter;
import org.pantry.shopping.controllers.PresentersFactory;
import org.pantry.shopping.presenters.TextCartPresenter;
import org.pantry.shopping.presenters.TextListPresenter;
import org.pantry.shopping.presenters.ViewsFactory;

public class PresentersConfiguration implements PresentersFactory {

    private final ListPresenter listPresenter;
    private final CartPresenter cartPresenter;

    public PresentersConfiguration(ViewsFactory views) {
        listPresenter = new TextListPresenter(views);
        cartPresenter = new TextCartPresenter(views);
    }

    @Override
    public ListPresenter getListPresenter() {
        return listPresenter;
    }

    @Override
    public CartPresenter getCartPreserter() {
        return cartPresenter;
    }
}
