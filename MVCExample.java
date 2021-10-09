import java.awt.*;
import javax.swing.*;

public class MVCExample extends JFrame{
  Canvas canvas;
  Circle circle;
  Button button;

  public MVCExample(){
    circle = new Circle();
    canvas = new Canvas(cirle);
    button = new Button(circle,canvas);

    this.getContentPane().add(button,BorderLayout.CENTER);
    this.getContentPane().add(canvas,BorderLayout.CENTER);
  }
  public static void main(String[] args){
    MVCExample mvcExample = new MVCExample(;
    mvcExample.setDefaultCloseOperation(JFramw.EXIT_ON_CLOSE);
    mvcExample.setBounds(0,0,300,300);
    mvcExample.setVisible(true);
  }
}
