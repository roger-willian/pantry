package org.pantry.shopping.presenters;

import org.pantry.shopping.cases.output.CartItemResponse;
import org.pantry.shopping.controllers.CartPresenter;

import java.util.List;

public class TextCartPresenter implements CartPresenter {
    private final TextCartView view;

    public TextCartPresenter(ViewsFactory views) {
        view = views.getTextCartView();
    }

    @Override
    public void present(List<CartItemResponse> items) {
        Double total = items.stream().map(it->it.pricePerUnit()*it.quantity()).reduce(0.0, Double::sum);
        String totalFormatted = formatPrice(total);
        List<CartItemViewModel> itemsFormatted = items.stream().map(this::formatItem).toList();
        CartViewModel viewModel = new CartViewModel(itemsFormatted, totalFormatted);
        view.updateView(viewModel);
    }

    private CartItemViewModel formatItem(CartItemResponse item) {
        String name = formatName(item.name());
        String quantity = formatQuantity(item.quantity());
        String unit = formatUnit(item.unit());
        String pricePerUnit = formatPrice(Double.valueOf(item.pricePerUnit()));
        String subtotal = formatPrice(item.quantity() * item.pricePerUnit());
        String expiration = formatExpiration(item.expiration());
        return new CartItemViewModel(name, quantity, unit, pricePerUnit, subtotal, expiration);
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
        String truncated = String.format("%.21s", name);
        String rightPadded = truncated + " ".repeat(16 - truncated.length());
        return rightPadded;
    }

    private String formatPrice(Double price) {
        String money = String.format("%6.2f", price/100.0);
        return money;
    }

    private String formatExpiration(Integer expiration) {
        Integer year = expiration/10000;
        Integer month = (expiration % 10000) / 100;
        Integer day = expiration % 100;
        return String.format("%02d/%02d/%04d", day, month, year);
    }
}
