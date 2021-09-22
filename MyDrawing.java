import java.awt.*;
import java.io.*;


public class MyDrawing implements Cloneable, Serializable{
  int x,y,w,h;
  Color lineColor;
  Color fillColor = Color.white;
  int lineWidth;
  boolean isSelected = false;
  transient Shape region;
  final int SIZE = 10;

  public MyDrawing(){
    x = y = w = h = 0;
    //  w = h = 40;
    lineColor = Color.black;

    lineWidth = getLineWidth();
    setRegion();
  }

  float alpha = 1.0f;
  public void setAlpha(float a){
    alpha = a;
  }
  public float getAlpha(){
    return alpha;
  }


  public void draw(Graphics g){
    if(isSelected){
      g.setColor(Color.black);
      snap(x,y,getGridWidth());
      g.fillRect(x+w/2-SIZE/2,y-SIZE/2,SIZE,SIZE);
      g.fillRect(x-SIZE/2,y+h/2-SIZE/2,SIZE,SIZE);
      g.fillRect(x+w/2-SIZE/2,y+h-SIZE/2,SIZE,SIZE);
      g.fillRect(x+w-SIZE/2,y+h/2-SIZE/2,SIZE,SIZE);
      g.fillRect(x-SIZE/2,y-SIZE/2,SIZE,SIZE);
      g.fillRect(x+w-SIZE/2,y-SIZE/2,SIZE,SIZE);
      g.fillRect(x-SIZE/2,y+h-SIZE/2,SIZE,SIZE);
      g.fillRect(x+w-SIZE/2,y+h-SIZE/2,SIZE,SIZE);

    }

  }
  public int getSIZE(){
    return SIZE;
  }

  public boolean getSelected(){
    return isSelected;
  }
  public void setSelected(boolean isSelected){
    this.isSelected = isSelected;
  }

  public boolean contains(int x,int y){
    //継承した子クラスで定義する
    return true;
  }

  public void setRegion(){
  }
  public void move(int x,int y){
    setLocation(x,y);
  }
  //::::::::::::::::::::::::::::::::::::::::::::

  public void setLocation(int x, int y) {
    this.x = x;
    this.y = y;
  }
  public void setSize(int w,int h) {
    this.w = w;
    this.h = h;
  }
  public void setFillColor(Color fillColor) {
    this.fillColor = fillColor;
  }
  public void setLineColor(Color lineColor) {
    this.lineColor = lineColor;
  }
  public void setLineWidth(int lineWidth) {
    this.lineWidth = lineWidth;
  }
  boolean shadow;
  public void setShadow(boolean sh){
    shadow = sh;
  }
  public boolean getShadow(){
    return shadow;
  }


  public int getX() {
    return x;
  }
  public int getY() {
    return y;
  }
  public int getW() {
    return w;
  }
  public int getH() {
    return h;
  }
  // 属性を取得するメソッド
  public Color getFillColor() {
    return fillColor;
  }
  public Color getLineColor() {
    return lineColor;
  }
  public int getLineWidth(){
    return lineWidth;
  }
  @Override
  public MyDrawing clone(){
    MyDrawing b = null;
    try{
      b = (MyDrawing)super.clone();
    }catch(Exception e){
      e.printStackTrace();
    }
    return b;
  }

  int grid = 0;
  public void setGridWidth(int g){
    grid = g;
  }
  public int getGridWidth(){
    return grid;
  }


  public void snap(int x,int y,int width){
    int judge = width / 4;
    if(width != 0){
      if(x%width < judge && y%width< judge){
        System.out.println("True");
        setLocation(x-x%width,y-y%width);
      }
    }
  }


}
