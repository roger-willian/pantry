package org.pantry.shopping.controllers;

import org.pantry.shopping.cases.output.ListItemResponse;

import java.util.List;

public interface ListPresenter {
    void present(List<ListItemResponse> response);
}
