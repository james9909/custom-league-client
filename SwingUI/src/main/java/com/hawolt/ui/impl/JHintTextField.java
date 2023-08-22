package com.hawolt.ui.impl;

import javax.swing.*;
import java.awt.*;

/**
 * Created: 06/08/2023 13:13
 * Author: Twitter @hawolt
 **/

public class JHintTextField extends JTextField {
    private final String hint;

    public JHintTextField(String hint) {
        this.hint = hint;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (!getText().isEmpty()) return;
        int height = getHeight();
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        Insets insets = getInsets();
        FontMetrics fontMetrics = g.getFontMetrics();
        int background = getBackground().getRGB();
        int foreground = getForeground().getRGB();
        int mask = 0xFEFEFEFE;
        int blend = ((background & mask) >>> 1) + ((foreground & mask) >>> 1);
        g.setColor(new Color(blend, true));
        g.drawString(hint, insets.left, height / 2 + fontMetrics.getAscent() / 2 - 2);
    }
}
