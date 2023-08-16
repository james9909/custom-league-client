package com.hawolt.ui.login;

import com.hawolt.LeagueClientUI;
import com.hawolt.ui.impl.JHintTextField;
import com.hawolt.util.panel.MainUIComponent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created: 06/08/2023 13:10
 * Author: Twitter @hawolt
 **/

public class LoginUI extends MainUIComponent implements ActionListener {
    private final JHintTextField username;
    private final JPasswordField password;
    private final ILoginCallback callback;

    public static void show(LeagueClientUI leagueClientUI) {
        LoginUI loginUI = new LoginUI(leagueClientUI);
    }

    private LoginUI(LeagueClientUI clientUI) {
        super(clientUI);
        this.setLayout(new GridLayout(0, 1, 0, 5));
        this.setBorder(new EmptyBorder(5, 5, 5, 5));
        this.add(username = new JHintTextField("Username"));
        this.add(password = new JPasswordField());
        JButton login = new JButton("Login");
        login.addActionListener(this);
        this.add(login);
        this.setPreferredSize(new Dimension(300, 125));
        this.container.add(this);
        this.callback = clientUI;
        this.init();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = this.username.getText();
        String password = new String(this.password.getPassword());
        callback.onLogin(username, password);
    }
}
