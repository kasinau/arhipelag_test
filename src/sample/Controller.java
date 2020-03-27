package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.text.Text;

import java.awt.*;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.IOException;  // Import this class to handle errors
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.StringJoiner;

public class Controller implements Initializable
{

    @FXML
    Text map;
    @FXML
    DialogPane dialog;
    String action;

    StringProperty mapData = new SimpleStringProperty(arrayToString(getMap()));

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle)
    {
        map.textProperty().bind(mapData);
    }

    @FXML
    private void handleAddRowButtonAction(ActionEvent event)
    {
        action = "add_row";
        System.out.println("You clicked me!");
        mapData.setValue("map updated");
//        dialog.setContentText("Selecteaza pozitia N/S");
//        dialog.setVisible(true);
    }

    private String arrayToString(String[][] array)
    {
        String map = "";
        int i = 2, j;
        boolean endOfDataReached;
        do {
            endOfDataReached = true;
            for (j = 1; j < array[i].length; j++) {
                if (array[i][j] == null) {
                    continue;
                }
                map += array[i][j] + "\t";
                endOfDataReached = false;
            }
            i++;
            map += System.lineSeparator();
        } while (endOfDataReached == false);
        System.out.println(map);
        return map;
//        StringJoiner sj = new StringJoiner(System.lineSeparator());
//        for (String[] row : array) {
//            sj.add(Arrays.toString(row));
//        }
//        String arrayToString = sj.toString();
//        String cleanedString = arrayToString.replaceAll("null", "");
//        System.out.println(cleanedString);
    }

    private static String[][] getMap() {
        String[][] mapData = new String[100][100];
        try {
            File myObj = new File("/Users/rpavelco/Projects/IdeaProjects/arhipelag/src/sample/Insule.in");
            Scanner myReader = new Scanner(myObj);
            int i = 0;
            while (myReader.hasNextLine()) {
                int j = 0;
                String data = myReader.nextLine();
                String[] arrayData = data.split(" ");
                for (String s : arrayData) {
                    mapData[i][j] = s;
                    System.out.print(s + " ");
                    j++;
                }
                i++;

                // char[] ch = data.toCharArray();
                // // Printing array
                // for (char c : ch) {
                // 	System.out.print(c);
                // }
                System.out.println();
                // System.out.println(data);
                // System.out.println(data.getClass().getName());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return mapData;
    }

    public static void write (String filename, String[][]mapData) throws IOException{
        try {
            File myObj = new File(filename);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    // private static void showMap() {
    // 	try {
    //        	File myObj = new File("Insule.in");
    //        	Scanner myReader = new Scanner(myObj);
    //        	while (myReader.hasNextLine()) {
    //        		String data = myReader.nextLine();
    //        		System.out.println(data);
    //        	}
    //        	myReader.close();
    //        } catch (FileNotFoundException e) {
    //        	System.out.println("An error occurred.");
    //        	e.printStackTrace();
    //        }
    // }

    public static void main(String[] args) {
//        String[][] mapData = Main.getMap();
//        try {
//            Main.write("Insule.out", mapData);
//        } catch (IOException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }
    }
}
