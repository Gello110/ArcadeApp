package cs1302.arcade.breakout;

/**
 * Directions the ball can go
 */
public enum BallDir {
    NW(-3, -3),
    NE(3, -3),
    SW(-3, 3),
    SE(3, 3);

    int x;
    int y;

    /**
     *  constructs BallDir with x and y directoin
     *
     * @param x x direction of the ball
     * @param y y direction of the ball
     */
    private BallDir(int x, int y){
        this.x = x;
        this.y = y;

    }//BallDir

    /**
     * returns the x direction of the ball
     *
     * @return x direction of the ball
     */
    public int getX() {
        return x;
    }//getX

    /**
     * returns the y direction of the ball
     *
     * @return y direction of the ball
     */
    public int getY(){
        return y;
    }//getY
}//BallDir
