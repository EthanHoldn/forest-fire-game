package E;

import java.awt.image.BufferedImage;

public class GameObject {
	float x;
	public float y;
	public float scale;
	public long time;
	public BufferedImage txtr;
	public GameObject (float  x, float  y, float  scale, BufferedImage txtr) {
		this.x = x;
		this.y = y;
		this.scale = scale;
		this.txtr = txtr;
	}
}
