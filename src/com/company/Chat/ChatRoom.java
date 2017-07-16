package com.company.Chat;


import com.company.GameState;
import com.company.Main;
import com.vdurmont.emoji.EmojiParser;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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




    Map<Integer, String> playersID = new HashMap<>();

    Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;

    public ChatRoom chatRoom;
    public static void main(String[] args){
        launch(args);
    }

    public static void myStart(){
        try {
            new ChatRoom().start(new Stage());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws IOException, URISyntaxException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("chatRoom.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Welcome");
        primaryStage.getIcons().add(new Image(this.getClass().getClassLoader().getResource("icon.jpg").toString()));
        primaryStage.setScene(new Scene(root, 600, 420));
        primaryStage.setMaximized(true);
        primaryStage.setMinHeight(420);
        primaryStage.setMinWidth(600);
        primaryStage.setMaxHeight(420);
        primaryStage.setMaxWidth(600);
        primaryStage.show();
        chatRoom = loader.getController();
        chats.start();
    }

    Thread chats = new Thread(() -> {
        try {
            final HBox hBox1 = new HBox();
            ImageView imageView1 = new ImageView();
            imageView1.setFitWidth(20);
            imageView1.setFitHeight(20);
            imageView1.setImage(new Image(new FileInputStream(new File((chatRoom.getClass().getResource("groupImage.png")).toURI())), 20, 20, false, false));
            hBox1.getChildren().add(imageView1);
            hBox1.getChildren().add(new Label("group"));
            Platform.runLater(() -> chatRoom.playerChats.getChildren().add(hBox1));
            hBox1.setOnMouseClicked(event -> {
                chatRoom.chatImage.setImage(((ImageView) hBox1.getChildren().get(0)).getImage());
            });
            socket = new Socket("localhost", 5033 + Main.gameID);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            while(in != null){
                ChatMessage chatMessage = (ChatMessage) (in.readObject());
                if(chatMessage.getMessageType() == ChatMessage.MessageType.TEXT){
                    String[] str = chatMessage.getMessage().split(" ");
                    if(str[0].equals("___")){
                        if(!playersID.containsKey(Integer.parseInt(str[1]))){
                            playersID.put(Integer.parseInt(str[1]), str[2]);
                            HBox hBox = new HBox();
                            ImageView imageView = new ImageView();
                            imageView.setFitWidth(20);
                            imageView.setFitHeight(20);
                            imageView.setImage(new Image(new FileInputStream(new File((chatRoom.getClass().getResource("userManImage.png")).toURI())), 20, 20, false, false));
                            hBox.getChildren().add(imageView);
                            hBox.getChildren().add(new Label(str[2]));
                            hBox.setOnMouseClicked(event -> {
                                image.setImage(((ImageView) hBox.getChildren().get(0)).getImage());
                            });
                            Platform.runLater(() -> chatRoom.playerChats.getChildren().add(hBox));
                            continue;
                        }
                    }
                }
                chatMessage.show(false, this);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    });

    public void sendImage(String sender, File image, ChatRoom controller) throws IOException {
        sendData(new ChatMessage(image, sender, ChatMessage.MessageType.IMAGE), controller);
    }

    public void sendVoice(String sender, File voice, ChatRoom controller) throws IOException {
        sendData(new ChatMessage(voice, sender, ChatMessage.MessageType.VOICE), controller);
    }

    public void sendGif(String sender, File gif, ChatRoom controller) throws IOException {
        sendData(new ChatMessage(gif, sender, ChatMessage.MessageType.GIF), controller);
    }

    public void sendMessage(String sender, String message, ChatRoom controller) throws IOException {
        sendData(new ChatMessage(message, sender), controller);
    }

    public void sendData(ChatMessage chatMessage, ChatRoom controller) throws IOException {
        chatMessage.show(true, controller);
        out.writeObject(chatMessage);
    }






    public void addStack(String message, boolean messageIsFromMe) throws FileNotFoundException, URISyntaxException {
        Label lbl = new Label(message);
        add(lbl, messageIsFromMe);
    }

    public void addStack(File image, boolean messageIsFromMe, boolean isImage) throws FileNotFoundException, URISyntaxException {
        ImageView imageView = new ImageView();
        imageView.setFitHeight(250);
        imageView.setFitWidth(250);
        imageView.setImage(new Image(new FileInputStream(image), 250, 250, false,false));
        add(imageView, messageIsFromMe);
    }

    public void addStack(File voice, boolean messageIsFromMe) throws FileNotFoundException, URISyntaxException {
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
        add(button, messageIsFromMe);
    }


    Boolean isPaused = true;
    public void addStackGif(File file, boolean messageIsFromMe) throws FileNotFoundException, URISyntaxException {
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
        add(root, messageIsFromMe);
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


    public void add(javafx.scene.Node e, boolean messageIsFromMe) throws FileNotFoundException, URISyntaxException {
        HBox hBox = new HBox();
        ImageView imageView = new ImageView();
        if(chatBox != null){
            if(messageIsFromMe){
                imageView.setImage(new Image(new FileInputStream("resource/p2.png")));
            }
            else {
                imageView.setImage(new Image(new FileInputStream("resource/userManImage.png")));
            }
            imageView.prefWidth(20);
            imageView.prefHeight(20);
            imageView.setFitWidth(20);
            imageView.setFitHeight(20);
            imageView.maxWidth(20);
            imageView.maxHeight(20);
            hBox.getChildren().add(imageView);
            hBox.getChildren().add(e);
            chatBox.getChildren().add(hBox);
            Label label = new Label();
            label.setPrefHeight(20);
            chatBox.getChildren().add(label);
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
                sendImage(GameState.player.getName(), file, this);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        });
    }

    private int numberOfVoices = 0;
    public void receiveVoice() throws IOException, LineUnavailableException, URISyntaxException {
        Recorder recorder = new Recorder();
        File file = new File((getClass().getResource("voices/voice" + numberOfVoices + ".wav")).toURI());
        recorder.beginRecording(file);
        ChatRoom controller = build("Stop", 80, 50);
        controller.stop.setOnAction(event -> {
            recorder.endRecording();
            ((Stage) controller.stop.getScene().getWindow()).close();
            try {
                sendVoice(GameState.player.getName(), file, this);
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
                sendGif(GameState.player.getName(), file, this);
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
        sendMessage(GameState.player.getName(), txtField.getText(), this);
        txtField.setText("");
    }

}