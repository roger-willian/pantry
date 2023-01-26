package org.pantry.shopping.cases.input;

import org.pantry.shopping.cases.output.ListItemResponse;

import java.util.List;

public interface ViewListUC {
    List<ListItemResponse> execute(ViewListRequest req);
}
