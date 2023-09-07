package com.hawolt.client.resources.ledge.store.objects;

import com.hawolt.logger.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created: 28/07/2023 01:58
 * Author: Twitter @hawolt
 **/

public class StoreItem {
    private List<Price> list = new ArrayList<>();
    private String offerId, name, description;
    private InventoryType inventoryType;
    private boolean active, valid;
    private JSONObject object;
    private long itemId;
    private float discount;
    private int discountCost;
    private Date releaseDate;

    public StoreItem(JSONArray array) {
        this.valid = !array.isEmpty();
        if (!valid) return;
        JSONObject item = array.getJSONObject(0);
        if (item.has("offerId")) this.offerId = item.getString("offerId");
        this.inventoryType = InventoryType.valueOf(item.getString("inventoryType"));
        this.active = item.getBoolean("active");
        this.itemId = item.getLong("itemId");
        JSONArray prices = item.getJSONArray("prices");
        for (int i = 0; i < prices.length(); i++) {
            list.add(new Price(prices.getJSONObject(i)));
        }
        if (item.has("sale")) {
            JSONObject sale = item.getJSONObject("sale");
            if (sale.has("prices")) {
                JSONArray salePrices = sale.getJSONArray("prices");
                this.discount = salePrices.getJSONObject(0).getFloat("discount");
                this.discountCost = salePrices.getJSONObject(0).getInt("cost");
                //TODO does not support BE sale prices
                for (int i = 0; i < salePrices.length(); i++) {
                    JSONObject salePrice = salePrices.getJSONObject(i);
                    if (!salePrice.has("discount")) continue;
                    this.discount = salePrice.getFloat("discount");
                    this.discountCost = salePrice.getInt("cost");
                }
            }
        }
        if (item.has("localizations")) {
            JSONObject localizations = item.getJSONObject("localizations");
            if (localizations.has("en_GB")) {
                JSONObject enGB = localizations.getJSONObject("en_GB");
                if (enGB.has("description")) this.description = enGB.getString("description");
                if (enGB.has("name")) this.name = enGB.getString("name");
            }
        }
        String releaseDate = item.getString("releaseDate");
        try {
            this.releaseDate = Date.from(Instant.from(DateTimeFormatter.ISO_DATE_TIME.parse(releaseDate)));
        } catch (DateTimeParseException e) {
            Logger.error("Could not parse release date '{}': {}", releaseDate, e);
        }
        this.object = item;
    }

    public JSONObject asJSON() {
        return object;
    }

    public boolean isValid() {
        return valid;
    }

    public boolean isRiotPointPurchaseAvailable() {
        return list.stream().anyMatch(price -> price.getCurrency().equals("RP"));
    }

    public int getRiotPointCost() {
        return list.stream().filter(price -> price.getCurrency().equals("RP")).mapToInt(Price::getCost).sum();
    }

    public boolean hasDiscount() {
        return discount != 0;
    }

    public float getDiscount() {
        return discount;
    }

    public int getDiscountedCost() {
        if (hasDiscount()) {
            return discountCost;
        } else {
            return getRiotPointCost();
        }
    }

    public boolean isBlueEssencePurchaseAvailable() {
        return list.stream().anyMatch(price -> price.getCurrency().equals("IP"));
    }

    public int getBlueEssenceCost() {
        return list.stream().filter(price -> price.getCurrency().equals("IP")).mapToInt(Price::getCost).sum();
    }

    public int getPointCost() {
        return (int) Math.ceil(getBlueEssenceCost() / 450D);
    }

    public List<Price> getList() {
        return list;
    }

    public String getOfferId() {
        return offerId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public InventoryType getInventoryType() {
        return inventoryType;
    }

    public boolean isActive() {
        return active;
    }

    public long getItemId() {
        return itemId;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    @Override
    public String toString() {
        return "StoreItem{" +
                "list=" + list +
                ", offerId='" + offerId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", inventoryType=" + inventoryType +
                ", active=" + active +
                ", valid=" + valid +
                ", object=" + object +
                ", itemId=" + itemId +
                ", releaseDate=" + releaseDate +
                '}';
    }
}