/**
 * *******************************************************************
 * File: TestRunner.java
 * *********************************************************************
 */
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
   public static void main(String[] args) {

      // + - * / for positive numbers
      Result result = JUnitCore.runClasses(RPNEvaluationTestCases.class);
      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }
      String testResult = result.wasSuccessful()?"Successful":"Failed";
      System.out.println("Test result for + - * / operators for positive numbers only : " + testResult);

      result = JUnitCore.runClasses(RPNEvaluationNegativeNumberTestCases.class);
      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }
      testResult = result.wasSuccessful()?"Successful":"Failed";
      System.out.println("Test result for + - * / operators for positive and negative numbers : " + testResult);

      result = JUnitCore.runClasses(RPNEvaluationIncDecrTestCases.class);
      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }
      testResult = result.wasSuccessful()?"Successful":"Failed";
      System.out.println("Test result for ++ and -- operators for positive and negative numbers : " + testResult);

      result = JUnitCore.runClasses(DfsTestCases.class);
      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }
      testResult = result.wasSuccessful()?"Successful":"Failed";
      System.out.println("Test result for entire spreadsheet evaluation : " + testResult);
   }
}
