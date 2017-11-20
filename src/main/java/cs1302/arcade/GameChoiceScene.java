package cs1302.arcade;

import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

import cs1302.arcade.minesweeper.Minesweeper;
import cs1302.arcade.breakout.Breakout;

/**
 * Class used to provide the user with a window to choose the game
 * they wish to play.
 */
public class GameChoiceScene extends Scene {

    private ArcadeApp mainClass;
    private ImageView parent;

    /**
     * Constructs a GameChoiceScene Object.
     *
     * @param mainClass The driver class of this program. Used to set the current scene of the stage.
     */
    public GameChoiceScene(ArcadeApp mainClass) {
       	super((parent = new ImageView())); //Instantiate parent and pass parent as an argument to super.

	this.mainStage = mainStage;
	
	init(); //initialize the scene
    }

    /**
     * Initialize the scene. Sets the background and adds two images allowing users to choose their game of choice.
     */
    private void init() {
        Image background = new Image(getClass().getResource("Arcade-Background.jpg").toString()); //Load in the background
	
	setImage(parent, background, 600, 400); //Set the background image to the parent ImageView with size 600 x 400

	Image minesweeper = new Image(getClass().getResource("Minesweeper.png").toString()); //Load in the Minesweeper game image
	ImageView msSelector = new ImageView(); //Create the ImageView object that will display the Minesweeper game choice

	setImage(msSelector, minesweeper, 100, 200); //Set the minesweeper image to the Minesweeper selector ImageView with size 100 x 200
	
	msSelector.setX(100); //Set image location
	msSelector.setY(100);
	msSelector.setOnMouseClicked(event -> mainClass.setScene(new Minesweeper())); //On click open MineSweeper

	Image breakout = new Image(getClass().getResource("Breakout.png").toString()); //Load in the Breakout game image
	ImageView bSelector = new ImageView(); //Create the ImageView object that will display the Breakout game choice

	setImage(bSelector, breakout, 100, 200); //Set the breakout image to the breakout selector ImageView with size 100 x 200

	bSelector.setX(300); //Set image location
	bSelector.setY(100);
	bSelector.setOnMouseClicked(event -> mainClass.setScene(new Breakout())}); //On click open breakout
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