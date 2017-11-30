package cs1302.arcade.minesweeper;

import javafx.scene.image.Image;

/**
 *  Represents a cell on the minesweeper board
 */
public class Cell{
    private CellType type;
    private CellType state;
    private boolean isChecked;
	private int row;
	private int col;
    
    /** 
     *  Constructs a cell object
     */
    public Cell(int row, int col){
		state = CellType.UNPRESSED;
		isChecked = false;
		this.row = row;
		this.col = col;
    }//Cell

    /**
     *  sets a mine at the cell
     */
    public void setMine(){
	type = CellType.MINE;

    }//setMine

    /**
     *  Sets state so that the number matches the number of 
     *  surrounding mines
     *
     *  @param  i  The number of mines surrounding cell
     */
    public void surr(int i){
	switch(i){
	    case 0:
		type = CellType.EMPTY;
		break;
	    case 1:
		type = CellType.ONE;
		break;
	    case 2:
		type = CellType.TWO;
		break;
	    case 3:
		type = CellType.THREE;
		break;
	    case 4:
		type = CellType.FOUR;
		break;
	    case 5:
		type = CellType.FIVE;
		break;
	    case 6:
		type = CellType.SIX;
		break;
	    case 7:
		type = CellType.SEVEN;
		break;
	    case 8:
		type = CellType.EIGHT;
		break;

	}//switch i

    }//surr

    /**
     *  returns the state of the cell as a CellType object
     *
     *  @return The CellType of the cell
     */
    public CellType reveal(){
	state = type;
	return type;

    }//getState

    /**
     *  flags or unflags cell
     *
     *  @param boolean <code>true</code> if flagging, <code>false</code> if unflagging
     */
    public CellType change(boolean flagging){
	if(state.equals(CellType.MINE)){
	    return state;
		
	}//if cell is mine

	if(flagging){
	    state = CellType.FLAGGED;

	}else{
	    state = CellType.UNPRESSED;

	}//if flagging or unflagging mine
	return state;

    }//change

    /**
     *  sets isChecked to true
     */
    public void check(){
	isChecked = true;

    }//check

    /**
     *  Returns the state of the cell
     * 
     *  @return the state of they cell represented by CellType
     */
    public CellType getState(){
	return state;

    }//get state

    /**
     *  Returns if the cell has been looked at
     *
     *  @returns <code>true</code>if has been looked at
     */
    public boolean getIsChecked(){
	return isChecked;

    }//getIsChecked

    /**
     *  Returns the type of the cell
     *
     *  @return the type of the cell represented by CellType
     */
    public CellType getType(){
	return type;
	
    }//getType


    /**
     *  Sets the cell state to the given state
     *
     *  @param state the CellType the cell is to be set
     */
    public void setState(CellType state){
	this.state = state;

    }//setState

	/**
	 * Get the row this cell is located on
	 *
	 * @return The row this cell is located on
	 */
	public int getRow() {
    	return this.row;
	}

	/**
	 * Get the column this cell is located on
	 *
	 * @return The column this cell is location on
	 */
	public int getColumn() {
    	return this.col;
	}
}//Cell