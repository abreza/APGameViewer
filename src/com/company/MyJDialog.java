package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyJDialog extends JDialog {

    private MapViewer mapViewer;
    private JPanel buttonPane;
    private static final long serialVersionUID = 1L;

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
        JButton button = new JButton("Quit");
        buttonPane.add(button);
        button.addActionListener(new QuitListener());
        getContentPane().add(buttonPane, BorderLayout.PAGE_END);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
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
        JButton button = new JButton("Quit");
        buttonPane.add(button);
        button.addActionListener(new QuitListener());
        getContentPane().add(buttonPane, BorderLayout.PAGE_END);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public void addButton(String message) {
        JButton button = new JButton(message);
        button.addActionListener(new ButtonListener(button));
        buttonPane.add(button);
    }

    public JRootPane createRootPane() {
        JRootPane rootPane = new JRootPane();
        KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");
        Action action = new AbstractAction() {

            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                System.out.println("escaping..");
                setVisible(false);
                dispose();
            }
        };
        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(stroke, "ESCAPE");
        rootPane.getActionMap().put("ESCAPE", action);
        return rootPane;
    }

    class ButtonListener implements ActionListener {

        private JButton button;

        public ButtonListener(JButton button) {
            this.button = button;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            mapViewer.sendAndGetResponse("inspect " + button.getText() + "\n");
            setVisible(false);
            dispose();
        }
    }

    class QuitListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            mapViewer.sendAndGetResponse("back\n");
            setVisible(false);
            dispose();
        }
    }

}