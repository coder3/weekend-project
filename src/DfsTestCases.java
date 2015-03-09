/**
 * *******************************************************************
 * File: TestCases.java
 * *********************************************************************
 */

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DfsTestCases {

   Spreadsheet spreadsheet;

   @Test
   public void valueCheck1() {
      String input[][] = readFromfile("input.txt");
      spreadsheet = new Spreadsheet(input);
      spreadsheet.evaluateSpreadSheet();
      assertEquals("20.00000",input[0][0]);
      assertEquals("20.00000",input[0][1]);
      assertEquals("20.00000",input[0][2]);
      assertEquals("8.66667",input[1][0]);
      assertEquals("3.00000",input[1][1]);
      assertEquals("1.50000",input[1][2]);
   }

   //One of the lines(B2) in the input is empty which
   // should be evaluated to 0.00000
   @Test
   public void inputEmptyLine() {
      String input[][] = readFromfile("inputEmptyLine.txt");
      spreadsheet = new Spreadsheet(input);
      spreadsheet.evaluateSpreadSheet();
      assertEquals("20.00000",input[0][0]);
      assertEquals("20.00000",input[0][1]);
      assertEquals("0.00000",input[0][2]);
      assertEquals("3.00000",input[1][0]);
      assertEquals("0.00000",input[1][1]);
      assertEquals("3.00000",input[1][2]);
   }

   @Test
   public void lastCellValue() {
      String input[][] = readFromfile("lastCellValue.txt");
      spreadsheet = new Spreadsheet(input);
      spreadsheet.evaluateSpreadSheet();
      assertEquals("39.00000",input[0][0]);
      assertEquals("39.00000",input[0][1]);
      assertEquals("39.00000",input[0][2]);
      assertEquals("39.00000",input[1][0]);
      assertEquals("39.00000",input[1][1]);
      assertEquals("39.00000",input[1][2]);
   }

   @Test
   public void centerCellValue() {
      String input[][] = readFromfile("centerCellValue.txt");
      spreadsheet = new Spreadsheet(input);
      spreadsheet.evaluateSpreadSheet();
      assertEquals("39.00000",input[0][0]);
      assertEquals("-450.00000",input[0][1]);
      assertEquals("45.00000",input[0][2]);
      assertEquals("40.00000",input[1][0]);
      assertEquals("39.00000",input[1][1]);
      assertEquals("38.00000",input[1][2]);
      assertEquals("35.00000",input[2][0]);
      assertEquals("3.50000",input[2][1]);
      assertEquals("39.00000",input[2][2]);
   }

   //A3->A2->B2
   //A3->B1
   //A2->A1->B1
   //A1->B2
   //A2->B2
   //B1->B2
   @Test
   public void nonCycleTest() {
      String input[][] = readFromfile("nonCycleTest.txt");
      spreadsheet = new Spreadsheet(input);
      spreadsheet.evaluateSpreadSheet();
      assertEquals("6.00000",input[0][0]);
      assertEquals("9.00000",input[0][1]);
      assertEquals("12.00000",input[0][2]);
      assertEquals("3.00000",input[1][0]);
      assertEquals("1.00000",input[1][1]);
      assertEquals("15.00000",input[1][2]);
   }

   //A3->A2->B2
   //A3->B1
   //A2->A1->B1 -- cycle right here at A1
   //A1->B2
   //A2->B2
   //B1->A2 -- cycle right here
   @Test
   public void cycleTest() {
      String input[][] = readFromfile("cycleTest.txt");
      try{
         spreadsheet = new Spreadsheet(input);
         spreadsheet.evaluateSpreadSheet();
         fail("Expected cycle at cell A1");
      } catch (CycleDetectedException e){
         String msg = "Cycle detected at cell A1";
         assertEquals(msg, e.getMessage());
      }
   }

   @Test
   public void invalidReference() {
      String input[][] = readFromfile("invalidReference.txt");
      try{
         spreadsheet = new Spreadsheet(input);
         spreadsheet.evaluateSpreadSheet();
         fail("Invalid reference to cell B4 at cell B1");
      } catch (InvalidRPNException e){
         String msg = "The expression " + input[1][0] + " at cell B1 is not valid";
         assertEquals(msg, e.getMessage());
      }
   }

   @Test
   public void invalidReferenceLB() {
      String input[][] = readFromfile("invalidReferenceLB.txt");
      try{
         spreadsheet = new Spreadsheet(input);
         spreadsheet.evaluateSpreadSheet();
         fail("Invalid reference to cell B0 at cell B1");
      } catch (InvalidRPNException e){
         String msg = "The expression " + input[1][0] + " at cell B1 is not valid";
         assertEquals(msg, e.getMessage());
      }
   }



   /*
   ****************************************************************************************************************************
   Large size spreadsheet with one row and many many columns
   Ex - If the number of cells = 5, Spreadsheet has 1 row and 5 columns i.e there are 5 cells A1,A2,A3,A4,A5

   The test input in which case will be as follows

   Input
   A2 | A3| A4 | A5 | 33

   Output

   33.00000 | 33.00000 | 33.00000 | 33.00000 | 33.00000

   This test case will need a single DFS traversing the entire spreadsheet at once

   ******************************************************************************************************************************
   */
   @Test
   public void largeSpreadSheet() throws IOException{
      System.out.println("Spread sheet size  = 100");
      long processorCount = Runtime.getRuntime().availableProcessors();
      System.out.println("Number of processors  = " + processorCount);
      long heapSize = Runtime.getRuntime().totalMemory();
      System.out.println("Heap size at start in MB  = " + heapSize/(1024*1024));
      long freeMemory = Runtime.getRuntime().freeMemory();
      System.out.println("Free heap size at start in MB  = " + freeMemory/(1024*1024));
      long usedMemory = heapSize - freeMemory ;
      System.out.println("Used heap size at start in MB  = " + usedMemory/(1024*1024));
      long maxMemory = Runtime.getRuntime().maxMemory();
      System.out.println("Max heap size at start in MB  = " + maxMemory/(1024*1024));

      writeToFile("largeSpreadsheet.txt");

      long startTime = System.nanoTime();
      String input[][] = readFromfile("largeSpreadsheet.txt");

      spreadsheet = new Spreadsheet(input);
      spreadsheet.evaluateSpreadSheet();

      long endTime = System.nanoTime();
      heapSize = Runtime.getRuntime().totalMemory();
      System.out.println("Heap size at end in MB  = " + heapSize/(1024*1024));
      freeMemory = Runtime.getRuntime().freeMemory();
      System.out.println("Free heap size at end in MB  = " + freeMemory/(1024*1024));
      usedMemory = heapSize - freeMemory ;
      System.out.println("Used heap size at end in MB  = " + usedMemory/(1024*1024));
      maxMemory = Runtime.getRuntime().maxMemory();
      System.out.println("Max heap size at end in MB  = " + maxMemory/(1024*1024));

      System.out.println("Time(in ms) to read from file and evaluate spreadsheet  = " + (endTime-startTime)/(1000*1000));

      for(int j = 0; j < 100 ; j++){
         assertEquals("33.00000",input[0][j]);
      }

   }

   private String[][] readFromfile(String fileName){
      BufferedReader br = null;
      try {
         br = new BufferedReader(new FileReader(fileName));
         // Read the first line and get spreadsheet/array dimensions
         try{
            String dimensions = br.readLine();
            if(dimensions != null){
               String[] dimArray = dimensions.split("\\s");
               if(dimArray.length < 2){
                  System.out.println("Please provide at least 2 dimensions in the input file " + fileName);
                  System.exit(2);
               }
               try{
                  int N = Integer.parseInt(dimArray[0]);
                  int M = Integer.parseInt(dimArray[1]);
                  String[][] input = new String[M][N];
                  for(byte i = 0; i < M; i++){
                     for(int j = 0; j < N ; j++){
                        input[i][j] = br.readLine();
                     }
                  }
                  return input;
               } catch (NumberFormatException e){
                  System.out.println("Please provide valid numerical dimensions in the input file " + fileName);
                  System.exit(1);
               }
            }
         } catch (IOException e){
            System.out.println("IO Exception while reading from file " + fileName + " due to " + e.getMessage());
            System.exit(1);
         }
      }  catch(IOException e){
         System.out.println("IO Exception while trying to open file " + fileName + " due to " + e.getMessage());
         System.exit(1);
      }
      finally {
         try{
            if(br != null){
               br.close();
            }
         }
         catch(IOException e){

         }
      }
      return null;
   }

   private void writeToFile(String fileName) throws IOException {
      PrintWriter writer = null;
      try{
         writer = new PrintWriter(fileName, "UTF-8");
         writer.println("100 1");
         for(int i= 1 ; i < 100 ; i++){
            writer.println("A" + (i+1));
         }
         writer.println("33");

      } finally {
         if(writer != null){
            writer.close();
         }
      }
   }
}
