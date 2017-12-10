package cs1302.arcade.breakout;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Makes a block to be broken in Breakout game
 */
class Block {
    private final int width;
    private final int height;
    private int x;
    private int y;
    private Rectangle r;
    private boolean present;
    private BlockType type;

    /**
     * Constructs a Block object
     *
     */
    Block(int level) {
        this.width = 50;
        this.height = 20;

        for (BlockType blockType : BlockType.values()) {
            if (blockType.ordinal() == level)
                type = blockType;
        }
    }//Block

    /**
     * returns a rectangle to be rendered to the screen
     *
     * @param x x coordinate of the rectangle
     * @param y y coordinate of the rectangle
     * @return the rectangle to render to screen
     */
    Rectangle render(int x, int y) {
        present = true;
        this.x = x;
        this.y = y;
        r = new Rectangle(x, y, width, height);

        r.setFill(type.getColor());

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
    boolean getPresent() {
        return present;
    }//getPresent

    /**
     * breaks the block and keeps it from being hit again
     */
    void destroy() {
        present = false;
    }//break

    /**
     * Returns the type of this block
     *
     * @return a BlockType representing the type of this block
     */
    BlockType getType() {
        return this.type;
    }

    /**
     * Enum that represents the type of the block
     */
    enum BlockType {
        RED(7, Color.RED),
        ORANGE(5, Color.ORANGE),
        GREEM(3, Color.GREEN),
        YELLOW(1, Color.YELLOW);

        private final int score;
        private final Color color;

        /**
         * Creates a BlockType
         *
         * @param score The points this type adds to score when broken
         * @param color The color of this block type
         */
        BlockType(int score, Color color) {
            this.color = color;
            this.score = score;
        }

        /**
         * Gets the score breaking this block type earns
         *
         * @return points to add for breaking this block
         */
        public int getScore() {
            return score;
        }

        /**
         * Gets the color of this block type
         *
         * @return the color of this block type
         */
        Color getColor() {
            return color;
        }
    }
}//Block
