package com.company.Chat;

import javafx.application.Platform;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;

public class ChatMessage implements Serializable{
    private String message;
    private String sender;
    private File image;
    private File gif;
    public enum MessageType {
        TEXT, IMAGE, GIF, VOICE
    }
    private MessageType messageType;
    private File voice;

    public ChatMessage(String message, String sender) {
        this.message = message;
        this.sender = sender;
        this.messageType = MessageType.TEXT;
    }

    public ChatMessage(File file, String sender, MessageType messageType) {
        if(messageType == MessageType.IMAGE){
            this.image = file;
        }
        else if(messageType == MessageType.GIF){
            this.gif = file;
        }
        else{
            this.voice = file;
        }
        this.sender = sender;
        this.messageType = messageType;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public void show(boolean messageIsFromMe, ChatRoom controller){
        switch (messageType){
            case TEXT:
                textShow(messageIsFromMe, controller);
                break;
            case IMAGE:
                imageShow(messageIsFromMe, controller);
                break;
            case VOICE:
                voiceShow(messageIsFromMe, controller);
                break;
            case GIF:
                gifShow(messageIsFromMe, controller);
                break;
        }
    }

    public void textShow(boolean messageIsFromMe, ChatRoom controller) {
        try{
            if (messageIsFromMe) {
                controller.addStack(getMessage(), messageIsFromMe);
                System.out.println("-    " + getMessage());
            }
            else {
                Platform.runLater(() -> {
                    try {
                        controller.addStack(getMessage(), messageIsFromMe);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                });
                System.out.println(getMessage());
            }
        }catch(IOException ex){} catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public void imageShow(boolean messageIsFromMe, ChatRoom controller){
        try{
            if (messageIsFromMe) {
                controller.addStack(getImage(), messageIsFromMe, true);
                System.out.println(getSender() + " send a image");
            }
            else {
                Platform.runLater(() -> {
                    try {
                        controller.addStack(getImage(), messageIsFromMe);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                });
                System.out.println(getSender() + " send a image");
            }
        }catch(IOException ex){} catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }
    public void voiceShow(boolean messageIsFromMe, ChatRoom controller){
        try{
            if (messageIsFromMe) {
                controller.addStack(getVoice(), messageIsFromMe);
                System.out.println(getSender() + " send a voice");
            }
            else {
                Platform.runLater(() -> {
                    try {
                        controller.addStack(getVoice(), messageIsFromMe);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                });
                System.out.println(getSender() + " send a voice");
            }
        }catch(IOException ex){} catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public void gifShow(boolean messageIsFromMe, ChatRoom controller){
        try{
            if (messageIsFromMe) {
                controller.addStackGif(getGif(), messageIsFromMe);
                System.out.println(getSender() + " send a gif");
            }
            else {
                Platform.runLater(() -> {
                    try {
                        controller.addStackGif(getGif(), messageIsFromMe);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                });
                System.out.println(getSender() + " send a gif");
            }
        }catch(IOException ex){} catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public File getImage() {
        return image;
    }

    public File getVoice() {
        return voice;
    }

    public File getGif() {
        return gif;
    }

    public void setImage(File image) {
        this.image = image;
    }

    public MessageType getMessageType() {
        return messageType;
    }
}

