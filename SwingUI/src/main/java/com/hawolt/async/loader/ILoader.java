package com.hawolt.async.loader;

import java.io.InputStream;

/**
 * Created: 20/04/2023 16:25
 * Author: Twitter @hawolt
 **/

public interface ILoader<K, V> {
    V load(InputStream stream);
    V load(K o);
}
