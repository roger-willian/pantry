package org.pantry.shopping.cases;

import io.cucumber.datatable.DataTableType;
import io.cucumber.datatable.DataTableTypeRegistry;
import io.cucumber.datatable.TableEntryTransformer;
import io.cucumber.datatable.TableRowTransformer;
import org.pantry.shopping.cases.data.GatewaysFactory;
import org.pantry.shopping.cases.output.AddToListResponse;
import org.pantry.shopping.cases.output.DelFromListResponse;
import org.pantry.shopping.cases.output.ListItemResponse;
import org.pantry.shopping.entities.ListItem;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ScenarioContext {
    private final GatewaysFactory databases;
    private final VolatileCartGateway cart;
    private final VolatileListGateway list;

    AddToListResponse lastAddToListResponse;
    DelFromListResponse lastDelFromListResponse;
    List<ListItemResponse> lastViewListResponse;

    public ScenarioContext() {
        this.databases = mock(GatewaysFactory.class);
        list = new VolatileListGateway();
        cart = new VolatileCartGateway();
        when(databases.getShoppingListGateway()).thenReturn(list);
        when(databases.getShoppingCartGateway()).thenReturn(cart);
    }

    public GatewaysFactory getDatabases() {
        return databases;
    }

    public VolatileListGateway getList() {
        return list;
    }
    public VolatileCartGateway getCart() {
        return cart;
    }
}
