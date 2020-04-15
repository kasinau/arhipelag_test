package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.FileWriter;
import java.io.IOException;  // Import this class to handle errors
import java.net.URL;
import java.util.*;

public class Controller implements Initializable
{

    @FXML
    Text map;
    @FXML
    Text perimeterUnits;
    @FXML
    Text numberOfIslands;
    @FXML
    Text maxAreaIsland;
    @FXML
    Text maxAreaSquareInfo;
    @FXML
    Text sortedColumnsInfo;
    @FXML
    Text confirmationLabel;
    @FXML
    Text confirmationError;
    @FXML
    TextField actionLocation;
    @FXML
    TextField maxHLimitation;
    @FXML
    GridPane confirmationForm;
    @FXML
    GridPane maxAreaSquare;
    @FXML
    GridPane mapPath;
    @FXML
    TextField p1i;
    @FXML
    TextField p1j;
    @FXML
    TextField p2i;
    @FXML
    TextField p2j;
    @FXML
    TextField o1i;
    @FXML
    TextField o1j;
    @FXML
    TextField o2i;
    @FXML
    TextField o2j;

    String action;
    String rowError = "Valoarea de intrare trebuie sa fie egala cu N sau S";
    String columnError = "Valoarea de intrare trebuie sa fie egala cu E sau V";

    String[][] mapArrayData = getMap();
    StringProperty mapData = new SimpleStringProperty(arrayToString(mapArrayData));
    StringProperty perimeterUnitsData = new SimpleStringProperty(String.valueOf(getPerimeterUnits()));
    StringProperty numberOfIslandsData = new SimpleStringProperty(String.valueOf(countIslands(mapArrayData)));
    StringProperty maxAreaIslandData = new SimpleStringProperty(String.valueOf(maxAreaOfIsland(stringToIntMap(mapArrayData))));
    StringProperty sortedColumnsInfoData = new SimpleStringProperty(getSortedColumnsString(getMapSortInfo(mapArrayData)));


    // M x N matrix
    private int M = 0;
    private int N = 0;

    @Override
    public void initialize(URL location, ResourceBundle resourceBundle)
    {
        map.textProperty().bind(mapData);
        perimeterUnits.textProperty().bind(perimeterUnitsData);
        numberOfIslands.textProperty().bind(numberOfIslandsData);
        maxAreaIsland.textProperty().bind(maxAreaIslandData);
        sortedColumnsInfo.textProperty().bind(sortedColumnsInfoData);
        confirmationLabel.textProperty().bind(new SimpleStringProperty(""));
        confirmationError.textProperty().bind(new SimpleStringProperty(""));
        initializeMaxAreaSquareInfo(Math.min(Integer.valueOf(mapArrayData[0][0]), Integer.valueOf(mapArrayData[0][1])));
        setMatrixDimensions();
    }

    private void initializeMaxAreaSquareInfo(int maxH) {
        int[] maxSquareInfo = getMaxSubSquareInfo(
                stringToIntMap(mapArrayData),
                maxH
        );
        System.out.println(Arrays.toString(maxSquareInfo));
        setMaxAreaSquareData(getMaxAreaSquareString(maxSquareInfo));
    }

