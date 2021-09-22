import java.util.Enumeration;
import java.util.Vector;
import java.awt.Rectangle;
import java.awt.*;
import java.awt.geom.*;
import java.lang.Object;
import java.awt.Color;
import java.awt.Graphics2D;

public class Mediator{
  Vector<MyDrawing> drawings;
  Vector<MyDrawing> selectRectangle;//これは選択図形用の長方形
  MyCanvas canvas;
  Vector<MyDrawing> selectedDrawing;
  //selectの時に作る図形
  MyDrawing regionDrawing = null;
  Vector<MyDrawing> buffer;
  Vector<MyDrawing> clone;

  public Mediator(MyCanvas canvas){
    this.canvas = canvas;
    drawings = new Vector<MyDrawing>();
    selectedDrawing = new Vector<MyDrawing>();
    buffer = new Vector<MyDrawing>();
    clone = new Vector<MyDrawing>();
    selectRectangle = new Vector<MyDrawing>();
  }
  public Enumeration<MyDrawing> drawingElements(){
    return drawings.elements();
  }
  public Enumeration<MyDrawing> selectRectangleElements(){
    return selectRectangle.elements();
  }
  //図形の状態の変化を行う
  public void Change(){
    if(selectedDrawing.size() != 0){
      for(int i = 0;i < selectedDrawing.size(); i++){
        selectedDrawing.get(i).setFillColor(getColor());
        selectedDrawing.get(i).setLineColor(getlineColor());
        selectedDrawing.get(i).setLineWidth(getLineWidth());
        selectedDrawing.get(i).setAlpha(getAlpha());
        if(getShadow()){
          selectedDrawing.get(i).setShadow(true);
        }else{
          selectedDrawing.get(i).setShadow(false);
        }
        repaint();
      }
    }

  }
  public void MouseDragged(int x,int y){
    int xloc = drawings.get(drawings.size()-1).getX();
    int yloc = drawings.get(drawings.size()-1).getY();
    drawings.get(drawings.size()-1).setSize(x-xloc,y-yloc);
    drawings.get(drawings.size()-1).setRegion();
    repaint();
  }


  public void RegionMouseDragged(int x,int y){
    int xloc = regionDrawing.getX();
    int yloc = regionDrawing.getY();
    regionDrawing.setSize(x-xloc,y-yloc);
    regionDrawing.setRegion();
    repaint();
  }
  Color c = Color.white;
  public void setColor(Color color){
    c = color;
  }
  public Color getColor(){
    return c;
  }
  Color linecolor = Color.black;
  public void setlineColor(Color color){
    linecolor = color;
  }
  public Color getlineColor(){
    return linecolor;
  }
  int linewidth = 1;
  public void setLineWidth(int bold){
    linewidth = bold;
  }
  public int getLineWidth(){
    return linewidth;
  }
  boolean shadowstate = false;
  boolean shadowState = false;
  public void setShadow(boolean s){
    shadowState = s;
  }
  public boolean getShadow(){
    return shadowState;
  }
  public void addDrawing(MyDrawing d){
    d.setLineWidth(getLineWidth());
    d.setFillColor(getColor());
    d.setLineColor(getlineColor());
    d.setAlpha(getAlpha());
    drawings.add(d);
    setSelectedDrawing(d);
  }



  public void addRegionDrawing(MyDrawing d){
    Color none = new Color(0,0,0,0);
    d.setFillColor(none);
    selectRectangle.add(d);//選択用の長方形専用の変数
    regionDrawing = d;
  }


  public Vector<MyDrawing> getSelectDrawing(){
    return selectedDrawing;
  }
  private int iniX,iniY;
  int[] gapX;
  int[] gapY;
  public void move(int x,int y){
    if(selectedDrawing != null){
      for(int i = 0;i < selectedDrawing.size();i++){
        selectedDrawing.get(i).move(x - gapX[i], y - gapY[i]);
        selectedDrawing.get(i).setGridWidth(getGridWidth());
        selectedDrawing.get(i).setRegion();
      }
      repaint();
    }
  }

  public void initialLocation(int x,int y){
    iniX = x;
    iniY = y;
    gapX = new int[selectedDrawing.size()];
    gapY = new int[selectedDrawing.size()];
    for(int i = 0;i < selectedDrawing.size();i++){
      gapX[i] = x - selectedDrawing.get(i).getX();
      gapY[i] = y - selectedDrawing.get(i).getY();
    }
  }


