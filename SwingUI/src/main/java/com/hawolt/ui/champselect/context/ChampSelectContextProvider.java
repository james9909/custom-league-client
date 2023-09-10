package com.hawolt.ui.champselect.context;

import com.hawolt.ui.champselect.ChampSelectUI;

/**
 * Created: 10/09/2023 03:39
 * Author: Twitter @hawolt
 **/

public class ChampSelectContextProvider {
    protected final ChampSelectUI champSelectUI;
    protected final ChampSelectContext context;

    public ChampSelectContextProvider(ChampSelectUI champSelectUI, ChampSelectContext context) {
        this.champSelectUI = champSelectUI;
        this.context = context;
    }
}
