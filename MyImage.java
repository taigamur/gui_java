import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;

public class MyImage extends MyDrawing{
  BufferedImage image = null;
  BufferedImage img = null;

  public MyImage(int xpt,int ypt){
    super();
    setLocation(30,30);
    setSize(400,300);
    try{
      JFileChooser fc = new JFileChooser();
      int returnVal = fc.showOpenDialog(null);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = fc.getSelectedFile();
        image = ImageIO.read(file);
      }
    }catch(Exception e){
      System.out.println(e);
    }
  }

  public boolean contains(int x,int y){
    return region.contains(x,y);
  }

  public void setRegion(){
    int x = getX();
    int y = getY();
    int w = getW();
    int h = getH();
    if(w < 0) {
      x += w;
      w *= -1;
    }
    if(h < 0) {
      y += h;
      h *= -1;
    }
    region = new Rectangle(x,y,w,h);
  }
  public void draw(Graphics g) {

    int x = getX();
    int y = getY();
    int w = getW();
    int h = getH();
    if(w < 0) {
      x += w;
      w *= -1;
    }
    if(h < 0) {
      y += h;
      h *= -1;
    }

    Graphics2D g2 = (Graphics2D) g;
    g2.drawImage(image,x,y,w,h,null);
    super.draw(g);
  }

  private void writeObject(ObjectOutputStream stream) throws IOException{
      stream.writeInt(getX());
      stream.writeInt(getY());
      stream.writeInt(getW());
      stream.writeInt(getH());
  }

  private void readObject(ObjectInputStream stream)throws IOException {
      int ix = stream.readInt();
      int  iy = stream.readInt();
      int  iw = stream.readInt();
      int ih = stream.readInt();
      Rectangle s = new Rectangle(ix,iy,iw,ih);
      region =  s;
  }
}
