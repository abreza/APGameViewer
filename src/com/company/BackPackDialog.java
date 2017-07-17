package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BackPackDialog extends MyJDialog {

    private JLabel message;

    public BackPackDialog(JFrame parent, String title, MapViewer viewer, String... messages) {
        super(parent, title, viewer, true, messages);

        for (JButton button: jButtons) {
            if (button.getActionListeners()[0] instanceof QuitListener)
                continue;
            button.removeActionListener(button.getActionListeners()[0]);
            button.addActionListener(new BackPackButtonListener(button));
        }

        JPanel messagePane = new JPanel();
        JLabel messageLabel = new JLabel(title);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        messagePane.add(messageLabel);
        getContentPane().add(messagePane, BorderLayout.PAGE_START);

        getContentPane().add(buttonPane, BorderLayout.PAGE_END);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
        jButtons.get(focusedButtonId).requestFocus();

    }

    class BackPackButtonListener implements ActionListener{

        private JButton button;

        public BackPackButtonListener(JButton button) {
            this.button = button;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            dispose();
            mapViewer.changeInRequest();
            mapViewer.sendAndGetResponse("backpack/" + button.getText().substring(0, button.getText().indexOf('.')) + "\n");
        }
    }
}
