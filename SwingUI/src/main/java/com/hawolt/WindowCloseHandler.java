package com.hawolt;

import com.hawolt.service.LocalSettingsService;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created: 22/08/2023 09:32
 * Author: Twitter @hawolt
 **/

public class WindowCloseHandler extends WindowAdapter {

    private final JFrame closingFrame;

    public WindowCloseHandler(JFrame closingFrame) {
        this.closingFrame = closingFrame;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        super.windowClosing(e);

        int option = JOptionPane.showOptionDialog(null,
                "Do you want to exit or logout?",
                StaticConstant.PROJECT,
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new String[]{"LOGOUT", "EXIT", "CANCEL"},
                "EXIT");

        if (option == 0) LocalSettingsService.get().deleteFile();

        if (option != 2) closingFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
