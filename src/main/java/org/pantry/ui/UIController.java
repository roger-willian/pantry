package org.pantry.ui;

import org.pantry.shopping.controllers.ShoppingController;
import org.pantry.ui.components.Menu;
import org.pantry.ui.components.Prompt;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UIController {
    private final ShoppingController controller;

    public UIController(ShoppingController controller) {
        this.controller = controller;
    }
    public void start() {
        Menu mainMenu = new Menu("Shopping List menu");
        mainMenu.addItem("add item", this::addToShoppingList);
        mainMenu.addItem("del item", this::delFromShoppingList);
        mainMenu.addItem("fetch to cart (directly)", this::fetchToShoppingCart);
        mainMenu.addItem("fetch to cart (from list)", this::fetchFromList);
        mainMenu.addItem("shopping cart", this::accessShoppingCart);
        mainMenu.setBackOption("quit");
        mainMenu.setBeforeAction(this::viewShoppingList);
        mainMenu.select();
    }

    private void fetchFromList() {
        Long id = Prompt.ask("Which product (id)?", Long::parseLong, this::isPositive);
        Double qty = Prompt.ask("How much/many?", Double::parseDouble, this::isPositive);
        Double pricePerUnit = Prompt.ask("How much it costs?", Double::parseDouble, this::isPositive);
        Date expirationDate = Prompt.ask("Expires when (dd/mm/yyyy)?", this::parseDate, this::isValidDate);
        Integer expiration = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(expirationDate));
        controller.fetchFromShoppingList(id, qty, (int) (pricePerUnit*100), expiration);
    }

    private void addToShoppingList() {
        Double qty = Prompt.ask("How much/many?", Double::parseDouble, this::isPositive);
        String un = Prompt.ask("Which unit?", this::identity, this::isNotEmpty);
        String name = Prompt.ask("Which product?", this::identity, this::isNotEmpty);
        controller.addToShoppingList(qty, un, name);
    }

    private void delFromShoppingList() {
        Long id = Prompt.ask("Which product (id)?", Long::parseLong, this::isPositive);
        controller.delFromShoppingList(id);
    }

    private void viewShoppingList() {
        controller.viewShoppingList();
    }

    private void accessShoppingCart() {
        Menu cartMenu = new Menu("Shopping Cart menu");
        cartMenu.addItem("return item to list", this::returnFromShoppingCart);
        cartMenu.setBackOption("back to list");
        cartMenu.setBeforeAction(this::viewShoppingCart);
        cartMenu.select();
    }

    private void viewShoppingCart() {
        controller.viewShoppingCart();
    }

    private void fetchToShoppingCart() {
        String name = Prompt.ask("Which product?", this::identity, this::isNotEmpty);
        String unit = Prompt.ask("Which unit?", this::identity, this::isNotEmpty);
        Double quantity = Prompt.ask("How much/many?", Double::parseDouble, this::isPositive);
        Double pricePerUnit = Prompt.ask("How much it costs?", Double::parseDouble, this::isPositive);
        Date expirationDate = Prompt.ask("Expires when (dd/mm/yyyy)?", this::parseDate, this::isValidDate);
        Integer expiration = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(expirationDate));
        controller.fetchToShoppingCart(quantity, unit, name, (int) (pricePerUnit*100), expiration);
    }

    private void returnFromShoppingCart() {
        Long id = Prompt.ask("Which product (id)?", Long::parseLong, this::isPositive);
        Double qty = Prompt.ask("How much/many?", Double::parseDouble, this::isPositive);
        controller.returnFromShoppingCart(id, qty);
    }

    private String identity(String response) {
        return response;
    }
    private boolean isPositive(Double value) {
        return value > 0D;
    }
    private boolean isPositive(Long value) {
        return value > 0L;
    }

    private boolean isNotEmpty(String response) {
        return !response.trim().isEmpty();
    }

    private boolean isValidDate(Date value) {
        return value.after(new Date());
    }

    private Date parseDate(String date) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        format.setLenient(false);
        return format.parse(date);
    }
}
