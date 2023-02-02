package org.pantry.shopping.cases.input;

import org.pantry.shopping.cases.output.DelFromCartResponse;

public interface DelFromCartUC {
    DelFromCartResponse execute(DelFromCartRequest request);
}
