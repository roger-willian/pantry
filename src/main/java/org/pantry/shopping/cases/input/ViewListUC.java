package org.pantry.shopping.cases.input;

import org.pantry.shopping.cases.output.ListItemInternalResponse;

import java.util.List;

public interface ViewListUC {
    List<ListItemInternalResponse> execute(ViewListInternalRequest req);
}
