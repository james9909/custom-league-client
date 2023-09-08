package com.hawolt.util.paint.custom;

import java.awt.*;

/**
 * Created: 08/09/2023 12:26
 * Author: Twitter @hawolt
 **/

public interface DrawableGraphic extends DrawableDimension {
    void update(Rectangle area);

    Rectangle getContentArea();
}
