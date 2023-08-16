package com.hawolt.client.cache;

import com.hawolt.logger.Logger;

import java.util.function.Consumer;

/**
 * Created: 14/08/2023 17:42
 * Author: Twitter @hawolt
 **/

public class CachedValueLoader<T> implements Runnable {
    private final Consumer<CachedValueLoader<?>> consumer;
    private final ExceptionalRunnable<T> runnable;
    private final CacheType type;
    private Exception e;
    private T value;

    public CachedValueLoader(CacheType type, ExceptionalRunnable<T> runnable, Consumer<CachedValueLoader<?>> consumer) {
        this.consumer = consumer;
        this.runnable = runnable;
        this.type = type;
    }

    public CacheType getType() {
        return type;
    }

    public Exception getException() {
        return e;
    }

    public T getValue() {
        return value;
    }

    @Override
    public void run() {
        Logger.info("Caching value for {}", type);
        try {
            this.value = runnable.run();
        } catch (Exception e) {
            this.e = e;
        }
        this.consumer.accept(this);
    }
}
