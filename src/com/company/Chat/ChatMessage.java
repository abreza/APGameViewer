package com.company.Chat;

import java.io.File;
import java.io.Serializable;

public class ChatMessage implements Serializable{
    private String message;
    private String sender;
    private String receiver;
    private File image;
    private File gif;
    public enum MessageType {
        TEXT, IMAGE, GIF, VOICE
    }
    private MessageType messageType;
    private File voice;

    public ChatMessage(String message, String sender, String receiver) {
        this.message = message;
        this.sender = sender;
        this.messageType = MessageType.TEXT;
        this.receiver = receiver;
    }

    public ChatMessage(File file, String sender, MessageType messageType, String receiver) {
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
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
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

    public String getReceiver() {
        return receiver;
    }
}

