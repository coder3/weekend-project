import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;

/**
 * ******************************************************************************
 * File: CellExpressionEvaluator.java
 * This class contains the methods for evaluating the expression for a given cell
 * ******************************************************************************
 */
class CellExpressionEvaluator {

   private static final String ADDITION = "+";
   private static final String SUBTRACT = "-";
   private static final String MULTIPLY = "*";
   private static final String DIVIDE = "/";
   private static final String INCREMENT = "++";
   private static final String DECREMENT = "--";

   /**
    * ********************************************************
    * Constructor is made private to prevent instantiation
    * ********************************************************
    */
   private CellExpressionEvaluator(){

   }

   /**
    * *****************************************************************************
    * Checks if the input String is one of the supported operators and it requires
    * two operands
    * @param input
    *         The String which needs to be tested
    * @return true if the input String is one of the supported operators and it
    *         requires two operands
    * *****************************************************************************
    */
   private static boolean isDoubleOperandOperator(String input) {
      return (input.equals(ADDITION) || input.equals(SUBTRACT) || input.equals(MULTIPLY) || input
              .equals(DIVIDE));
   }

   /**
    * **************************************************************************
    * Checks if the input String is one of the supported operators and it requires
    * only one operand
    * @param input
    *         The String which needs to be tested
    * @return true if the input String is one of the supported operators and it
    *         requires only one operands
    * ***************************************************************************
    */
   private static boolean isSingleOperandOperator(String input) {
      return (input.equals(INCREMENT) || input.equals(DECREMENT));
   }

   /**
    * ****************************************************************************
    * Evaluates the RPN expression for a given cell ( along with all the other cells
    * which it depends on) in the spreadsheet specified by the row and column
    * in the pair
    * Logic : Try to evaluate the RPN expression in the cell specified by the start pair.
    *         If the cell references other cells, then the other cells are traversed first
    *         using the Depth First Search pattern. A Stack data structure is used to
    *         carry out DFS instead of recursion to avoid stack overflow issues since
    *         the spreadsheet could be very large
    *         If an RPN expression is empty ("") or null, then it is evaluated to be 0.00000
    * @param start
    *         The row and column attributes in the start pair  specify the cell for which
    *         the expression needs to be evaluated
    * @param spreadsheet
    *         Associated spreadsheet object which contains information about the
    *         values/expressions for all the cells and also the associated visit
    *         status
    *         Two dimensional array representing the spreadsheet
    * @throws EmptyRPNException
    *             when the RPN equation just has spaces.
    * @throws RPNDivideByZeroException
    *             when the RPN equation attempts to divide by zero.
    * @throws RPNUnderflowException
    *             when the RPN equation has a mathematical operator before
    *             there are enough numerical values for it to evaluate.
    * @throws InvalidRPNException
    *             when the RPN equation is a String which is unable to be
    *             manipulated.
    * @throws RPNOverflowException
    *             when the RPN equation has too many numerical values and not
    *             enough mathematical operators with which to evaluate them.
    * ***************************************************************************
    */
   public static void calcRPN(Pair start,Spreadsheet spreadsheet) {

      String[][] cells = spreadsheet.getCells();
      Color[][] visitStatus = spreadsheet.getVisitStatus();

      // Stack to carry on recursion iteratively
      Deque<Pair> dfsStack = new ArrayDeque<Pair>();
      dfsStack.push(start);
      Pair currentPair;
      String errorMessage;

      stackLoop:
      while(!dfsStack.isEmpty()){
         currentPair = dfsStack.peek();
         // set status to GREY (visiting)
         visitStatus[currentPair.getRow()][currentPair.getColumn()] = Color.GREY;
         String inputCell = cells[currentPair.getRow()][currentPair.getColumn()];
         // Eliminate any leading or trailing whitespace from input
         if(inputCell == null || inputCell.isEmpty()){
            cells[currentPair.getRow()][currentPair.getColumn()] = String.format("%.5f",0.0);
            visitStatus[currentPair.getRow()][currentPair.getColumn()] = Color.BLACK;
            //pop the current pair which has already been evaluated
            dfsStack.pop();
            continue;
         }

         inputCell = inputCell.trim();
         if(inputCell.isEmpty()){
            throw new EmptyRPNException("Illegal Argument " + cells[currentPair.getRow()][currentPair.getColumn()] + " at cell " + getCellName(currentPair.getRow(),currentPair.getColumn()));
         }

         // scanner to manipulate input and stack to store double values
         String next;
         Deque<Double> operandStack = new ArrayDeque<Double>();
         Scanner scan = new Scanner(inputCell);

         // loop while there are tokens left in scan
         while (scan.hasNext()) {
            // retrieve the next token from the input
            next = scan.next();

            // check if the token is a mathematical operator
            if (isDoubleOperandOperator(next)) {
               // ensure there are enough operands on stack
               if (operandStack.size() > 1) {
                  if (next.equals(ADDITION)) {
                     operandStack.push( operandStack.pop() + operandStack.pop());
                  } else if (next.equals(SUBTRACT)) {
                     operandStack.push( - operandStack.pop() +  operandStack.pop());
                  } else if (next.equals(MULTIPLY)) {
                     operandStack.push( operandStack.pop() *  operandStack.pop());
                  } else if (next.equals(DIVIDE)) {
                     double first = operandStack.pop();
                     double second = operandStack.pop();

                     if (first == 0) {
                        errorMessage = "The RPN equation " + cells[currentPair.getRow()][currentPair.getColumn()] +" at " +
                                "cell " +getCellName(currentPair.getRow(),currentPair.getColumn()) + " attempted to divide by zero";
                        throw new RPNDivideByZeroException(errorMessage);
                     } else {
                        operandStack.push(second / first);
                     }
                  }
               } else {
                  errorMessage = "Operator " + next + " occured before there were enough numerical values for it to evaluate in " +
                          "the expression " + cells[currentPair.getRow()][currentPair.getColumn()] + " at cell "
                          + getCellName(currentPair.getRow(),currentPair.getColumn());
                  throw new RPNUnderflowException(errorMessage);
               }
            } else if(isSingleOperandOperator(next)){
               // ensure there are enough numbers on stack
               if (operandStack.size() > 0) {
                  if (next.equals(INCREMENT)) {
                     operandStack.push( operandStack.pop() + 1);
                  } else if (next.equals(DECREMENT)) {
                     operandStack.push( operandStack.pop() - 1);
                  }
               } else {
                  errorMessage = "Operator " + next + " occured before there were enough numerical values for it to evaluate in " +
                          "the expression " + cells[currentPair.getRow()][currentPair.getColumn()] + " at cell "
                          + getCellName(currentPair.getRow(),currentPair.getColumn());
                  throw new RPNUnderflowException(errorMessage);
               }
            } else {
               try {
                  operandStack.push(Double.valueOf(next));
               } catch (NumberFormatException c) {
                  //Check if it references other cells
                  Pair newPair = getPairFromCellName(next,cells.length,cells[0].length);
                  if(newPair != null){
                     if(visitStatus[newPair.getRow()][newPair.getColumn()] == Color.GREY){
                        throw new CycleDetectedException("Cycle detected at cell " + getCellName(newPair.getRow(),newPair.getColumn()));
                     } else if(visitStatus[newPair.getRow()][newPair.getColumn()] == Color.BLACK){
                        operandStack.push(Double.valueOf(cells[newPair.getRow()][newPair.getColumn()]));
                     } else {
                        dfsStack.push(newPair);
                        continue stackLoop;
                     }
                  } else {
                     errorMessage = "The expression " + cells[currentPair.getRow()][currentPair.getColumn()] +
                             " at cell " + getCellName(currentPair.getRow(),currentPair.getColumn()) +
                             " is not valid";
                     throw new InvalidRPNException(
                             errorMessage);
                  }
               }
            }
         }
         if (operandStack.size() > 1) {
            errorMessage = "Extra Operands in the expression " + cells[currentPair.getRow()][currentPair.getColumn()] +
                    " at cell " + getCellName(currentPair.getRow(),currentPair.getColumn());
            throw new RPNOverflowException(errorMessage);
         }

         // Set the value evaluated on the cell
         cells[currentPair.getRow()][currentPair.getColumn()] = String.format("%.5f",operandStack.pop());

         // Mark the cell visit status as BLACK (Visited or Evaluated)
         visitStatus[currentPair.getRow()][currentPair.getColumn()] = Color.BLACK;

         //pop the current pair which has already been evaluated
         dfsStack.pop();
      }
   }

