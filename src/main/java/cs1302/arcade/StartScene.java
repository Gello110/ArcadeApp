package cs1302.arcade;

import javafx.scene.Scene;

/**
 * The start screen displayed to the player when they first open the application
 */
public class StartScene extends Scene {

    private Stage mainStage;

    /**
     * Constructs a StartScene Object.
     *
     * @param mainStage the Stage Object where this Scene will be displayed.
     */
    public StartScene(Stage mainStage) {
	this.mainStage = mainStage;
    }
}