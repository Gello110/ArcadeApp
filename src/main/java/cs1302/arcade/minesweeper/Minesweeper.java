package cs1302.arcade.minesweeper;

import java.util.Set;
import java.util.HashSet;

import cs1302.arcade.ArcadeApp;
import cs1302.arcade.GameChoiceScene;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import cs1302.arcade.Game;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 *  Class representing a minesweeper game
 */
public class Minesweeper extends Game{
    
    private static final int GAME_COLUMNS = 16;
    private static final int GAME_ROWS = 16;

    private Board gameBoard; //Board containing all the game logic and cells
    private Text minesLeft; //Text displaying the mines left
    private Text time; //Text displaying the time the player has been playing
    private GridPane gPane;//GridPane containing th cells of the game
    private int timer;//Seconds since game has started
    private Timeline timing;//timer for the game

    private Set<Cell> cellsChanged; //Set containing the cells changed since the last update of the screen

    /**
     *  Constructs a minesweeper game
     */
    public Minesweeper(ArcadeApp app){
	    super("Minesweeper", app);

	    gameBoard = new Board(GAME_ROWS, GAME_COLUMNS, this); //Initiate game board
        cellsChanged = new HashSet<>();
    }//Minesweeper

    @Override
    public Scene initScene(){
        VBox parent = new VBox(); //Parent Node
        MenuBar bar = new MenuBar(app.getHighScores()); //Menu bar to access high scores
        Scene scene = new Scene(parent); //Scene that will be used
        BorderPane bPane = new BorderPane(); //Will contain the scores

        minesLeft = new Text(String.format("%03d", gameBoard.getMinesLeft())); //The mines left to be found
        minesLeft.setFont(Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toString(), 30));
        Button newGame = new Button("New Game"); //Button player clicks to start a new game.
        time = new Text(String.format("%03d", timer)); //The time spent playing the game
        time.setFont(Font.loadFont( getClass().getClassLoader().getResource("digital.ttf").toString(), 30));
        Button change = new Button("Switch Game");
        change.setOnAction(e -> app.setCurrentScene(new GameChoiceScene(app)));
        HBox buttons = new HBox(20);
        buttons.getChildren().addAll(newGame, change);
        buttons.setAlignment(Pos.CENTER);

        //makes keyframe
        timing = new Timeline();
        KeyFrame keyframe = new KeyFrame(Duration.seconds(1), e -> {
            timer++;
            Platform.runLater(() -> time.setText(String.format("%03d", timer)));  //Display time to player
        });
        timing.setCycleCount(999); //Only 999 seconds displayed
        timing.getKeyFrames().add(keyframe);
        timing.play();

        newGame.setOnAction(action -> app.setCurrentGame(new Minesweeper(app))); //Reset board for new game

        bPane.setRight(minesLeft); //Set position
        bPane.setCenter(buttons);
        bPane.setLeft(time);

        BorderPane.setAlignment(minesLeft, Pos.CENTER_LEFT); //Set alignment
        BorderPane.setAlignment(buttons, Pos.CENTER);
        BorderPane.setAlignment(time, Pos.CENTER_RIGHT);

        gPane = new GridPane(); //Will contain the grid of tiles the player interacts with

        Cell[][] board = gameBoard.getBoard(); //Get game board

        for(int row = 0; row < board.length; row++) { //Iterate through board
            for (int col = 0; col < board[row].length; col++) {
                Cell cell = board[row][col]; //Get cell at row and column
                ImageView view = new ImageView(cell.getState().getImage()); //Make image view

                view.setFitWidth(30); //Set image width and height
                view.setFitHeight(30);
                view.setOnMouseClicked(action -> processAction(cell, action)); //Process mouse click

                gPane.add(view, col, row); //Adds it to the pane
            }
        }

        parent.getChildren().addAll(bar, bPane, gPane); //Add children to stuff

        return scene;
    }//init Scene

    /**
     * End the current game
     *
     * @param won Whether the player won or not
     */
    void endGame(boolean won){
        timing.stop();
        Text message = new Text(); //Message to display

        if(won){
            message.setText("Congrats\nYou Won\nScore: " + getScore()); //Show win message
        }else{
            message.setText("You Lost\nScore: " + getScore()); //Show lose message

            //reveals all the mines in the game
            Cell[][] board = gameBoard.getBoard(); //get game board
            for (Cell[] aBoard : board) { //Loop through board
                for (int j = 0; j < board[0].length; j++) {
                    Cell c = aBoard[j]; //The cell
                    if(c.getState().equals(CellType.HIT)) {
                        continue;
                    }//if hit
                    boolean isMine = aBoard[j].getType() == CellType.MINE; //Cell is mine
                    boolean isFlagged = aBoard[j].getState() == CellType.FLAGGED; //Cell is flagged

                    if (isMine && !isFlagged) { //Mine and not flagged, show to player
                        c.setState(CellType.MINE);
                        cellsChanged.add(c);
                    }//if cell is mine and not flagged

                    if (isFlagged && !isMine) { //flagged and not a mine, tell player they got it wrong
                        c.setState(CellType.WRONG);
                        cellsChanged.add(c);
                    }//if cell is flagged and not a mine
                }//for j
            }//for i in gameboard
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
        s.showAndWait(); //show
    }//endGame

    @Override
    public void updateScene(Scene scene){
        for(Cell c: cellsChanged){//replaces all the changed cells
            ImageView newImage = new ImageView(c.getState().getImage());
            newImage.setFitWidth(30);
            newImage.setFitHeight(30);
            newImage.setSmooth(true);
            newImage.setPreserveRatio(true);

            newImage.setOnMouseClicked(action -> processAction(c, action)); //Process mouse click

            gPane.add(newImage, c.getColumn(), c.getRow());
        }//for all cells in cellsChanged

        cellsChanged.clear();//clears the cells in the set
    }//updateScene

    @Override
    public boolean isOver(){
	    return gameBoard.getOver();
    }//isOver

    @Override
    public int getScore() {
        return timer;
    }

    /**
     * Processes user clicks
     *
     * @param cell The cell clicked
     * @param action The action event
     */
    private void processAction(Cell cell, MouseEvent action) {
        if(action.getButton() == null || action.getButton() == MouseButton.NONE || action.getButton() == MouseButton.MIDDLE)
            return;

        if(action.getButton() == MouseButton.PRIMARY) { //Left Click
            if(cell.getState() == CellType.FLAGGED)
                return;

            gameBoard.reveal(cell.getRow(), cell.getColumn(), true); //reveal the cell
        } else { //Right Click
            if(cell.getState() == CellType.FLAGGED){
                gameBoard.unflag(cell.getRow(), cell.getColumn()); //unflag the cell
            }else if(cell.getState() == CellType.UNPRESSED) {
                gameBoard.flag(cell.getRow(), cell.getColumn()); //flag the cell
                minesLeft.setText(String.format("%03d", gameBoard.getMinesLeft()));
            }
        }

        cellsChanged.add(cell);
    }


    /**
     *  Adds a cell to the set of changed cells
     *
     *  @param c the Cell to be added to cellsChanged
     */
    void addChanged(Cell c){
	    cellsChanged.add(c);

    }//addChanged

}//Minsweeper