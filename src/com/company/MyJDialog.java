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
    protected ArrayList<JButton> jButtons = new ArrayList<>();
    private static final long serialVersionUID = 1L;

    public MyJDialog(JFrame parent, String title, String message, MapViewer viewer, boolean firstConstructor) {
        super(parent, title);
        setModalityType(ModalityType.APPLICATION_MODAL);
        Point p = new Point(800, 500);
        setLocation(p.x, p.y);
        this.mapViewer = viewer;

        JPanel messagePane = new JPanel();
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        messagePane.add(messageLabel);
        getContentPane().add(messagePane, BorderLayout.PAGE_START);

        setButtonPane();
        makeQuitButton();
    }

    private void setButtonPane() {
        buttonPane = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(new ImageIcon(this.getClass().getClassLoader()
                        .getResource("wooden-texture.jpg")).getImage(), 0, 0, null);
            }
        };
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.Y_AXIS));
    }



    public MyJDialog(JFrame parent, String title, String message, MapViewer viewer){
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

        setButtonPane();
        for (int i = 0; i < messages.length; i++) {
            addButton(messages[i]);
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
    }

    public void addButton(String message) {
        JButton button = new JButton(message);
        button.addActionListener(new ButtonListener(button));
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
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