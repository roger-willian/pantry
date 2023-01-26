package org.pantry.shopping.views;

import org.pantry.shopping.presenters.CartItemViewModel;
import org.pantry.shopping.presenters.CartViewModel;
import org.pantry.shopping.presenters.TextCartView;

public class TerminalCartView implements TextCartView {
    @Override
    public void updateView(CartViewModel cart) {
        clear();
        System.out.println("   s h o p p i n g    c a r t ");
        System.out.println("--------------------------------");
        cart.items().forEach(this::printItem);
        System.out.println("--------------------------------");
        this.printTotal(cart.items().size(), cart.totalPrice());
        System.out.println();
    }

    private void printItem(CartItemViewModel item) {
        System.out.printf("%s %s\n", item.expiration(), item.name());
        System.out.printf("%s %s x%s  R$ %s\n", item.quantity(), item.unit(), item.pricePerUnit(), item.subtotal());
    }

    private void printTotal(int items, String total) {
        System.out.printf("Total: %2d items        R$ %s\n", items, total);
    }

    private void clear() {
        System.out.println(System.lineSeparator().repeat(100));
    }
}
