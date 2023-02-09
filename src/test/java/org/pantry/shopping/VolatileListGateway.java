package org.pantry.shopping;

import org.pantry.shopping.cases.data.ShoppingListGateway;
import org.pantry.shopping.entities.ListItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.function.Predicate.not;

public class VolatileListGateway implements ShoppingListGateway {
    List<ListItem> items = new ArrayList<>();

    private Long nextId() {
        return 1 + items.stream().map(ListItem::id).max(Long::compareTo).orElse(0L);
    }

    @Override
    public List<ListItem> findAll() {
        return items;
    }

    @Override
    public ListItem addItem(ListItem item) {
        if (item.id() != null) throw new IllegalArgumentException("ID should be null");
        if (existsSimilar(item)) throw new IllegalArgumentException("Duplicate");

        ListItem withId = new ListItem(nextId(), item.quantity(), item.unit(), item.name());
        items.add(withId);
        return withId;
    }

    @Override
    public boolean existsSimilar(ListItem item) {
        return items.stream().anyMatch(item::isSimilar);
    }

    @Override
    public ListItem updateItem(ListItem item) {
        if (item.id() == null) throw new IllegalArgumentException("ID cant be null");
        ListItem target = findById(item.id()).orElseThrow(() -> new IllegalArgumentException("Not found"));

        Optional<ListItem> duplicate = findSimilar(item).filter(not(target::equals));
        if (duplicate.isPresent()) throw new IllegalArgumentException("Duplicate");

        items.remove(target);
        items.add(item);
        return item;
    }

    @Override
    public Optional<ListItem> findSimilar(ListItem item) {
        return items.stream().filter(item::isSimilar).findAny();
    }

    @Override
    public ListItem removeById(Long id) {
        ListItem target = findById(id).orElseThrow(() -> new IllegalArgumentException("Not found"));

        items.remove(target);
        return target;
    }

    @Override
    public Optional<ListItem> findById(Long id) {
        return items.stream().filter(it-> Objects.equals(it.id(), id)).findAny();
    }

    public void addItemWithId(ListItem item) {
        items.add(item);
    }
}
