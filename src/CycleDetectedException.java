/**
 * *******************************************************************
 * File: CycleDetectedException.java
 * *********************************************************************
 */
class CycleDetectedException extends RuntimeException {
   public CycleDetectedException(String msg){
      super(msg);
   }
}
