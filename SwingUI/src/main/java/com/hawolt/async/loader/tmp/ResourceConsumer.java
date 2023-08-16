package com.hawolt.async.loader.tmp;

/**
 * Created: 16/08/2023 18:24
 * Author: Twitter @hawolt
 **/

public interface ResourceConsumer<T, V> {
    void onException(Object o, Exception e);

    void consume(T t);

    T transform(V v) throws Exception;
}
