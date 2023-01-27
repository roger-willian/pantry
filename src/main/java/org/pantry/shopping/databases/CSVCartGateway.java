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

    private long nextId() {
        loadItems();
        return 1 + items.stream().map(CartItem::id).max(Long::compareTo).orElse(0L);
    }

    @Override
    protected CartItem loadItem(CSVRecord record) {
        Long id = Long.parseLong(record.get(0));
        Double quantity = Double.parseDouble(record.get(1));
        String unit = record.get(2);
        String productName = record.get(3);
        Integer pricePerUnit = Integer.parseInt(record.get(4));
        Integer expirationDate = Integer.parseInt(record.get(5));
        return new CartItem(id, quantity, unit, productName, pricePerUnit, expirationDate);
    }

    @Override
    protected Iterable<Object> storeItem(CartItem item) {
        return Arrays.asList(item.id(), item.quantity(), item.unit(), item.name(), item.pricePerUnit(), item.expiration());
    }

    @Override
    public List<CartItem> findAll() {
        loadItems();
        return items;
    }

    @Override
    public CartItem addItem(CartItem item) {
        loadItems();

        if (item.id() != null) throw new IllegalArgumentException("Cart item id must be null on insertion");
        if (existsSimilar(item)) throw new IllegalArgumentException("Cart item would be duplicate on insertion");

        CartItem newItem = new CartItem(nextId(), item.quantity(), item.unit(), item.name(), item.pricePerUnit(), item.expiration());
        items.add(newItem);
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
        Optional<CartItem> target = findById(item.id());
        if (target.isEmpty()) throw new IllegalArgumentException("Cart item id not found on update");

        Optional<CartItem> duplicate = findSimilar(item).filter(it->!it.equals(target.get()));
        if (duplicate.isPresent()) throw new IllegalArgumentException("Cart item would be duplicate on update");

        items.removeIf(target.get()::equals);
        items.add(target.get());
        storeItems();
        return item;
    }

    @Override
    public Optional<CartItem> findById(Long id) {
        loadItems();
        return items.stream().filter(it->it.id() == id).findAny();
    }

    @Override
    public CartItem removeById(Long id) {
        loadItems();
        Optional<CartItem> target = findById(id);
        if (target.isEmpty()) throw new IllegalArgumentException("Cart item id not found on remove");

        items.removeIf(target.get()::equals);
        storeItems();
        return target.get();
    }
}
