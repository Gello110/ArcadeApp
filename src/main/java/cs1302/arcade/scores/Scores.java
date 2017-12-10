package cs1302.arcade.scores;


import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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

            BufferedReader breakout;
            BufferedReader minesweeper;

            breakout = new BufferedReader(new FileReader(b));
            minesweeper = new BufferedReader(new FileReader(m));

            String person;
            String[] data;//data for a person

            int i = 0;
            while ((person = breakout.readLine()) != null && i < 10) {
                data = person.split(" ");
                bScores.add(new Entry(data[0], Integer.parseInt(data[1])));
                i++;
            }

            i = 0;
            while((person = minesweeper.readLine()) != null && i < 10){
                data = person.split(" ");
                mScores.add(new Entry(data[0], Integer.parseInt(data[1])));
                i++;
            }

            breakout.close();
            minesweeper.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }//Scores

    /**
     * Makes an application modal window that displays the top high scores
     */
    public void displayScores(){
        Stage s = new Stage();
        VBox outerbox = new VBox(20);
        VBox bBox = new VBox(10);
        VBox mBox = new VBox(10);
        HBox hBox = new HBox(10);
        outerbox.setAlignment(Pos.CENTER);

        //breakout scores
        TableColumn<Entry, String> bName = new TableColumn<>("Name");
        bName.setCellValueFactory(new PropertyValueFactory<Entry, String>("name"));
        bName.setMinWidth(150);
        TableColumn<Entry, Integer> bScore = new TableColumn<>("Score");
        bScore.setCellValueFactory(new PropertyValueFactory<Entry, Integer>("score"));
        bScore.setMinWidth(150);

        TableView<Entry> bTable = new TableView<>();//table of breakout scores
        bTable.getColumns().addAll(bName, bScore);
        bTable.setItems(FXCollections.observableArrayList(bScores));

        Text bTitle = new Text("Breakout Scores");
        bTitle.setFont(new Font(20));

        bBox.getChildren().addAll(bTitle, bTable);

        //minesweeper scores
        TableColumn<Entry, String> mName = new TableColumn<>("Name");
        mName.setCellValueFactory(new PropertyValueFactory<Entry, String>("name"));
        mName.setMinWidth(150);
        TableColumn<Entry, Integer> mScore = new TableColumn<>("Score");
        mScore.setCellValueFactory(new PropertyValueFactory<Entry, Integer>("score"));
        mScore.setMinWidth(150);

        TableView<Entry> mTable = new TableView<>();//table of minesweeper scores
        mTable.getColumns().addAll(mName, mScore);
        mTable.setItems(FXCollections.observableArrayList(mScores));

        Text mTitle =  new Text("Minesweeper Scores");
        mTitle.setFont(new Font(20));

        mBox.getChildren().addAll(mTitle, mTable);

        //prepares outerbox
        hBox.getChildren().addAll(bBox, mBox);
        Text title = new Text("High Scores");
        title.setFont(new Font(30));
        outerbox.getChildren().addAll(title, hBox);

        //prepares stage
        Scene scene = new Scene(outerbox);
        s.setScene(scene);
        s.initModality(Modality.APPLICATION_MODAL);
        s.sizeToScene();
        s.setResizable(false);
        s.show();


    }//displayScores

    /**
     * adds score to list
     *
     * @param game  name of the game
     * @param name  name of the player
     * @param score  score of the player
     * @return
     */
    public boolean addScore(String game, String name, int score){
        if(game.equalsIgnoreCase("minesweeper")){
            //adds score to list of scores and sorts
            mScores.add(new Entry(name,score));
            mScores.sort((e1, e2) -> e2.compareTo(e1));

            //truncates list of scores if list longer than 10
            if(mScores.size() > 10){
                mScores.subList(0,9);
            }
        }else if(game.equalsIgnoreCase("breakout")){
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
        try {
            //makes file objects to write to
            File bScore = new File("bScore.txt");//breakout score file
            bScore.createNewFile();
            BufferedWriter bWriter = new BufferedWriter(new FileWriter(bScore));
            File mScore = new File("mScores.txt");//minesweeper score file
            mScore.createNewFile();
            BufferedWriter mWriter = new BufferedWriter(new FileWriter(mScore));

            //writes breakout scores
            for(Entry e: bScores){
                bWriter.write(e.getName() + " " + e.getScore());
                bWriter.newLine();
            }//for e in bScores

            //writes minesweeper scores
            for(Entry e: mScores){
                mWriter.write(e.getName() + " " + e.getScore());
            }//for e in mScores

            bWriter.close();
            mWriter.close();

        }catch(Exception e){
            e.printStackTrace();
        }//try catch
    }//write

}//Scores