    private void setMatrixDimensions()
    {
        N = Integer.valueOf(mapArrayData[0][0]);
        M = Integer.valueOf(mapArrayData[0][1]);
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

    @FXML
    private void handleMaxAreaLimitation(ActionEvent event) throws IOException
    {
        String maxLimitation = maxHLimitation.getText();
        if (Integer.valueOf(maxLimitation) > 0) {
            initializeMaxAreaSquareInfo(Integer.valueOf(maxLimitation));
        }
    }

    @FXML
    private void calculatePath(ActionEvent event) throws IOException, CloneNotSupportedException
    {
//        int mat[][] =
//                {
//                        { 1, 1, 1, 1, 1, 0, 0, 1, 1, 1 },
//                        { 0, 1, 1, 1, 1, 1, 0, 1, 0, 1 },
//                        { 0, 0, 1, 0, 1, 1, 1, 0, 0, 1 },
//                        { 1, 0, 1, 1, 1, 0, 1, 1, 0, 1 },
//                        { 0, 0, 0, 1, 0, 0, 0, 1, 0, 1 },
//                        { 1, 0, 1, 1, 1, 0, 0, 1, 1, 0 },
//                        { 0, 0, 0, 0, 1, 0, 0, 1, 0, 1 },
//                        { 0, 1, 1, 1, 1, 1, 1, 1, 0, 0 },
//                        { 1, 1, 1, 1, 1, 0, 0, 1, 1, 1 },
//                        { 0, 0, 1, 0, 0, 1, 1, 0, 0, 1 },
//                };

        // construct a matrix to keep track of visited cells
        int[][] visited = new int[M][N];

        WayInfo wayInfo = new WayInfo();
        wayInfo.minDist = Integer.MAX_VALUE;
        wayInfo.way = new ArrayList<>();

        WayInfo wayInfoTmp = new WayInfo();
        wayInfoTmp.minDist = 0;
        wayInfoTmp.way = new ArrayList<>();
        wayInfo = findShortestPath(stringToIntMap(mapArrayData), visited, 0, 0, 3, 8,
                wayInfo, wayInfoTmp);

        if(wayInfo.minDist != Integer.MAX_VALUE) {
            System.out.println("The shortest path from source to destination "
                    + "has length " + wayInfo.minDist);
        }
        else {
            System.out.println("Destination can't be reached from source");
        }
        System.out.println(Arrays.deepToString(wayInfo.way.toArray()));

//        int[][] cleanPathArray = new int[wayInfo.minDist + 1][2];
////        System.out.println(wayInfo.way.size());
////        System.out.println(Arrays.toString(wayInfo.way.get(0)));
//        int i = wayInfo.minDist;
//        int waySize = wayInfo.way.size();
//        System.out.println(waySize);
//        for (int j = waySize; i >= 0 && j > 0; j--) {
//            System.out.println(i);
//            System.out.println(j);
////            System.out.println(Arrays.toString(wayInfo.way.get(j - 1)));
//            int[] wayItem = wayInfo.way.get(j - 1);
//            if (j < waySize - 1 && (cleanPathArray[i + 1][0] != wayItem[0] && cleanPathArray[i + 1][1] != wayItem[1])) {
//                System.out.println("Skipping item " + Arrays.toString(wayItem) + " compared to item " + Arrays.toString(cleanPathArray[i + 1]));
//                continue;
//            }
//            System.out.println("Copy item " + Arrays.toString(wayItem));
//            System.arraycopy(wayItem, 0, cleanPathArray[i], 0, 2);
//            i--;
//        }
//        System.out.println(Arrays.deepToString(cleanPathArray));
    }

    private void reInitialize() {
        mapArrayData = getMap();
        mapData = new SimpleStringProperty(arrayToString(mapArrayData));
        perimeterUnitsData = new SimpleStringProperty(String.valueOf(getPerimeterUnits()));
        numberOfIslandsData = new SimpleStringProperty(String.valueOf(countIslands(mapArrayData)));
        maxAreaIslandData = new SimpleStringProperty(String.valueOf(maxAreaOfIsland(stringToIntMap(mapArrayData))));
        sortedColumnsInfoData = new SimpleStringProperty(getSortedColumnsString(getMapSortInfo(mapArrayData)));
        map.textProperty().bind(mapData);
        perimeterUnits.textProperty().bind(perimeterUnitsData);
        numberOfIslands.textProperty().bind(numberOfIslandsData);
        maxAreaIsland.textProperty().bind(maxAreaIslandData);
        sortedColumnsInfo.textProperty().bind(sortedColumnsInfoData);
        confirmationLabel.textProperty().bind(new SimpleStringProperty(""));
        confirmationError.textProperty().bind(new SimpleStringProperty(""));
        actionLocation.setText("");
        confirmationForm.visibleProperty().setValue(false);
        action = "";
        initializeMaxAreaSquareInfo(Math.min(Integer.valueOf(mapArrayData[0][0]), Integer.valueOf(mapArrayData[0][1])));
        setMatrixDimensions();
    }

    private String getMaxAreaSquareString(int[] maxAreaSquareInfo)
    {
        return "Patratul cu aria maxima are suprafata de " + maxAreaSquareInfo[0] + " unitati si coordonalele " + maxAreaSquareInfo[1] + "/" + maxAreaSquareInfo[2] + " si " + maxAreaSquareInfo[3] + "/" + maxAreaSquareInfo[4];
    }

    private void setMaxAreaSquareData(String maxAreaSquareString)
    {
        maxAreaSquareInfo.textProperty().bind(new SimpleStringProperty(maxAreaSquareString));
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

    private String[][] getMap() {
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
            units += Integer.valueOf(mapData[i][0]);
            units += Integer.valueOf(mapData[i][integerNumberOfColumns - 1]);
        }

        return units;
    }

    private void writeToFile (String filename, String mapData) throws IOException{
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

    private int countIslands(String[][] map)
    {
        int count = 0;

        for (int i = 1; i <= Integer.parseInt(map[0][0]); i++) {
            for (int j = 0; j < Integer.parseInt(map[0][1]); j++) {
                if (Integer.parseInt(map[i][j]) == 1) {
                    if ((i <= 1 || Integer.parseInt(map[i-1][j]) == 0) && (j == 0 || Integer.parseInt(map[i][j-1]) == 0))
                        count++;
                }
            }
        }

        return count;
    }

    private String getSortedColumnsString(int[][] sortedMapInfo)
    {
        String sortedColumnsStringInfo = "Lista coloanelor in ordine crescatoare(nr coloanei/nr unitati):";
        for (int i = 0; i <  sortedMapInfo.length; i++) {
            sortedColumnsStringInfo += ' ' + String.valueOf(sortedMapInfo[i][0] + 1) + '/' + String.valueOf(sortedMapInfo[i][1]);
        }
        return  sortedColumnsStringInfo;
    }

    private int[][] getMapSortInfo(String[][] map)
    {
        int rows = Integer.parseInt(map[0][0]);
        int columns = Integer.parseInt(map[0][1]);
        int[][] sortInfo = new int[columns][2];

        for (int j = 0; j < columns; j++) {
            sortInfo[j][0] = j;
            sortInfo[j][1] = 0;
            for (int i = 1; i <= rows; i++) {
                sortInfo[j][1] += Integer.parseInt(map[i][j]);
            }
        }
        int[] tempArray = new int[2];
        for (int i = 0; i < columns; i++) {
            for (int j = 1; j < (columns - i); j++) {
                if (sortInfo[j - 1][1] > sortInfo[j][1]) {
                    System.arraycopy(sortInfo[j - 1], 0, tempArray, 0, 2);
                    System.arraycopy(sortInfo[j], 0, sortInfo[j - 1], 0, 2);
                    System.arraycopy(tempArray, 0, sortInfo[j], 0, 2);
                }
            }
        }

        return sortInfo;
    }

    private int[][] stringToIntMap(String[][] stringMap)
    {
        int rows = Integer.valueOf(stringMap[0][0]);
        int columns = Integer.valueOf(stringMap[0][1]);
        int[][] integerMap = new int[rows][columns];
        for (int i = 1; i <= rows; i++) {
            for (int j = 0; j < columns; j++) {
                integerMap[i - 1][j] = Integer.valueOf(stringMap[i][j]);
            }
        }
        return integerMap;
    }

    private int maxAreaOfIsland(int[][] grid)
    {
        if (grid == null || grid.length == 0 ||
                grid[0] == null || grid[0].length == 0) {
            return 0;
        }

        int rst = 0;
        int rows = grid.length;
        int cols = grid[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 1) {
                    int area = infect(grid, i, j, rows, cols, 0);
                    rst = Math.max(rst, area);
                }
            }
        }

        return rst;
    }

