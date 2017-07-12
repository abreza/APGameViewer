package com.company;


import javax.swing.*;
import java.awt.*;

public class YesNoDialog extends MyJDialog {

    private JButton buttonYes = new JButton("Yes");
    private JButton buttonNo = new JButton("No");

    public YesNoDialog(JFrame parent, String title, String message, MapViewer viewer) {
        super(parent, title, message, viewer, true);
        messages = new String[]{"Yes", "No"};
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
        buttonPane.remove(0);
        jButtons.remove(0);
        buttonPane.add(buttonYes);
        buttonPane.add(buttonNo);
        buttonsInPaneId.add(0, 0);
        buttonsInPaneId.add(1, 1);
        jButtons.add(buttonYes);
        jButtons.add(buttonNo);
        buttonPane.add(buttonExit);
        jButtons.add(buttonExit);
        getContentPane().add(buttonPane, BorderLayout.PAGE_END);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
        jButtons.get(focusedButtonId).requestFocus();
    }



}
