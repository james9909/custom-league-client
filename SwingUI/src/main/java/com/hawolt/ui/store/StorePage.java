package com.hawolt.ui.store;

import com.hawolt.client.LeagueClient;
import com.hawolt.client.resources.ledge.store.objects.StoreItem;
import com.hawolt.util.ui.DynamicGridLayout;
import com.hawolt.util.panel.ChildUIComponent;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created: 09/08/2023 18:45
 * Author: Twitter @hawolt
 **/

public class StorePage extends ChildUIComponent implements IStorePage {

    private final Map<Long, StoreElement> map = new HashMap<>();
    private final LeagueClient client;
    private final ChildUIComponent grid;
    private final List<Long> owned;

    public StorePage(LeagueClient client, Long... ids) {
        super(new BorderLayout());
        this.client = client;
        this.owned = Arrays.asList(ids);
        ChildUIComponent component = new ChildUIComponent(new BorderLayout());
        grid = new ChildUIComponent(new DynamicGridLayout(0, 5, 5, 5));
        grid.setBackground(Color.GRAY);
        add(component, BorderLayout.NORTH);
        component.add(grid, BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.getVerticalScrollBar().setUnitIncrement(15);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        add(scrollPane, BorderLayout.CENTER);
        setBorder(new EmptyBorder(5, 5, 5, 0));
    }

    public void append(StoreItem item) {
        if (owned.contains(item.getItemId())) return;
        JSONObject object = item.asJSON();
        long itemId = object.getLong("itemId");
        StoreElement element = new StoreElement(client, this, item);
        map.put(itemId, element);
        grid.add(element);
    }

    @Override
    public void removeStoreElement(StoreElement component) {
        grid.remove(component);
        revalidate();
        repaint();
    }
}
