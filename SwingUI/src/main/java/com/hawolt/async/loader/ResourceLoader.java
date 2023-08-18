package com.hawolt.async.loader;

import com.hawolt.StaticConstant;
import com.hawolt.async.ExecutorManager;
import com.hawolt.generic.data.Unsafe;
import com.hawolt.http.OkHttp3Client;
import com.hawolt.io.Core;
import com.hawolt.io.RunLevel;
import com.hawolt.logger.Logger;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created: 16/08/2023 18:21
 * Author: Twitter @hawolt
 **/

public class ResourceLoader {
    private static final ExecutorService service = ExecutorManager.registerService(
            "resource-loader-jobs",
            Executors.newFixedThreadPool(8)
    );

    private static final Map<String, List<ResourceConsumer<?, byte[]>>> pending = new HashMap<>();
    private static final LinkedList<Runnable> queue = new LinkedList<>();
    private static final Map<String, byte[]> cache = new HashMap<>();

    static {
        ScheduledExecutorService scheduler = ExecutorManager.getScheduledService("resource-loader");
        scheduler.scheduleWithFixedDelay(() -> {
            if (queue.isEmpty()) return;
            service.execute(queue.remove(0));
        }, 0, 20, TimeUnit.MILLISECONDS);
    }

    private static <T> T convert(Object in) {
        return Unsafe.cast(in);
    }


    public static void load(String uri, ResourceConsumer<?, byte[]> consumer) {
        load(uri, false, consumer);
    }

    public static void load(String uri, boolean priority, ResourceConsumer<?, byte[]> consumer) {
        if (cache.containsKey(uri)) {
            try {
                consumer.consume(uri, Unsafe.cast(consumer.transform(cache.get(uri))));
            } catch (Exception e) {
                consumer.onException(uri, e);
            }
        } else if (pending.containsKey(uri)) {
            pending.get(uri).add(consumer);
        } else {
            pending.put(uri, new ArrayList<>());
            pending.get(uri).add(consumer);
            Runnable runnable = () -> {
                Request request = new Request.Builder()
                        .url(uri)
                        .header("User-Agent", StaticConstant.USER_AGENT)
                        .get()
                        .build();
                Call call = OkHttp3Client.perform(request);
                try (Response response = call.execute()) {
                    try (ResponseBody body = response.body()) {
                        handleConsumption(uri, body.bytes());
                    }
                } catch (IOException e) {
                    handleError(uri, e);
                }
            };
            queue.add(priority ? 0 : queue.size(), runnable);
        }
    }

    public static void loadLocalResource(String name, ResourceConsumer<?, byte[]> consumer) {
        loadLocalResource(name, false, consumer);
    }

    public static void loadLocalResource(String name, boolean priority, ResourceConsumer<?, byte[]> consumer) {
        if (cache.containsKey(name)) {
            try {
                consumer.consume(name, Unsafe.cast(consumer.transform(cache.get(name))));
            } catch (Exception e) {
                consumer.onException(name, e);
            }
        } else if (pending.containsKey(name)) {
            pending.get(name).add(consumer);
        } else {
            pending.put(name, new ArrayList<>());
            pending.get(name).add(consumer);
            Runnable runnable = () -> {
                try (InputStream stream = RunLevel.get(name)) {
                    handleConsumption(name, Core.read(stream).toByteArray());
                } catch (IOException e) {
                    handleError(name, e);
                }
            };
            queue.add(priority ? 0 : queue.size(), runnable);
        }
    }

    private static void handleConsumption(String o, byte[] b) {
        cache.put(o, b);
        List<ResourceConsumer<?, byte[]>> list = new ArrayList<>(pending.get(o));
        for (ResourceConsumer<?, byte[]> consumer : list) {
            try {
                consumer.consume(o, convert(consumer.transform(b)));
            } catch (Exception e) {
                Logger.error(e);
            }
        }
    }

    private static void handleError(String o, Exception e) {
        List<ResourceConsumer<?, byte[]>> list = new ArrayList<>(pending.get(o));
        for (ResourceConsumer<?, byte[]> consumer : list) {
            consumer.onException(o, e);
        }
    }
}
