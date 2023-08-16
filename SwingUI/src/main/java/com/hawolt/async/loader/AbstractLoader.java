package com.hawolt.async.loader;

import com.hawolt.async.ExecutorManager;
import com.hawolt.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * Created: 06/08/2023 15:03
 * Author: Twitter @hawolt
 **/

public abstract class AbstractLoader<K, V> implements ILoader<K, V> {
    protected static final ExecutorService service = ExecutorManager.getService("loader");
    protected final Map<K, V> cache = new HashMap<>();
    protected final Object lock = new Object();

    public AbstractLoader() {
        AbstractLoader.service.execute(() -> {
            try {
                synchronized (lock) {
                    this.init();
                }
            } catch (URISyntaxException | IOException e) {
                Logger.error(e);
            }
        });
    }

    public Map<K, V> getCache() {
        synchronized (lock) {
            return cache;
        }
    }

    @Override
    public V load(InputStream stream) {
        return null;
    }

    @Override
    public V load(K o) {
        return cache.get(o);
    }

    public abstract void init() throws URISyntaxException, IOException;

    public abstract URI getResource() throws URISyntaxException;
}
