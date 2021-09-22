import java.awt.event.*;
import javax.swing.*;

public class SelectButton extends JButton{
  State state;
  public SelectButton(State state){
  //super(Select);
    addActionListener(new SelectListener());
            this.state = state;
          }

    class SelectListener implements ActionListener{
      public void actionPerformed(ActionEvent e){
        state.setState(State.SELECT);
    }
  }
}
