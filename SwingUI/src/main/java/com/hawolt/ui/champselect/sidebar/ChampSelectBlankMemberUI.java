package com.hawolt.ui.champselect.sidebar;

import javax.swing.*;
import java.awt.*;

/**
 * Created: 06/08/2023 14:14
 * Author: Twitter @hawolt
 **/

public class ChampSelectBlankMemberUI extends JPanel {

    public ChampSelectBlankMemberUI() {
        this.setBackground(Color.DARK_GRAY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension dimension = getSize();
        int bar = 30;
        g.setColor(Color.DARK_GRAY.darker());
        g.fillRect(0, dimension.height - bar, dimension.width, bar);
    }
}
