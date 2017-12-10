package cs1302.arcade.minesweeper;

/**
 *  Represents a cell on the minesweeper board
 */
public class Cell{
    private CellType type;
    private CellType state;
    private boolean isChecked;
	private final int row;
	private final int col;
    
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
    	type = CellType.getTypeByNumber(i); //Get CellType by number
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
     *  @param flagging <code>true</code> if flagging, <code>false</code> if unflagging
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
     *  @return <code>true</code>if has been looked at
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