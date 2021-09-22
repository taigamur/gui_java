import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Vector;
import java.awt.Color;
import javax.swing.JColorChooser;
import java.io.*;

public class MyApplication extends JFrame{
  State state;
  MyCanvas canvas;
  Mediator mediator;
  Shape shape;
  MyDrawing selectedDrawing;
  JComboBox comboBox,comboBox2,comboBox3,comboBox4,comboBox5,comboBox6,comboBox7;
  String color = "white";
  MyRectangle regionrectangle;
  int flag = 0;



  public MyApplication(){
    super("MyPaintApplication");
    JPanel jp = new JPanel();
    JPanel jp2 = new JPanel();
    jp.setLayout(new FlowLayout());
    jp2.setLayout(new FlowLayout());
    state = new State();
    // RectButton rectButton = new RectButton(state);
    // jp.add(rectButton);
    OvalButton ovalButton = new OvalButton(state);
    jp.add(ovalButton);
    Button selectButton = new Button("Select");
    selectButton.addActionListener(new SelectButtonListener());
    jp.add(selectButton);
    //画像用のボタンを作成
    Button imageButton = new Button("image");
    imageButton.addActionListener(new ImageButtonListener());
    jp.add(imageButton);


    String[] colorlist = {"fillcolor","spoit","white","black","red","blue","green","yellow","OtherColors"};
    comboBox = new JComboBox<String>(colorlist);
    comboBox.addActionListener(new ColorSelectListener());
    jp.add(comboBox);
    String[] colorlist2 = {"linecolor","white","black","red","blue","green","yellow","OtherColors"};
    comboBox2 = new JComboBox<String>(colorlist2);
    comboBox2.addActionListener(new LineColorSelectListener());
    jp.add(comboBox2);
    String[] line = {"lineWidth","1","2","3"};
    comboBox3 = new JComboBox<String>(line);
    comboBox3.addActionListener(new LineWidthListener());
    jp.add(comboBox3);
    String[] alpha = {"透明度","1","0.9","0.8","0.7","0.6","0.5","0.4","0.3","0.2","0.1"};
    comboBox5 = new JComboBox<String>(alpha);
    comboBox5.addActionListener(new AlphaListener());
    jp.add(comboBox5);
    String[] option = {"option","cut","copy","paste","delete"};
    comboBox4 = new JComboBox<String>(option);
    jp.add(comboBox4);


    JCheckBox shadow = new JCheckBox("shadow");
    shadow.addItemListener(new ShadowListener());
    jp.add(shadow);
    Button inputButton = new Button("load");
    inputButton.addActionListener(new inputButtonListener());
    jp.add(inputButton);
    Button outputButton = new Button("save");
    outputButton.addActionListener(new outputButtonListener());
    jp.add(outputButton);
    String[] s = {"snap","40","60","80","100"};
    comboBox7 = new JComboBox<String>(s);
    comboBox7.addActionListener(new SnapListener());
    jp.add(comboBox7);

    canvas = new MyCanvas();
    mediator = canvas.getMediator();
    canvas.setBackground(Color.white);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(jp,BorderLayout.NORTH);
    getContentPane().add(canvas,BorderLayout.CENTER);

    canvas.addMouseListener(new MouseAdapter(){

      ///////////////////////////////////////////////////////////////////////////
      public void mousePressed(MouseEvent e){
        mediator.Change();
        if(state.getState() == State.RECTANGLE){
          mediator.selectedDrawing.clear();
          mediator.clearDrawings();
          MyRectangle rectangle = new MyRectangle(e.getX(),e.getY());
          mediator.addDrawing(rectangle);
        }else if(state.getState() == State.OVAL){
          mediator.selectedDrawing.clear();
          mediator.clearDrawings();
          MyDrawing oval = new MyOval(e.getX(),e.getY());
          mediator.addDrawing(oval);
        }else if(state.getState() == State.IMAGE){
          //アクションリスなで処理する
        }else if(state.getState() == State.SPOIT){
          try{
            Robot robot = new Robot();
            Point s = MouseInfo.getPointerInfo().getLocation();
            Color spoit = robot.getPixelColor((int)s.getX(),(int)s.getY());
            for(int i = 0;i < mediator.selectedDrawing.size();i++){
              mediator.selectedDrawing.get(i).setFillColor(spoit);
            }
            mediator.repaint();
          }catch(Exception ea){
            System.out.println(ea);
          }
        }
        else if(state.getState() == State.SELECT){
          mediator.setJudge(e.getX(),e.getY());
          flag = mediator.selectedState(e.getX(),e.getY());
          if(flag == 1){
            //図形のリサイズは選択図形が１つの場合に限る
            if(mediator.selectedDrawing.size() == 1){
              if(mediator.getJudge() >= 0 && mediator.getJudge() <= 7){
                flag = 4;
              }
            }
            String option = (String)comboBox4.getSelectedItem();
            if(option == "cut"){
              mediator.cut();
            }else if(option == "copy"){
              mediator.copy();
            }else if(option == "delete"){
              mediator.delete();
            }
            mediator.initialLocation(e.getX(),e.getY());

          }else if(flag == 2){

            mediator.selectedDrawing.clear();
            mediator.setSelected(e.getX(),e.getY());
            mediator.initialLocation(e.getX(),e.getY());
          }
          else if(flag == 3){
            if(mediator.getJudge() >= 0 && mediator.getJudge() <= 7){

              mediator.setJudge(e.getX(),e.getY());
              mediator.selectedDrawing.get(0).setSelected(true);
              flag = 4;
            }else{
              if(comboBox4.getSelectedItem() == "paste"){
                mediator.paste(e.getX(),e.getY());
              }
              mediator.selectedDrawing.clear();
              mediator.setSelected(e.getX(),e.getY());
              regionrectangle = new MyRectangle(e.getX(),e.getY());
              regionrectangle.setSize(0,0);
              regionrectangle.setDashed(true);
              mediator.addRegionDrawing(regionrectangle);
            }
          }
        }

        mediator.repaint();



      }
      public void mouseReleased(MouseEvent e){
        if(mediator.selectRectangle.size() != 0){
          mediator.selectRectangle.clear();
          mediator.addSelectedDrawing();
        }
        if(getState() != 3){
          if(mediator.drawings.size() != 0){
            MyDrawing t = mediator.drawings.get(mediator.drawings.size()-1);
            t.snap(t.getX(),t.getY(),mediator.getGridWidth());
          }
        }
        mediator.repaint();
      }
    });

    canvas.addMouseMotionListener(new MouseMotionAdapter(){
      public void mouseDragged(MouseEvent e){
        if(state.getState() != 3 && state.getState() != 5){//スポイトツールモードの時は何もしない
          mediator.MouseDragged(e.getX(),e.getY());
        }
        if(state.getState() == 3){
          if(flag == 4){
            mediator.resize(e.getX(),e.getY());
          }else if(flag == 2 || flag == 1){

            mediator.Change();
            mediator.move(e.getX() , e.getY());
          }else if(flag == 3){
            int x = mediator.selectRectangle.get(0).getX();
            int y = mediator.selectRectangle.get(0).getY();
            mediator.selectRectangle.get(0).setSize(e.getX() - x, e.getY() - y);
            mediator.setMultiSelected(e.getX(),e.getY());
            mediator.repaint();
          }
        }
      }
      public void mouseMoved(MouseEvent e){

        if(mediator.CursorJudge(e.getX(),e.getY())){
            setCursor(new Cursor(Cursor.HAND_CURSOR));
          }else{
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
          }

      }
    });


  }





