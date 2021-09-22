import java.awt.event.*;
import javax.swing.*;

public class OvalButton extends JButton{
  State state;
  public OvalButton(State state){
    super("Oval");

    addActionListener(new OvalListener());
    this.state = state;
  }

  class OvalListener implements ActionListener{
    public void actionPerformed(ActionEvent e){
      state.setState(State.OVAL);
    }
  }
}
