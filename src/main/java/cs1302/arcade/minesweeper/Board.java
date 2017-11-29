package cs1302.arcade.minesweeper;

import java.util.Random;
import javafx.scene.image.ImageView;
import java.util.ArrayList;
import javafx.scene.image.Image;

/**
 *  Class that represents a board of cells for minesweeper
 */
public class Board {
    private int round;
    private Cell[][] board;
	private int mines;

    /**
     *  Constructs a Board for minesweeper
	 *
	 *  @param rows The number of rows in the board
	 *  @param cols The number of cols in the board
     */
    public Board(int rows, int cols){
		board = new Cell[rows][cols];
		round = 0;
		mines = 40;
		ArrayList<Cell> unmined = new ArrayList<>();


		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[i].length; j++){
				board[i][j] = new Cell(i, j);

				unmined.add(board[i][j]);
			}//for j
		}//for i

		for(int i = 0; i < mines; i++){
			Random r = new Random();
			int j = r.nextInt(unmined.size());

			unmined.get(r.nextInt(j)).setMine();
			unmined.remove(j);
		}//for i

		//sets
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[0].length; j++){
				if(!board[i][j].reveal().equals(CellType.MINE)){
					board[i][j].surr(surr(i, j));
				}//if cell at i j is not a mine
			}//for j
		}//for i
    }//Board

    /**
     *  gets the number of mines surrounding a particular cell of 
     *  row, col
     *
     *  @param row  the row of the cell to be analysed
     *  @param col the column of the cell to be analysed
     *  @return the number of mines surrounding the cell, 1-8
     */
    private int surr(int row, int col){
		int count = 0;

		for(int i = row - 1; i < row + 2; i++){
			for(int j = col - 1; j < col + 2; j++){
				if(-1 < i && i  < board.length - 1 && -1 < j && j< board[0].length - 1){
					if(board[i][j].getType().equals(CellType.MINE)){
						count++;

					}//if
				}//if
			}//for j
		}//for i
    }//surr

    /**
     *  Reveals the contents of the cell at row, col
     *
     *  @param row  t if(flagging){he row of the cell to be revealed
     *  @param col  the columm of the cell to be revealed
     *  @param the picture to show to the user
     *  @param player <code>true</code> if called non-recursively
     */
    public ImageView reveal(int row, int col, boolean player){
		if(row < 0 || col < 0 || row > board.length - 1
		   || col > board[0].length || board[row][col].getIsChecked()
		   || board[row][col].getType().equals(CellType.MINE)
		   || !player){
			return null;

		}//if row, col is out of bounds or if the cell is a mine

		if(board[row][col].getType().equals(CellType.MINE)
		   && player){
			lose();
			return null;

		}//if player initiated and a mine

		board[row][col].check();//reveals current board

		int i = surr(row, col);
		if(i == 0){
			reveal(row - 1, col - 1, false);
			reveal(row, col - 1, false);
			reveal(row + 1, col - 1, false);
			reveal(row - 1, col, false);
			reveal(row + 1, col, false);
			reveal(row - 1, col + 1, false);
			reveal(row, col + 1, false);
			reveal(row + 1, col + 1, false);

		}//if no mines surround current spot

		return board[row][col].reveal();
    }//reveal

    /**
     *  Called when player reveals a mine and loses the game
     */
    public void lose(){
		System.out.println("YoU LoSe, LoOoOoOoSeRrRrR");
    }//lose

    /**
     *  Called when player wins the game to terminate game
     */
    public void win(){
		System.out.println("I mean I guess you won....");
    }//win

    /**
     *  Checks to see if game has been completed
     * 
     *  @return <code>true</code>if the player has won
     */
    public boolean hasWon(){
		for(int i = 0; i < board.length; i++){
			for(int j = 0; j < board[0].length; j++){
				Cell cell = board[i][j]; //Cell at point

				if(cell.getType() != CellType.MINE && cell.getState() == CellType.UNPRESSED) //Cell isn't revealed and isn't a mine
					return false; //They haven't revealed all non-mine cells
			}//for j
		}//for i

		return true; //They've revealed all non-mine cells
    }//hasWon

    /**
     *  increments round
     */
    public void newRound(){
		round++;
    }//newRound
    
    /**
     *  flags the cell at row, col to have a mine
     *
     *  @param row  the row of the cell to be flagged
     *  @param col the column of the cell to be flagged
     *  @param the picture to show the user
     */
    public ImageView flag(int row, int col){
    	round++;
		return new ImageView(board[row][col].change(true).getImage());
    }//flag

    /**
     *  unflags the cell at the row, col 
     *
     *  @param row the row of the cell to unflag
     *  @param col the column of the cell to unflag
     *  @return the image to show the user
     */
    public ImageView unflag(int row, int col){
    	round++;
		return new ImageView(board[row][col].change(false).getImage());
	}//unflag

	/**
	 *  Returns the score for the round
	 *
	 *  @return the score for the round
	 */
	public int getScore(){
		if(round >= 216){
			return 0;
		}
		return board.length * board[0].length - round - 40;
	}//getScore

	/**
	 * Gets the total number of flags the user has placed
	 *
	 * @return The total number of flags placed
	 */
	public int getFlagged() {
    	int flagged = 0;

    	for(Cell[] cellRow : cells) { //Iterate through cells
    		for(Cell c : cellRow) {
    			if(c.getState() == CellType.FLAGGED) //Check if flagged
    				flagged++; //Increment number of flagged cells by one
			}
		}

    	return flagged;
	}

	/**
	 * Gets the amount of mines on the board
	 *
	 * @return The amount of mines on the board
	 */
	public int getMines() {
    	return mines;
	}

	/**
	 * Returns the number of mines left for the user to find. Computed as total mines - total cells flagged
	 *
	 * @return The number of mines left for the user to find
	 */
	public int getMinesLeft() {
		return getMines() - getFlagged();
	}

	/**
	 * Gets the two dimensional Cell array representing the game board
	 *
	 * @return The game board
	 */
	public Cell[][] getBoard() {
		return board;
	}
}//Board