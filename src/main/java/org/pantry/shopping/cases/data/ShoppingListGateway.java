package org.pantry.shopping.cases.data;

import org.pantry.shopping.entities.ListItem;

import java.util.List;
import java.util.Optional;

public interface ShoppingListGateway {
    List<ListItem> allListItems();

    ListItem addItem(ListItem item);

    boolean existsSimilar(ListItem item);

    ListItem updateItem(ListItem item);

    Optional<ListItem> findSimilar(ListItem item);

    void removeSimilar(ListItem item);
}
