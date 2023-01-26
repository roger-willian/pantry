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
        Menu mainMenu = new Menu("Shopping List");
        mainMenu.addItem("add item", this::addToShoppingList);
        mainMenu.addItem("del item", this::delFromShoppingList);
        mainMenu.addItem("fetch to cart", this::fetchToShoppingCart);
        mainMenu.addItem("shopping cart", this::accessShoppingCart);
        mainMenu.setBackOption("quit");
        mainMenu.setBeforeAction(this::viewShoppingList);
        mainMenu.select();
    }

    private void addToShoppingList() {
        Double qty = Prompt.ask("How much/many?", Double::parseDouble);
        String un = Prompt.ask("Which unit?");
        String name = Prompt.ask("Which product?");
        controller.addToShoppingList(qty, un, name);
    }

    private void delFromShoppingList() {
        String name = Prompt.ask("Which product?");
        String un = Prompt.ask("Which unit?");
        controller.delFromShoppingList(un, name);
    }

    private void viewShoppingList() {
        controller.viewShoppingList();
    }

    private void accessShoppingCart() {
        Menu cartMenu = new Menu("Shopping Cart");
        cartMenu.addItem("return item to list", this::returnFromShoppingCart);
        cartMenu.setBackOption("back to list");
        cartMenu.setBeforeAction(this::viewShoppingCart);
        cartMenu.select();
    }

    private void viewShoppingCart() {
        controller.viewShoppingCart();
    }

    private void fetchToShoppingCart() {
        String name = Prompt.ask("Which product?");
        String unit = Prompt.ask("Which unit?");
        Double quantity = Prompt.ask("How much/many?", Double::parseDouble);
        Double pricePerUnit = Prompt.ask("How much it costs?", Double::parseDouble, this::isPositive);
        Date expirationDate = Prompt.ask("Expires when (dd/mm/yyyy)?", this::parseDate, this::isValidDate);
        Integer expiration = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(expirationDate));
        controller.fetchToShoppingCart(quantity, unit, name, (int) (pricePerUnit*100), expiration);
    }

    private void returnFromShoppingCart() {
        String name = Prompt.ask("Which product?");
        String unit = Prompt.ask("Which unit?");
        Double quantity = Prompt.ask("How much/many?", Double::parseDouble);
        Double pricePerUnit = Prompt.ask("How much it costs?", Double::parseDouble, this::isPositive);
        Date expirationDate = Prompt.ask("Expires when (dd/mm/yyyy)?", this::parseDate, this::isValidDate);
        Integer expiration = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(expirationDate));
        controller.returnFromShoppingCart(quantity, unit, name, (int) (pricePerUnit*100), expiration);
    }

    private boolean isPositive(Double value) {
        return value > 0.0;
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
