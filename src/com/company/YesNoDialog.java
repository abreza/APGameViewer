package com.company;


import javax.swing.*;
import java.awt.*;

public class YesNoDialog extends MyJDialog {

    private JButton buttonYes = new JButton("Yes");
    private JButton buttonNo = new JButton("No");

    public YesNoDialog(JFrame parent, String title, String message, MapViewer viewer) {
        super(parent, title, message, viewer, true);
        buttonYes.addActionListener(new ButtonListener("Y/N/y"));
        buttonNo.addActionListener(new ButtonListener("Y/N/n"));
        buttonYes.setFont(new Font("Arial",Font.PLAIN, 20));
        buttonYes.setOpaque(false);
        buttonYes.setContentAreaFilled(false);
        buttonYes.setBorderPainted(false);
        buttonNo.setFont(new Font("Arial", Font.PLAIN, 20));
        buttonNo.setOpaque(false);
        buttonNo.setContentAreaFilled(false);
        buttonNo.setBorderPainted(false);
        buttonPane.add(buttonYes);
        buttonPane.add(buttonNo);
        jButtons.add(buttonYes);
        jButtons.add(buttonNo);
        getContentPane().add(buttonPane, BorderLayout.PAGE_END);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
        jButtons.get(focusedButtonId).requestFocus();
    }



}
