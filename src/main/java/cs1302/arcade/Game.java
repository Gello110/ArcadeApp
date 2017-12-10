package cs1302.arcade;

import javafx.scene.Scene;

/**
 * Class that represents Games in the arcade, all games extend this class
 */
public abstract class Game {

    private final String name;
    private int score;

    /**
     * Constructs the game object.
     *
     * @param name The name of the game.
     */
    protected Game(String name) {
        this.name = name; // Set game name
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
     */
    public abstract void updateScene();

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
     * Adds to the current score of the game
     *
     * @param score The score to add
     */
    protected void addScore(int score) {
        this.score += score;
    }
}