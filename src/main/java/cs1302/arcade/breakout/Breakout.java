package cs1302.arcade.breakout;

import javafx.scene.Parent;
import javafx.scene.Scene;
import cs1302.arcade.Game;
import javafx.scene.layout.Region;

/**
 *  Class representing a breakout game
 */
public class Breakout extends Game{
    
    

    /**
     *  Constructs a breakout game
     */
    public Breakout(){
	super("Breakout");

    }//Breakout

    @Override
    public Scene initScene(){
        Region region = new Region();
	    Scene scene = new Scene(region);

        return scene;
    }//init Scene

    @Override
    public void updateScene(Scene scene){


    }//updateScene

    @Override
    public boolean isOver(){
        return false;
    }//isOver

}//Breakout