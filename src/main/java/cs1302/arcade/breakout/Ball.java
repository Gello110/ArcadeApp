package cs1302.arcade.breakout;

import javafx.scene.shape.Circle;

/**
 * Ball that breaks the blocks and bounces
 */
class Ball {
    private int radius;
    private int x;
    private int y;
    private Circle c;
    private double speed;
    private int brokenBlocks;
    private boolean hitOrange;
    private boolean hitRed;

    /**
     *  creates a ball object with a radius
     *
     * @param radius  the radius of the ball
     */
    Ball(int radius, int speed){
        this.speed = speed;
        this.radius = radius;
    }//Ball

    /**
     * renders the circle to the screen
     *
     * @param x x position of the ball
     * @param y y position of the ball
     * @return the circle to be rendered to the screen
     */
    Circle render(int x, int y){
        this.x = x;
        this.y = y;
        c = new Circle(x, y, radius);
        return c;
    }//render

    /**
     *
     * @param b the Direction the ball is going
     * @return the circle to be rendered to screen
     */
    Circle render(BallDir b){
        this.x += (speed * b.getX());//updates x
        this.y += (speed * b.getY());//updates y

        c.setCenterX(this.x);
        c.setCenterY(this.y);
        return c;
    }//render

    /**
     * Add to the number of blocks broken. Used to speed up the ball.
     */
    void addBlockBroken() {
        brokenBlocks++;

        switch (brokenBlocks) {
            case 4:
                speed += .25;
                break;
            case 12:
                speed += .5;
        }
    }

    /**
     * Notify that the ball has hit an orange block
     */
    void hitOrange() {
        if(!hitOrange) {
            multiplySpeed(.10);
            hitOrange = true;
        }
    }

    /**
     * Notify that the ball has hit a red block
     */
    void hitRed() {
        if(!hitRed) {
            multiplySpeed(.15);
            hitRed = true;
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
