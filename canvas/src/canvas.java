import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.awt.*;
import java.beans.EventHandler;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.awt.Color.BLACK;

public class canvas extends Application {
    DataOutputStream outputToClient=null;
    DataInputStream inputfromclient=null;
    Color color = Color.ANTIQUEWHITE;
    Canvas canvas = new Canvas(500, 600);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    Label labelforclue=new Label();
    Label labelfortimer=new Label();
    int i=30;
    Runnable timer=new timer();
    Thread thread1=new Thread(timer);
    Media clockmedia=new Media(new File("src\\Fast-clock-ticking-sound-effect.mp3").toURI().toString());
    MediaPlayer clockplayer=new MediaPlayer(clockmedia);
    Media timesupmedia=new Media(new File("src\\Clock-ringing.mp3").toURI().toString());
    MediaPlayer timesupPlayer=new MediaPlayer(timesupmedia);
    Media welcomemedia=new Media(new File("src\\2019-03-17_-_Too_Crazy_-_David_Fesliyan.mp3").toURI().toString());
    MediaPlayer welcomeplayer=new MediaPlayer(welcomemedia);
    TextArea tfforgues=new TextArea();
    String guess;

    int p=0;

    Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage=stage;
        Image imageclock=new Image("clock.jpg",200,200,false,true);

        ImageView imageviewclock=new ImageView(imageclock);
        StackPane stackpaneforclock=new StackPane();
        stackpaneforclock.getChildren().addAll(imageviewclock,labelfortimer);
        labelfortimer.setAlignment(Pos.CENTER);
        labelfortimer.setFont(new Font("Verdana",50));
       // thread1.start();

        labelfortimer.setText("00:"+String.valueOf(i));
                   /* Circle circle1 = new Circle(10);
                    circle1.setCenterX(100);
                    circle1.setCenterY(4);
                    circle1.setFill(Color.BLUE);
                    circle1.setOnMouseClicked(e -> {
                        color = Color.BLUE;
                        canvas.setOnMouseDragged(m -> {
                            gc.setFill(color);
                            gc.fillOval(m.getX() - 2, m.getY() - 2, 5, 5);
                        });
                    });
                    Circle circle2 = new Circle(10);
                    circle2.setCenterX(100);
                    circle2.setCenterY(20);
                    circle2.setFill(Color.RED);
                    circle2.setOnMouseClicked(e -> {
                        color = Color.RED;
                        canvas.setOnMouseDragged(m -> {
                            gc.setFill(color);
                            gc.fillOval(m.getX() - 2, m.getY() - 2, 5, 5);
                        });
                    });
                    Circle circle3 = new Circle(10);
                    circle3.setCenterX(100);
                    circle3.setCenterY(40);
                    circle3.setFill(Color.YELLOW);
                    circle3.setOnMouseClicked(e -> {
                        color = Color.YELLOW;
                        canvas.setOnMouseDragged(m -> {
                            gc.setFill(color);
                            gc.fillOval(m.getX() - 2, m.getY() - 2, 5, 5);
                        });
                    });
                    Circle circle4 = new Circle(10);
                    circle4.setCenterX(100);
                    circle4.setCenterY(60);
                    circle4.setFill(Color.GREEN);
                    circle4.setOnMouseClicked(e -> {
                        color = Color.GREEN;
                        canvas.setOnMouseDragged(m -> {
                            gc.setFill(color);
                            gc.fillOval(m.getX() - 2, m.getY() - 2, 5, 5);
                        });
                    });
                    Rectangle rect = new Rectangle(20, 20);
                    rect.setX(100);
                    rect.setY(80);
                    rect.setStroke(Color.BLACK);
                    rect.setFill(Color.WHITE);
                    rect.setStroke(Color.BLACK);
                    rect.setOnMouseClicked(e -> {
                        color = Color.WHITE;
                        canvas.setOnMouseDragged(m -> {
                            gc.setFill(color);
                            gc.fillRect(m.getX() - 2, m.getY() - 2, 10, 10);
                        });
                    });*/
                    labelforclue.setFont(new Font("Verdana",25));
                    labelforclue.setText(getrandomeword());

                    Label welcome = new Label("");

        Image image=new Image("pictionary.png",750,600,false,true);
        ImageView imageview=new ImageView(image);
        welcome.setFont(new Font("Curlz MT", 25));
        welcome.setStyle("-fx-font-weight: bold");
        welcome.setTextFill(Color.WHITE);
        welcome.setText("\n \n \n \n \nWelcome to PICTIONARY.\n Here pictures are your Dictionary. \n Get Ready for the clues. \n How much to draw you need to choose \n You have 30 Seconds only.\n Time will not run slowly");
        //welcome.setLayoutX(40);
        StackPane stackPane=new StackPane();
        stackPane.getChildren().addAll(imageview,welcome);
        welcome.setAlignment(Pos.BOTTOM_CENTER);
        FadeTransition ft=new FadeTransition(Duration.millis(2000),stackPane);
        ft.setDelay(Duration.millis(15000));
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setCycleCount(1);
        ft.setAutoReverse(false);
        ft.play();
        welcomeplayer.play();
        HBox hbox = new HBox();
        Image imageboard=new Image("board.png",500,600,false,true);

