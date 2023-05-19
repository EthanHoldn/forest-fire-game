package E;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.xml.stream.events.StartDocument;

import java.io.File;
import A.*;


public class Load {
    public static BufferedImage image(String path){
        try {
			System.out.println("A/"+path);
			//BufferedImage i = ImageIO.read(Load.class.getResourceAsStream("A/"+path));
			//return i;
			return ImageIO.read(new File("A/"+path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
    }
}
