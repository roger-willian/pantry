package org.pantry.shopping.presenters;

import org.pantry.shopping.cases.output.ListItemResponse;
import org.pantry.shopping.controllers.ListPresenter;

import java.util.List;

public class TextListPresenter implements ListPresenter {

    private final TextListView view;

    public TextListPresenter(ViewsFactory views) {
        this.view = views.getTextListView();
    }


    @Override
    public void present(List<ListItemResponse> response) {
        view.updateView(response.stream().map(this::formatItem).toList());
    }

    private ListItemViewModel formatItem(ListItemResponse item) {
        String id = formatId(item.id());
        String quantity = formatQuantity(item.quantity());
        String unit = formatUnit(item.unit());
        String name = formatName(item.name());
        return new ListItemViewModel(id, quantity, unit, name);
    }

    private String formatId(Long id) {
        String number = String.format("%03d", id);
        return number;
    }

    private String formatQuantity(Double quantity) {
        String number = String.format("%7.3f", quantity);
        String trimmed = number.replaceAll(",?\\.?0+$","");
        String leftPadded = String.format("%7s", trimmed);
        return leftPadded;
    }

    private String formatUnit(String unit) {
        String truncated = String.format("%.5s", unit);
        String rightPadded = truncated + " ".repeat(5 - truncated.length());
        return rightPadded;
    }

    private String formatName(String name) {
        String truncated = String.format("%.10s", name);
        String rightPadded = truncated + " ".repeat(10 - truncated.length());
        return rightPadded;
    }
}
