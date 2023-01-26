package org.pantry.shopping.cases.data;

public interface GatewaysFactory {
    ShoppingListGateway getShoppingListGateway();

    ShoppingCartGateway getShoppingCartGateway();
}
