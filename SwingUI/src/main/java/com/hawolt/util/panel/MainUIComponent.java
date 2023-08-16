package com.hawolt.util.panel;

import javax.swing.*;
import java.awt.*;

/**
 * Created: 06/08/2023 13:20
 * Author: Twitter @hawolt
 **/

public class MainUIComponent extends JPanel {

    private final JFrame frame;

    protected Container container;

    public MainUIComponent(JFrame frame) {
        this.frame = frame;
        this.container = frame.getContentPane();
        this.container.removeAll();
    }

    public void init() {
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    public JFrame getJFrame() {
        return frame;
    }
}
