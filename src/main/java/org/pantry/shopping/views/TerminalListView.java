package org.pantry.shopping.views;

import org.pantry.shopping.presenters.ListItemViewModel;
import org.pantry.shopping.presenters.TextListView;

import java.util.List;

public class TerminalListView implements TextListView {
    @Override
    public void updateView(List<ListItemViewModel> viewModel) {
        clear();
        System.out.println("            S H O P P I N G    L I S T            ");
        System.out.println("--------------------------------------------------");
        System.out.println(" id     qty un    product");
        System.out.println("--------------------------------------------------");
        viewModel.forEach(this::printItem);
        System.out.println("--------------------------------------------------");
        System.out.println();
    }

    private void printItem(ListItemViewModel item) {
        System.out.printf("%s %s %s %s\n", item.id(), item.quantity(), item.unit(), item.name());
    }

    private void clear() {
        System.out.println(System.lineSeparator().repeat(100));
    }
}
