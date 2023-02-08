package org.pantry.shopping.cases.input;

import org.pantry.shopping.cases.output.DelFromCartInternalResponse;

public interface DelFromCartUC {
    DelFromCartInternalResponse execute(DelFromCartInternalRequest request);
}
