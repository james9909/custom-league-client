package com.hawolt.util.paint.custom;

import java.awt.*;

/**
 * Created: 08/09/2023 13:48
 * Author: Twitter @hawolt
 **/

public interface DrawableDimension extends Drawable {
    Dimension getDimension();

    int getX();

    int getY();
}
