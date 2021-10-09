import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

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
