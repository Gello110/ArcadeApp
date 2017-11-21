package cs1302.arcade;

import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.Light;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

/**
 * The start screen displayed to the player when they first open the application
 */
public class StartScene extends Scene {

    private ImageView parent;

    public StartScene() {
	super((parent = new ImageView()));
	Image background = new Image(getClass().getResource("Arcade-Background.jpg").toString()); //Load in the background

	parent.setImage(image); //Set the image to the ImageView
	parent.setPreserveRatio(true); //Preserve image quality
	parent.setSmooth(true);
	parent.setCache(true); //Cache image to save space
	parent.setFitWidth(600); //Set preferred fit width and height
	parent.setFitHeight(400);

	Light.Distant light = new Light.Distant(); //Create Distant Object to simulate distant light
	light.setAzimuth(-135.0);

	Lighting lighting = new Lighting(); //Create Lighting Object
	lighting.setLight(light); //Set light
	lighting.setSurfaceScale(5.0);

	Text text = new Text(); //Create text
	text.setText("ARCADE"); //ARCADE Text
	text.setFill(Color.STEELBLUE); //Set color
	text.setFont(Font.font(null, FontWeight.BOLD, 60)); //Set font
	text.setEffect(lighting); //Set effect

	ScaleTransition transition = new ScaleTransition(Duration.millis(2000), text); //Create ScaleTransition with a duration of 2 seconds and the text object
	
	transition.setCycleCount(Animation.INDEFINITE); //Cycle indefinite amount of times
	transition.setByX(1.5f); //Amount to increase x and y by
	transition.setByY(1.5f);	
	transition.setFromX(10); //From x and y
	transition.setFromY(10);
	transition.setToX(200); //To x and y
	transition.setToY(200);

	transition.play(); //Play animation

	parent.getChildren().add(text);
    }
}