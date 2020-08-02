package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    MediaPlayer player;
    @FXML
    private Slider timeSlider;
    @FXML
    private Button playBtn;
    @FXML
    private Button nextBtn;
    @FXML
    private Button preBtn;
    @FXML
    private MediaView mediaView;

    @FXML
    void openSongMenu(ActionEvent event) {
        try {
            // System.out.println("Open Song clicked");
            FileChooser chooser = new FileChooser();
            File file = chooser.showOpenDialog(null);
            Media m = new Media(file.toURI().toURL().toString());
            if (player != null)
                player.dispose();

            player = new MediaPlayer(m);
            mediaView.setMediaPlayer(player);

            player.setOnReady(() -> {
                //when player gets ready
                timeSlider.setMin(0);
                timeSlider.setMax(player.getMedia().getDuration().toMinutes());
                timeSlider.setValue(0);
                try {
                    playBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/play.png"))));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            player.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                    Duration d = player.getCurrentTime();
                    timeSlider.setValue(d.toMinutes());
                }
            });

            timeSlider.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    if (timeSlider.isPressed()) {
                        double val = timeSlider.getValue();
                        player.seek(new Duration(val * 60 * 1000));
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void playit(ActionEvent event) {
        try {
            MediaPlayer.Status status = player.getStatus();
            if (status == MediaPlayer.Status.PLAYING) {

                player.pause();
                playBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/play.png"))));

            } else {
                player.play();
                playBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/Pause.png"))));

            }
            player.setVolume(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            playBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/play.png"))));
            preBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/previous.png"))));
            nextBtn.setGraphic(new ImageView(new Image(new FileInputStream("src/icons/next.png"))));


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void prevBtnClick(ActionEvent event) {
    Double d=player.getCurrentTime().toSeconds();
   d=d-10;
   player.seek(new Duration(d*1000));

    }
    @FXML
    void nextBtnClick(ActionEvent event) {
        Double d=player.getCurrentTime().toSeconds();
        d=d+10;
        player.seek(new Duration(d*1000));

    }
}

