package com.hawolt.util.ui;

import com.hawolt.util.ColorPalette;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;

public class LComboBox<E> extends JComboBox<E> {
    private String labelText = "";
    private boolean mouseOver;

    private LHighlightType highlightType;
    private int selectedIndicatorSize = 5;

    private Color optionBackground = ColorPalette.ACCENT_COLOR;

    public LComboBox(E[] items) {
        init();
        for (int i = 0; i < items.length; i++)
            addItem(items[i]);
    }

    public LComboBox() {
        init();
    }

    private void init() {
        setBackground(ColorPalette.BACKGROUND_COLOR);
        setBorder(
                BorderFactory.createCompoundBorder(
                        new MatteBorder(0, 0, 1, 0, Color.DARK_GRAY),
                        new EmptyBorder(labelText.isEmpty() ? 5 : getHeight() * 10, 3, 5, 3)
                )
        );
        setUI(new LComboUI(this));

        setForeground(Color.WHITE); //Set text to white
        setFont(new Font("Dialog", Font.BOLD, 18));

        highlightType = LHighlightType.COMPONENT;

        setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> jlist, Object o, int i, boolean bln, boolean bln1) {
                Component com = super.getListCellRendererComponent(jlist, o, i, bln, bln1);
                setBorder(
                        BorderFactory.createCompoundBorder(
                                new MatteBorder(0, 1, 1, 1, Color.DARK_GRAY),
                                new EmptyBorder(5, 5, 5, 5)
                        )
                );

                com.setForeground(Color.WHITE); //Set text to white
                com.setFont(new Font("Dialog", Font.BOLD, 18));
                com.setBackground(optionBackground);

                if (bln) {
                    com.setBackground(ColorPalette.BUTTON_SELECTION_COLOR);
                }
                return com;
            }
        });

    }

    public void setOptionBackground(Color color) {
        optionBackground = color;
    }

    public String getLabelText() {
        return labelText;
    }

    public void setLabelText(String labelText) {
        this.labelText = labelText;
    }

    private class LComboUI extends BasicComboBoxUI {
        private boolean show;
        private LComboBox combo;

        public LComboUI(LComboBox combo) {
            this.combo = combo;
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent me) {
                    mouseOver = true;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent me) {
                    mouseOver = false;
                    repaint();
                }
            });
            addPopupMenuListener(new PopupMenuListener() {
                @Override
                public void popupMenuWillBecomeVisible(PopupMenuEvent pme) {
                    show = true;
                    repaint();
                }

                @Override
                public void popupMenuWillBecomeInvisible(PopupMenuEvent pme) {
                    arrowButton.setBackground(Color.WHITE);
                    show = false;
                }

                @Override
                public void popupMenuCanceled(PopupMenuEvent pme) {
                    arrowButton.setBackground(Color.WHITE);
                    show = false;
                }
            });
        }

        @Override
        public void paintCurrentValueBackground(Graphics grphcs, Rectangle rctngl, boolean bln) {

        }

        @Override
        protected JButton createArrowButton() {
            return new ArrowButton();
        }

        @Override
        protected ComboPopup createPopup() {
            BasicComboPopup pop = new BasicComboPopup(comboBox) {
                @Override
                protected LScrollPane createScroller() {
                    list.setFixedCellHeight(30);
                    LScrollPane scroll = new LScrollPane(list);
                    scroll.setBackground(Color.WHITE);
                    return scroll;
                }
            };
            pop.setBorder(new EmptyBorder(0, 0, 0, 0));
            return pop;
        }

        @Override
        public void paint(Graphics grphcs, JComponent jc) {
            //super.paint(grphcs, jc);
            Graphics2D g2 = (Graphics2D) grphcs;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

            if (show) {
                if (mouseOver)
                    arrowButton.setBackground(ColorPalette.BUTTON_SELECTION_COLOR.darker());
                else
                    arrowButton.setBackground(ColorPalette.BUTTON_SELECTION_COLOR);
            }

            int x = 0, y = 0, width = getWidth(), height = getHeight();
            if (!labelText.isEmpty()) {
                y = height / 2;
                height /= 2;
            }
            switch (highlightType) {
                case LEFT:
                    width = selectedIndicatorSize;
                    break;
                case TOP:
                    height = selectedIndicatorSize;
                    break;
                case RIGHT:
                    x = width - selectedIndicatorSize;
                    width = selectedIndicatorSize;
                    break;
                case BOTTOM:
                    y = height - selectedIndicatorSize;
                    height = selectedIndicatorSize;
                    break;
            }
            Area buttonArea = new Area(new Rectangle(x, y, width, height));
            g2.setColor(ColorPalette.BUTTON_SELECTION_COLOR);
            if (mouseOver)
                g2.fill(buttonArea);

            FontMetrics metrics = getFontMetrics(getFont());
            y = getHeight() / 2 + PaintHelper.getFontHeight(metrics) / 2;
            if (!labelText.isEmpty()) {
                //Label
                PaintHelper.drawShadowText(g2, combo.getLabelText(), 5, getHeight() / 4 + PaintHelper.getFontHeight(metrics) / 2, getForeground());
                y = getHeight() / 2 + getHeight() / 4 + PaintHelper.getFontHeight(metrics) / 2;
            }

            //Selected element text
            PaintHelper.drawShadowText(g2, getSelectedItem().toString(), 5, y, getForeground());

            g2.dispose();
        }

        private class ArrowButton extends JButton {

            public ArrowButton() {
                setContentAreaFilled(false);
                setBorder(new EmptyBorder(5, 5, 5, 5));
                setBackground(Color.WHITE);
            }

            @Override
            public void paint(Graphics grphcs) {
                super.paint(grphcs);
                Graphics2D g2 = (Graphics2D) grphcs;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int width = getWidth();
                int height = getHeight();
                int size = 10;
                int x = (width - size) / 2;
                int y = (height - size) / 2 + (labelText.isEmpty() ? 0 : (height + size) / 4);
                int px[] = {x, x + size, x + size / 2};
                int py[] = {y, y, y + size};
                g2.setColor(getBackground());
                g2.fillPolygon(px, py, px.length);
                g2.dispose();
            }
        }
    }
}
