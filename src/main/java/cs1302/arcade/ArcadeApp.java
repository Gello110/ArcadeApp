
package cs1302.arcade;

import java.time.LocalTime;
import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ArcadeApp extends Application {

    Stage stage;
    boolean isShown;

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        stage.setTitle("Arcade");

        setCurrentScene(new StartScene()); //Show start scene

        Timeline timeline = new Timeline(); //Construct timeline
        timeline.setCycleCount(1);
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(3000), event -> setCurrentScene(new GameChoiceScene(this)))); //Display game choice scene three seconds later
        timeline.play();
    } // start

    /**
     * Sets the current scene of the stage being shown.
     *
     * @param scene The scene to set
     */
    public void setCurrentScene(Scene scene) {
        stage.setScene(scene);
        stage.sizeToScene();

        if(!isShown) {
            stage.show();
            isShown = true;
        }
    }

    /**
     * Sets the current game being played
     *
     * @param game The new game to set
     */
    public void setCurrentGame(Game game) {
        Scene scene = game.initScene();

        setCurrentScene(scene);

        Timeline timeline = new Timeline();
        EventHandler<ActionEvent> handler = event -> {
            if(game.isOver()) {
                timeline.stop();
                return;
            }

            game.updateScene(scene);
        };
        KeyFrame keyFrame = new KeyFrame(Duration.millis(50), handler);

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }


    public static void main(String[] args) {
        try {
            Application.launch(args);
        } catch (UnsupportedOperationException e) {
            System.out.println(e);
            System.err.println("If this is a DISPLAY problem, then your X server connection");
            System.err.println("has likely timed out. This can generally be fixed by logging");
            System.err.println("out and logging back in.");
            System.exit(1);
        } // try
    } // main

} // ArcadeApp

