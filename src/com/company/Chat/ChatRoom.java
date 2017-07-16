package com.company.Chat;


import com.company.Main;
import com.vdurmont.emoji.EmojiParser;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by reza on 7/14/17.
 */
public class ChatRoom extends Application{

    @FXML
    VBox playerChats;
    @FXML
    ImageView chatImage;
    @FXML
    Label chatName;
    @FXML
    VBox chatBox;
    @FXML
    ImageView emoji;
    @FXML
    Button send;
    @FXML
    ImageView image;
    @FXML
    ImageView gif;
    @FXML
    TextField txtField;
    @FXML
    Button stop;
    @FXML
    FlowPane ePane;
    @FXML
    Button browse;
    @FXML
    TextField gifAddress;
    @FXML
    TextField imageAddress;




    public static Map<Integer, String> playersID = new HashMap<>();
    public static Map<Integer, VBox> playerChatBoxes = new HashMap<>();
    public static Socket socket;
    public static ObjectOutputStream out;
    public static ObjectInputStream in;
    public static int currentVBoxID;
    public static ChatRoom chatRoom;
    public static int senderId = Main.playerId, rexeiverId = Main.playerId;
    public static ChatMessage online = new ChatMessage("online", Integer.toString(Main.playerId), Integer.toString(Main.playerId));
    public static ChatMessage messageToServer = online;
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException, URISyntaxException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("chatRoom.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Welcome");
        primaryStage.setScene(new Scene(root, 600, 420));
        primaryStage.setMaximized(true);
        primaryStage.setMinHeight(420);
        primaryStage.setMinWidth(600);
        primaryStage.setMaxHeight(420);
        primaryStage.setMaxWidth(600);
        primaryStage.show();
        chatRoom = loader.getController();
        primaryStage.getIcons().add(new Image(chatRoom.getClass().getClassLoader().getResource("icon.jpg").toString()));
        chats.start();
    }

    Thread chats = new Thread(() -> {
            playerChatBoxes.put(-1, new VBox());
            final HBox hBox1 = new HBox();
            ImageView imageView1 = new ImageView();
            imageView1.setFitWidth(20);
            imageView1.setFitHeight(20);
        try {
            imageView1.setImage(new Image(new FileInputStream(new File((chatRoom.getClass().getResource("groupImage.png")).toURI())), 20, 20, false, false));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        hBox1.getChildren().add(imageView1);
            Label label1 = new Label();
            label1.setPrefWidth(10);
            hBox1.getChildren().add(label1);
            hBox1.getChildren().add(new Label("group"));
            Platform.runLater(() -> chatRoom.playerChats.getChildren().add(hBox1));
            hBox1.setOnMouseClicked(e -> {
                chatRoom.chatImage.setImage(((ImageView) hBox1.getChildren().get(0)).getImage());
                chatRoom.chatName.setText(label1.getText());
                chatRoom.currentVBoxID = -1;
                chatRoom.chatBox.getChildren().remove(0, chatRoom.chatBox.getChildren().size());
                for (Node node : playerChatBoxes.get(chatRoom.currentVBoxID).getChildren()) {
                    chatRoom.chatBox.getChildren().add(node);
                }
            });
            while (true){
                try {
                    Thread.sleep(10);
                    if(socket != null){
                        socket.close();
                    }
                    socket = new Socket("localhost", 5033 + Main.gameID);
                    out = new ObjectOutputStream(socket.getOutputStream());
                    out.writeObject(messageToServer);
                    out.flush();
                    if(messageToServer != online){
                        messageToServer = online;
                        continue;
                    }
                    in = new ObjectInputStream(socket.getInputStream());
                    while(in != null && socket != null && !socket.isClosed()){
                            ChatMessage chatMessage = (ChatMessage) (in.readObject());
                            if(chatMessage.getMessage() != null){
                                System.out.println(chatMessage.getSender() + ": " + chatMessage.getMessage());
                            }
                            if(chatMessage.getSender().equals("-10")){
                                break;
                            }
                            else if(chatMessage.getSender().equals("-2")){
                                String[] str = chatMessage.getMessage().split(" ");
                                if(!playersID.containsKey(Integer.parseInt(str[0])) && Integer.parseInt(str[0]) != Main.playerId){
                                    playersID.put(Integer.parseInt(str[0]), str[1]);
                                    HBox hBox = new HBox();
                                    ImageView imageView = new ImageView();
                                    imageView.setFitWidth(20);
                                    imageView.setFitHeight(20);
                                    imageView.setImage(new Image(new FileInputStream(new File((chatRoom.getClass().getResource("userManImage.png")).toURI())), 20, 20, false, false));
                                    hBox.getChildren().add(imageView);
                                    Label label = new Label();
                                    label.setPrefWidth(10);
                                    hBox.getChildren().add(label);
                                    hBox.getChildren().add(new Label(str[1]));
                                    hBox.setOnMouseClicked(e -> {
                                        chatRoom.chatImage.setImage(((ImageView) hBox.getChildren().get(0)).getImage());
                                        chatRoom.chatName.setText(label.getText());
                                        chatRoom.currentVBoxID = Integer.parseInt(str[0]);
                                        chatRoom.chatBox.getChildren().remove(0, chatRoom.chatBox.getChildren().size());
                                        for (Node node : playerChatBoxes.get(chatRoom.currentVBoxID).getChildren()) {
                                            chatRoom.chatBox.getChildren().add(node);
                                        }
                                    });
                                    playerChatBoxes.put(Integer.parseInt(str[0]), new VBox());
                                    Platform.runLater(() -> chatRoom.playerChats.getChildren().add(hBox));
                                    continue;
                                }
                            }
                            else if(chatMessage.getReceiver().equals("-1")){
                                show(false, chatRoom, chatRoom.playerChatBoxes.get(-1), chatMessage);
                            }
                            else if(chatMessage.getReceiver().equals(Integer.toString(Main.playerId))){
                                show(false, chatRoom, chatRoom.playerChatBoxes.get(Integer.parseInt(chatMessage.getSender())), chatMessage);
                            }
                    }
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
    });

    public void sendImage(File image) throws IOException {
        sendData(new ChatMessage(image, Integer.toString(Main.playerId), ChatMessage.MessageType.IMAGE, Integer.toString(currentVBoxID)));
    }

    public void sendVoice(File voice) throws IOException {
        sendData(new ChatMessage(voice, Integer.toString(Main.playerId), ChatMessage.MessageType.VOICE, Integer.toString(currentVBoxID)));
    }

    public void sendGif(File gif) throws IOException {
        sendData(new ChatMessage(gif, Integer.toString(Main.playerId), ChatMessage.MessageType.GIF, Integer.toString(currentVBoxID)));
    }

    public void sendMessage(String message) throws IOException {
        sendData(new ChatMessage(message, Integer.toString(Main.playerId), Integer.toString(currentVBoxID)));
    }

    public void sendData(ChatMessage chatMessage) throws IOException {
        show(true, chatRoom, chatRoom.playerChatBoxes.get(Integer.parseInt(chatMessage.getReceiver())), chatMessage);
        if(socket != null)
            socket.close();
        messageToServer = chatMessage;
    }



    public void addStack(String message, boolean messageIsFromMe, VBox playerVBox) throws FileNotFoundException, URISyntaxException {
        Label lbl = new Label(message);
        add(lbl, messageIsFromMe, playerVBox);
    }

    public void addStack(File image, boolean messageIsFromMe, boolean isImage, VBox playerVBox) throws FileNotFoundException, URISyntaxException {
        ImageView imageView = new ImageView();
        imageView.setFitHeight(250);
        imageView.setFitWidth(250);
        imageView.setImage(new Image(new FileInputStream(image), 250, 250, false,false));
        add(imageView, messageIsFromMe, playerVBox);
    }

    public void addStack(File voice, boolean messageIsFromMe, VBox playerVBox) throws FileNotFoundException, URISyntaxException {
        Button button = new Button("Play");
        button.setOnAction(event ->{
            try {
                Clip clip = AudioSystem.getClip();
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(voice);
                clip.open(inputStream);
                clip.start();
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }});
        add(button, messageIsFromMe, playerVBox);
    }


    Boolean isPaused = true;
    public void addStackGif(File file, boolean messageIsFromMe, VBox playerVBox) throws FileNotFoundException, URISyntaxException {
        GifDecoder gif = new GifDecoder();
        gif.read(new BufferedInputStream(new FileInputStream(file)));
        Animation animation = new Animation(gif, 1000);
        HBox root = new HBox();
        ImageView imageView = animation.getView();
        imageView.setFitHeight(250);
        imageView.setFitWidth(250);
        imageView.setEffect(new BoxBlur());
        imageView.setOnMouseClicked(event -> {
            if(isPaused){
                imageView.setEffect(null);
                animation.play();
            }
            else{
                imageView.setEffect(new BoxBlur());
                animation.pause();
            }
            isPaused = !isPaused;
        });
        root.getChildren().addAll( imageView);
        add(root, messageIsFromMe, playerVBox);
    }

    public class Animation extends Transition {
        private ImageView imageView;
        private int count;
        private int lastIndex;
        private Image[] sequence;

        public Animation(GifDecoder d, double durationMs) {
            sequence = new Image[ d.getFrameCount()];
            for( int i=0; i < d.getFrameCount(); i++) {
                WritableImage wimg = null;
                BufferedImage bimg = d.getFrame(i);
                sequence[i] = SwingFXUtils.toFXImage( bimg, wimg);
            }
            init(durationMs);
        }

        private void init(double durationMs) {
            this.imageView = new ImageView();
            this.imageView.setImage(sequence[0]);
            this.count = sequence.length;
            setCycleCount(100);
            setCycleDuration(Duration.millis(durationMs));
            setInterpolator(Interpolator.LINEAR);
        }

        protected void interpolate(double k) {
            final int index = Math.min((int) Math.floor(k * count), count - 1);
            if (index != lastIndex) {
                imageView.setImage(sequence[index]);
                lastIndex = index;
            }
        }

        public ImageView getView() {
            return imageView;
        }

    }


    public void add(javafx.scene.Node e, boolean messageIsFromMe, VBox playerVBox) throws FileNotFoundException, URISyntaxException {
        HBox hBox = new HBox();
        ImageView imageView = new ImageView();
        if(chatBox != null){
            if(messageIsFromMe){
                imageView.setImage(new Image(new FileInputStream(new File((chatRoom.getClass().getResource("p1.png")).toURI()))));
            }
            else {
                imageView.setImage(new Image(new FileInputStream(new File((chatRoom.getClass().getResource("userManImage.png")).toURI()))));
            }
            imageView.prefWidth(20);
            imageView.prefHeight(20);
            imageView.setFitWidth(20);
            imageView.setFitHeight(20);
            imageView.maxWidth(20);
            imageView.maxHeight(20);
            hBox.getChildren().add(imageView);
            hBox.getChildren().add(e);
            playerVBox.getChildren().add(hBox);
            Label label = new Label();
            label.setPrefHeight(20);
            playerVBox.getChildren().add(label);
            if(playerChatBoxes.get(chatRoom.currentVBoxID) == playerVBox){
                Platform.runLater(() -> {
                    chatRoom.chatBox.getChildren().add(hBox);
                    chatRoom.chatBox.getChildren().add(label);
                });
            }
        }
    }

    public void emojis() throws IOException {
        ArrayList<Label> labels = new ArrayList<>();
        for (int i = 12; i <= 91; i++) {
            labels.add(new Label(EmojiParser.parseToUnicode(EmojiParser.parseToHtmlDecimal("&#1285" + i + ";"))));
        }
        ChatRoom controller = build("Emojis", 200, 300);
        FlowPane emojiPane = controller.ePane;
        for (Label label : labels) {
            label.setOnMouseClicked(event -> {
                txtField.setText(txtField.getText() + label.getText());
                ((Stage) label.getScene().getWindow()).close();
            });
            emojiPane.getChildren().add(label);
        }
    }

    public void choosePhoto() throws IOException {
        ChatRoom controller = build("ImageBrowse", 310, 33);
        controller.browse.setOnAction(event ->{
            try {
                File file = new File((getClass().getResource("images/" + controller.imageAddress.getText() + ".png")).toURI());
                ((Stage) controller.browse.getScene().getWindow()).close();
                sendImage(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        });
    }

    private static int numberOfVoices = 0;
    public void receiveVoice() throws IOException, LineUnavailableException, URISyntaxException {
        Recorder recorder = new Recorder();
        File file = new File((this.getClass().getResource("voices/voice" + numberOfVoices + ".wav")).toURI());
        recorder.beginRecording(file);
        ChatRoom controller = build("Stop", 80, 50);
        controller.stop.setOnAction(event -> {
            recorder.endRecording();
            ((Stage) controller.stop.getScene().getWindow()).close();
            try {
                sendVoice(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void chooseGif() throws IOException {
        ChatRoom controller = build("GifBrowse", 300, 33);
        controller.browse.setOnAction(event ->{
            try {
                File file = new File("gifs/" + controller.gifAddress.getText() + ".gif");
                ((Stage) controller.browse.getScene().getWindow()).close();
                ByteArrayOutputStream s = new ByteArrayOutputStream();
                s.close();
                sendGif(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    public ChatRoom build(String fxmlName, int width, int height) throws IOException {
        Stage stage = new Stage();
        stage.setTitle(fxmlName);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlName + ".fxml"));
        stage.setScene(new Scene(loader.load(), width, height));
        stage.show();
        return loader.getController();
    }

    @FXML
    public void send() throws IOException {
        sendMessage(txtField.getText());
        txtField.setText("");
    }



    public void show(boolean messageIsFromMe, ChatRoom controller, VBox playerVBox, ChatMessage chatMessage){
        switch (chatMessage.getMessageType()){
            case TEXT:
                textShow(messageIsFromMe, controller, playerVBox, chatMessage);
                break;
            case IMAGE:
                imageShow(messageIsFromMe, controller, playerVBox, chatMessage);
                break;
            case VOICE:
                voiceShow(messageIsFromMe, controller, playerVBox, chatMessage);
                break;
            case GIF:
                gifShow(messageIsFromMe, controller,  playerVBox, chatMessage);
                break;
        }
    }

    public void textShow(boolean messageIsFromMe, ChatRoom controller, VBox playerVBox, ChatMessage chatMessage) {
        try{
            if (messageIsFromMe) {
                controller.addStack(chatMessage.getMessage(), messageIsFromMe, playerVBox);
                System.out.println("-    " + chatMessage.getMessage());
            }
            else {
                Platform.runLater(() -> {
                    try {
                        controller.addStack(chatMessage.getMessage(), messageIsFromMe, playerVBox);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                });
                System.out.println(chatMessage.getMessage());
            }
        }catch(IOException ex){
            ex.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public void imageShow(boolean messageIsFromMe, ChatRoom controller, VBox playerVBox, ChatMessage chatMessage){
        try{
            if (messageIsFromMe) {
                controller.addStack(chatMessage.getImage(), messageIsFromMe, true, playerVBox);
                System.out.println(chatMessage.getSender() + " send a image");
            }
            else {
                Platform.runLater(() -> {
                    try {
                        controller.addStack(chatMessage.getImage(), messageIsFromMe, true,  playerVBox);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                });
                System.out.println(chatMessage.getSender() + " send a image");
            }
        }catch(IOException ex){} catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }
    public void voiceShow(boolean messageIsFromMe, ChatRoom controller, VBox playerVBox, ChatMessage chatMessage){
        try{
            if (messageIsFromMe) {
                controller.addStack(chatMessage.getVoice(), messageIsFromMe, playerVBox);
                System.out.println(chatMessage.getSender() + " send a voice");
            }
            else {
                Platform.runLater(() -> {
                    try {
                        controller.addStack(chatMessage.getVoice(), messageIsFromMe, playerVBox);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                });
                System.out.println(chatMessage.getSender() + " send a voice");
            }
        }catch(IOException ex){} catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public void gifShow(boolean messageIsFromMe, ChatRoom controller, VBox playerVBox, ChatMessage chatMessage){
        try{
            if (messageIsFromMe) {
                controller.addStackGif(chatMessage.getGif(), messageIsFromMe, playerVBox);
                System.out.println(chatMessage.getSender() + " send a gif");
            }
            else {
                Platform.runLater(() -> {
                    try {
                        controller.addStackGif(chatMessage.getGif(), messageIsFromMe, playerVBox);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                });
                System.out.println(chatMessage.getSender() + " send a gif");
            }
        }catch(IOException ex){} catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

}