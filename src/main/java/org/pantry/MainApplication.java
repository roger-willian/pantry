package org.pantry;

import org.pantry.configuration.GatewaysConfiguration;
import org.pantry.configuration.PresentersConfiguration;
import org.pantry.configuration.UCConfiguration;
import org.pantry.configuration.ViewsConfiguration;
import org.pantry.shopping.cases.data.GatewaysFactory;
import org.pantry.shopping.cases.input.UCFactory;
import org.pantry.shopping.controllers.PresentersFactory;
import org.pantry.shopping.controllers.ShoppingController;
import org.pantry.shopping.presenters.ViewsFactory;
import org.pantry.ui.UIController;

public class MainApplication {

    public static void main(String[] args) {
        GatewaysFactory databases = new GatewaysConfiguration();
        UCFactory cases = new UCConfiguration(databases);
        ViewsFactory views = new ViewsConfiguration();
        PresentersFactory presenters = new PresentersConfiguration(views);
        ShoppingController controller = new ShoppingController(cases, presenters);

        UIController uiController = new UIController(controller);
        uiController.start();
    }
}