  public void repaint(){
    canvas.repaint();
  }

  int flagsetSelected = 0;

  public int selectedState(int x,int y){
    if(selectedDrawing != null){
      for(int i=0;i < selectedDrawing.size();i++){
        if(selectedDrawing.get(i).contains(x,y)){
          return 1;
        }
      }
      for(int i = drawings.size()-1;i >=0;i--){
        if(drawings.get(i).contains(x,y)){
          return 2;
        }
      }
    }
    return 3;
  }
  //全ての図形をint見てその図形が含まれているか選ぶ
  public void setSelected(int x,int y){
    for(int i = drawings.size()-1;i>=0;i--){
      if(drawings.get(i).contains(x,y)){
        selectedDrawing.add(drawings.get(i));
        drawings.get(i).setSelected(true);
        //ここに並び替えのメソッドを入れる
        order(i);
        if(i > 0){
          for(int j = i-1;j>=0;j--){
            drawings.get(j).setSelected(false);
            repaint();
          }
          repaint();
          break;
        }
      }else{
        drawings.get(i).setSelected(false);
        repaint();
      }
    }
  }
  public void clearDrawings(){
    for(int i = 0; i < drawings.size();i++){
      drawings.get(i).setSelected(false);
    }
  }

  public void setMultiSelected(int x,int y){
    for(int i = drawings.size()-1;i >=0;i--){
      if(containX(x,i) && containY(y,i)){
        drawings.get(i).setSelected(true);
      }else{
        drawings.get(i).setSelected(false);
      }
    }
    repaint();
  }
  public void addSelectedDrawing(){
    for(int i = drawings.size()-1;i >=0; i--){
      if(drawings.get(i).getSelected() == true){
        selectedDrawing.add(drawings.get(i));
      }
    }
  }
  //i番目の図形をみている
  public boolean containX(int x , int i){
    int selectX = drawings.get(i).getX() + drawings.get(i).getW();

    if(selectRectangle.get(0).getX() < drawings.get(i).getX() && x > selectX){
      return true;
    }else if(selectRectangle.get(0).getX() > drawings.get(i).getX() && x < selectX){
      return true;
    }else{
      return false;
    }
  }
  public boolean containY(int y , int i){
    int selectY = drawings.get(i).getY() + drawings.get(i).getH();
    if(selectRectangle.get(0).getY()< drawings.get(i).getY() && y > selectY){
      return true;
    }else if(selectRectangle.get(0).getY()> drawings.get(i).getY() && y < selectY){
      return true;
    }else{
      return false;
    }
  }

  public void setSelectedDrawing(MyDrawing d){
    d.setRegion();
  }


  public void clearBuffer(){
    buffer.clear();
  }
  int locX;
  int locY;
  public void copy(){
    clearBuffer();
    if(selectedDrawing.size()>0){
      for(int i =0;i < selectedDrawing.size();i++){
        buffer.add(selectedDrawing.get(i).clone());
        buffer.get(i).setLocation(selectedDrawing.get(i).getX(),selectedDrawing.get(i).getY());
      }
    }
    locX = selectedDrawing.get(selectedDrawing.size()-1).getX();
    locY = selectedDrawing.get(selectedDrawing.size()-1).getY();
  }
  public void cut(){
    for(int i = 0;i < selectedDrawing.size();i++){
      buffer.add(selectedDrawing.get(i).clone());
      buffer.get(i).setLocation(selectedDrawing.get(i).getX(),selectedDrawing.get(i).getY());
      removeDrawing(selectedDrawing.get(i));
    }
    locX = selectedDrawing.get(selectedDrawing.size()-1).getX();
    locY = selectedDrawing.get(selectedDrawing.size()-1).getY();
  }
  public void removeDrawing(MyDrawing d){
    drawings.remove(d);
  }
  public void paste(int x, int y){
    for(int i = 0;i < buffer.size();i++){
      clone.add(buffer.get(i).clone());
      clone.get(i).setLocation(buffer.get(i).getX() -locX + x,
      buffer.get(i).getY() - locY + y);
      addDrawing(clone.get(i));
    }
    repaint();
  }
  public void delete(){
    for(int i = 0;i < selectedDrawing.size();i++){
      removeDrawing(selectedDrawing.get(i));
    }
  }
  float alpha = 1.0f;
  public void setAlpha(float a){
    alpha = a;

  }

