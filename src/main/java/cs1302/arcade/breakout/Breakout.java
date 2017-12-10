package cs1302.arcade.breakout;

import cs1302.arcade.ArcadeApp;
import cs1302.arcade.Game;
import cs1302.arcade.GameChoiceScene;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *  Class representing a breakout game
 */
public class Breakout extends Game{
    private static final int HEIGHT = 600;
    private static final int WIDTH = 750;

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
        super("Breakout", app);

        lives = 3;
        //initialises the array of blocks to be broken
        createLevel(1, 4, 1);
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

        BorderWidths widths = new BorderWidths(3, 3, 0, 3); //Set the widths so bottom has no border
        Border border = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, widths)); //create border


        pane.setBorder(border); //Add border

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
            if(dir == BallDir.SE){//if ball is moving left
                dir = BallDir.NE;
            }else if(dir == BallDir.SW){//if ball is moving right
                dir = BallDir.NW;
            }
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
        int xPlace = 5;
        int yPlace = 100;
        for(Block[] row: blocks){
            for(Block block: row){
                pane.getChildren().add(block.render(xPlace, yPlace));
                xPlace += 53;
            }//for row
            xPlace = 5;
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
    int getWidth(){
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

        message.setFont(new Font(40)); //Make the font larger

        Stage s = new Stage(); //Create new stage do display win
        VBox container = new VBox(50); //Parent container
        Scene scene = new Scene(container); //Scene to show

        message.setTextAlignment(TextAlignment.CENTER); //Set alighment
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(20)); //Set padding

        container.getChildren().addAll(message, getInputArea());

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
            if(dir == BallDir.NE){//if ball is moving left
                dir = BallDir.SE;
            }else if(dir == BallDir.NW){//if ball is moving right
                dir = BallDir.SW;
            }

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
            if(dir == BallDir.NW){
                dir = BallDir.NE;
            }else if(dir == BallDir.SW){
                dir = BallDir.SE;
            }
        }
    }//hit left

    /**
     * changes direction of the ball if it hits right border
     */
    private void hitRight(){
        if(ball.getX() + ball.getRadius() >= WIDTH){
            if(dir == BallDir.NE){
                dir = BallDir.NW;
            }else if(dir == BallDir.SE){
                dir = BallDir.SW;
            }
        }
    }//hit right

    /**
     * gets rid of block if hit
     *
     * @return true if the block was hit
     */
    private boolean hitBlock(Block b){
        if(ball.getC().getBoundsInParent().intersects(b.getR().getBoundsInParent()) && b.getPresent()){
            if(dir == BallDir.SE || dir == BallDir.SW) { //Coming from the top
                if(b.getX() < ball.getX() && ball.getX() < b.getX() + b.getWidth()) { //above the block
                    if(dir == BallDir.SW)
                        dir = BallDir.NW;
                    else
                        dir = BallDir.NE;
                } else { //At the side of the block
                    if(dir == BallDir.SW)
                        dir = BallDir.SE;
                    else
                        dir = BallDir.SW;
                }
            } else { //Coming from the bottom (SW or SE)
                if(ball.getY() + ball.getRadius() < b.getY() + b.getHeight() && !(b.getX() < ball.getX() && ball.getX() < b.getX() + b.getWidth())) { //Side of the block
                    if(dir == BallDir.NE)
                        dir = BallDir.NW;
                    else
                        dir = BallDir.NE;
                } else { //Below the block
                    if (dir == BallDir.NE)
                        dir = BallDir.SE;
                    else
                        dir = BallDir.SW;
                }
            }

            pane.getChildren().remove(b.getR());
            b.destroy();

            addScore(b.getType().getScore());
            return true;
        }

        return false;
    }//hitBlock
}//Breakout