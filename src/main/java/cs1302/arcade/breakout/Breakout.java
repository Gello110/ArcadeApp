package cs1302.arcade.breakout;

import cs1302.arcade.ArcadeApp;
import cs1302.arcade.Game;
import cs1302.arcade.GameChoiceScene;
import cs1302.arcade.minesweeper.Cell;
import cs1302.arcade.minesweeper.CellType;
import javafx.animation.KeyFrame;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    private int blocksDestroyed;
    private BallDir dir;//trajectory of the ball
    private Pane pane;
    private Text life;
    private Text currLevel;
    private Text score;
    private boolean ended;

    private boolean rightPressed;
    private boolean leftPressed;

    /**
     *  Constructs a breakout game
     */
    public Breakout(ArcadeApp app){
        super("Breakout");

        lives = 3;
        //initialises the array of blocks to be broken
        createLevel(1, 4, 1);

        this.app = app;
    }//Breakout

    @Override
    public Scene initScene(){
        VBox parent = new VBox();//parent node
        Scene scene = new Scene(parent);
        BorderPane stats = new BorderPane();//bar for score and level
        stats.setPadding(new Insets(10, 10, 10, 10));


        //creates the score and level to be shown to player
        Font font = Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toString(), 35);
        score = new Text(String.format("%04d", getScore()));
        score.setFont(font);
        currLevel = new Text(String.format("%02d", level));
        currLevel.setFont(font);
        life = new Text(String.format("%01d", lives));
        life.setFont(font);

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
        displayBlocks();

        //renders the paddle to the screen
        paddle = new Paddle(150, 20, this);
        pane.getChildren().add(paddle.render(275, HEIGHT - 50));

        //handles input from the player to move the paddle
        scene.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.RIGHT) {
                rightPressed = true;
                leftPressed = false;
            }else if(e.getCode() == KeyCode.LEFT){
                rightPressed = false;
                leftPressed = true;
            }
        });

        scene.setOnKeyReleased(e -> {
            if(e.getCode() == KeyCode.LEFT) {
                leftPressed = false;
            }else if(e.getCode() == KeyCode.RIGHT){
                rightPressed = false;
            }
        });

        newBall();

        parent.getChildren().addAll(bar, stats, pane);

        return scene;
    }//init Scene

    @Override
    public void updateScene(Scene scene){
        if(ended) return;

        if(rightPressed) {
            paddle.render(15,0);
        } else if(leftPressed) {
            paddle.render(-15, 0);
        }


        boolean hit = false;
        //checks of ball had hit any blocks
        for(Block[] row: blocks){
            for(Block block: row){
                if(hitBlock(block)) {
                    ball.addBlockBroken();

                    if(block.getType() == Block.BlockType.ORANGE) {
                        ball.hitOrange();
                    } else if(block.getType() == Block.BlockType.RED) {
                        ball.hitRed();
                    }

                    hit = true;
                    blocksDestroyed++;
                    break;
                }
            }
        }

        if(hit) {
            if(blocksDestroyed >= (blocks.length * blocks[0].length)) {
                if(level == 1) {
                    blocksDestroyed = 0;

                    pane.getChildren().remove(ball.getC());//removes ball from screen
                    createLevel(2, 8, 2);
                    displayBlocks();
                    newBall();
                } else endGame(true);

                currLevel.setText(String.format("%02d", level));
                return;
            }
        }else{
            //checks if ball has hit sides of window
            hitLeft();
            hitRight();
            hitUp();
            hitBottom();
        }

        //checks if hit paddle
        if(ball.getY() + ball.getRadius() > paddle.getY()
                && ball.getC().getBoundsInParent().intersects(paddle.getPaddle().getBoundsInParent())){
            goUp();
        }

        score.setText(String.format("%04d", getScore()));

        ball.render(dir);//changes position of the ball

    }//updateScene

    /**
     * Creates the level
     *
     * @param level The level being created
     * @param rows The number of rows being created
     * @param increment The number of rows that are a certain type
     */
    private void createLevel(int level, int rows, int increment) {
        this.level = level;
        blocks = new Block[rows][14];
        int blockType = 0;

        for(int i = 0; i < blocks.length; i++){
            for(int j = 0; j < blocks[0].length; j++){
                blocks[i][j] = new Block(50, 20, blockType);
            }//for j

            if((i + 1) % increment == 0) {
                blockType++;
            }
        }//for i
    }

    /**
     * Displays the blocks to the screen
     */
    private void displayBlocks() {
        int xPlace = 3;
        int yPlace = 100;
        for(Block[] row: blocks){
            for(Block block: row){
                pane.getChildren().add(block.render(xPlace, yPlace));
                xPlace += 53;
            }//for row
            xPlace = 3;
            yPlace += 23;
        }//for blocks
    }

    @Override
    public boolean isOver(){
        return ended;
    }//isOver

    /**
     * makes a new ball
     */
    private void newBall(){
        dir = BallDir.NE;
        ball = new Ball(7, 2);
        pane.getChildren().add(ball.render(375, HEIGHT - 100));

    }//newBall

    /**
     * reuturns the width of the pane
     *
     * @return the width of the gameplay pane
     */
    public int getWidth(){
        return WIDTH;
    }//getWidth

    /**
     * Ends the current game because the player either won or lost
     *
     * @param won Whether the player won the game or not
     */
    private void endGame(boolean won){
        ended = true;


        Text message = new Text(); //Message to display
        if(won){
            message.setText("Congrats\nYou Won\nScore: " + getScore()); //Show win message
        }else{
            message.setText("You Lost\nScore: " + getScore()); //Show lose messages
        }//if else

        Stage s = new Stage(); //Create new stage do display win
        VBox container = new VBox(50); //Parent container
        Scene scene = new Scene(container); //Scene to show

        container.setAlignment(Pos.CENTER);
        container.getChildren().add(message);

        s.setScene(scene); //set the scene
        s.initModality(Modality.APPLICATION_MODAL);
        s.sizeToScene(); //size it
        s.show(); //show
    }//gameEnded

    /**
     * changes direction of the ball hits uppoer border
     */
    private void hitUp(){
        if(ball.getY() - ball.getRadius() <= 0){
            goDown();

            paddle.shrink();
        }
    }//hitUp

    /**
     * changes round if ball hits bottom of border
     */
    private void hitBottom(){
        if(ball.getY() + ball.getRadius() >= HEIGHT){
            lives--;
            pane.getChildren().remove(ball.getC());//removes ball from screen
            life.setText(String.format("%01d", lives));

            if(lives == 0) {
                endGame(false);
                return;
            }
            newBall();
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
            goLeft();
            System.out.println("hit right wall");
        }
    }//hit right

    /**
     * gets rid of block if hit
     *
     * @return true if the block was hit
     */
    private boolean hitBlock(Block b){
        if(ball.getC().getBoundsInParent().intersects(b.getR().getBoundsInParent()) && b.getPresent()){
            if(ball.getX() + ball.getRadius() >= b.getX() && ball.getX() < b.getX()
                    && ball.getY() - ball.getRadius() > b.getHeight() + b.getY() && ball.getY() + ball.getRadius() < b.getY()){//if hit left of block
                System.out.println("go Left");
                goLeft();
            }else if(ball.getX() - ball.getRadius() <= b.getX() + b.getWidth() && ball.getX() > b.getX()
                    && ball.getY() - ball.getRadius() > b.getY() + b.getHeight() && ball.getY() + ball.getRadius() < b.getY()){//if hit right of block
                System.out.println("go right");
                goRight();
            }else if(ball.getY() + ball.getRadius() >= b.getY() && ball.getY() < b.getY()){//if hit top of block
                System.out.println("go Up");
                goUp();
            }else if(ball.getY() - ball.getRadius() <= b.getY() + b.getHeight() && ball.getY() > b.getY()){//if hit bottom of block
                System.out.println("go Down");
                goDown();
            }
            System.out.println("changed\n");
            pane.getChildren().remove(b.getR());
            b.destroy();

            addScore(b.getType().getScore());
            return true;
        }

        return false;
    }//hitBlock

    /**
     * turns the ball to go left
     */
    private void goLeft(){
        if(dir == BallDir.NE){
            dir = BallDir.NW;
        }else if(dir == BallDir.SE){
            System.out.println("go left down from right down");
            dir = BallDir.SW;
        }
    }//goLeft

    /**
     * turns the ball to go right
     */
    private void goRight(){
        if(dir == BallDir.NW){
            dir = BallDir.NE;
        }else if(dir == BallDir.SW){
            dir = BallDir.SE;
        }
    }//goRight

    /**
     * turns the ball to go down
     */
    private void goDown(){
        if(dir == BallDir.NE){//if ball is moving left
            dir = BallDir.SE;
        }else if(dir == BallDir.NW){//if ball is moving right
            dir = BallDir.SW;
        }
    }//go up

    /**
     * turns the ball to go up
     */
    private void goUp(){
        if(dir == BallDir.SE){//if ball is moving left
            dir = BallDir.NE;
        }else if(dir == BallDir.SW){//if ball is moving right
            dir = BallDir.NW;
        }
    }//go up

}//Breakout