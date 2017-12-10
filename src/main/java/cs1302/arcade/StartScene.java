package cs1302.arcade;

import javafx.animation.ScaleTransition;
import javafx.scene.Scene;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * The start screen displayed to the player when they first open the application
 */
class StartScene extends Scene {

    public StartScene() {
		super(new StackPane());

		StackPane parent = (StackPane) getRoot(); //The parent of the scene graph
		ImageView imageView = new ImageView(); //The image view containing the background
		Image background = new Image(getClass().getClassLoader().getResource("Arcade-Background.jpg").toString()); //Load in the background

		imageView.setImage(background); //Set the image to the ImageView
		imageView.setPreserveRatio(true); //Preserve image quality
		imageView.setSmooth(true);
		imageView.setCache(true); //Cache image to save space
		imageView.setFitWidth(600); //Set preferred fit width and height
		imageView.setFitHeight(400);

		Light.Distant light = new Light.Distant(); //Create Distant Object to simulate distant light
		light.setAzimuth(-135.0);

		Lighting lighting = new Lighting(); //Create Lighting Object
		lighting.setLight(light); //Set light
		lighting.setSurfaceScale(5.0);

		Text text = new Text(); //Create text
		text.setText("ARCADE"); //ARCADE Text
		text.setFill(Color.STEELBLUE); //Set color
		text.setFont(Font.font(null, FontWeight.BOLD, 1)); //Set font
		text.setEffect(lighting); //Set effect

		ScaleTransition transition = new ScaleTransition(Duration.millis(2000), text); //Create ScaleTransition with a duration of 2 seconds and the text object

		transition.setCycleCount(1); //Cycle indefinite amount of times
		transition.setByX(1.5f); //Amount to increase x and y by
		transition.setByY(1.5f);
		transition.setFromX(10); //From x and y
		transition.setFromY(10);
		transition.setToX(150); //To x and y
		transition.setToY(150);

		transition.play(); //Play animation

//		parent.setPrefSize(600, 400); //Set size
		parent.getChildren().addAll(imageView, text); //Set children
    }
}