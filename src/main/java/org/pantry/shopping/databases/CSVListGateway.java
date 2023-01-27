package org.pantry.shopping.databases;

import org.apache.commons.csv.CSVRecord;
import org.pantry.shopping.cases.data.ShoppingListGateway;
import org.pantry.shopping.entities.ListItem;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CSVListGateway extends CSVGateway<ListItem> implements ShoppingListGateway {

    public CSVListGateway(String fileName) {
        super(fileName);
    }

    private long nextId() {
        loadItems();
        return 1 + items.stream().map(ListItem::id).max(Long::compareTo).orElse(0L);
    }

    @Override
    protected ListItem loadItem(CSVRecord record) {
        Long id = Long.parseLong(record.get(0));
        Double quantity = Double.parseDouble(record.get(1));
        String unit = record.get(2);
        String productName = record.get(3);
        return new ListItem(id, quantity, unit, productName);
    }

    @Override
    protected Iterable<Object> storeItem(ListItem item) {
        return Arrays.asList(item.id(), item.quantity(), item.unit(), item.name());
    }

    @Override
    public List<ListItem> findAll() {
        loadItems();
        return items;
    }

    @Override
    public ListItem addItem(ListItem item) {
        loadItems();

        if (item.id() != null) throw new IllegalArgumentException("List item id must be null on insert");
        if (existsSimilar(item)) throw new IllegalArgumentException("List item would be duplicate on insert");

        ListItem newItem = new ListItem(nextId(), item.quantity(), item.unit(), item.name());
        items.add(newItem);

        storeItems();
        return newItem;
    }

    @Override
    public boolean existsSimilar(ListItem item) {
        loadItems();
        return items.stream().anyMatch(item::isSimilar);
    }

    @Override
    public ListItem updateItem(ListItem item) {
        loadItems();
        Optional<ListItem> target = findById(item.id());
        if (target.isEmpty()) throw new IllegalArgumentException("List item id not found on update");

        Optional<ListItem> duplicate = findSimilar(item).filter(it -> !it.equals(target.get()));
        if (duplicate.isPresent()) throw new IllegalArgumentException("List item would be duplicate on update");

        items.removeIf(target.get()::equals);
        items.add(item);
        storeItems();
        return item;
    }

    @Override
    public Optional<ListItem> findSimilar(ListItem item) {
        loadItems();
        return items.stream().filter(item::isSimilar).findAny();
    }

    @Override
    public ListItem removeById(Long id) {
        loadItems();
        Optional<ListItem> target = findById(id);
        if (target.isEmpty())  throw new IllegalArgumentException("List item id not found");

        items.removeIf(target.get()::equals);
        storeItems();
        return target.get();
    }

    @Override
    public Optional<ListItem> findById(Long id) {
        loadItems();
        return items.stream().filter(it->it.id() == id).findAny();
    }
}
