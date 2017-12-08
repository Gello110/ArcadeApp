package cs1302.arcade.breakout;

import cs1302.arcade.ArcadeApp;
import cs1302.arcade.GameChoiceScene;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import cs1302.arcade.Game;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.HashSet;

/**
 *  Class representing a breakout game
 */
public class Breakout extends Game{
    private static final int HEIGHT = 650;
    private static final int WIDTH = 750;

    private ArcadeApp app;
    private Block[][] blocks;
    private Paddle paddle;
    private Ball ball;
    private int level;
    private int lives;
    private int speed;
    private BallDir dir;//trajectory of the ball
    private Pane pane;
    private Text life;

    /**
     *  Constructs a breakout game
     */
    public Breakout(ArcadeApp app){

        super("Breakout");

        level = 1;
        lives = 3;
        speed = 2;
        //initialises the array of blocks to be broken
        blocks = new Block[3][7];
        for(int i = 0; i < blocks.length; i++){
            for(int j = 0; j < blocks[0].length; j++){
                blocks[i][j] = new Block(100, 30);
            }//for j
        }//for i


        this.app = app;
    }//Breakout

    @Override
    public Scene initScene(){
        VBox parent = new VBox();//parent node
        Scene scene = new Scene(parent);
        BorderPane stats = new BorderPane();//bar for score and level
        stats.setPadding(new Insets(10, 10, 10, 10));


        //creates the score and level to be shown to player
        Text score = new Text(String.format("%04d", getScore()));
        score.setFont(Font.loadFont( getClass().getClassLoader().getResource("digital.ttf").toString(), 35));
        Text currLevel = new Text(String.format("%02d", level));
        currLevel.setFont(Font.loadFont( getClass().getClassLoader().getResource("digital.ttf").toString(), 35));
        life = new Text(String.format("%01d", lives));
        life.setFont(Font.loadFont( getClass().getClassLoader().getResource("digital.ttf").toString(), 35));
        stats.setCenter(life);
        stats.setLeft(currLevel);
        stats.setRight(score);


        Menu game = new Menu("Game");
        //menu item  for new game
        MenuItem newGame = new MenuItem("_New Game");
        newGame.setOnAction(e -> app.setCurrentGame(new Breakout(app)));//resets the game

        //menuItem to return to switch games
        MenuItem sGame = new MenuItem("_Switch Game");//menuItem to return to game select screen
        sGame.setOnAction(e -> app.setCurrentScene(new GameChoiceScene(app)));//returns to old scene

        //adds all menutiems to game
        game.getItems().addAll(newGame, sGame);
        MenuBar bar = new MenuBar(game);


        //makes the pane for the game
        pane = new Pane();
        pane.setPrefHeight(HEIGHT);
        pane.setPrefWidth(WIDTH);
        pane.setMaxHeight(HEIGHT);
        pane.setMinHeight(HEIGHT);
        pane.setMaxWidth(WIDTH);
        pane.setMinWidth(WIDTH);

        //adds rectangles to pane
        int xPlace = 6;
        int yPlace = 100;
        for(Block[] row: blocks){
            for(Block block: row){
                pane.getChildren().add(block.render(xPlace, yPlace));
                xPlace += 106;
            }//for row
            xPlace = 6;
            yPlace += 36;
        }//for blocks

        //renders the paddle to the screen
        paddle = new Paddle(200, 30, this);
        pane.getChildren().add(paddle.render(275, HEIGHT - 50));

        //handles input from the player to move the paddle
        scene.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.LEFT){
                paddle.render(-30,0);
            }else if(e.getCode() == KeyCode.RIGHT){
                paddle.render(30, 0);
            }
        });

        newBall();

        parent.getChildren().addAll(bar, stats, pane);

        return scene;
    }//init Scene

    @Override
    public void updateScene(Scene scene){
        //checks if ball has hit sides of window
        hitLeft();
        hitRight();
        hitUp();
        hitBottom();

        //checks of ball had hit any blocks
        for(Block[] row: blocks){
            for(Block block: row){
                hitBlock(block);
            }
        }

        //checks if hit paddle
        if(ball.getY() + ball.getRadius() > paddle.getY() && ball.getY() < paddle.getY()
                && ball.getC().getBoundsInParent().intersects(paddle.getPaddle().getBoundsInParent())){
            goUp();
        }

        ball.render(dir, speed);//changes position of the ball

    }//updateScene

    @Override
    public boolean isOver(){
        return false;
    }//isOver

    /**
     * returns the score for the game
     *
     * @return  the current soore
     */
    public int getScore() {
        return 50;
    }//getScore

    /**
     * makes a new ball
     */
    private void newBall(){
        dir = BallDir.NE;
        ball = new Ball(10, this);
        pane.getChildren().add(ball.render(375, HEIGHT - 100));
    }//newBall

    /**
     *  returns the height of the pane
     *
     * @return  the height of the gameplay pane
     */
    public int getHeight(){
        return HEIGHT;
    }//getHeight

    /**
     * reuturns the width of the pane
     *
     * @return the width of the gameplay pane
     */
    public int getWidth(){
        return WIDTH;
    }//getWidth

    /**
     * changes direction of the ball hits uppoer border
     */
    private void hitUp(){
        if(ball.getY() - ball.getRadius() <= 0){
            goDown();
        }
    }//hitUp

    /**
     * changes round if ball hits bottom of border
     */
    private void hitBottom(){
        if(ball.getY() + ball.getRadius() >= HEIGHT){
            lives--;
            pane.getChildren().remove(ball.getC());//removes rectangle from screen
            newBall();
            life.setText(String.format("%01d", lives));
        }
    }//hit bottom

    /**
     * changes direction of the ball if it hits left border
     */
    private void hitLeft(){
        if(ball.getX() - ball.getRadius() <= 0){
            goRight();
        }
    }//hit left

    /**
     * changes direction of the ball if it hits right border
     */
    private void hitRight(){
        if(ball.getX() + ball.getRadius() >= WIDTH){
            goLeft();;
        }
    }//hit right

    /**
     * gets rid of block if hit
     */
    private void hitBlock(Block b){
        if(ball.getC().getBoundsInParent().intersects(b.getR().getBoundsInParent()) && b.getPresent()){
            if(ball.getX() + ball.getRadius() >= b.getX() && ball.getX() < b.getX()){//if hit left of block
                goLeft();
            }
            if(ball.getX() - ball.getRadius() <= b.getX() + b.getWidth() && ball.getX() > b.getX()){//if hit right of block
                goRight();
            }
            if(ball.getY() + ball.getRadius() >= b.getY() && ball.getY() < b.getY()){//if hit top of block
                goUp();
            }
            if(ball.getY() - ball.getRadius() <= b.getY() + b.getHeight() && ball.getY() > b.getY()){//if hit bottom of block
                goDown();
            }
            pane.getChildren().remove(b.getR());
            b.destroy();
        }
    }//hitBlock

    /**
     * turns the ball to go left
     */
    private void goLeft(){
        if(dir == BallDir.NE){
            dir = BallDir.NW;
        }else if(dir == BallDir.SE){
            dir = BallDir.SW;
        }
    }//goLeft

    /**
     * turns the ball to go right
     */
    public void goRight(){
        if(dir == BallDir.NW){
            dir = BallDir.NE;
        }else if(dir == BallDir.SW){
            dir = BallDir.SE;
        }
    }//goRight

    /**
     * turns the ball to go down
     */
    public void goDown(){
        if(dir == BallDir.NE){//if ball is moving left
            dir = BallDir.SE;
        }else if(dir == BallDir.NW){//if ball is moving right
            dir = BallDir.SW;
        }
    }//go up

    /**
     * turns the ball to go up
     */
    public void goUp(){
        if(dir == BallDir.SE){//if ball is moving left
            dir = BallDir.NE;
        }else if(dir == BallDir.SW){//if ball is moving right
            dir = BallDir.NW;
        }
    }//go up

}//Breakout