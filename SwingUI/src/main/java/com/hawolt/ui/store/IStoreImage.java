package com.hawolt.ui.store;

import com.hawolt.client.resources.ledge.store.objects.InventoryType;

import java.awt.image.BufferedImage;
import java.util.concurrent.CompletableFuture;

/**
 * Created: 09/08/2023 19:31
 * Author: Twitter @hawolt
 **/

public interface IStoreImage {
    String getImageURL(InventoryType type, long itemId);
}
