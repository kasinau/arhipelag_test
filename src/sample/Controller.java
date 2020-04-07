package sample;

import com.sun.media.jfxmedia.events.NewFrameEvent;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import javax.swing.*;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.FileWriter;
import java.io.IOException;  // Import this class to handle errors
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Scanner; // Import the Scanner class to read text files

public class Controller implements Initializable
{

    @FXML
    Text map;
    @FXML
    Text perimeterUnits;
    @FXML
    Text confirmationLabel;
    @FXML
    Text confirmationError;
    @FXML
    TextField actionLocation;
    @FXML
    GridPane confirmationForm;

    String action;
    String rowError = "Valoarea de intrare trebuie sa fie egala cu N sau S";
    String columnError = "Valoarea de intrare trebuie sa fie egala cu E sau V";

    StringProperty mapData = new SimpleStringProperty(arrayToString(getMap()));
    StringProperty perimeterUnitsData = new SimpleStringProperty(String.valueOf(getPerimeterUnits()));

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle)
    {
        map.textProperty().bind(mapData);
        perimeterUnits.textProperty().bind(perimeterUnitsData);
        confirmationLabel.textProperty().bind(new SimpleStringProperty(""));
        confirmationError.textProperty().bind(new SimpleStringProperty(""));
    }

    @FXML
    private void handleAddRowButtonAction(ActionEvent event)
    {
        action = "add_row";
        confirmationLabel.textProperty().bind(new SimpleStringProperty("Selecteaza pozitia N/S"));
        confirmationForm.visibleProperty().setValue(true);
    }

    @FXML
    private void handleDeleteRowButtonAction(ActionEvent event)
    {
        action = "delete_row";
        confirmationLabel.textProperty().bind(new SimpleStringProperty("Selecteaza pozitia N/S"));
        confirmationForm.visibleProperty().setValue(true);
    }

    @FXML
    private void handleAddColumnButtonAction(ActionEvent event)
    {
        action = "add_column";
        confirmationLabel.textProperty().bind(new SimpleStringProperty("Selecteaza pozitia E/V"));
        confirmationForm.visibleProperty().setValue(true);
    }

    @FXML
    private void handleDeleteColumnButtonAction(ActionEvent event)
    {
        action = "delete_column";
        confirmationLabel.textProperty().bind(new SimpleStringProperty("Selecteaza pozitia E/V"));
        confirmationForm.visibleProperty().setValue(true);
    }

    @FXML
    private void handleActionLocation(ActionEvent event) throws IOException
    {
        boolean isValid = isActionInputValid();
        if (!isValid) {
            if (action == "add_row" || action == "delete_row") {
                confirmationError.textProperty().bind(new SimpleStringProperty(rowError));
            } else {
                confirmationError.textProperty().bind(new SimpleStringProperty(columnError));
            }
        } else {
            confirmationError.textProperty().bind(new SimpleStringProperty(""));
            handleAction();
            reInitialize();
        }
    }

    private void reInitialize() {
        mapData = new SimpleStringProperty(arrayToString(getMap()));
        perimeterUnitsData = new SimpleStringProperty(String.valueOf(getPerimeterUnits()));
        map.textProperty().bind(mapData);
        perimeterUnits.textProperty().bind(perimeterUnitsData);
        confirmationLabel.textProperty().bind(new SimpleStringProperty(""));
        confirmationError.textProperty().bind(new SimpleStringProperty(""));
        actionLocation.setText("");
        confirmationForm.visibleProperty().setValue(false);
        action = "";
    }

    private void handleAction() throws IOException
    {
        switch (action) {
            case "add_row" : addRow();
                break;
            case "delete_row" : deleteRow();
                break;
            case "add_column" : addColumn();
                break;
            case "delete_column" : deleteColumn();
                break;
        }
    }

    private void addRow() throws IOException
    {
        System.out.println("addRow");
        String [][] mapData = getMap();
        switch (actionLocation.getText()) {
            case "N": {
                int currentRowsNumber = Integer.parseInt(mapData[0][0]);
                for (int i = currentRowsNumber; i > 0; i--) {
                    System.arraycopy(mapData[i], 0, mapData[i + 1], 0, mapData[i].length);
                }
                String firstRow = "" + (currentRowsNumber + 1) + " " + mapData[0][1] + System.lineSeparator();
                writeDataToFile(firstRow, mapData);
            }
            break;
            case "S": {
                int currentRowsNumber = Integer.parseInt(mapData[0][0]);
                System.arraycopy(mapData[currentRowsNumber], 0, mapData[currentRowsNumber + 1], 0, mapData[currentRowsNumber].length);
                String firstRow = "" + (currentRowsNumber + 1) + " " + mapData[0][1] + System.lineSeparator();
                writeDataToFile(firstRow, mapData);
            }
            break;
        }
    }

    private void deleteRow() throws IOException
    {
        String [][] mapData = getMap();
        switch (actionLocation.getText()) {
            case "N": {
                int currentRowsNumber = Integer.parseInt(mapData[0][0]);
                for (int i = 1; i < currentRowsNumber; i++) {
                    System.arraycopy(mapData[i + 1], 0, mapData[i], 0, mapData[i].length);
                }
                String firstRow = "" + (currentRowsNumber - 1) + " " + mapData[0][1] + System.lineSeparator();
                writeDataToFile(firstRow, mapData);
            }
            break;
            case "S": {
                int currentRowsNumber = Integer.parseInt(mapData[0][0]);
                System.arraycopy(mapData[currentRowsNumber + 1], 0, mapData[currentRowsNumber], 0, mapData[currentRowsNumber].length);
                String firstRow = "" + (currentRowsNumber - 1) + " " + mapData[0][1] + System.lineSeparator();
                writeDataToFile(firstRow, mapData);
            }
            break;
        }
    }

    private void addColumn() throws IOException
    {
        System.out.println("addColumn");
        String [][] mapData = getMap();
        switch (actionLocation.getText()) {
            case "E": {
                int currentRowsNumber = Integer.parseInt(mapData[0][0]);
                int currentColumnsNumber = Integer.parseInt(mapData[0][1]);
                for (int i = 1; i <= currentRowsNumber; i++) {
                    mapData[i][currentColumnsNumber] = mapData[i][currentColumnsNumber - 1];
                }
                String firstRow = "" + currentRowsNumber + " " + (currentColumnsNumber + 1) + System.lineSeparator();
                writeDataToFile(firstRow, mapData);
            }
            break;
            case "V": {
                int currentRowsNumber = Integer.parseInt(mapData[0][0]);
                int currentColumnsNumber = Integer.parseInt(mapData[0][1]);
                for (int i = 1; i <= currentRowsNumber; i++) {
                    System.out.println(mapData[i].length);
                    if (mapData[i][0] == null) {
                        break;
                    }
                    for (int j = currentColumnsNumber; j > 0; j--) {
                        mapData[i][j] = mapData[i][j - 1];
                    }
                    mapData[i][0] = mapData[i][1];
                }
                String firstRow = "" + currentRowsNumber + " " + (currentColumnsNumber + 1) + System.lineSeparator();
                writeDataToFile(firstRow, mapData);
            }
            break;
        }
    }

    private void deleteColumn() throws IOException
    {
        System.out.println("deleteColumn");
        String [][] mapData = getMap();
        switch (actionLocation.getText()) {
            case "E": {
                int currentRowsNumber = Integer.parseInt(mapData[0][0]);
                int currentColumnsNumber = Integer.parseInt(mapData[0][1]);
                for (int i = 1; i <= currentRowsNumber; i++) {
                    mapData[i][currentColumnsNumber - 1] = null;
                }
                String firstRow = "" + currentRowsNumber + " " + (currentColumnsNumber - 1) + System.lineSeparator();
                writeDataToFile(firstRow, mapData);
            }
            break;
            case "V": {
                int currentRowsNumber = Integer.parseInt(mapData[0][0]);
                int currentColumnsNumber = Integer.parseInt(mapData[0][1]);
                for (int i = 1; i <= currentRowsNumber; i++) {
                    System.out.println(mapData[i].length);
                    if (mapData[i][0] == null) {
                        break;
                    }
                    for (int j = 0; j <= currentColumnsNumber; j++) {
                        mapData[i][j] = mapData[i][j + 1];
                    }
                }
                String firstRow = "" + currentRowsNumber + " " + (currentColumnsNumber - 1) + System.lineSeparator();
                writeDataToFile(firstRow, mapData);
            }
            break;
        }
    }

    private void writeDataToFile(String firstRow, String[][] mapData) throws IOException
    {
        String stringMapData = firstRow;
        stringMapData += arrayToString(mapData);
        stringMapData = stringMapData.replaceAll("\t", " ");
        stringMapData = stringMapData.replaceAll(" " + System.lineSeparator(), System.lineSeparator());
        stringMapData = stringMapData.replaceAll(System.lineSeparator() + System.lineSeparator(), System.lineSeparator());
        System.out.println(stringMapData);
        writeToFile(System.getProperty("user.dir") + "/src/sample/Insule.in", stringMapData);
    }

    private boolean isActionInputValid ()
    {
        switch (action) {
            case "add_row":
            case "delete_row":
                return (Objects.equals(actionLocation.getText(), "N") || Objects.equals(actionLocation.getText(), "S"));
            case "add_column":
            case "delete_column":
                return (Objects.equals(actionLocation.getText(), "E") || Objects.equals(actionLocation.getText(), "V"));
        }
        return false;
    }

    private String arrayToString(String[][] array)
    {
        String map = "";
        int i = 1, j;
        boolean endOfDataReached;
        do {
            endOfDataReached = true;
            for (j = 0; j < array[i].length; j++) {
                if (array[i][j] == null || array[i][j].length() == 0) {
                    continue;
                }
                map += array[i][j] + "\t";
                endOfDataReached = false;
            }
            i++;
            map += System.lineSeparator();
            map += System.lineSeparator();
        } while (endOfDataReached == false);
        return map;
    }

    private static String[][] getMap() {
        String[][] mapData = new String[100][100];
        try {
            File myObj = new File(System.getProperty("user.dir") + "/src/sample/Insule.in");
            Scanner myReader = new Scanner(myObj);
            int i = 0;
            while (myReader.hasNextLine()) {
                int j = 0;
                String data = myReader.nextLine();
                String[] arrayData = data.split(" ");
                for (String s : arrayData) {
                    mapData[i][j] = s;
                    j++;
                }
                i++;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return mapData;
    }

    private int getPerimeterUnits() {
        String[][] mapData = getMap();
        int units = 0;
        int integerNumberOfColumns = Integer.valueOf(mapData[0][1]);
        int integerNumberOfRows = Integer.valueOf(mapData[0][0]);
        for (int i = 0; i < integerNumberOfColumns; i++) {
            units += Integer.valueOf(mapData[1][i]);
            units += Integer.valueOf(mapData[integerNumberOfRows][i]);
        }
        for (int i = 2; i < integerNumberOfRows; i++) {
            System.out.println(Integer.valueOf(mapData[i][0]));
            System.out.println(Integer.valueOf(mapData[i][integerNumberOfColumns - 1]));
            units += Integer.valueOf(mapData[i][0]);
            units += Integer.valueOf(mapData[i][integerNumberOfColumns - 1]);
        }

        return units;
    }

    private static void writeToFile (String filename, String mapData) throws IOException{
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
        try {
            FileWriter myWriter = new FileWriter(filename);
            myWriter.write(mapData);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
