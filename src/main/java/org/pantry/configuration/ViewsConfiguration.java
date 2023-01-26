package org.pantry.configuration;

import org.pantry.shopping.presenters.TextCartView;
import org.pantry.shopping.presenters.TextListView;
import org.pantry.shopping.presenters.ViewsFactory;
import org.pantry.shopping.views.*;

public class ViewsConfiguration implements ViewsFactory {
    private final TextListView textListView = new TerminalListView();
    private final TextCartView textCartView = new TerminalCartView();

    @Override
    public TextListView getTextListView() {
        return textListView;
    }

    @Override
    public TextCartView getTextCartView() {
        return textCartView;
    }
}
