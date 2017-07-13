package com.company;

import javafx.application.Platform;
import javafx.stage.Stage;
import org.newdawn.slick.GameContainer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PauseDialog extends MyJDialog{

    private JButton buttonContinue = new JButton("Continue"), buttonBackPack = new JButton("Backpack");
    private JButton buttonStatus = new JButton("Status");
    private GameContainer gc;


    public PauseDialog(JFrame parent, String title, String message, MapViewer viewer, GameContainer gc) {
        super(parent, title, message, viewer, true);
        this.gc = gc;
        messages = new String[]{"Continue", "Backpack", "Status"};
        setListeners();
        setButtonGraphic(buttonContinue);
        setButtonGraphic(buttonBackPack);
        setButtonGraphic(buttonStatus);
        buttonPane.remove(0);
        jButtons.remove(0);
        setButtonsInPaneId();
//        buttonPane.remove(0);
//        jButtons.remove(0);
        buttonPane.add(buttonContinue);
        buttonPane.add(buttonBackPack);
        buttonPane.add(buttonStatus);
        buttonPane.add(buttonExit);
        jButtons.add(buttonContinue);
        jButtons.add(buttonBackPack);
        jButtons.add(buttonStatus);
        jButtons.add(buttonExit);



        getContentPane().add(buttonPane, BorderLayout.PAGE_END);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setVisible(true);
        jButtons.get(focusedButtonId).requestFocus();

    }

    private void setListeners() {
        buttonExit.removeActionListener(buttonExit.getActionListeners()[0]);
        buttonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
                gc.exit();
            }
        });
        buttonBackPack.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
                mapViewer.sendAndGetResponse("backpack\n");
            }
        });
        buttonContinue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
            }
        });
        buttonStatus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
                MyJDialog dialog = new MyJDialog(new JFrame(), "status", mapViewer, "time\n", "my name\n", "player\n");
                dialog.setSize(500, 300);
            }
        });
    }

    private void setButtonsInPaneId() {
        buttonsInPaneId.add(0, 0);
        buttonsInPaneId.add(1, 1);
        buttonsInPaneId.add(2, 2);
    }

    private void setButtonGraphic(JButton button) {
        button.setFont(new Font("Arial",Font.PLAIN, 20));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
    }
}
