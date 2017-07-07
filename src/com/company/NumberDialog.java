package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class NumberDialog extends MyJDialog {

    private JTextField numberField = new HintTextField("type number here");
    private JButton enterButton = new JButton("Enter");

    public NumberDialog(JFrame parent, String title, String message, MapViewer viewer) {
        super(parent, title, message, viewer, true);
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
                mapViewer.sendAndGetResponse("Number/" + numberField.getText() + "\n");
            }
        });
        enterButton.setFont(new Font("Arial",Font.PLAIN, 20));
        enterButton.setOpaque(false);
        enterButton.setContentAreaFilled(false);
        enterButton.setBorderPainted(false);
        numberField.setOpaque(false);
        numberField.setFont(new Font("Arial",Font.PLAIN, 20));
        buttonPane.add(numberField);
        buttonPane.add(enterButton);
        jButtons.add(enterButton);
        getContentPane().add(buttonPane, BorderLayout.PAGE_END);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
        jButtons.get(focusedButtonId).requestFocus();

    }

    class HintTextField extends JTextField implements FocusListener {

        private final String hint;
        private boolean showingHint;

        public HintTextField(final String hint) {
            super(hint);
            this.hint = hint;
            this.showingHint = true;
            super.addFocusListener(this);
        }

        @Override
        public void focusGained(FocusEvent e) {
            if(this.getText().isEmpty()) {
                super.setText("");
                showingHint = false;
            }
        }
        @Override
        public void focusLost(FocusEvent e) {
            if(this.getText().isEmpty()) {
                super.setText(hint);
                showingHint = true;
            }
        }

        @Override
        public String getText() {
            return showingHint ? "" : super.getText();
        }
    }
}
