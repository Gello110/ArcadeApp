package cs1302.arcade.breakout;

import javafx.scene.shape.Rectangle;

/**
 * represents a paddle that the player interacts with
 */
class Paddle{
    private int width;
    private final int height;
    private int x;
    private int y;
    private Rectangle paddle;
    private final Breakout game;
    private boolean shrunk;

    /**
     *  Makes a new paddle object
     *
     * @param game  the breakout game*/
     Paddle(Breakout game){
        this.game = game;
        this.height = 20;
        this.width = 150;
    }//Paddle

    /**
     *  Makes a rectangle to represent the paddle to be rendered
     *
     * @param x  x coordinate of the paddle to be rendered
     * @param y  y coordinate of the paddle to be rendered
     * @return  the rectangle representing the paddle
     */
     Rectangle render(int x, int y){
        if(paddle == null) {//makes a paddle if no paddle exists
            this.x = x;
            this.y = y;
            paddle = new Rectangle(x, y, width, height);
        }else{//changes the position of the paddle if paddle already exists
            if(this.x + x > 0 && this.x + x + width <= game.getWidth()) {//checks if paddle is already at edge
                this.x += x;
                paddle.setX(this.x);
            }
        }

        return paddle;
    }//render

    /**
     * Shrinks the paddle after it hits the top wall
     */
    void shrink() {
        if(!shrunk) {
            width = (int) (width * .75);
            shrunk = true;

            paddle.setWidth(width);
        }
    }

    /**
     * returns the y position of the paddle
     *
     * @return the y position of the paddle
     */
     int getY() {
        return y;
    }


    /**
     * returns the rectangle representing the block
     *
     * @return the rectangle representing the block
     */
     Rectangle getPaddle() {
        return paddle;
    }
}//Paddle
