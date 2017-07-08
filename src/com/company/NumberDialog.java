package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
        jButtons.get(0).requestFocus();

    }

    @Override
    public JRootPane createRootPane() {
        JRootPane rootPane = new JRootPane();
        KeyStroke strokeDown = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0);
        Action actionDown = new AbstractAction() {

            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                if (focusedButtonId == 2)
                    focusedButtonId = 0;
                else
                    focusedButtonId++;
                if (focusedButtonId == 1)
                    numberField.requestFocus();
                else if (focusedButtonId == 0)
                    jButtons.get(0).requestFocus();
                else
                    jButtons.get(1).requestFocus();
            }
        };
        KeyStroke strokeUp = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0);
        Action actionUp = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (focusedButtonId == 0)
                    focusedButtonId = 2;
                else
                    focusedButtonId--;
                if (focusedButtonId == 1)
                    numberField.requestFocus();
                else if (focusedButtonId == 0)
                    jButtons.get(0).requestFocus();
                else
                    jButtons.get(1).requestFocus();
            }
        };
        KeyStroke strokeEnter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        Action actionEnter = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (focusedButtonId == 0)
                    jButtons.get(0).doClick();
                if (focusedButtonId == 2)
                    jButtons.get(1).doClick();
            }
        };
        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(strokeDown, "DOWN");
        inputMap.put(strokeUp, "UP");
        inputMap.put(strokeEnter, "ENTER");
        rootPane.getActionMap().put("UP", actionUp);
        rootPane.getActionMap().put("DOWN", actionDown);
        rootPane.getActionMap().put("ENTER", actionEnter);
        return rootPane;
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
