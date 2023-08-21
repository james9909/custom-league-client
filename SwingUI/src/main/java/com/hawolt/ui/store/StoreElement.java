package com.hawolt.ui.store;

import com.hawolt.LeagueClientUI;
import com.hawolt.client.LeagueClient;
import com.hawolt.client.cache.CacheType;
import com.hawolt.client.cache.CachedValueLoader;
import com.hawolt.client.resources.ledge.store.objects.InventoryType;
import com.hawolt.client.resources.ledge.store.objects.StoreItem;
import com.hawolt.client.resources.purchasewidget.CurrencyType;
import com.hawolt.client.resources.purchasewidget.PurchaseWidget;
import com.hawolt.util.AudioEngine;
import com.hawolt.util.panel.ChildUIComponent;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created: 09/08/2023 19:01
 * Author: Twitter @hawolt
 **/

public class StoreElement extends ChildUIComponent implements IStoreElement {
    private final List<StoreButton> buttons = new ArrayList<>();
    private final LeagueClient client;
    private final StoreImage image;
    private final StoreItem item;
    private final IStorePage page;

    public StoreElement(LeagueClient client, IStorePage page, StoreItem item) {
        super(new BorderLayout());
        this.add(image = new StoreImage(item), BorderLayout.CENTER);
        this.setBackground(Color.DARK_GRAY);
        this.setPreferredSize(new Dimension(150, 250));
        this.client = client;
        this.item = item;
        this.page = page;
        this.build();
    }

    private void build() {
        if (item.isBlueEssencePurchaseAvailable() && item.getBlueEssenceCost() > 0)
            buttons.add(new StoreButton(this, CurrencyType.IP, item.getBlueEssenceCost()));
        if (item.isRiotPointPurchaseAvailable() && item.getRiotPointCost() > 0)
            buttons.add(new StoreButton(this, CurrencyType.RP, item.getRiotPointCost()));
        ChildUIComponent component = new ChildUIComponent(new GridLayout(0, buttons.isEmpty() ? 1 : buttons.size(), 5, 0));
        for (StoreButton button : buttons) {
            component.add(button);
        }
        component.setBackground(Color.GRAY);
        add(component, BorderLayout.SOUTH);
        revalidate();
    }

    public StoreItem getItem() {
        return item;
    }

    public StoreImage getImage() {
        return image;
    }

    @Override
    public void purchase(CurrencyType currencyType, long price) {
        AudioEngine.play("air_button_press_1.wav");
        LeagueClientUI.service.execute(() -> {
            try {
                PurchaseWidget widget = client.getPurchaseWidget();
                JSONObject response = new JSONObject(widget.purchase(currencyType, InventoryType.CHAMPION, item.getItemId(), price));
                if (response.has("errorCode")) {
                    AudioEngine.play("friend_logout.wav");
                } else {
                    AudioEngine.play("openstore.wav");
                    page.removeStoreElement(this);
                    LeagueClientUI.service.execute(
                            new CachedValueLoader<>(
                                    CacheType.INVENTORY_TOKEN,
                                    () -> client.getLedge().getInventoryService().getInventoryToken(),
                                    client
                            )
                    );
                    revalidate();
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}