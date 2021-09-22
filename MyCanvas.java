import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.JPanel;

public class MyCanvas extends JPanel{
  Mediator mediator;
  int gWidth = 100;
  boolean isGrid = false;


  public MyCanvas(){
    this.mediator = new Mediator(this);
    setBackground(Color.white);
  }

  public Mediator getMediator(){
    return mediator;
  }
  public void setGrid(int g){
    gWidth = g;
  }
  public boolean getGridState(){
    return isGrid;
  }
  public void setGridState(boolean s){
    isGrid = s;
  }

  public void paint(Graphics g){
    super.paint(g);
    Graphics2D g2 = (Graphics2D) g;
    g2.setStroke(new MyDashStroke(0.01f));
    if(getGridState()){
      g.setColor(Color.lightGray);
      for(int x = 0;x < getWidth();x +=gWidth){
        g.drawLine(x,0,x,getHeight());
      }
      for(int y = 0;y<getHeight();y+=gWidth){
        g2.drawLine(0,y,getWidth(),y);
      }
    }

    Enumeration<MyDrawing> e = mediator.drawingElements();
    while(e.hasMoreElements()){
      MyDrawing d = e.nextElement();
      d.draw(g);

    }
    Enumeration<MyDrawing> a = mediator.selectRectangleElements();
    while(a.hasMoreElements()){
      MyDrawing d = a.nextElement();
      d.draw(g);
    }
  }
}
