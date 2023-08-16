package com.hawolt.client.resources;

/**
 * Created: 14/01/2023 01:30
 * Author: Twitter @hawolt
 **/

public interface IUndocumentedEndpoint {
    int version();

    String name();

    String base();

    String rcp();
}