  public Dimension getPreferredSize(){
    return new Dimension(1200,1000);
  }
  public static void main(String[] args){
    MyApplication app = new MyApplication();
    app.pack();
    app.setVisible(true);


  }
  class SelectButtonListener implements ActionListener{
    public void actionPerformed(ActionEvent e){
      state.setState(3);
    }
  }

  class ColorSelectListener implements ActionListener{
    public void actionPerformed(ActionEvent e){
      String color = (String)comboBox.getSelectedItem();
      if(color == "fillColor"){
        mediator.setColor(Color.white);
      }
      else if(color == "spoit"){
        state.setState(5);
      }
      else if(color == "white"){
        mediator.setColor(Color.white);
      }else if(color == "black"){
        mediator.setColor(Color.black);
      }else if(color == "blue"){
        mediator.setColor(Color.blue);
      }else if(color == "green"){
        mediator.setColor(Color.green);
      }else if(color == "yellow"){
        mediator.setColor(Color.yellow);
      }else if(color == "red"){
        mediator.setColor(Color.red);
      }else if(color == "OtherColors"){
        Color selectedColor = JColorChooser.showDialog(null,"色を選択",Color.black);
        if(selectedColor == null){
          System.out.println("選択されませんでした");
        }else{
          mediator.setColor(selectedColor);
        }
      }
      mediator.Change();
    }
  }
  class LineColorSelectListener implements ActionListener{
    public void actionPerformed(ActionEvent e){
      String color = (String)comboBox2.getSelectedItem();

      if(color == "white"){
        mediator.setlineColor(Color.white);
      }else if(color == "black"){
        mediator.setlineColor(Color.black);
      }else if(color == "blue"){
        mediator.setlineColor(Color.blue);
      }else if(color == "green"){
        mediator.setlineColor(Color.green);
      }else if(color == "yellow"){
        mediator.setlineColor(Color.yellow);
      }else if(color == "red"){
        mediator.setlineColor(Color.red);
      }else if(color == "OtherColors"){
        Color selectedColor = JColorChooser.showDialog(null,"色を選択",Color.black);
        if(selectedColor == null){
          System.out.println("選択されませんでした");
        }else{
          mediator.setColor(selectedColor);
        }
      }
      mediator.Change();
    }
  }
  class LineWidthListener implements ActionListener{
    public void actionPerformed(ActionEvent e){
      String linewidth = (String)comboBox3.getSelectedItem();
      if(linewidth == "1"){
        mediator.setLineWidth(1);
      }else if(linewidth == "2"){
        mediator.setLineWidth(2);
      }else{
        mediator.setLineWidth(3);
      }
      mediator.Change();
    }
  }
  class ShadowListener implements ItemListener{
    public void itemStateChanged(ItemEvent e){
      int state = e.getStateChange();
      if(state == ItemEvent.SELECTED){
        mediator.setShadow(true);
      }else{
        mediator.setShadow(false);
      }
      mediator.Change();
    }
  }

