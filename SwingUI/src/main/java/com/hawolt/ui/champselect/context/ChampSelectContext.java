package com.hawolt.ui.champselect.context;

/**
 * Created: 29/08/2023 17:31
 * Author: Twitter @hawolt
 **/

public interface ChampSelectContext {
    ChampSelectInteractionContext getChampSelectInteractionContext();

    ChampSelectInterfaceContext getChampSelectInterfaceContext();

    ChampSelectSettingsContext getChampSelectSettingsContext();

    ChampSelectUtilityContext getChampSelectUtilityContext();

    ChampSelectDataContext getChampSelectDataContext();
}
