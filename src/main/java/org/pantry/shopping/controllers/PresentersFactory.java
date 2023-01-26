package org.pantry.shopping.controllers;

public interface PresentersFactory {
    ListPresenter getListPresenter();

    CartPresenter getCartPreserter();
}
