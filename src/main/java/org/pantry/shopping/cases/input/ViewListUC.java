package org.pantry.shopping.cases.input;

import org.pantry.shopping.cases.output.ListItemInternalResponse;
import org.pantry.shopping.cases.output.ViewListInternalResponse;

import java.util.List;

public interface ViewListUC {
    ViewListInternalResponse execute(ViewListInternalRequest req);
}
