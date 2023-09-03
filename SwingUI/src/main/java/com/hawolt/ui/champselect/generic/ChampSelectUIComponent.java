package com.hawolt.ui.champselect.generic;

import com.hawolt.ui.champselect.ChampSelectContext;
import com.hawolt.util.panel.ChildUIComponent;

/**
 * Created: 29/08/2023 17:13
 * Author: Twitter @hawolt
 **/

public abstract class ChampSelectUIComponent extends ChildUIComponent {
    protected ChampSelectContext context;

    public void configure(ChampSelectContext context) {
        this.context = context;
    }

    public void execute() {
        if (context == null) return;
        if (context.getCounter() == 2) {
            this.init();
        }
        this.update();
    }

    public void update() {

    }

    public void init() {

    }
}
