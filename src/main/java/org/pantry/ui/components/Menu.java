package org.pantry.ui.components;

import java.util.ArrayList;
import java.util.List;

public class Menu extends MenuItem {

    private final List<MenuItem> options = new ArrayList<>();
    private String backOption = "back";
    private Command.Action before = Command.NoAction;

    public Menu(String title) {
        super();
        this.setLabel(title);
    }

    @Override
    public void select() {
        int choice = chooseOneOption();
        while (choice != 0) {
            if (choice >= 0 && choice <= options.size())
                options.get(choice - 1).select();
            choice = chooseOneOption();
        }
    }

    private int chooseOneOption() {
        showOptions();
        String choice = System.console().readLine();
        try {
            return Integer.parseInt(choice);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void showOptions() {
        before.execute();
        printTitle();
        int i = 0;
        for (MenuItem option : options) {
            System.out.println(++i + " > " + option);
        }
        System.out.println("0 > " + backOption);
        printBar();
    }

    public void setBeforeAction(Command.Action before) {
        this.before = before;
    }

    public void addItem(MenuItem item) {
        options.add(item);
    }

    public void addItem(String label, Command.Action command) {
        this.addItem(new Command(label, command));
    }

    public void removeItem(int index) {
        options.remove(index);
    }

    public void setBackOption(String backOption) {
        this.backOption = backOption;
    }

    private void printTitle() {
        printBar();
        System.out.println("> " + getLabel().toUpperCase());
        printBar();
    }

    private void printBar() {
        System.out.println("================================");
    }
}
