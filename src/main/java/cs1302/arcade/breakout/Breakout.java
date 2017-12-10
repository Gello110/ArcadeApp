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
        currLevel = new Text(String.format("%02d", level));
        life = new Text(String.format("%01d", lives));

        score.setFont(font); //sets the fonts
        currLevel.setFont(font);
        life.setFont(font);

        stats.setCenter(life); //sets location of stats
        stats.setLeft(currLevel);
        stats.setRight(score);


        Menu game = new Menu("Game");
        //menu item  for new game
        MenuItem newGame = new MenuItem("_New Game");
        newGame.setOnAction(e -> app.setCurrentGame(new Breakout(app)));//resets the game

        //menuItem to return to switch games
        MenuItem sGame = new MenuItem("_Switch Game");//menuItem to return to game select screen
        sGame.setOnAction(e -> {
            app.setCurrentScene(new GameChoiceScene(app));
            ended = true;
        });//returns to old scene

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
        paddle = new Paddle(this);
        pane.getChildren().add(paddle.render(275, HEIGHT - 50));

        //handles input from the player to move the paddle
        scene.setOnKeyPressed(e -> { //handles key pressed down
            if(e.getCode() == KeyCode.RIGHT) { //right key pressed
                rightPressed = true; //notify right pressed and ignore left
                leftPressed = false;
            }else if(e.getCode() == KeyCode.LEFT){ //left key pressed
                rightPressed = false; //notify left and ignore right
                leftPressed = true;
            }
        });

        scene.setOnKeyReleased(e -> { //handles key released
            if(e.getCode() == KeyCode.LEFT) { //left key unpressed
                leftPressed = false;
            }else if(e.getCode() == KeyCode.RIGHT){ //right key unpressed
                rightPressed = false;
            }
        });

        newBall(); //spawn first ball

        parent.getChildren().addAll(bar, stats, pane); //add everything to pane

        return scene;
    }//init Scene

    @Override
    public void updateScene(){
        if(ended) return; //Game has ended, stop updating

        if(rightPressed) { //right key is pressed, move right
            paddle.render(15,0);
        } else if(leftPressed) { //left key is pressed, move left
            paddle.render(-15, 0);
        }

        boolean hit = false;
        //checks of ball had hit any blocks
        for(Block[] row: blocks){ //iterate through blocks
            for(Block block: row){
                if(hitBlock(block)) { //check if block is hit by the ball
                    ball.addBlockBroken(); //add to number of blocks this ball has broken

                    if(block.getType() == Block.BlockType.ORANGE) {
                        ball.hitOrange(); //notify ball has hit orange, for possible speed increase
                    } else if(block.getType() == Block.BlockType.RED) {
                        ball.hitRed(); //notify ball has hit red, for possible speed increase
                    }

                    hit = true; //block has been hit
                    blocksDestroyed++; //add to total blocks destroyed
                    break;
                }
            }
        }

        if(hit) { //if a block has been hit
            if(blocksDestroyed >= (blocks.length * blocks[0].length)) { //if all blocks have been hit
                if(level == 1) { //if level is level 1, create level 2
                    blocksDestroyed = 0; //reset total blocks destroyed

                    pane.getChildren().remove(ball.getC());//removes ball from screen
                    createLevel(2, 8, 2); //creates level 2
                    displayBlocks(); //displays blocks
                    newBall(); //creates new ball
                } else endGame(true); //end game, player has won

                currLevel.setText(String.format("%02d", level)); //update level text
                return;
            }
        }else{
            //checks if ball has hit sides of window
            hitSides();
            hitUp();
            hitBottom();
        }

        //checks if hit paddle
        if(ball.getY() + ball.getRadius() > paddle.getY()
                && ball.getC().getBoundsInParent().intersects(paddle.getPaddle().getBoundsInParent()) && ball.getDirection().isS()){
            ball.changeDirection(1, -1); //bounce back up
        }

        score.setText(String.format("%04d", getScore())); //update score text

        ball.render();//changes position of the ball

    }//updateScene

    /**
     * Creates the level
     *
     * @param level The level being created
     * @param rows The number of rows being created
     * @param increment The number of rows that are a certain type
     */
    private void createLevel(int level, int rows, int increment) {
        this.level = level; //set level
        blocks = new Block[rows][14]; //create block array representing blocks with specified number of rows and 14 columns
        int blockType = 0; //Keeps track of block type for different block types

        for(int i = 0; i < blocks.length; i++){ //Iterate through blocks
            for(int j = 0; j < blocks[0].length; j++){
                blocks[i][j] = new Block(blockType); //set the block with height 20 and width 50
            }//for j

            if((i + 1) % increment == 0) { //add blocktype if specified increment is reached
                blockType++;
            }
        }//for i
    }

    /**
     * Displays the blocks to the screen
     */
    private void displayBlocks() {
        int xPlace = 5; //X coord on screen blocks start at
        int yPlace = 100; //Y coord on screen blocks start at
        for(Block[] row: blocks){ //iterate through the blocks
            for(Block block: row){
                pane.getChildren().add(block.render(xPlace, yPlace)); //add the block at its position
                xPlace += 53; //add to x coord position (width plus spacing)
            }//for row
            xPlace = 5; //reset x coord start
            yPlace += 23; //add to y coord position (height plus spacing)
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
        ball = new Ball(); //creates ball with radius of 7 and speed of 2
        pane.getChildren().add(ball.render(HEIGHT - 100)); //add ball to scene

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

        container.getChildren().addAll(message, getInputArea(s));

        s.setScene(scene); //set the scene
        s.initModality(Modality.APPLICATION_MODAL);
        s.sizeToScene(); //size it
        s.show(); //show
    }//gameEnded

    /**
     * changes direction of the ball hits uppoer border
     */
    private void hitUp(){
        if(ball.getY() - ball.getRadius() <= 0 && ball.getDirection().isN()){
            ball.changeDirection(1, -1);

            paddle.shrink(); //shrink paddle
        }
    }//hitUp

    /**
     * changes round if ball hits bottom of border
     */
    private void hitBottom(){
        if(ball.getY() + ball.getRadius() >= HEIGHT){ //ball is under bottom
            lives--; //subtract lives
            pane.getChildren().remove(ball.getC());//removes ball from screen
            life.setText(String.format("%01d", lives)); //update text displaying lives

            if(lives == 0) { //If no lives left
                endGame(false); //end the game
                return;
            }

            newBall(); //Spawn a new ball
        }
    }//hit bottom

    /**
     * changes direction of the ball if it hits either the left or right border
     */
    private void hitSides(){
        if((ball.getX() - ball.getRadius() <= 0 && ball.getDirection().isW()) || (ball.getX() + ball.getRadius() >= WIDTH && ball.getDirection().isE())){ //check sides
            ball.changeDirection(-1, 1); //change x direction
        }
    }//hit sides

    /**
     * gets rid of block if hit
     *
     * @return true if the block was hit
     */
    private boolean hitBlock(Block b){
        if(ball.getC().getBoundsInParent().intersects(b.getR().getBoundsInParent()) && b.getPresent()){ //Block and ball intersected
            int height;
            int width;

            if(ball.getY() < b.getY()) //ball is above the block
                height = Math.abs(ball.getY() + ball.getRadius() - b.getY()); //get height from above (doesn't include block height)
            else
                height = Math.abs(ball.getY() - ball.getRadius() - (b.getY() + b.getHeight())); //get height from above (does include block height)

            if(ball.getX() < b.getX()) //ball is to the left
                width = Math.abs(ball.getX() + ball.getRadius() - b.getX()); //get width from left (doesn't include block width)
            else
                width = Math.abs(ball.getX() - ball.getRadius() - (b.getX() + b.getWidth())); //get width from right (does include block width)

            boolean dirChanged = false;

            if(width > 3 && width >= height) { //substantial hit on top or bottom (width of intersection is greater than height)
                ball.changeDirection(1, -1); //change y dir
                dirChanged = true; //signal direction change
            }

            if(height > 3 && height >= width){ //substantial hit on left or right (height of intersection is greater than width)
                ball.changeDirection(-1, 1); //change x dir
                dirChanged = true; //signal direction change
            }


            if(dirChanged) { //Direction changed
                pane.getChildren().remove(b.getR()); //remove block
                b.destroy();

                addScore(b.getType().getScore()); //add points to score
                return true; //signal block broken
            }
        }
        return false; //signal block not broken
    }//hitBlock
}//Breakout