import java.awt.*;
import javax.swing.*;

public class Canvas extends JPanel{
  private Circle circle;

  public Canvas(Circle circle){
    this.circle = circle;
  }

  public void paint(Graphics graphics){
    super.paint(graphics);
    graphics.fillOval(0,0,circle,getSize(),circle.getSize());
  }
}
