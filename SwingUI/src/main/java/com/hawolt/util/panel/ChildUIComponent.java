package com.hawolt.util.panel;

import com.hawolt.util.ColorPalette;

import javax.swing.*;
import java.awt.*;

/**
 * Created: 06/08/2023 13:39
 * Author: Twitter @hawolt
 **/

public class ChildUIComponent extends JPanel {
    public ChildUIComponent(LayoutManager layout) {
        setLayout(layout);
        setBackground(ColorPalette.BACKGROUND_COLOR);
    }
}