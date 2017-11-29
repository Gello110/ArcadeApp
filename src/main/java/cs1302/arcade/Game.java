package cs1302.arcade;

import javafx.scene.Scene;

public abstract class Game {

    private String name;
    private int score;
    private Scene currentScene;

    /**
     * Constructs the game object.
     *
     * @param name The name of the game.
     */
    public Game(String name) {
	this.name = name; // Set game name

	//TODO Construct scene

	initScene(currentScene); //Init scene
	initGame(); //begin game
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
     * Initializes the game. Calls all updates to the scene graph.
     */
    private void initGame() {
	//TODO Set up KeyFrame and Timeline for Scene
    }
    
    /**
     * Gets the current score of the game.
     *
     * @return The score of the game.
     */
    public int getScore() {
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
     * Gets the current scene of this game.
     *
     * @return The current Scene object for this game.
     */
    public Scene getScene() {
	return this.currentScene;
    }
}