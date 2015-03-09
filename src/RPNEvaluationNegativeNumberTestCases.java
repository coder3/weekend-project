/**
 * *******************************************************************
 * File: TestCases.java
 * *********************************************************************
 */

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class RPNEvaluationNegativeNumberTestCases {

   // Positive numbers test cases

   @Test
   public void valueCheck1() {
      String input[][] = new String[1][1];
      input[0][0] = "23.3 5 16.2 + -8 * -";
      Spreadsheet spreadsheet = new Spreadsheet(input);
      spreadsheet.evaluateSpreadSheet();
      assertEquals(String.format("%.5f", 192.9),input[0][0]);
   }

   @Test
   public void valueCheck2() {
      String input[][] = new String[1][1];
      input[0][0] = "2 3 / 3 -2 / *";
      Spreadsheet spreadsheet = new Spreadsheet(input);
      spreadsheet.evaluateSpreadSheet();
      assertEquals(String.format("%.5f",-1.0),input[0][0]);
   }

   @Test
   public void rpnUnderflowExceptionTest() {
      String input[][] = new String[1][1];
      input[0][0] = "-12 * 3 -15 18.723 - + -52 /";
      Spreadsheet spreadsheet = new Spreadsheet(input);
      try{
         spreadsheet.evaluateSpreadSheet();
         fail("Expecting RPNUnderflowException");
      } catch (RPNUnderflowException e){
         String msg = "Operator * occured before there were enough numerical values for it to evaluate in " +
                 "the expression " + input[0][0] + " at cell A1";
         assertEquals(msg, e.getMessage());
      }
   }

   @Test
   public void rpnOverflowExceptionTest() {
      String input[][] = new String[1][1];
      input[0][0] = "12.2 -17 / -33.333 - 12";
      Spreadsheet spreadsheet = new Spreadsheet(input);
      try{
         spreadsheet.evaluateSpreadSheet();
         fail("Expecting RPNOverflowException");
      } catch (RPNOverflowException e){
         String msg = "Extra Operands in the expression " + input[0][0] + " at cell A1";
         assertEquals(msg, e.getMessage());
      }
   }

   @Test
   public void rpnDivideByZeroExceptionTest() {
      String input[][] = new String[1][1];
      input[0][0] = "-52.2 12 + -17 - 9.7 10 0 / + -";
      Spreadsheet spreadsheet = new Spreadsheet(input);
      try{
         spreadsheet.evaluateSpreadSheet();
         fail("Expecting RPNDivideByZeroException");
      } catch (RPNDivideByZeroException e){
         String msg = "The RPN equation " + input[0][0] +" at cell A1 attempted to divide by zero";
         assertEquals(msg, e.getMessage());
      }
   }
}
