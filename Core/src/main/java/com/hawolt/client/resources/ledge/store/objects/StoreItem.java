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
    private final List<Price> prices = new ArrayList<>();
    private String offerId, name, description;
    private SubInventoryType subInventoryType;
    private int discountCostBE, discountCostRP;
    private float discountBE, discountRP;
    private InventoryType inventoryType;
    private boolean active, valid;
    private JSONObject object;
    private Date releaseDate;
    private long itemId;

    public StoreItem(JSONArray array) {
        this.valid = !array.isEmpty();
        if (!valid) return;
        JSONObject item = array.getJSONObject(0);
        if (item.has("offerId")) this.offerId = item.getString("offerId");
        this.inventoryType = InventoryType.valueOf(item.getString("inventoryType"));
        if (item.has("subInventoryType")) {
            this.subInventoryType = SubInventoryType.valueOf(item.getString("subInventoryType").toUpperCase());
        }
        this.active = item.getBoolean("active");
        this.itemId = item.getLong("itemId");
        JSONArray prices = item.getJSONArray("prices");
        for (int i = 0; i < prices.length(); i++) {
            this.prices.add(new Price(prices.getJSONObject(i)));
        }
        if (item.has("sale")) {
            JSONObject sale = item.getJSONObject("sale");
            if (sale.has("prices")) {
                JSONArray salePrices = sale.getJSONArray("prices");
                for (int i = 0; i < salePrices.length(); i++) {
                    JSONObject salePrice = salePrices.getJSONObject(i);
                    if (salePrice.getString("currency").equals("IP")) {
                        this.discountCostBE = salePrice.getInt("cost");
                        if (!salePrice.has("discount")) continue;
                        if (salePrice.getFloat("discount") == 0) continue;
                        this.discountBE = salePrice.getFloat("discount");
                    }
                    if (salePrice.getString("currency").equals("RP")) {
                        this.discountCostRP = salePrice.getInt("cost");
                        if (!salePrice.has("discount")) continue;
                        if (salePrice.getFloat("discount") == 0) continue;
                        this.discountRP = salePrice.getFloat("discount");
                    }
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
        return getCorrectRiotPointCost() > 0 || prices.stream().anyMatch(price -> price.getCurrency().equals("RP"));
    }

    public int getRiotPointCost() {
        return prices.stream().filter(price -> price.getCurrency().equals("RP")).mapToInt(Price::getCost).sum();
    }

    public boolean hasDiscount() {
        return hasDiscountBE() || hasDiscountRP();
    }

    public boolean hasDiscountBE() {
        return discountBE != 0 || discountCostBE > 0;
    }

    public boolean hasDiscountRP() {
        return discountRP != 0 || discountCostRP > 0;
    }

    public float getDiscountBE() {
        return discountBE;
    }

    public float getDiscountRP() {
        return discountRP;
    }

    public int getCorrectBlueEssenceCost() {
        if (hasDiscountBE()) {
            return discountCostBE;
        } else {
            return getBlueEssenceCost();
        }
    }

    public int getCorrectRiotPointCost() {
        if (hasDiscountRP()) {
            return discountCostRP;
        } else {
            return getRiotPointCost();
        }
    }

    public boolean isBlueEssencePurchaseAvailable() {
        return getCorrectBlueEssenceCost() > 0 || prices.stream().anyMatch(price -> price.getCurrency().equals("IP"));
    }

    public int getBlueEssenceCost() {
        return prices.stream().filter(price -> price.getCurrency().equals("IP")).mapToInt(Price::getCost).sum();
    }

    public int getPointCost() {
        return (int) Math.ceil(getBlueEssenceCost() / 450D);
    }

    public List<Price> getPrices() {
        return prices;
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

    public boolean hasSubInventoryType() {
        return subInventoryType != null;
    }

    public SubInventoryType getSubInventoryType() {
        return subInventoryType;
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
                "list=" + prices +
                ", offerId='" + offerId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", inventoryType=" + inventoryType +
                ", subInventoryType=" + subInventoryType +
                ", active=" + active +
                ", valid=" + valid +
                ", object=" + object +
                ", itemId=" + itemId +
                ", releaseDate=" + releaseDate +
                '}';
    }
}