import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class canvaclient extends Application {
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;
    Canvas canvas=new Canvas(500,500);
    int connect=0;
    GraphicsContext gc=canvas.getGraphicsContext2D();
    TextField tf=new TextField();

    Button submit=new Button("Submit");
    String word="";
    int starttimer=0;
    int i=30;
    Label timerLabel=new Label();
    Runnable timer=new timer();
    Media clockmedia=new Media(new File("src\\Fast-clock-ticking-sound-effect.mp3").toURI().toString());
    MediaPlayer clockplayer=new MediaPlayer(clockmedia);
    Media timesupmedia=new Media(new File("src\\Clock-ringing.mp3").toURI().toString());
    MediaPlayer timesupPlayer=new MediaPlayer(timesupmedia);
    Media correctanswermedia=new Media(new File("src\\Correct-answer.mp3").toURI().toString());
    MediaPlayer correctanserplayer=new MediaPlayer(correctanswermedia);
    Media wronganswermedia=new Media(new File("src\\fail-buzzer-04.mp3").toURI().toString());
    MediaPlayer wronganserplayer=new MediaPlayer(wronganswermedia);
    Thread thread1=new Thread(timer);
    Stage stage;
    @Override
    public void start(Stage stage) throws Exception {
        this.stage=stage;
        //clockplayer.play();
        timerLabel.setFont(new Font("Verdana",30));
        tf.setMaxWidth(200);
        tf.setMaxHeight(100);
        tf.setBorder(new Border(new BorderStroke(Color.PURPLE, BorderStrokeStyle.SOLID, null, new BorderWidths(5))));
        GridPane buttontf=new GridPane();
        tf.setFont(new Font("Verdana",20));
        submit.setFont(new Font("Verdana",15));
       buttontf.setVgap(5);
       buttontf.setHgap(5);
        buttontf.add(tf,0,0);
        buttontf.add(submit,1,0);
        submit.setPrefSize(100,100);
        submit.setBorder(new Border(new BorderStroke(Color.CADETBLUE, BorderStrokeStyle.SOLID, null, new BorderWidths(3))));
        StackPane stfortimer=new StackPane();
        Image clockimage=new Image("clock.jpg",100,100,false,true);
        ImageView imageviewclock=new ImageView(clockimage);
        Image boardimage=new Image("board.png",500,500,false,true);
        BackgroundImage bgforboard=new BackgroundImage(boardimage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        StackPane stforboard=new StackPane();
        stforboard.setBackground(new Background(bgforboard));
        stforboard.getChildren().add(canvas);
       // stfortimer.setBackground(new Background(bgforclock));

        timerLabel.setPadding(new Insets(35,25,0,13));
        stfortimer.getChildren().addAll(imageviewclock,timerLabel);
        stfortimer.setAlignment(Pos.TOP_RIGHT);
        stfortimer.setPadding(new Insets(10,10,10,10));
       // Button btconnect=new Button("connect");
      //  btconnect.setOnAction(e->{
        try {
            //Create a socket to connect to the server
            Socket socket = new Socket("localhost" , 8000);
            connect=-1;

            //Create an input stream to receive data to the server
            fromServer = new DataInputStream(socket.getInputStream());

            //Create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException ex){
            ex.printStackTrace();
        }

            try{
                 word=fromServer.readUTF();
            }
            catch(Exception ex) {
                ex.printStackTrace();}

            new Thread(()-> {
                try {
                    while(connect==-1){
                    double x = fromServer.readDouble();
                    double y = fromServer.readDouble();
                    if(x>0) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                thread1.start();
                            }
                        });
                    }
                    gc.setFill(Color.WHITE);
                    gc.fillOval(x, y, 5, 5);

                }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }).start();



          submit.setOnAction(e->{
              wronganserplayer.stop();
              String answer=tf.getText();
              if(tf.getText().equals(word)){
                  clockplayer.pause();
                  wronganserplayer.pause();
                  correctanserplayer.play();
                  //correctanserplayer.pause();
                  i=-1;

                      try {
                          toServer.writeInt(i);
                          toServer.flush();

                      }
                      catch(Exception ex){
                      ex.printStackTrace();}


              }
              else{
                  try {
                      toServer.writeInt(2);
                      toServer.writeUTF(answer);

                      toServer.flush();
                  } catch (IOException ex) {
                      ex.printStackTrace();
                  }
                  wronganserplayer.play();
                  //
              };
          });







        GridPane pane=new GridPane();
        pane.add(stforboard,0,0);
        pane.add(buttontf,0,1);

        pane.add(stfortimer,1,0);
        Scene scene=new Scene(pane,610,550);
        this.stage.setScene(scene);
        this.stage.show();


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
                    if(i==0){
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Image imagetime=new Image("timesup.png",700,550,false,false);
                                ImageView imageViewtime=new ImageView(imagetime);
                                clockplayer.pause();
                                timesupPlayer.play();
                                StackPane pane = new StackPane();
                                pane.getChildren().addAll(imageViewtime);

                                Scene scene = new Scene(pane, 610, 550);
                                stage.setScene(scene);
                                stage.show();
                            }
                        });
                    }
                    if(i==-1){
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Image imagetime=new Image("right answer.png",700,550,false,false);
                                ImageView imageViewtime=new ImageView(imagetime);

                                correctanserplayer.pause();
                                Media cheermedia=new Media(new File("src\\cheer.mp3").toURI().toString());
                                MediaPlayer cheerplayer=new MediaPlayer(cheermedia);
                                cheerplayer.play();
                                StackPane pane = new StackPane();
                                pane.getChildren().addAll(imageViewtime);

                                Scene scene = new Scene(pane, 610, 550);
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
                    timerLabel.setText(String.valueOf(i));
                }
            });

        }
    }
}
