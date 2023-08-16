package com.hawolt.async.loader.impl;

import com.hawolt.async.ExecutorManager;
import com.hawolt.async.loader.ILoader;
import com.hawolt.io.Core;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created: 06/08/2023 15:07
 * Author: Twitter @hawolt
 **/

public class ImageLoader implements ILoader<String, CompletableFuture<BufferedImage>> {
    private static final ExecutorService service = ExecutorManager.registerService(
            "image-loader-jobs",
            Executors.newFixedThreadPool(8)

    );
    public static ImageLoader instance = new ImageLoader();

    private final Map<Object, CompletableFuture<BufferedImage>> internal = new HashMap<>();
    private final Map<Object, BufferedImage> cache = new HashMap<>();

    private BufferedImage resolve(InputStream stream) throws IOException {
        return ImageIO.read(new ByteArrayInputStream(Core.read(stream).toByteArray()));
    }

    @Override
    public CompletableFuture<BufferedImage> load(InputStream o) {
        CompletableFuture<BufferedImage> future = new CompletableFuture<>();
        if (cache.containsKey(o)) return internal.get(o);
        else internal.put(o, future);

        if (cache.containsKey(o)) {
            future.complete(cache.get(o));
        } else {
            service.execute(() -> {
                try {
                    future.complete(resolve(o));
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            });
        }
        return future;
    }

    @Override
    public CompletableFuture<BufferedImage> load(String o) {
        CompletableFuture<BufferedImage> future = new CompletableFuture<>();
        if (cache.containsKey(o)) return internal.get(o);
        else internal.put(o, future);

        if (cache.containsKey(o)) {
            future.complete(cache.get(o));
        } else {
            service.execute(() -> {
                try {
                    HttpsURLConnection connection = (HttpsURLConnection) new URL(o).openConnection();
                    connection.setRequestProperty("User-Agent", "ClientUI");
                    try (InputStream stream = connection.getInputStream()) {
                        future.complete(resolve(stream));
                    }
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            });
        }
        return future;
    }
}
