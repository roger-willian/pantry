package org.pantry.shopping.cases.data;

import org.pantry.shopping.entities.CartItem;

import java.util.List;
import java.util.Optional;

public interface ShoppingCartGateway {
    List<CartItem> findAll();

    CartItem addItem(CartItem item);

    boolean existsSimilar(CartItem item);

    Optional<CartItem> findSimilar(CartItem increment);

    CartItem updateItem(CartItem item);

    Optional<CartItem> findById(Long id);

    CartItem removeById(Long id);
}
