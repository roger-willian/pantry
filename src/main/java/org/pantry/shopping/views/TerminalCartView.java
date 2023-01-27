package org.pantry.shopping.views;

import org.pantry.shopping.presenters.CartItemViewModel;
import org.pantry.shopping.presenters.CartViewModel;
import org.pantry.shopping.presenters.TextCartView;

public class TerminalCartView implements TextCartView {
    @Override
    public void updateView(CartViewModel cart) {
        clear();
        System.out.println("            S H O P P I N G    C A R T ");
        System.out.println("--------------------------------------------------");
        System.out.println(" id expiration product");
        System.out.println("    qty unit  x R$  price/unit =         R$ subtot");
        System.out.println("--------------------------------------------------");
        cart.items().forEach(this::printItem);
        System.out.println("--------------------------------------------------");
        this.printTotal(cart.items().size(), cart.totalPrice());
        System.out.println();
    }

    private void printItem(CartItemViewModel item) {
        System.out.printf("%s %s %s\n", item.id(), item.expiration(), item.name());
        System.out.printf("%s %s x R$ %s/unit =       + R$ %s\n", item.quantity(), item.unit(), item.pricePerUnit(), item.subtotal());
    }

    private void printTotal(int items, String total) {
        System.out.printf("Fetched %2d items                 TOTAL = R$ %s\n", items, total);
    }

    private void clear() {
        System.out.println(System.lineSeparator().repeat(100));
    }
}
