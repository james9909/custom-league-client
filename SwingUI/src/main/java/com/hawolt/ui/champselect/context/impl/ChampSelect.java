package com.hawolt.ui.champselect.context.impl;

import com.hawolt.ui.champselect.ChampSelectUI;
import com.hawolt.ui.champselect.context.*;

/**
 * Created: 10/09/2023 03:17
 * Author: Twitter @hawolt
 **/

public class ChampSelect implements ChampSelectContext {
    private final ChampSelectInteractionContext champSelectInteractionContext;
    private final ChampSelectInterfaceContext champSelectInterfaceContext;
    private final ChampSelectSettingsContext champSelectSettingsContext;
    private final ChampSelectUtilityContext champSelectUtilityContext;
    private final ChampSelectDataContext champSelectDataContext;

    public ChampSelect(ChampSelectUI champSelectUI) {
        this.champSelectInteractionContext = new ChampSelectInteraction(champSelectUI, this);
        this.champSelectInterfaceContext = new ChampSelectInterface(champSelectUI, this);
        this.champSelectSettingsContext = new ChampSelectSettings(champSelectUI, this);
        this.champSelectUtilityContext = new ChampSelectUtility(champSelectUI, this);
        this.champSelectDataContext = new ChampSelectData(champSelectUI, this);
    }

    @Override
    public ChampSelectInteractionContext getChampSelectInteractionContext() {
        return champSelectInteractionContext;
    }

    @Override
    public ChampSelectInterfaceContext getChampSelectInterfaceContext() {
        return champSelectInterfaceContext;
    }

    @Override
    public ChampSelectSettingsContext getChampSelectSettingsContext() {
        return champSelectSettingsContext;
    }

    @Override
    public ChampSelectUtilityContext getChampSelectUtilityContext() {
        return champSelectUtilityContext;
    }

    @Override
    public ChampSelectDataContext getChampSelectDataContext() {
        return champSelectDataContext;
    }
}
