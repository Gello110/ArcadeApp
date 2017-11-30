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
    private GridPane gPane;//GridPane containing th cells of the game
    private int timer;//Seconds since game has started

    private Set<Cell> cellsChanged; //Set containing the cells changed since the last update of the screen

    /**
     *  Constructs a minesweeper game
     */
    public Minesweeper(){
	    super("Minesweeper");

	    gameBoard = new Board(GAME_ROWS, GAME_COLUMNS, this);
        cellsChanged = new HashSet<>();
    }//Minesweeper

    @Override
    public Scene initScene(){
        VBox parent = new VBox(); //Parent Node
        Scene scene = new Scene(parent); //Scene that will be used
        BorderPane bPane = new BorderPane(); //Will contain the scores

        minesLeft = new Text("" + gameBoard.getMinesLeft()); //The mines left to be found
        Button newGame = new Button("New Game"); //Button player clicks to start a new game.
        time = new Text(String.format("%03d", timer)); //The time spent playing the game

	//makes keyframe
	timeline = new Timeline();
	keyframe = new KeyFrame(Duration.seconds(1), e -> {
		Thread t = new Thread(() -> {
			timer++;
			Platform.runLater(() -> time.setText(String.format(
									   "%03d", timer)));
			
		    });
		t.setDaemon(true);
		t.start();
	    });
	timeline.setCycleCount(999);
	timeline.getKeyFrames().add(keyframe);


        newGame.setOnAction(action -> gameBoard = new Board(GAME_ROWS, GAME_COLUMNS)); //Reset board for new game

        bPane.setLeft(minesLeft); //Set position
        bPane.setCenter(newGame);
        bPane.setLeft(time);

        BorderPane.setAlignment(minesLeft, Pos.CENTER_LEFT); //Set alignment
        BorderPane.setAlignment(newGame, Pos.CENTER);
        BorderPane.setAlignment(time, Pos.CENTER_RIGHT);

        gPane = new GridPane(); //Will contain the grid of tiles the player interacts with

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

    /**
     *
   
     */
    public void gameEnded(boolean won){
	Text message = new Text();
	if(won){
	    message.setText("Congrats\nYou Won\nScore: " + gameBoard.getScore());

	}else{
	    message.setText("You Lost\mScore: " + gameBoard.getScore());

	    //reveals all the mines in the game
	    Cell[][] board = gameBoard.getBoard();
	    for(int i = 0; i < board.length; i++){
		for(int j = 0; j < board[0].length; j++){
		    Cell c = board[i][j];
		    boolean isMine = board[i][j].getType() == CellType.MINE;
		    boolean isFlagged = board[i][j].getState() == CellType.FLAGGED;
		    if(isMine && !isFlagged){
			c.setState(CellType.MINE);
			cellsChanged.addd(c);
			
		    }//if cell is mine and not flagged

		    if(isFlagged && !isMine){
			c.setState(CellType.WRONG);
			cellsChanged.add(c);

		    }//if cell is flagged and not a mine

		}//for j
		

	    }//for i in gameboard

	}//if else

	Stage s = new Stage();
	Vbox container = new VBox(50);
	container.setAlignment(Pos.CENTER);
	contaiment.add(message);
	
    }//gameEnded

    @Override
    public void updateScene(Scene scene){
	for(Cell c: cellsChanged){//replaces all the changed cells
	    for(ImageView img: gPane.getChildren()){
		if(gpane.getColumnIndex(img) == c.getColumn() &&
		   gpane.getRowIndex(img) == c.getRow()){
		    img = c.getState().getImage();

		}//if the image is equal to the column and row of the cell

	    }//for all imageview in gpane

	}//for each cell in cellsChanged
	cellsChanged.clear();//clears the cells in the set

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


    /**
     *  Adds a cell to the set of changed cells
     *
     *  @param c the Cell to be added to cellsChanged
     */
    public void addChanged(Cell c){
	cellsChanged.add(c);

    }//addChanged

}//Minsweeper