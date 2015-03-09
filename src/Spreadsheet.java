import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * *******************************************************************
 * File: Spreadsheet.java
 * Rows A-Z and Columns 1 - N
 * Empty lines and the cells for which the input has not been provided
 * are considered as zeroes (just like spreadsheet)
 */
public class Spreadsheet {

   //Two dimensional array representing the spreadsheet
   private String[][] cells;

   /*
   *         Two dimensional array containing the visit status for each cell of
   *         the spreadsheet
   *         WHITE - The cell has not been visited yet
   *         GREY -  The cell is being visited i.e the adjacent unvisited nodes of the
   *                 current cell are about to be visited or are being visited
   *         BLACK - The cell has already been visited i.e. the expression at the
   *                 current cell has already been evaluated
   */
   private Color[][] visitStatus;

   public Spreadsheet(String[][] cells){
      this.cells = cells;
      this.visitStatus = new Color[cells.length][cells[0].length];
   }

   /*
    ********************************************************************************
    * Reads the input from standard input, creates a spreadsheet object, calls the
    * evaluate method on the spreadsheet and finally prints the evaluated spreadsheet
    * back on to standard output
    *******************************************************************************
    */
   public static void main(String[] args){
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      // Read the first line and get spreadsheet/array dimensions
      try{
         String dimensions = br.readLine();
         if(dimensions != null){
            dimensions = dimensions.trim();
            Scanner scan = new Scanner(dimensions);
            try{
               int N = 0;
               if(scan.hasNext()){
                  N = Integer.parseInt(scan.next());
               } else {
                  System.out.println("Please provide at least 2 dimensions");
                  System.exit(1);
               }
               int M = 0;
               if(scan.hasNext()){
                  M = Integer.parseInt(scan.next());
               } else {
                  System.out.println("Please provide at least 2 dimensions");
                  System.exit(2);
               }
               if(M < 1 || M > 26){
                  System.out.println("Please enter a valid row count : 1 - 26");
                  System.exit(1);
               }
               if(N < 1){
                  System.out.println("Please enter a positive non-zero value for column count");
                  System.exit(1);
               }
               String[][] input = new String[M][N];
               for(byte i = 0; i < M; i++){
                  for(int j = 0; j < N ; j++){
                     input[i][j] = br.readLine();
                  }
               }
               try{
                  Spreadsheet spreadsheet = new Spreadsheet(input);
                  spreadsheet.evaluateSpreadSheet();
                  printArray(dimensions,spreadsheet.getCells());
               }catch(CycleDetectedException e){
                  System.out.println(e.getMessage());
                  System.exit(5);
               }catch(IllegalArgumentException e){
                  System.out.println(e.getMessage());
                  System.exit(4);
               }
            } catch (NumberFormatException e){
               System.out.println("Please provide valid numerical dimensions");
               System.exit(3);
            }
         } else {
            System.out.println("Please provide valid dimensions");
            System.exit(2);
         }
      } catch (IOException e){
         System.out.println("IO Exception while reading from console due to " + e.getMessage());
         System.exit(1);
      }
   }

 /*
  ********************************************************************************
  * Evaluates the current spreadsheet
  * Logic :
  * 1)Mark all the cells in the spreadsheet as WHITE (Unvisited)
  * 2)Loop around the all the cells in the spreadsheet and if it is unvisited(WHITE),
  *   create a Pair object specifying this particular cell and call the calculate
  *   RPN logic for this cell
 *******************************************************************************
 */
   public void evaluateSpreadSheet(){
      for(int i = 0; i < visitStatus.length; i++){
         for(int j = 0; j < visitStatus[0].length; j++){
            visitStatus[i][j] = Color.WHITE;
         }
      }
      for(byte i = 0; i < cells.length; i++){
         for(int j = 0; j < cells[0].length; j++){
            if(visitStatus[i][j] == Color.WHITE){
               Pair pair = new Pair(i,j);
               CellExpressionEvaluator.calcRPN(pair,this);
            }
         }
      }
   }

 /*
 ********************************************************************************
 * Print the spreadsheet along with the dimensions on to standard input
 *******************************************************************************
 */
   private static void printArray(String dimensions, String[][] input){
      System.out.println(dimensions);
      for(int i = 0 ; i < input.length ; i++){
         for(int j = 0; j < input[0].length ; j++){
            System.out.println(input[i][j]);
         }
      }
   }

   public String[][] getCells() {
      return cells;
   }

   public Color[][] getVisitStatus() {
      return visitStatus;
   }
}
