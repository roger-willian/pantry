package org.pantry.shopping.views;

import org.pantry.shopping.presenters.ListItemViewModel;
import org.pantry.shopping.presenters.TextListView;

import java.util.List;

public class TerminalListView implements TextListView {
    @Override
    public void updateView(List<ListItemViewModel> viewModel) {
        clear();
        System.out.println("   s h o p p i n g    l i s t ");
        System.out.println("--------------------------------");
        viewModel.forEach(this::printItem);
        System.out.println("--------------------------------");
        System.out.println();
    }

    private void printItem(ListItemViewModel item) {
        System.out.printf("%s %s %s\n", item.quantity(), item.unit(), item.name());
    }

    private void clear() {
        System.out.println("***");
//        System.out.println(System.lineSeparator().repeat(100));
    }
}