   /**
    * ****************************************************************************
    * Converts cell name to Pair
    * Example A1 -> (0,0) , C5 -> (2,4)
    * @param cellName
    *         Name of the cell like A1,A2,B1,B2 etc
    * @param rowCount
    *         Number of rows in the spreadsheet
    * @param colCount
    *         Number of columns in the spreadsheet
    * @return A Pair object corresponding to the cell name
    * ***************************************************************************
    */
   private static Pair getPairFromCellName(String cellName,int rowCount, int colCount){
      byte i;
      int j;
      if(cellName.length() < 2) {
         return null;
      }
      char firstChar = cellName.substring(0,1).charAt(0);
      if(firstChar < 'A' || firstChar > 'Z'){
         return null;
      }
      i = (byte)(firstChar - 'A');
      String colString = cellName.substring(1);
      try{
         j = Integer.parseInt(colString) - 1;
      } catch(NumberFormatException e){
         return null;
      }
      if(i >= rowCount || j >= colCount || i < 0 || j < 0){
         return null;
      }
      return new Pair(i,j);
   }

   /**
    * ****************************************************************************
    * Converts row and column to cell name
    * Example row = 0, column = 3  results into a cell name of A4
    * @param row
    *         row index of the cell starting from 0
    * @param column
    *         column index of the cell starting from 0
    * @return name of the cell (like A1, A2, A3 ..)
    * ***************************************************************************
    */
   private static String getCellName(byte row,int column){
      char rowName = (char)('A' + row);
      return rowName + String.valueOf(column+1);
   }
}
