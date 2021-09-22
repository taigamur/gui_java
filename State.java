public class State{
  public static final int RECTANGLE = 1;
  public static final int OVAL = 2;
  public static final int SELECT = 3;
  public static final int IMAGE = 4;
  public static final int SPOIT = 5;

  public int currentState;

  public State(){
    currentState = RECTANGLE;
  }
  public void setState(int state){
    currentState = state;
  }

  public int getState(){
    return currentState;
  }
}
