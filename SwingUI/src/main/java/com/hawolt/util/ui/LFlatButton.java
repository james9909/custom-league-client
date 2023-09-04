package com.hawolt.util.ui;

import com.hawolt.util.ColorPalette;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;

public class LFlatButton extends JButton {
    private int selectedIndicatorSize = 5;
    private Color selectedColor;

    private boolean showSelectionIndicator;
    private LHighlightType highlightType;

    private LTextAlign textAlign;

    public LFlatButton() {
        init();
        textAlign = LTextAlign.LEFT;
    }

    public LFlatButton(String text, LTextAlign textAlign) {
        super(text);
        init();
        this.textAlign = textAlign;
        highlightType = LHighlightType.LEFT;
    }

    public LFlatButton(String text, LTextAlign textAlign, LHighlightType highlightType) {
        super(text);
        this.highlightType = highlightType;
        init();
        this.textAlign = textAlign;
    }

    protected void init() {
        setContentAreaFilled(false);
        setFocusPainted(false); //Disable text border on focus
        setBorder(new EmptyBorder(8, 8, 8, 8));
        setForeground(Color.WHITE); //Set text to white

        setFont(new Font("Dialog", Font.BOLD, 18));

        selectedColor = ColorPalette.BUTTON_SELECTION_COLOR;

        //Change color on mouse press
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                showSelectionIndicator = true;
            }

            public void mouseEntered(MouseEvent evt) {
                showSelectionIndicator = true;
            }

            public void mouseExited(MouseEvent evt) {
                if (isSelected())
                    showSelectionIndicator = true;
                else
                    showSelectionIndicator = false;
            }
        });

        //Change color on button selection
        addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (isSelected())
                    showSelectionIndicator = true;
                else
                    showSelectionIndicator = false;
            }
        });

        setBackground(new Color(0, 0, 0, 0)); //Set default background color
    }

    public void setHighlightType(LHighlightType direction) {
        highlightType = direction;
    }

    public void setSelectionIndicatorSize(int size) {
        selectedIndicatorSize = size;
    }

    public void setSelectionIndicatorColor(Color col) {
        selectedColor = col;
    }

    public void setTextAlign(LTextAlign align) {
        textAlign = align;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g2d = (Graphics2D) graphics.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int x = 0, y = 0, width = 0, height = 0;
        switch (highlightType) {
            case LEFT:
                x = 0;
                y = 0;
                width = selectedIndicatorSize;
                height = getHeight();
                break;
            case TOP:
                x = 0;
                y = 0;
                width = getWidth();
                height = selectedIndicatorSize;
                break;
            case RIGHT:
                x = getWidth() - selectedIndicatorSize;
                y = 0;
                width = selectedIndicatorSize;
                height = getHeight();
                break;
            case BOTTOM:
                x = 0;
                y = getHeight() - selectedIndicatorSize;
                width = getWidth();
                height = selectedIndicatorSize;
                break;
            case COMPONENT:
                x = 0;
                y = 0;
                width = getWidth();
                height = getHeight();
                break;
        }
        Area buttonArea = new Area(new Rectangle(x, y, width, height));
        g2d.setColor(selectedColor);
        if (showSelectionIndicator)
            g2d.fill(buttonArea);

        g2d.setFont(getFont());
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        FontMetrics metrics = getFontMetrics(getFont());
        String text = getText();
        switch (textAlign) {
            case LEFT:
                if (highlightType != LHighlightType.LEFT)
                    x = getWidth() / 20;
                else
                    x = getWidth() / 20 + selectedIndicatorSize;
                break;
            case CENTER:
                x = getWidth() / 2 - metrics.stringWidth(text) / 2;
                break;
            case RIGHT:
                if (highlightType != LHighlightType.RIGHT)
                    x = getWidth() - metrics.stringWidth(text) - getWidth() / 20;
                else
                    x = getWidth() - metrics.stringWidth(text) - getWidth() / 20 - selectedIndicatorSize;
                break;
        }
        y = getHeight() / 2 + (metrics.getAscent() - metrics.getDescent() - metrics.getLeading()) / 2;
        PaintHelper.drawShadowText(g2d, text, x, y, getForeground());

        g2d.dispose();
        //  super.paintComponent(graphics);
    }
}
