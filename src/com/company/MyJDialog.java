package com.company;


import javafx.scene.input.KeyCode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class MyJDialog extends JDialog {

    int focusedButtonId = 0;
    private MapViewer mapViewer;
    private JPanel buttonPane;
    private ArrayList<JButton> jButtons = new ArrayList<>();
    private static final long serialVersionUID = 1L;

    public MyJDialog(JFrame parent, String title, String message, MapViewer viewer, boolean isYesNo){
        super(parent, title);
        setModalityType(ModalityType.APPLICATION_MODAL);
        Point p = new Point(800, 500);
        setLocation(p.x, p.y);
        this.mapViewer = viewer;

        JPanel messagePane = new JPanel();
        JLabel messageLabel = new JLabel(message);
        messagePane.add(messageLabel);
        getContentPane().add(messagePane, BorderLayout.PAGE_START);

        buttonPane = new JPanel();
        makeQuitButton();

        JButton buttonYes = new JButton("Yes");
        JButton buttonNo = new JButton("No");
        buttonYes.addActionListener(new ButtonListener("Y/N/y"));
        buttonNo.addActionListener(new ButtonListener("Y/N/n"));
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

    public MyJDialog(JFrame parent, String title, String message, MapViewer viewer){
        super(parent, title);
        setModalityType(ModalityType.APPLICATION_MODAL);
        Point p = new Point(800, 500);
        setLocation(p.x, p.y);
        this.mapViewer = viewer;

        JPanel messagePane = new JPanel();
        JLabel messageLabel = new JLabel(message);
        messagePane.add(messageLabel);
        getContentPane().add(messagePane, BorderLayout.PAGE_START);

        buttonPane = new JPanel();
        makeQuitButton();
        getContentPane().add(buttonPane, BorderLayout.PAGE_END);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
        jButtons.get(focusedButtonId).requestFocus();
    }

    public MyJDialog(JFrame parent, String title, MapViewer viewer, String... messages) {
        super(parent, title);
        setModalityType(ModalityType.APPLICATION_MODAL);
        Point p = new Point(800, 500);
        setLocation(p.x, p.y);
        this.mapViewer = viewer;

        buttonPane = new JPanel();
        for (int i = 0; i < messages.length; i++) {
            addButton(messages[i]);
        }

        makeQuitButton();

        getContentPane().add(buttonPane, BorderLayout.PAGE_END);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
        jButtons.get(focusedButtonId).requestFocus();

    }

    private void makeQuitButton() {
        JButton button = new JButton("Quit");
        buttonPane.add(button);
        jButtons.add(button);
        button.addActionListener(new QuitListener());
    }

    public void addButton(String message) {
        JButton button = new JButton(message);
        button.addActionListener(new ButtonListener(button));
        buttonPane.add(button);
        jButtons.add(button);
    }

    public JRootPane createRootPane() {
        JRootPane rootPane = new JRootPane();
        KeyStroke strokeDown = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0);
        Action actionDown = new AbstractAction() {

            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                if (focusedButtonId == jButtons.size() - 1)
                    focusedButtonId = -1;
                jButtons.get(++focusedButtonId).requestFocus();
            }
        };
        KeyStroke strokeUp = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0);
        Action actionUp = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (focusedButtonId == 0)
                    focusedButtonId = jButtons.size();
                jButtons.get(--focusedButtonId).requestFocus();
            }
        };
        KeyStroke strokeEnter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        Action actionEnter = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jButtons.get(focusedButtonId).doClick();
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

    class ButtonListener implements ActionListener {

        private JButton button = null;
        private String message;

        public ButtonListener(String message){
            this.message = message;
        }

        public ButtonListener(JButton button) {
            this.button = button;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            dispose();
            if (button != null)
                mapViewer.sendAndGetResponse("inspect " + button.getText() + "\n");
            else {
                mapViewer.sendAndGetResponse(message + "\n");
            }
        }
    }

    class QuitListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            setVisible(false);
            dispose();
            mapViewer.sendAndGetResponse("back\n");
        }
    }


}