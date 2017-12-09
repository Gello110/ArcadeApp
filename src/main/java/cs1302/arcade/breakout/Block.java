package cs1302.arcade.breakout;

import javafx.scene.shape.Rectangle;

/**
 * Makes a block to be broken in Breakout game
 */
class Block {
    private int width;
    private int height;
    private int x;
    private int y;
    private Rectangle r;
    private boolean present;

    /**
     *  Constructs a Block object
     *
     * @param width  the width of the block
     * @param height  the height of the block
     */
     Block(int width, int height){
        this.width = width;
        this.height = height;

    }//Block

    /**
     *  returns a rectangle to be rendered to the screen
     *
     * @param x  x coordinate of the rectangle
     * @param y  y coordinate of the rectangle
     * @return the rectangle to render to screen
     */
     Rectangle render(int x, int y) {
        present = true;
        this.x = x;
        this.y = y;
        r = new Rectangle(x, y, width, height);

        return r;
    }//render

    /**
     * returns the x position of the block
     *
     * @return the x position of the block
     */
     int getX() {
        return x;
    }

    /**
     * returns the y position of the block
     *
     * @return the y position of the block
     */
     int getY() {
        return y;
    }

    /**
     * returns the height of the block
     *
     * @return the height of the block
     */
     int getHeight() {
        return height;
    }

    /**
     * returns the width of the block
     *
     * @return the width of the block
     */
     int getWidth() {
        return width;
    }

    /**
     * returns the rectangle representing the block
     *
     * @return the rectangle representing the block
     */
     Rectangle getR() {
        return r;
    }//getR

    /**
     * returns <code>true</code> if block on screen, <code>false</code> if not
     *
     * @return wheter or not block is on screen
     */
     boolean getPresent(){
        return present;
    }//getPresent

    /**
     * breaks the block and keeps it from being hit again
     */
     void destroy(){
        present = false;
    }//break
}//Block
