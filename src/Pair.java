/**
 * *******************************************************************
 * File: Pair.java
 * *********************************************************************
 */
class Pair {
   byte row;
   int column;
   public Pair(byte row, int column){
      this.row = row;
      this.column = column;
   }

   @Override
   public boolean equals(Object other){
      if(this == other){
         return true;
      }
      if(other == null || this.getClass() != other.getClass()){
         return false;
      }
      Pair otherPair = (Pair)other;
      return this.row == otherPair.row && this.column == otherPair.column;
   }

   @Override
   public int hashCode(){
      return (row ^ column);
   }

   public byte getRow() {
      return row;
   }

   public int getColumn() {
      return column;
   }
}
