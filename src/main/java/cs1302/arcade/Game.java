package cs1302.arcade;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

/**
 * Class that represents Games in the arcade, all games extend this class
 */
public abstract class Game {

    private String name;
    protected ArcadeApp app;
    private int score;

    /**
     * Constructs the game object.
     *
     * @param name The name of the game.
     */
    public Game(String name, ArcadeApp app) {
        this.name = name; // Set game name
        this.app = app; //Set the main class
    }

    /**
     * Where each game initializes their scene.
     *
     * @return The scene the game will use.
     */
    public abstract Scene initScene();

    /**
     * Updates the scene that is displayed to the player.
     *
     * @param scene The scene the player sees
     */
    public abstract void updateScene(Scene scene);

    /**
     * Checks whether the game has ended.
     *
     * @return If game is over
     */
    public abstract boolean isOver();

    /**
     * Gets the current score of the game.
     *
     * @return The score of the game.
     */
    protected int getScore() {
        return this.score;
    }

    /**
     * Sets the score for the current game.
     *
     * @param score The score to set.
     */
    protected void setScore(int score) {
        this.score = score;
    }

    /**
     * Adds to the current score of the game
     *
     * @param score The score to add
     */
    protected void addScore(int score) {
        this.score += score;
    }

    /**
     * Get the input area where the player puts in their intials
     *
     * @return The HBox object containing the input area
     */
    protected HBox getInputArea() {
        HBox hBox = new HBox(); //create HBox that holds input info

        Text text = new Text("Your name:"); //Text before input area
        TextField input = new TextField("Name"); //input area
        Button button = new Button("Save"); //button to save score
        EventHandler<ActionEvent> handler = e -> { //event handler called when player enters information
            String initials = input.getText();

            if(initials.isEmpty())
                return;

            app.getScoreHandler().addScore(name, initials, getScore());
        };

        input.setOnAction(handler); //set event handler
        button.setOnAction(handler);

        input.setMaxWidth(100); //set width of input area

        hBox.getChildren().addAll(text, input, button); //add nodes
        hBox.setSpacing(5); //set spacing
        hBox.setAlignment(Pos.CENTER); //set alignment

        return hBox;
    }
}