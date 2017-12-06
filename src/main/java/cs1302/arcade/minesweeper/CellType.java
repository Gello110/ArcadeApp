package cs1302.arcade.minesweeper;

import javafx.scene.image.Image;

/** 
 *  Enumeration representing the state of a cell
 */
public enum CellType{
    FLAGGED(-2, "minesweeper_flag.jpg" ),
	UNPRESSED(-3, "minesweeper_unpressed.jpg"),
	EMPTY(0, "minesweeper_pressed.jpg"),
	MINE(-1, "minesweeper_bomb.jpg"),
	ONE(1, "minesweeper_1.jpg"),
	TWO(2, "minesweeper_2.png"),
	THREE(3, "minesweeper_3.jpg"),
	FOUR(4, "minesweeper_4.jpg"),
	FIVE(5, "minesweeper_5.jpg"),
	SIX(6, "minesweeper_6.jpg"),
	SEVEN(7, "minesweeper_7.jpg"),
	EIGHT(8, "minesweeper_8.jpg"),
	WRONG(9, "minesweeper_wrong_flag.jpg"),
	HIT(10, "minesweeper_bomb_hit.jpg");

    private int type;
    private Image pic;

    /**
     *  Contructs a CellType
     *
     *  @param type  The type of the cell
     *  @param url  The string that holds the file location
     */
    CellType(int type, String url){
		this.type = type;
		this.pic = new Image(getClass().getClassLoader().getResource(url).toString());
    }//CellType

    /**
     *  returns the type of cell it is
     *
     *  @return the type of cell
     */
    public int getType(){
		return type;
    }//

    /**
     *  Returns the image of cell
     *
     *  @return the image that represents the cell
     */
    public Image getImage(){
		return pic;
    }//getImage

	public static CellType getTypeByNumber(int number) {
    	for(CellType type : values())
    		if(type.getType() == number)
    			return type;

    	return CellType.UNPRESSED;
	}
}//CellType