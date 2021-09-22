import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.*;
import java.io.*;

public class MyRectangle extends MyDrawing{
	public MyRectangle(int xpt,int ypt) {
		super();
		setSize(w,h);
		setLocation(xpt,ypt);

	}

	private boolean isDashed = false;
	public void  setDashed(boolean b){
	  isDashed = b;
	}
	public boolean getDashed(){
	  return isDashed;
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

	/////////////////////////////////////////////

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

	//	snap(x,y);

		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(getLineWidth()));

		if(getShadow()){
		 g2.setColor(Color.black);
		 g2.fillRect(x+4,y+4,w,h);
	 }
	 AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,getAlpha());
	 g2.setComposite(ac);
		g2.setColor(getFillColor());
		g2.fillRect(x,y,w,h);
		g2.setColor(getLineColor());
		if(getDashed()){
		g2.setStroke(new MyDashStroke(getLineWidth()));
	   }
		g2.drawRect(x,y,w,h);
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