    private int infect(int[][] grid, int i, int j, int rows, int cols, int area)
    {
        if (i < 0 || i >= rows || j < 0 || j >= cols || grid[i][j] != 1) {
            return area;
        }

        // Mark the explored island cells with 2.
        grid[i][j] = 2;
        area++;
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : dirs) {
            area = infect(grid, i + dir[0], j + dir[1], rows, cols, area);
        }

        return area;
    }

    private int[] getMaxSubSquareInfo(int[][] islandsMap, int maxH)
    {
        System.out.println("Inaltimea maxima: " + maxH);
        int i,j;
        int rows = islandsMap.length;         //no of rows in islandsMap[][]
        int columns = islandsMap[0].length;     //no of columns in islandsMap[][]
        int[][] newDiagonal = new int[rows][columns];
        int[][] newArea = new int[rows][columns];

        int max_of_s, max_i, max_j;


        for(i = 0; i < rows; i++) {
            newDiagonal[i][0] = islandsMap[i][0];
            newArea[i][0] = islandsMap[i][0];
        }

        for(j = 0; j < columns; j++) {
            newDiagonal[0][j] = islandsMap[0][j];
            newArea[0][j] = islandsMap[0][j];
        }

        for(i = 1; i < rows; i++) {
            for(j = 1; j < columns; j++) {
                newArea[i][j] = 0;
                if(islandsMap[i][j] == 1) {
                    newDiagonal[i][j] = newDiagonal[i-1][j-1] + 1;
                    for (int ariaStartRow = i + 1 - newDiagonal[i][j]; ariaStartRow <= i; ariaStartRow++) {
                        for (int ariaStartColumn = j + 1 - newDiagonal[i][j]; ariaStartColumn <= j; ariaStartColumn++) {
                            newArea[i][j] += islandsMap[ariaStartRow][ariaStartColumn];
                        }
                    }
                } else {
                    newDiagonal[i][j] = 0;
                }
            }
        }

        int[] resultArray = new int[5];
        resultArray[0] = 0;
        for (i = 0; i < rows; i++) {
            for (j = 0; j < columns; j++) {
                if (newArea[i][j] > resultArray[0] && newDiagonal[i][j] <= maxH) {
                    resultArray[0] = newArea[i][j];
                    resultArray[1] = i + 1;
                    resultArray[2] = j - newDiagonal[i][j] + 2;
                    resultArray[3] = i - newDiagonal[i][j] + 2;
                    resultArray[4] = j + 1;
                }
            }
        }

        return resultArray;
    }



    // Check if it is possible to go to (x, y) from current position. The
    // function returns false if the cell has value 0 or already visited
    private boolean isSafe(int mat[][], int visited[][], int x, int y)
    {
        return !(mat[x][y] == 0 || visited[x][y] != 0);
    }

    // if not a valid position, return false
    private boolean isValid(int x, int y)
    {
        return (x < M && y < N && x >= 0 && y >= 0);
    }

    // Find Shortest Possible Route in a Matrix mat from source cell (0, 0)
    // to destination cell (x, y)

    // 'min_dist' stores length of longest path from source to destination
    // found so far and 'dist' maintains length of path from source cell to
    // the current cell (i, j)

    public WayInfo findShortestPath(int[][] mat, int[][] visited, int i, int j, int x, int y, WayInfo min_dist, WayInfo dist) throws CloneNotSupportedException
    {
        int[] stepToAdd = new int[2];
        stepToAdd[0] = i;
        stepToAdd[1] = j;
        if (!min_dist.pathFound) {
            min_dist.way.add(stepToAdd);
        }
        if (!min_dist.pathFound) {
            dist.way.add(stepToAdd);
        }
        // if destination is found, update min_dist
        if (i == x && j == y)
        {
            min_dist.pathFound = true;
            dist.pathFound = true;
            int minPath = Integer.min(dist.minDist, min_dist.minDist);
            if (min_dist.minDist == minPath) {
                return min_dist;
            }
            return dist;
        }

        // set (i, j) cell as visited
        visited[i][j] = 1;

        // go to bottom cell
        if (isValid(i + 1, j) && isSafe(mat, visited, i + 1, j)) {
            WayInfo newDist = dist.clone();
            newDist.minDist += 1;
            min_dist = findShortestPath(mat, visited, i + 1, j, x, y, min_dist, newDist);
        }

        // go to right cell
        if (isValid(i, j + 1) && isSafe(mat, visited, i, j + 1)) {
            WayInfo newDist = dist.clone();
            newDist.minDist += 1;
            min_dist = findShortestPath(mat, visited, i, j + 1, x, y, min_dist, newDist);
        }

        // go to top cell
        if (isValid(i - 1, j) && isSafe(mat, visited, i - 1, j)) {
            WayInfo newDist = dist.clone();
            newDist.minDist += 1;
            min_dist = findShortestPath(mat, visited, i - 1, j, x, y, min_dist, newDist);
        }

        // go to left cell
        if (isValid(i, j - 1) && isSafe(mat, visited, i, j - 1)) {
            WayInfo newDist = dist.clone();
            newDist.minDist += 1;
            min_dist = findShortestPath(mat, visited, i, j - 1, x, y, min_dist, newDist);
        }

        // Backtrack - Remove (i, j) from visited matrix
        visited[i][j] = 0;

        return min_dist;
    }


}

class WayInfo implements Cloneable
{
    public boolean pathFound = false;
    public int minDist;
    public ArrayList<int[]> way;

    public WayInfo clone() throws CloneNotSupportedException
    {
        return (WayInfo)super.clone();
    }
}
