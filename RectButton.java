import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class RectButton extends JButton{
  State state;
  public RectButton(State state){
    super("Rectangle");

    addActionListener(new RectListener());
    this.state = state;
  }

  class RectListener implements ActionListener{
    public void actionPerformed(ActionEvent e){
      state.setState(State.RECTANGLE);
    }
  }
}
