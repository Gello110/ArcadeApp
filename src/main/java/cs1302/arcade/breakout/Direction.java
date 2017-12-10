package cs1302.arcade.breakout;

class Direction {
    private int xDir;
    private int yDir;

    Direction() {
        this.xDir = 2;
        this.yDir = -2;
    }

    void multDir(int x, int y) {
        xDir *= x;
        yDir *= y;
    }

    int getXDir() {
        return xDir;
    }

    int getYDir() {
        return yDir;
    }

    boolean isE() {
        return isNE() || isSE();
    }

    boolean isW() {
        return isNW() || isSW();
    }

    boolean isS() {
        return isSW() || isSE();
    }

    boolean isN() {
        return isNE() || isNW();
    }

    private boolean isNE() {
        return xDir > 0 && yDir < 0;
    }

    private boolean isNW() {
        return xDir < 0 && yDir < 0;
    }

    private boolean isSE() {
        return xDir > 0 && yDir > 0;
    }

    private boolean isSW() {
        return xDir < 0 && yDir > 0;
    }
}
