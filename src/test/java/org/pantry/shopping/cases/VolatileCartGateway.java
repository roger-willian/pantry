package org.pantry.shopping.cases;

import org.pantry.shopping.cases.data.ShoppingCartGateway;
import org.pantry.shopping.entities.CartItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.function.Predicate.not;

public class VolatileCartGateway implements ShoppingCartGateway {

    List<CartItem> items = new ArrayList<>();

    private long nextId() {
        return 1 + items.stream().map(CartItem::id).max(Long::compareTo).orElse(0L);
    }

    @Override
    public List<CartItem> findAll() {
        return items;
    }

    @Override
    public CartItem addItem(CartItem item) {
        if (item.id() != null) throw new IllegalArgumentException("ID should be null");
        if (existsSimilar(item)) throw new IllegalArgumentException("Duplicate");

        CartItem withId = new CartItem(nextId(), item.quantity(), item.unit(), item.name(), item.pricePerUnit(), item.expiration());
        items.add(withId);
        return withId;
    }

    @Override
    public boolean existsSimilar(CartItem item) {
        return items.stream().anyMatch(item::isSimilar);
    }

    @Override
    public Optional<CartItem> findSimilar(CartItem item) {
        return items.stream().filter(item::isSimilar).findAny();
    }

    @Override
    public CartItem updateItem(CartItem item) {
        if (item.id() == null) throw new IllegalArgumentException("ID cant be null");
        CartItem target = findById(item.id()).orElseThrow(()->new IllegalArgumentException("Not found"));

        Optional<CartItem> duplicate = findSimilar(item).filter(not(target::equals));
        if (duplicate.isPresent()) throw new IllegalArgumentException("Duplicate");

        items.remove(target);
        items.add(item);
        return item;
    }

    @Override
    public Optional<CartItem> findById(Long id) {
        return items.stream().filter(it->it.id() == id).findAny();
    }

    @Override
    public CartItem removeById(Long id) {
        CartItem target = findById(id).orElseThrow(()->new IllegalArgumentException("Not found"));
        items.remove(target);
        return target;
    }

    public void addWithId(CartItem item) {
        items.add(item);
    }
}
