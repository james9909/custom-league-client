package com.hawolt.util.ui;

import com.hawolt.util.ColorPalette;

import javax.accessibility.Accessible;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListDataListener;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.sun.java.accessibility.util.AWTEventMonitor.addMouseListener;

public class LComboBox<E> extends LFlatButton
{
    private String labelText = "Selection";

    private boolean mouseOver;

    private int selectedIndex = -1;
    private HashMap<E,LFlatButton> itemList = new HashMap<E,LFlatButton>();
    private JPanel comboItemContainer;

    private boolean isOpen;

    public LComboBox(){
        super();
        setHighlightType(LHighlightType.COMPONENT);
    }

    public LComboBox(String label){
        super();
        labelText = label;
        setHighlightType(LHighlightType.COMPONENT);
    }

    public LComboBox(String label, LHighlightType highlightType){
        super();
        labelText = label;
        setHighlightType(highlightType);
    }

    //No need to call init as super is called so init is called by default
    @Override
    protected void init()
    {
        super.init();
        comboItemContainer = new JPanel(new GridLayout());
        //comboItemContainer.setBackground(new Color(0,0,0,0));
        comboItemContainer.setBounds(0,getHeight(),getWidth(),100);
        addActionListener(o ->
        {
            if(isOpen)
            {
                comboItemContainer.removeAll();
                System.out.println("Closing...");
                System.out.println(comboItemContainer.getComponentCount());
                isOpen = false;
            } else {
                for(LFlatButton button : itemList.values())
                {
                    comboItemContainer.add(button);
                }
                System.out.println("Opening...");
                System.out.println(comboItemContainer.getComponentCount());
                isOpen = true;
            }
        });
        //System.out.println("Opening...");
    }

    public void setSelectedIndex(int index){
        selectedIndex = index;
        setText(labelText + getEntryAtIndex(index).getValue().getText());
    }

    public void addItem(E newItem){
        LFlatButton button = new LFlatButton(newItem.toString(), LTextAlign.LEFT);
        System.out.println(newItem.toString());
        button.setBackground(ColorPalette.ACCENT_COLOR);
        button.setPreferredSize(new Dimension(0,50));
        int itemIndex = itemList.size();
        button.addActionListener(o ->{
            setSelectedIndex(itemIndex);
            comboItemContainer.removeAll();
            isOpen = false;
        });
        itemList.put(newItem, button);
        if(itemList.size() == 1)
            setSelectedIndex(0);
        //This is to check if buttons are created correctly
        else if(itemList.size() == 7)
            setSelectedIndex(6);
    }

    public int getSelectedIndex(){
        return selectedIndex;
    }

    public E getItemAt(int index){
        if(itemList.size() > index)
        {
            int i = 0;
            for(HashMap.Entry<E,LFlatButton> entry : itemList.entrySet())
            {
                if(i == index)
                    return entry.getKey();
                i++;
            }
        }

        return null;
    }

    private Map.Entry<E, LFlatButton> getEntryAtIndex(int index)
    {
        int i = 0;
        for(HashMap.Entry<E,LFlatButton> entry : itemList.entrySet())
        {
            if(i == index)
                return entry;
            i++;
        }
        return null;
    }
}
