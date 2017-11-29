package cs1302.arcade.minesweeper;

import java.util.Set;
import java.util.HashSet;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.image.ImageView;
import javafx.geometry.Pos;
import cs1302.arcade.Game;

/**
 *  Class representing a minesweeper game
 */
public class Minesweeper extends Game{
    
    private static final int GAME_COLUMNS = 16;
    private static final int GAME_ROWS = 16;

    private Board gameBoard; //Board containing all the game logic and cells
    private Text minesLeft; //Text displaying the mines left
    private Text time; //Text displaying the time the player has been playing

    private Set<Cell> cellsChanged; //Set containing the cells changed since the last update of the screen

    /**
     *  Constructs a minesweeper game
     */
    public Minesweeper(){
	    super("Minesweeper");

	    gameBoard = new Board(GAME_ROWS, GAME_COLUMNS);
        cellsChanged = new HashSet<>();
    }//Minesweeper

    @Override
    public Scene initScene(){
        VBox parent = new VBox(); //Parent Node
        Scene scene = new Scene(parent); //Scene that will be used
        BorderPane bPane = new BorderPane(); //Will contain the scores

        minesLeft = new Text("" + gameBoard.getMinesLeft()); //The mines left to be found
        Button newGame = new Button("New Game"); //Button player clicks to start a new game.
        time = new Text("000"); //The time spent playing the game

        //TODO Set formatting for text

        newGame.setOnAction(action -> gameBoard = new Board(GAME_ROWS, GAME_COLUMNS)); //Reset board for new game

        bPane.setLeft(minesLeft); //Set position
        bPane.setCenter(newGame);
        bPane.setLeft(time);

        BorderPane.setAlignment(minesLeft, Pos.CENTER_LEFT); //Set alignment
        BorderPane.setAlignment(newGame, Pos.CENTER);
        BorderPane.setAlignment(time, Pos.CENTER_RIGHT);

        GridPane gPane = new GridPane(); //Will contain the grid of tiles the player interacts with

        Cell[][] board = gameBoard.getBoard(); //Get game board

        for(int row = 0; row < board.length; row++) { //Iterate through board
            for (int col = 0; col < board[row].length; col++) {
                Cell cell = board[row][col]; //Get cell at row and column
                ImageView view = new ImageView(cell.getState().getImage()); //Make image view

                view.setFitWidth(15); //Set image width and height
                view.setFitHeight(15);
                view.setOnMouseClicked(action -> processAction(cell, action)); //Process mouse click

                gPane.add(view, col, row); //Adds it to the pane
            }
        }

        parent.getChildren().addAll(bPane, gPane); //Add children to stuff

        return scene;
    }//init Scene

    @Override
    public void updateScene(Scene scene){

    }//updateScene

    @Override
    public boolean isOver(){
	return true;

    }//isOver

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
            gameBoard.reveal(cell.getRow(), cell.getColumn(), true); //reveal the cell
        } else { //Right Click
            gameBoard.flag(cell.getRow(), cell.getColumn()); //flag the cell
        }

        cellsChanged.add(cell);
    }

}//Minsweeper