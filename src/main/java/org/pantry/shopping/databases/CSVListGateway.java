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

    @Override
    protected ListItem loadItem(CSVRecord record) {
        Double quantity = Double.parseDouble(record.get(0));
        String unit = record.get(1);
        String productName = record.get(2);
        return new ListItem(quantity, unit, productName);
    }

    @Override
    protected Iterable<Object> storeItem(ListItem item) {
        return Arrays.asList(item.quantity(), item.unit(), item.name());
    }

    @Override
    public List<ListItem> allListItems() {
        loadItems();
        return items;
    }

    @Override
    public ListItem addItem(ListItem item) {
        loadItems();
        if (existsSimilar(item))
            throw new IllegalArgumentException();
        else
            items.add(item);

        storeItems();
        return item;
    }

    @Override
    public boolean existsSimilar(ListItem item) {
        loadItems();
        return items.stream().anyMatch(item::isSimilar);
    }

    @Override
    public ListItem updateItem(ListItem item) {
        loadItems();
        items.replaceAll(it -> {
            if (item.isSimilar(it)) return item;
            else return it;
        });
        storeItems();
        return item;
    }

    @Override
    public Optional<ListItem> findSimilar(ListItem item) {
        loadItems();
        return items.stream().filter(item::isSimilar).findAny();
    }

    @Override
    public void removeSimilar(ListItem item) {
        loadItems();
        items.removeIf(item::isSimilar);
        storeItems();
    }
}
