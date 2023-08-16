package com.hawolt.ui.queue.pop;

import com.hawolt.util.panel.ChildUIComponent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created: 11/08/2023 21:31
 * Author: Twitter @hawolt
 **/

public class QueueDialog extends JDialog implements ActionListener {
    private int selection;

    public QueueDialog(Frame frame, String title, long maxAfkMillis) {
        super(frame, title);
        ChildUIComponent container = new ChildUIComponent(new BorderLayout());
        container.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(container);

        container.setLayout(new BorderLayout());
        container.setPreferredSize(new Dimension(200, 60));

        QueueCountdown countdown = new QueueCountdown(this, maxAfkMillis);
        container.add(countdown, BorderLayout.CENTER);

        ChildUIComponent component = new ChildUIComponent(new GridLayout(0, 2, 5, 0));
        component.setBorder(new EmptyBorder(5, 0, 0, 0));

        JButton accept = new JButton("Accept");
        accept.addActionListener(this);
        component.add(accept);

        JButton decline = new JButton("Decline");
        decline.addActionListener(this);
        component.add(decline);

        container.add(component, BorderLayout.SOUTH);
        setLocationRelativeTo(frame);
    }

    public QueueDialog showQueueDialog() {
        setResizable(false);
        setModal(true);
        pack();
        setVisible(true);
        return this;
    }

    public int getSelection() {
        return selection;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Accept" -> selection = 1;
            case "Decline" -> selection = 2;
        }
        dispose();
    }
}
