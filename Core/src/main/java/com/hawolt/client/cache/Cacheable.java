package com.hawolt.client.cache;

/**
 * Created: 14/08/2023 19:42
 * Author: Twitter @hawolt
 **/

public interface Cacheable {
    void cache(CacheType type, Object o);
}