  public float getAlpha(){
    return alpha;
  }


  //dx,dy が図形の座標
  double size;
  double resizeX;
  double resizeY;
  double resizeH;
  double resizeW;
  public int Judge(double x,double y){
    if(selectedDrawing.size() == 1){
      size = (double)selectedDrawing.get(0).getSIZE()/2;
      resizeX = selectedDrawing.get(0).getX();
      resizeY = selectedDrawing.get(0).getY();
      resizeH = selectedDrawing.get(0).getH();
      resizeW = selectedDrawing.get(0).getW();
      if(resizeX - size <= x && x <= resizeX + size){
        if(resizeY - size <= y && y <= resizeY + size){
          return 0;
        }else if((resizeY + resizeH/2 - size) <= y && y <= (resizeY + resizeH/2 + size)){
          return 1;
        }else if(resizeY + resizeH - size <= y && y <= resizeY + resizeH + size){
          return 2;
        }
      }else if(resizeX + resizeW/2 -size <= x && x <= resizeX + resizeW/2 + size ){
        if(resizeY - size <= y && y <= resizeY + size){
          return 3;
        }else if(resizeY + resizeH - size <= y &&  y <= resizeY + resizeH + size){
          return 4;
        }
      }else if(resizeX + resizeW -size <= x && x <= resizeX + resizeW + size){
        if(resizeY + size >= y && y >= resizeY - size){
          return 5;
        }else if(resizeY + resizeH/2 - size <= y && y <= resizeY + resizeH/2 + size){
          return 6;
        }else if(resizeY + resizeH - size <= y && y <= resizeY + resizeH + size){
          return 7;
        }
      }
    }
    return 8;

  }
  int resizenum;
  public void setJudge(double x,double y){
    resizenum = Judge(x,y);

  }
  public int getJudge(){
    return resizenum;
  }
  public void resize(int x,int y){
    int gx = 0;
    int gy = 0;
    int gh = (int)resizeH;
    int gw = (int)resizeW;
    if(resizenum != 8){
      //xが左側のときの処理
      if(resizenum >= 0 && resizenum <= 2){
        gw = (int)resizeW +(int)resizeX - x;
        if(x < (int)resizeX + (int)resizeW){
          gx = x;
        }else{
          gx = (int)resizeX + (int)resizeW;
        }
        //xが右側の時の処理
      }else if(resizenum >= 5 && resizenum <= 7){
        gw = x - (int)resizeX;
        if(x < (int)resizeX){
          gx = x;
        }else{
          gx = (int)resizeX;
        }
      }else{
        gx = (int)resizeX;
      }
      //yが上側の時の処理
      if(resizenum == 0 || resizenum == 3 || resizenum == 5){
        gh = (int)resizeH + (int)resizeY - y;
        if(y < (int)resizeY + (int)resizeH){
          gy = y;
        }else{
          gy = (int)resizeY + (int)resizeH;
        }
      }else if(resizenum == 2 || resizenum == 4 || resizenum == 7){
        gh = y - (int)resizeY;
        if(y < (int)resizeY){
          gy = y;
        }else{
          gy = (int)resizeY;
        }
      }else{
        gy = (int)resizeY;
      }
    }
    if(gw < 0){
      gw = -gw;
    }
    if(gh < 0){
      gh = -gh;
    }
    selectedDrawing.get(0).setLocation(gx,gy);
    selectedDrawing.get(0).setSize(gw,gh);
    selectedDrawing.get(0).setRegion();
    repaint();
  }


  int grid = 0;
  public void setGridWidth(int g){
    grid = g;
  }
  public int getGridWidth(){
    return grid;
  }


//173
  public void order(int i){
    addDrawing(drawings.get(i));
    removeDrawing(drawings.get(i));
  }
  public boolean CursorJudge(double x,double y){
    setJudge(x,y);
    for(int i = 0;i < selectedDrawing.size();i ++){
      if(selectedDrawing.get(i).contains((int)x,(int)y)){
        return true;
      }
      else if(getJudge() >= 0 && getJudge() <= 7){
        return true;
      }
    }
    return false;
  }


}
