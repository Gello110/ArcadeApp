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
    private Breakout game;

    /**
     *  creates a ball object with a radius
     *
     * @param radius  the radius of the ball
     */
    Ball(int radius, Breakout game){
        this.game = game;
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
    Circle render(BallDir b, int speed){
        this.x += (speed * b.getX());//updates x
        this.y += (speed * b.getY());//updates y

        c.setCenterX(this.x);
        c.setCenterY(this.y);
        return c;
    }//render

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