       // ImageView imageviewboard=new ImageView(imageboard);
       // imageviewboard.setFitHeight(50);
        //imageviewboard.setFitWidth(50);
        BackgroundImage backgroundimage = new BackgroundImage(imageboard,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
                hbox.setBackground(new Background(backgroundimage));
                    hbox.getChildren().addAll(canvas);
                    HBox hboxforall=new HBox();
                    BorderPane bpforlabelandclock=new BorderPane();
                    bpforlabelandclock.setCenter(stackpaneforclock);


                    bpforlabelandclock.setTop(labelforclue);
                    tfforgues.setFont(new Font("Verdana",15));
                    tfforgues.setText("Guesses by your partner:"+"\n");
                    bpforlabelandclock.setBottom(tfforgues);



                    hboxforall.getChildren().addAll(hbox,bpforlabelandclock);
                    StackPane stackPane1=new StackPane();
                    stackPane1.getChildren().addAll(hboxforall,stackPane);
                    Scene scene = new Scene(stackPane1,750,600);
                    stage.setScene(scene);
                    stackPane.setOnMousePressed(e->{
                        ft.pause();

                        stackPane1.getChildren().remove(stackPane);
                        thread1.start();
                        clockplayer.play();
                        welcomeplayer.pause();

                    });
                    stage.show();
        new Thread(() -> {
            try {
                // Create a server socket
                ServerSocket serverSocket = new ServerSocket(8000);


                while (true) {
                    // Listen for a new connection request
                    Socket socket = serverSocket.accept();




                    new Thread(new HandleAClient(socket)).start();
                }
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }).start();

    }

    class HandleAClient implements Runnable {
        private Socket socket; // A connected socket

        /**
         * Construct a thread
         */
        public HandleAClient(Socket socket) {
            this.socket = socket;
        }

        /**
         * Run a thread
         */
        public void run() {
            try {
                // Create data input and output streams

                outputToClient = new DataOutputStream(
                        socket.getOutputStream());
                outputToClient.writeUTF(labelforclue.getText());
                inputfromclient = new DataInputStream(socket.getInputStream());
                // Continuously serve the client

                while (true) {


                    canvas.setOnMouseDragged(e -> {

                        gc.setFill(color);
                        gc.fillOval(e.getX() - 2, e.getY() - 2, 5, 5);
                        System.out.println(e.getX() + e.getY());
                        try {
                            outputToClient.writeDouble(e.getX());
                            outputToClient.writeDouble(e.getY());

                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                    });

                    try {
                        p = inputfromclient.readInt();

                        while (p != -1) {


                            guess = inputfromclient.readUTF();
                            if (guess.length() != 0) {
                                Platform.runLater(() -> {
                                    tfforgues.appendText(guess + "\n");
                                });
                            }
                            p=inputfromclient.readInt();
                        }


                        if (p == -1) {
                            Platform.runLater(() -> {
                                i = -2;
                                Image imagetime = new Image("gotit.png", 800, 600, false, false);
                                ImageView imageViewtime = new ImageView(imagetime);
                                clockplayer.pause();
                                Media cheermedia = new Media(new File("src\\cheer.mp3").toURI().toString());
                                MediaPlayer cheerplayer = new MediaPlayer(cheermedia);
                                cheerplayer.play();
                                StackPane pane = new StackPane();
                                pane.getChildren().addAll(imageViewtime);
                                Scene scene = new Scene(pane, 650, 600);
                                stage.setScene(scene);
                                stage.show();
                            });
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    String getrandomeword() throws IOException {
        String[] fields = new String[50];
        int size=0;
        BufferedReader br=new BufferedReader(new FileReader("src/randomstuff"));
        try{


            String line;
            //String[] fields={};
            while ((line = br.readLine()) != null) {
                 fields[size]=line;
                 size++;
            }


        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        finally{
            br.close();
            Random rand=new Random();
            int meaw=rand.nextInt(size);
            return fields[meaw];


        }
    }
public class timer implements Runnable{

    @Override
    public void run() {

        try {
            while (i > 0) {
                i--;

                // labelfortimer.setText(String.valueOf(i));
                updateTimerLabel(i);

                //System.out.println(i);
                Thread.sleep(1000);

                if (i == 0) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                           Image imagetime=new Image("timesup.png");
                           ImageView imageViewtime=new ImageView(imagetime);
                           clockplayer.pause();
                           timesupPlayer.play();
                            StackPane pane = new StackPane();
                            pane.getChildren().addAll(imageViewtime);

                            Scene scene = new Scene(pane, 800, 600);
                            stage.setScene(scene);
                            stage.show();
                        }
                    });
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

    }
    public void updateTimerLabel(int i) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                labelfortimer.setText("00:"+String.valueOf(i));
            }
        });

    }
}

}
