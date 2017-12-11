package cs1302.arcade.scores;

/**
 * Class representing an entry in the high score table
 */
class Entry implements Comparable<Entry> {
    private final String name;
    private final Integer score;

    /**
     * Makes an entry object
     *
     * @param name  name of the player
     * @param score  score the player achieved
     */
    public Entry(String name, int score){
        this.name = name;
        this.score = score;
    }//Entry

    /**
     * returns the score for the entry
     *
     * @return the score of the entry
     */
    public Integer getScore(){
        return score;
    }//getScore

    /**
     * returns the name of the entry
     *
     * @return the name of the entry
     */
    public String getName(){
        return name;
    }//getName

    @Override
    public int compareTo(Entry other){
        return score.compareTo(other.getScore());
    }//compareTo

}//Entry
