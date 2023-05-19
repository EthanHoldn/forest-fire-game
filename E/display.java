package E;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import G.start;
import G.world;
import java.awt.Font;
import java.awt.RenderingHints;

public class display extends Thread {
    public static long[] average = new long[200];
    public int buffers = 3;
	double FPS = -1;
    JFrame jf;
    public static Canvas c;
    BufferStrategy bs;
	BufferedImage display;

    IM im;
    String name;
    public ArrayList<GameObject> images = new ArrayList<GameObject>();
	
    public display() {
		im = new IM();
		jf = new JFrame("Forest Fire");	//makes screen, sets it up, etc.
		c = new Canvas();
		c.getWidth();
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setExtendedState(JFrame.MAXIMIZED_BOTH);
		c.setBackground(new Color(30,30,30));
		jf.add(c);
		jf.pack();	
		c.addKeyListener(im);	//keyboard input
		jf.addKeyListener(im);  
		jf.addMouseListener(im);
		jf.addMouseMotionListener(im);
		jf.addMouseWheelListener(im);
		c.addMouseWheelListener(im);
		c.addMouseListener(im);
		c.addMouseMotionListener(im);
		jf.setVisible(true);
		

	}
    
    public void add(int x, int y, float scale, BufferedImage txtr){
        images.add(new GameObject(x, y, scale, txtr));
    }

	public void addButton(int  x, int  y, String name, BufferedImage txtr, String alignment){
        UIdistributor.add(new Button(x, y, name, txtr, alignment));
    }
	public void removeButton(String name){
		for (int i = 0; i < UIdistributor.buttons.size(); i++){
		if(UIdistributor.buttons.get(i).name == name)UIdistributor.buttons.remove(i);
		}
    }




	public void render() {
		
		bs = c.getBufferStrategy();
		if(bs==null) {
			c.createBufferStrategy(buffers);
			return;
		}
		Graphics2D g = (Graphics2D)bs.getDrawGraphics();
		g.setColor(new Color(30,30,30)); 
		g.fillRect(0, 0, c.getWidth(), c.getHeight());
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		
//if the world is active display map and aircraft
		if(world.mapGrid != null)compile.map(g);
//display map UI
		compile.mapUI(g);
//display UI
		UIdistributor.render(g);
//detect process key presses
		IM.keyFunctions();
//if in debug mode display
		if(start.debug)compile.debug(g);
//display FPS
		//ImageObserver.HEIGHT = 400;
		
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT);
		Font font = new Font("Serif", Font.PLAIN, 16);
		g.setColor(new Color(120,120,120)); 
      	g.setFont(font);
        g.drawString("FPS: "+ FPS, 5, 16);
		bs.show();
		g.dispose();
    }
    


	long frameEnd = 0;
    long frameStart = 0;
	double targetFPS = 1000/60;
    @Override
	public void run() {
		while (true){
			frameStart =  System.currentTimeMillis();
			if(frameStart-frameEnd>=targetFPS){
				FPS = 1000/(frameStart-frameEnd);
				frameEnd = System.currentTimeMillis();
				render();
			}
			
		}
	}

    





}