  class inputButtonListener implements ActionListener{

    @SuppressWarnings("unchecked")
    //ロードの処理
    public void actionPerformed(ActionEvent e){
      try{
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          File file = fc.getSelectedFile();
          FileInputStream fin = new FileInputStream(file);
          ObjectInputStream in = new ObjectInputStream(fin);
          mediator.drawings = (Vector<MyDrawing>)in.readObject();
          fin.close();
        }
      }catch(Exception ex){
        System.out.println(ex);
      }
      mediator.repaint();
    }
  }

  class outputButtonListener implements ActionListener{
    public void actionPerformed(ActionEvent e){
      //セーブの処理
      try {
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          File file = fc.getSelectedFile();
          FileOutputStream fout = new FileOutputStream(file);
          ObjectOutputStream out = new ObjectOutputStream(fout);
          out.writeObject(mediator.drawings);
          out.flush();
          fout.close();
        }
      } catch (Exception ex) {
        System.out.println(ex);
      }
    }
  }

  class AlphaListener implements ActionListener{
    public void actionPerformed(ActionEvent e){
      String alphaValue = (String)comboBox5.getSelectedItem();
      if(alphaValue == "1" || alphaValue == "transparency"){
        mediator.setAlpha(1.0f);
      }else if(alphaValue == "0.9"){
        mediator.setAlpha(0.9f);
      }else if(alphaValue == "0.8"){
        mediator.setAlpha(0.8f);
      }else if(alphaValue == "0.7"){
        mediator.setAlpha(0.7f);
      }else if(alphaValue == "0.6"){
        mediator.setAlpha(0.6f);
      }else if(alphaValue == "0.5"){
        mediator.setAlpha(0.5f);
      }else if(alphaValue == "0.4"){
        mediator.setAlpha(0.4f);
      }else if(alphaValue == "0.3"){
        mediator.setAlpha(0.3f);
      }else if(alphaValue == "0.2"){
        mediator.setAlpha(0.2f);
      }else if(alphaValue == "0.1"){
        mediator.setAlpha(0.1f);
      }
      mediator.Change();
    }
  }
  class ImageButtonListener implements ActionListener{
    public void actionPerformed(ActionEvent e){
      state.setState(4);
      mediator.selectedDrawing.clear();
      mediator.clearDrawings();
      MyImage image = new MyImage(0,0);
      mediator.addDrawing(image);
      mediator.repaint();
    }
  }
  class SnapListener implements ActionListener{
    public void actionPerformed(ActionEvent e){
      String s = (String)comboBox7.getSelectedItem();
      if(s == "snap"){
        canvas.setGridState(false);
        mediator.setGridWidth(0);
      }else{
        canvas.setGridState(true);
        int g = Integer.parseInt(s);
        canvas.setGrid(g);
        mediator.setGridWidth(g);
      }
      mediator.repaint();
    }
  }




}
