package org.pantry.shopping.databases;

import org.apache.commons.csv.CSVRecord;
import org.pantry.shopping.cases.data.ShoppingCartGateway;
import org.pantry.shopping.entities.CartItem;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CSVCartGateway extends CSVGateway<CartItem> implements ShoppingCartGateway {

    public CSVCartGateway(String fileName) {
        super(fileName);
    }

    @Override
    protected CartItem loadItem(CSVRecord record) {
        Double quantity = Double.parseDouble(record.get(0));
        String unit = record.get(1);
        String productName = record.get(2);
        Integer pricePerUnit = Integer.parseInt(record.get(3));
        Integer expirationDate = Integer.parseInt(record.get(4));
        return new CartItem(quantity, unit, productName, pricePerUnit, expirationDate);
    }

    @Override
    protected Iterable<Object> storeItem(CartItem item) {
        return Arrays.asList(item.quantity(), item.unit(), item.name(), item.pricePerUnit(), item.expiration());
    }

    @Override
    public List<CartItem> findAll() {
        loadItems();
        return items;
    }

    @Override
    public CartItem addItem(CartItem item) {
        loadItems();
        if (existsSimilar(item))
            throw new IllegalArgumentException();
        else
            items.add(item);

        storeItems();
        return item;
    }

    public boolean existsSimilar(CartItem item) {
        loadItems();
        return items.stream().anyMatch(item::isSimilar);
    }

    @Override
    public Optional<CartItem> findSimilar(CartItem item) {
        loadItems();
        return items.stream().filter(item::isSimilar).findAny();
    }

    @Override
    public CartItem updateItem(CartItem item) {
        loadItems();
        if (!existsSimilar(item))
            throw new IllegalArgumentException();
        else
            items.replaceAll(it->{
                if (item.isSimilar(it))
                    return item;
                else
                    return it;
            });
        storeItems();
        return item;
    }

    @Override
    public void removeSimilar(CartItem item) {
        loadItems();
        items.removeIf(item::isSimilar);
        storeItems();
    }
}
