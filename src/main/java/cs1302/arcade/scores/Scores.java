package cs1302.arcade.scores;


import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

/**
 * Class handling the high scores for minesweeper and breakout
 */
public class Scores {
    private ArrayList<Entry> bScores;
    private ArrayList<Entry> mScores;

    /**
     * creates a score class
     */
    public Scores() {
        bScores = new ArrayList<>();
        mScores = new ArrayList<>();
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
    public void displayScores(){
        Stage s = new Stage();
        VBox vbox = new VBox(10);
        HBox hBox = new HBox(10);
        vbox.setAlignment(Pos.CENTER);

        //breakout scores
        TableColumn<Entry, String> bname = new TableColumn<>("Name");
        bname.setCellValueFactory(new PropertyValueFactory<Entry, String>("name"));
        TableColumn<Entry, Integer> bscore = new TableColumn<>("Score");
        bscore.setCellValueFactory(new PropertyValueFactory<Entry, Integer>("score"));


        bname.setMinWidth(150);
        bscore.setMinWidth(150);

        TableView<Entry> btable = new TableView<>();//table of breakout scores
        btable.getColumns().addAll(bname, bscore);
        btable.setItems(FXCollections.observableArrayList(bScores));




        Text title = new Text("High Scores");
        title.setFont(new Font(30));

        vbox.getChildren().addAll(title, btable);


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
        if(game.equals("minesweeper")){
            //adds score to list of scores and sorts
            mScores.add(new Entry(name,score));
            mScores.sort((e1, e2) -> e2.compareTo(e1));

            //truncates list of scores if list longer than 10
            if(mScores.size() > 10){
                mScores.subList(0,9);
            }
        }else if(game.equals("breakout")){
            //adds score to list of scores and sorts
            bScores.add(new Entry(name, score));
            bScores.sort((e1, e2) -> e2.compareTo(e1));

            //truncates list of scores if list longer than 10
            if(bScores.size() > 10){
                bScores.subList(0, 9);
            }
        }else{
            return false;
        }
        return true;
    }//addScore

    /**
     * writes scores to files
     */
    public void write(){

    }//write

}//Scores
