package E;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class apparatus {
    public String name, type, action;
    public int speed, maxSpeed, heading, water, maxWater;
    public ArrayList<Integer> xDestList = new ArrayList<Integer>();
    public ArrayList<Integer> yDestList = new ArrayList<Integer>();

    public double x, y, velocity, maxAcceleration;
    public boolean active;
    public BufferedImage apparatusImage;
    public apparatus (
        String name, 
        String type,
        String action, 
        BufferedImage apparatusImage, 
        int heading, 
        int speed,
        double maxAcceleration,
        int maxWater
        

        ){
        this.name = name;
        this.type = type;
        this.action = action;
        this.apparatusImage = apparatusImage;
        this.speed = speed;
        this.heading = heading;
        this.maxWater = maxWater;
        this.active = false;
        this.velocity = 0;
        this.maxAcceleration = maxAcceleration;
    }
}
