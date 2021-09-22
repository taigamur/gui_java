import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.*;
import java.awt.*;
import java.awt.*;
import java.awt.BasicStroke;
import java.io.*;

public class MyOval extends MyDrawing{
	public MyOval(int xpt,int ypt) {
		super();
		setLocation(xpt,ypt);
	}
	//ovalには個別のメソッドを２つ実装する。
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
		//Ellipse2D
		Ellipse2D s = new Ellipse2D.Double(x,y,w,h);
		region = new Area(s);
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
		g2.setStroke(new BasicStroke(getLineWidth()));
		if(getShadow()){
			g2.setColor(Color.black);
			g2.fillRect(x+4,y+4,w,h);
		}
		AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,getAlpha());
		
		g2.setComposite(ac);
		g2.setColor(getFillColor());
		g2.fillOval(x,y,w,h);
		g2.setColor(getLineColor());
		g2.drawOval(x,y,w,h);
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
		Ellipse2D s = new Ellipse2D.Double(ix,iy,iw,ih);
		region = new Area(s);
	}

}
