package com.hawolt.ui.store;

import com.hawolt.client.resources.purchasewidget.CurrencyType;

/**
 * Created: 09/08/2023 19:31
 * Author: Twitter @hawolt
 **/

public interface IStoreElement {
    void purchase(CurrencyType currencyType, long price);
}
