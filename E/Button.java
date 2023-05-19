package E;

import java.awt.image.BufferedImage;

public class Button {
	public int x1;
	public int y1;
	public BufferedImage txtr;
	public String name;
	public String alignment;
    Button (int  x, int  y, String name, BufferedImage txtr, String alignment) {
		this.alignment = alignment;
		this.name = name;
		this.x1 = x;
		this.y1 = y;
		this.txtr = txtr;
	}
	public boolean click(int x, int y) {
		int true_x = x1;
		int true_y = y1;
		switch (alignment){
			case "top left":
				true_x = x1;
				true_y = y1;
				break;
			case "bottom center":
				//(display.width/2) - buttons.get(i).x1, display.height - buttons.get(i).y1,
				true_x = (display.c.getWidth()/2) - x1;
				true_y = display.c.getHeight() - y1;
				break;
			case "center":
				//(display.width/2) - buttons.get(i).x1, display.height - buttons.get(i).y1,
				true_x = (display.c.getWidth()/2) - x1;
				true_y = (display.c.getHeight()/2) - y1;
				break;

		}
		//System.out.println("\n---- " + name);
		//System.out.println("true_x: " + true_x);
		//System.out.println("true_y: " + true_y);
		//System.out.println("x: " + x);
		//System.out.println("y: " + y);
		//System.out.println("txtr.getWidth(): " + txtr.getWidth());
		//System.out.println("txtr.getHeight(): " + txtr.getHeight() + "\n");


		if(x >= true_x && x <= true_x+txtr.getWidth() && y >= true_y && y <=true_y+txtr.getHeight()){
			return true;
		}

		return false;
		
	}

}
