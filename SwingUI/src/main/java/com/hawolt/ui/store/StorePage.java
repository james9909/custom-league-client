package com.hawolt.ui.store;

import com.hawolt.client.LeagueClient;
import com.hawolt.client.resources.ledge.store.objects.StoreItem;
import com.hawolt.client.resources.ledge.store.objects.StoreSortOrder;
import com.hawolt.client.resources.ledge.store.objects.StoreSortProperty;
import com.hawolt.util.AudioEngine;
import com.hawolt.util.panel.ChildUIComponent;
import com.hawolt.util.ui.DynamicGridLayout;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
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

    private final StoreElementComparator comparator;

    public StorePage(LeagueClient client, List<Long> owned, StoreSortProperty... properties) {
        super(new BorderLayout(0, 5));
        this.client = client;
        this.owned = owned;
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

        comparator = new StoreElementComparator(properties.length > 0 ? properties[0] : null, StoreSortOrder.ASCENDING);
        JComboBox<StoreSortOption> sortBox = new JComboBox<>();
        for (StoreSortProperty property : properties) {
            sortBox.addItem(new StoreSortOption(property, StoreSortOrder.ASCENDING));
            sortBox.addItem(new StoreSortOption(property, StoreSortOrder.DESCENDING));
        }
        sortBox.addItemListener(listener -> {
            AudioEngine.play("air_button_press_1.wav");
            StoreSortOption option = sortBox.getItemAt(sortBox.getSelectedIndex());
            comparator.setProperty(option.property());
            comparator.setOrder(option.order());
            updateElements();
        });
        this.add(sortBox, BorderLayout.NORTH);
    }

    public void append(StoreItem item) {
        if (owned.contains(item.getItemId())) return;
        JSONObject object = item.asJSON();
        long itemId = object.getLong("itemId");
        StoreElement element = new StoreElement(client, this, item);
        map.put(itemId, element);
        grid.add(element);
        updateElements();
    }

    @Override
    public void removeStoreElement(StoreElement component) {
        grid.remove(component);
        map.remove(component.getItem().getItemId());
        updateElements();
    }

    public void updateElements() {
        grid.removeAll();
        map.values()
                .stream()
                .sorted(this.comparator)
                .forEach(this.grid::add);
        revalidate();
        repaint();
    }
}
