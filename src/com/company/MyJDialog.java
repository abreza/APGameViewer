package com.company;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class MyJDialog extends JDialog {

    int focusedButtonId = 0;
    protected MapViewer mapViewer;
    protected JPanel buttonPane;
    protected ArrayList<Integer> buttonsInPaneId = new ArrayList<>();
    protected ArrayList<JButton> jButtons = new ArrayList<>();
    protected String[] messages;
    protected JButton buttonExit;
    private static final long serialVersionUID = 1L;

    public MyJDialog(JFrame parent, String title, String message, MapViewer viewer, boolean firstConstructor) {
        super(parent, title);
        setModalityType(ModalityType.APPLICATION_MODAL);
        Point p = new Point(800, 500);
        setLocation(p.x, p.y);
        this.mapViewer = viewer;

        String newMessage = "<html>";
        newMessage += message.replace("<br>", "<br>");
        newMessage += "</html>";
        message = newMessage;
        JPanel messagePane = new JPanel();
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        messagePane.add(messageLabel);
        getContentPane().add(messagePane, BorderLayout.PAGE_START);

        setButtonPane();
        makeQuitButton();
    }

    private void setButtonPane() {
        buttonPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(new ImageIcon(this.getClass().getClassLoader()
                        .getResource("wooden-texture.jpg")).getImage(), 0, 0, null);
            }
        };
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.Y_AXIS));
    }


    public MyJDialog(JFrame parent, String title, String message, MapViewer viewer) {
        this(parent, title, message, viewer, true);
        getContentPane().add(buttonPane, BorderLayout.PAGE_END);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
        jButtons.get(focusedButtonId).requestFocus();
    }

    public MyJDialog(JFrame parent, String title, MapViewer viewer, boolean isFirst, String... messages) {
        super(parent, title);
        setModalityType(ModalityType.APPLICATION_MODAL);
        Point p = new Point(800, 500);
        setLocation(p.x, p.y);
        this.mapViewer = viewer;

        this.messages = messages;
        setButtonPane();
        if (messages.length < 10) {
            int i = 0;
            for (i = 0; i < messages.length; i++) {
                addButton(messages[i]);
                buttonsInPaneId.add(i);
            }
        } else {
            int i = 0;
            for (i = 0; i < messages.length; i++) {
                if (i < 10) {
                    addButton(messages[i]);
                    buttonsInPaneId.add(i);
                } else
                    makeButton(messages[i]);
            }
        }

        makeQuitButton();


    }

    public MyJDialog(JFrame parent, String title, MapViewer viewer, String... messages) {
        this(parent, title, viewer, true, messages);
        getContentPane().add(buttonPane, BorderLayout.PAGE_END);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
        jButtons.get(focusedButtonId).requestFocus();

    }

    private void makeQuitButton() {
        JButton button = new JButton("Quit");
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.addActionListener(new QuitListener());
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        buttonPane.add(button);
        jButtons.add(button);
        buttonExit = button;
    }

    public JButton makeButton(String message) {
        JButton button = new JButton(message);
        button.addActionListener(new ButtonListener(button));
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        jButtons.add(button);
        return button;
    }

    public void addButton(String message) {
        buttonPane.add(makeButton(message));
    }

    public JRootPane createRootPane() {
        JRootPane rootPane = new JRootPane();
        KeyStroke strokeDown = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0);
        Action actionDown = new AbstractAction() {

            private static final long serialVersionUID = 1L;

            public void actionPerformed(ActionEvent e) {
                boolean change = true;
                if (focusedButtonId == jButtons.size() - 1) {
                    change = false;
                    focusedButtonId = -1;
                    for (int i = 0; i < buttonsInPaneId.size(); i++) {
                        buttonsInPaneId.set(i, i);
                    }
                    for (int i = 0; i < buttonsInPaneId.size(); i++) {
                        if (buttonsInPaneId.get(i) < messages.length)
                            jButtons.get(i).setText(messages[buttonsInPaneId.get(i)]);
                    }
                    jButtons.get(0).requestFocus();
                }
                focusedButtonId++;
                if (focusedButtonId == jButtons.size() - 1) {
                    jButtons.get(focusedButtonId).requestFocus();
                } else if (change) {
                    if (!buttonsInPaneId.contains(focusedButtonId)) {
                        for (int i = 0; i < buttonsInPaneId.size() - 1; i++) {
                            buttonsInPaneId.set(i, buttonsInPaneId.get(i + 1));
                        }
                        buttonsInPaneId.set(buttonsInPaneId.size() - 1, focusedButtonId);
                        for (int i = 0; i < buttonsInPaneId.size(); i++) {
                            if (buttonsInPaneId.get(i) < messages.length)
                                jButtons.get(i).setText(messages[buttonsInPaneId.get(i)]);
                        }
                    }
                    jButtons.get(buttonsInPaneId.indexOf(focusedButtonId)).requestFocus();
                }
            }
        };
        KeyStroke strokeUp = KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0);
        Action actionUp = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (focusedButtonId == 0) {
                    focusedButtonId = jButtons.size();
                    for (int i = 0; i < buttonsInPaneId.size(); i++) {
                        buttonsInPaneId.set(i, jButtons.size() - 1 - buttonsInPaneId.size() + i);
                    }
                    for (int i = 0; i < buttonsInPaneId.size(); i++) {
                        if (buttonsInPaneId.get(i) < messages.length)
                            jButtons.get(i).setText(messages[buttonsInPaneId.get(i)]);
                    }
                }
                focusedButtonId--;
                if (focusedButtonId == jButtons.size() - 1)
                    buttonExit.requestFocus();
                else {
                    if (!buttonsInPaneId.contains(focusedButtonId)) {
                        for (int i = buttonsInPaneId.size() - 1; i > 0; i--) {
                            buttonsInPaneId.set(i, buttonsInPaneId.get(i - 1));
                        }
                        buttonsInPaneId.set(0, focusedButtonId);
                        for (int i = 0; i < buttonsInPaneId.size(); i++) {
                            if (buttonsInPaneId.get(i) < messages.length)
                                jButtons.get(i).setText(messages[buttonsInPaneId.get(i)]);
                        }
                    }
                    jButtons.get(buttonsInPaneId.indexOf(focusedButtonId)).requestFocus();
                }
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

        public ButtonListener(String message) {
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