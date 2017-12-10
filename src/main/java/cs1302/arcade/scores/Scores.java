package cs1302.arcade.scores;


import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

/**
 * Class handling the high scores for minesweeper and breakout
 */
public class Scores {
    ArrayList<Entry> bScores;
    ArrayList<Entry> mScores;

    /**
     * creates a score class
     */
    public Scores() {
        try {
            File b = new File("bScores.txt");
            File m = new File("mScores.txt");


            b.createNewFile();//makes sure file exists
            m.createNewFile();//makes sure file exists

            BufferedReader breakout = null;
            BufferedReader minesweeper = null;

            breakout = new BufferedReader(new FileReader(b));
            minesweeper = new BufferedReader(new FileReader(m));

            String person;
            String[] data;//data for a person

            int i = 0;
            while ((person = breakout.readLine()) != null || i < 10) {
                data = person.split(" ");
                bScores.add(new Entry(data[0], Integer.parseInt(data[1])));
                i++;
            }

            i = 0;
            while((person = breakout.readLine()) != null || i < 10){
                data = person.split(" ");
                mScores.add(new Entry(data[0], Integer.parseInt(data[1])));
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }//Scores

    /**
     * Makes an application modal window that displays the top high scores
     */
    public void getScores(){
        Stage s = new Stage();
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);

        TableView<String> table = new TableView<>();
        TableColumn<String, String> name = new TableColumn<>("Name");
        TableColumn<String, Integer> score = new TableColumn<>("Score");


    }

    /**
     * adds score to list
     *
     * @param game  name of the game
     * @param name  name of the player
     * @param score  score of the player
     * @return
     */
    public boolean addScore(String game, String name, int score){
        return true;
    }//addScore

    /**
     * writes scores to files
     */
    public void write(){

    }//write

}//Scores
