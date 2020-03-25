package sample;

import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.io.IOException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

public class Controller {
//    private static String[][] getMap() {
//        String[][] mapData = new String[100][100];
//        try {
//            File myObj = new File("Insule.in");
//            Scanner myReader = new Scanner(myObj);
//            int i = 0;
//            while (myReader.hasNextLine()) {
//                int j = 0;
//                String data = myReader.nextLine();
//                String[] arrayData = data.split(" ");
//                for (String s : arrayData) {
//                    mapData[i][j] = s;
//                    System.out.print(s + " ");
//                    j++;
//                }
//                i++;
//
//                // char[] ch = data.toCharArray();
//                // // Printing array
//                // for (char c : ch) {
//                // 	System.out.print(c);
//                // }
//                System.out.println();
//                // System.out.println(data);
//                // System.out.println(data.getClass().getName());
//            }
//            myReader.close();
//        } catch (FileNotFoundException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }
//        return mapData;
//    }
//
//    public static void write (String filename, String[][]mapData) throws IOException{
//        try {
//            File myObj = new File(filename);
//            if (myObj.createNewFile()) {
//                System.out.println("File created: " + myObj.getName());
//            } else {
//                System.out.println("File already exists.");
//            }
//        } catch (IOException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }
//    }

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
