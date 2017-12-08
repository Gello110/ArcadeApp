package cs1302.arcade;

import cs1302.arcade.breakout.Breakout;
import cs1302.arcade.minesweeper.Minesweeper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Class used to provide the user with a window to choose the game
 * they wish to play.
 */
public class GameChoiceScene extends Scene {

    private ArcadeApp mainClass;
    private StackPane parent;
    /**
     * Constructs a GameChoiceScene Object.
     *
     * @param mainClass The driver class of this program. Used to set the current scene of the stage.
     */
    public GameChoiceScene(ArcadeApp mainClass) {
       	super(new StackPane()); //Instantiate parent and pass parent as an argument to super.

		this.parent = (StackPane) getRoot(); //Get the parent of the scene graph
		this.mainClass = mainClass;

		init(); //initialize the scene
    }

    /**
     * Initialize the scene. Sets the background and adds two images allowing users to choose their game of choice.
     */
    private void init() {
		ImageView backgroundView = new ImageView(); //The background
    	Image background = new Image(getClass().getClassLoader().getResource("Arcade-Background.jpg").toString()); //Load in the background
	
		setImage(backgroundView, background, 600, 400); //Set the background image to the parent ImageView with size 600 x 400

		HBox pane = new HBox(); //To hold the selectors
		Image minesweeper = new Image(getClass().getClassLoader().getResource("Minesweeper.png").toString()); //Load in the Minesweeper game image
		ImageView msSelector = new ImageView(); //Create the ImageView object that will display the Minesweeper game choice

		setImage(msSelector, minesweeper, 200, 400); //Set the minesweeper image to the Minesweeper selector ImageView with size 100 x 200

		msSelector.setOnMouseClicked(event -> mainClass.setCurrentGame(new Minesweeper(mainClass))); //On click open MineSweeper

		Image breakout = new Image(getClass().getClassLoader().getResource("Breakout.png").toString()); //Load in the Breakout game image
		ImageView bSelector = new ImageView(); //Create the ImageView object that will display the Breakout game choice

		setImage(bSelector, breakout, 200, 600); //Set the breakout image to the breakout selector ImageView with size 100 x 200

		bSelector.setOnMouseClicked(event -> mainClass.setCurrentGame(new Breakout(mainClass))); //On click open breakout

		pane.setPadding(new Insets(20, 20, 20, 20));
		pane.setSpacing(40);
		pane.setAlignment(Pos.CENTER);
		parent.setAlignment(Pos.CENTER);

		pane.getChildren().addAll(msSelector, bSelector);
		parent.getChildren().addAll(backgroundView, pane); //Add children to parent
    }

    /**
     * Sets the Image of an ImageView Object and ensures the ratios are preserved.
     *
     * @param view The ImageView object the Image is being added to
     * @param image The Image Object being added to the ImageView
     * @param width The width of the image when displayed
     * @param height The height of the image when displayed
     */
    private void setImage(ImageView view, Image image, int width, int height) {
		view.setImage(image); //Set the image to the ImageView
		view.setPreserveRatio(true); //Preserve image quality
		view.setSmooth(true);
		view.setCache(true); //Cache image to save space
		view.setFitWidth(width); //Set preferred fit width and height
		view.setFitHeight(height);
    }
}