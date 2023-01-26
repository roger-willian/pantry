package org.pantry.shopping.cases.input;

import org.pantry.shopping.cases.output.ReturnFromCartResponse;

public interface ReturnFromCartUC {
    ReturnFromCartResponse execute(ReturnFromCartRequest request);
}
