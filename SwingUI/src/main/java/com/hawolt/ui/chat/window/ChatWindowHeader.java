package com.hawolt.ui.chat.window;

import com.hawolt.util.ColorPalette;
import com.hawolt.util.panel.ChildUIComponent;
import com.hawolt.util.ui.LFlatButton;
import com.hawolt.util.ui.LHighlightType;
import com.hawolt.util.ui.LLabel;
import com.hawolt.util.ui.LTextAlign;

import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created: 08/08/2023 20:51
 * Author: Twitter @hawolt
 **/

public class ChatWindowHeader extends ChildUIComponent {

    private final LLabel target;

    public ChatWindowHeader(LayoutManager layout) {
        super(layout);
        this.setBackground(ColorPalette.ACCENT_COLOR);
        this.setForeground(Color.WHITE);
        this.setBorder(new EmptyBorder(0, 5, 0, 0));
        this.add(target = new LLabel("", LTextAlign.LEFT), BorderLayout.CENTER);
        LFlatButton close = new LFlatButton("×", LTextAlign.CENTER, LHighlightType.COMPONENT);
        this.add(close, BorderLayout.EAST);
        close.addActionListener(listener -> {
            this.getParent().setVisible(false);
        });
    }

    public void setTarget(String name) {
        target.setText(name);
    }

}
