package org.pantry.shopping.cases.input;

import org.pantry.shopping.cases.output.CartItemInternalResponse;
import org.pantry.shopping.cases.output.ViewCartInternalResponse;

import java.util.List;

public interface ViewCartUC {
    ViewCartInternalResponse execute(ViewCartInternalRequest request);
}
