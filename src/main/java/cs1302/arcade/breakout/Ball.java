package cs1302.arcade.breakout;

import javafx.scene.shape.Circle;

/**
 * Ball that breaks the blocks and bounces
 */
class Ball {
    private final int radius;
    private int x;
    private int y;
    private Circle c;
    private double speed;
    private int brokenBlocks;
    private boolean hitOrange;
    private boolean hitRed;
    private final Direction direction;

    /**
     *  creates a ball object with a radius
     *
     */
    Ball(){
        this.speed = 2; //set initial speed
        this.radius = 7; //set radius
        this.direction = new Direction(); //set direction with initial xDir change of 2 and yDir change of -2
    }//Ball

    /**
     * renders the circle to the screen
     *
     * @param y y position of the ball
     * @return the circle to be rendered to the screen
     */
    Circle render(int y){
        this.x = 375; //set new position
        this.y = y;
        this.c = new Circle(375, y, radius); //create circle

        return c; //return new circle
    }//render

    /**
     * Renders the circle
     */
    void render(){
        this.x += (speed * direction.getXDir());//updates x
        this.y += (speed * direction.getYDir());//updates y

        c.setCenterX(this.x); //set location
        c.setCenterY(this.y);

    }//render

    /**
     * Change the direction of the ball using 1 or -1
     *
     * @param x integer to multiply x direction by
     * @param y integer to multiply y direction by
     */
    void changeDirection(int x, int y) {
        direction.multDir(x, y); //multiple direction
    }

    /**
     * Gets the direction of the ball
     *
     * @return a {@link Direction} Object representing the direction of this ball
     */
    Direction getDirection() {
        return this.direction;
    }

    /**
     * Add to the number of blocks broken. Used to speed up the ball.
     */
    void addBlockBroken() {
        brokenBlocks++; //add to broken blocks

        switch (brokenBlocks) {
            case 4: //four blocks broken
                multiplySpeed(.05); //increase speed by 5%
                break;
            case 12: //twelve blocks broken
                multiplySpeed(.1); //increase speed by 10%
        }
    }

    /**
     * Notify that the ball has hit an orange block
     */
    void hitOrange() {
        if(!hitOrange) { //if hasn't hit orange before
            multiplySpeed(.1); //increase speed by 10%
            hitOrange = true; //set to true, orange has been hit
        }
    }

    /**
     * Notify that the ball has hit a red block
     */
    void hitRed() {
        if(!hitRed) { //if hasn't hit red before
            multiplySpeed(.15); //increase speed by 15%
            hitRed = true; //set to true, red has been hit
        }
    }

    /**
     * Multiply the current speed by a percent
     *
     * @param percent The percent to multiply the speed by
     */
    private void multiplySpeed(double percent) {
        speed = speed + (percent * speed);
    }

    /**
     * returns the x position of the ball
     *
     * @return the x position of the ball
     */
     int getX(){
        return x;
    }//getX

    /**
     *  returns the y position of the ball
     *
     * @return the y position of the ball
     */
     int getY(){
        return y;
    }//getY

    /**
     * returns the radius of the ball
     *
     * @return the radius of the ball
     */
     int getRadius(){
        return radius;
    }//getRadius

    /**
     * returns the circle representing the ball
     *
     * @return the circle representing the ball
     */
     Circle getC(){
        return c;
    }

}//Ball
