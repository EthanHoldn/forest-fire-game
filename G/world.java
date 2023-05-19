package G;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.lang.Math;   
import java.util.Random;   
import java.awt.Color;
import java.awt.Graphics2D;

import E.*;
public class world {
    public static fire f;
    public static boolean intractable = false;
    public static boolean running = false;
    public static boolean pause =  false;
    public static tile mapGrid[][];
    public static int mapSize = 1;
    public static double ACRES_BURNED, BUILDINGS_DESTROYED;
    static display r = start.r;
    public static ArrayList<apparatus> apparatus = new ArrayList<apparatus>();

    public static void start(int size) {
        
        mapSize = size;
        ACRES_BURNED = 0; BUILDINGS_DESTROYED = 0;
        mapGrid = new tile[size][size];

        generateMap();
        
        
//center the map
        IM.scale = display.c.getHeight()/(size+0.0);
        IM.ox = (display.c.getWidth()-(size*IM.scale))/2;
        IM.oy = 0;
//generates the map image
        compile.map = imageGenerator.landTexture();
        
//starts fire thread, simulation and initial fire
        intractable = true;
        f =  new  fire (size,"Engine");
		f.setName("fire");
        f.start();
        pause = false;
        runApparatus();
    }
    
    public static void mainMenu(int size) {
        intractable = false;
        mapSize = size;
        mapGrid = new tile[size][size];
        generateMap();
        compile.map = imageGenerator.landTexture();
        f =  new  fire (size,"Engine");
		f.setName("fire");
        IM.scale = 2;
        IM.ox = 0;
        IM.oy = 0;
        f.start();
    }
    
    public static void generateMap(){
        OpenSimplexNoise noise = new OpenSimplexNoise(ThreadLocalRandom.current().nextLong());
        OpenSimplexNoise houseNoise = new OpenSimplexNoise(ThreadLocalRandom.current().nextLong()+1);
        final double FEATURE_SIZE = 30;
        final double houseFEATURE_SIZE = 100;
        int min = 255;
        int[][] houseArray = new int[world.mapGrid.length][world.mapGrid.length];
        //generates houses with perrin noise
        for (int x = 0; x < world.mapGrid.length; x++) {
            
            for (int y = 0; y < world.mapGrid[x].length; y++) {
                houseArray[x][y] = 255-(int) Math.abs(((noise.eval((x) / FEATURE_SIZE, (y) / FEATURE_SIZE, 0.0)*50)+((houseNoise.eval((x) / houseFEATURE_SIZE, (y) / houseFEATURE_SIZE, 0.0) > 0) ? houseNoise.eval((x) / houseFEATURE_SIZE, (y) / houseFEATURE_SIZE, 0.0)*205 : 0)));
                min = (houseArray[x][y] < min) ? houseArray[x][y] : min;
            }
        }
        for (int x = 0; x < mapSize; x++) {
            for (int y = 0; y < mapSize; y++) {
                double i = Math.abs(noise.eval((x) / FEATURE_SIZE, (y) / FEATURE_SIZE, 0.0))+(ThreadLocalRandom.current().nextInt(100)*.001);
                try{
                    if (Math.abs(noise.eval((x) / FEATURE_SIZE, (y) / FEATURE_SIZE, 0.0))+(ThreadLocalRandom.current().nextInt(100)*.0001) <= .03) {

                        mapGrid[x][y] = new tile("water", ThreadLocalRandom.current().nextInt(100000), 0);

                    } else if (rand((int) (Math.pow((houseArray[x][y]-min) * 255/(255-min+0.0), 2)/1000.0)+1) ==0 && rand(7) == 0 && houseArray[x][y] < 230) {

                        mapGrid[x][y] = new tile("house", ThreadLocalRandom.current().nextInt(100000), 0);

                    } else if (i <= .2) {

                        mapGrid[x][y] = new tile("tree", ThreadLocalRandom.current().nextInt(100000), 0);

                    } else if (i <= .4) {

                        mapGrid[x][y] = new tile("brush", ThreadLocalRandom.current().nextInt(100000), 0);

                    } else if (i > .4) {

                        mapGrid[x][y] = new tile("grass", ThreadLocalRandom.current().nextInt(100000), 0);

                    } else {

                        mapGrid[x][y] = new tile("MAP_ERROR", ThreadLocalRandom.current().nextInt(100000), 0);
                }
                }catch(Exception e){}
                
            }
        }
    }

    public static void runApparatus() {
        if(!pause){
        for (int i = 0; i < apparatus.size(); i++) {
            apparatus ap = apparatus.get(i);
            boolean atDestination = false;


            
                if(Math.sqrt(((ap.x-ap.xDestList.get(0))*(ap.x-ap.xDestList.get(0)))+((ap.y-ap.yDestList.get(0))*(ap.y-ap.yDestList.get(0)))) < .5){
                atDestination = true;
                if(ap.xDestList.size() > 1)ap.xDestList.remove(0);
                if(ap.yDestList.size() > 1)ap.yDestList.remove(0);
                ap.velocity = 0;
            }

            
            switch(apparatus.get(i).type) {
                case "bell 204":
                    //apparatus movement
                    fly(ap, atDestination);

                    //pick up water
                    fillWater(ap, atDestination);

                    ///drop water
                    dropWater(ap, atDestination); 

                    //autopilot
                    //go to fire
                    autopilotGoToFire(ap, atDestination);
                    //go to water
                    autopilotGoToWater(ap, atDestination);
                break;
                case "Bulldozer":
                    move(ap, atDestination);
                    bulldoze(ap, atDestination);
                break;
                default:
                  // code block
              }
        
        }   
        }
    }
    
    static void bulldoze(apparatus ap, boolean atDestination){
        if(ap.active && mapGrid[(int)ap.x][(int)ap.y].gID != "water"){
            mapGrid[(int)ap.x][(int)ap.y].gID = "dirt";
            compile.map.setRGB((int)ap.x, (int)ap.y, new Color(150, 95, 10).getRGB());
        }
    }

    static void autopilotGoToWater(apparatus ap, boolean atDestination){
        if(atDestination && ap.active && ap.water == 0){
            int xs = (int) ap.x;
            int ys = (int) ap.y;
            int closeX = xs;
            int closeY = ys;
            double closeDist = 200;
            for (int ox = xs-50; ox < xs+50; ox++) {
                for (int oy = ys-50; oy < ys+50; oy++) {
                    if(mapGrid[ox][oy].gID == "water" && Math.sqrt((xs-ox)*(xs-ox)+(ys-oy)*(ys-oy)) < closeDist ){
                        closeDist = Math.sqrt((xs-ox)*(xs-ox)+(ys-oy)*(ys-oy));
                        closeX = ox;
                        closeY = oy;
                    }
                }
            }
            //if(closeDist > 1){
                ap.xDestList.set(0, closeX);
                ap.yDestList.set(0, closeY);
            //}

        }
    }
    
    static void autopilotGoToFire(apparatus ap, boolean atDestination){
        if(atDestination && ap.active && ap.water != 0){
            int xs = (int) ap.x;
            int ys = (int) ap.y;
            int closeX = xs;
            int closeY = ys;
            double closeDist = 200;
            for (int ox = xs-50; ox < xs+50; ox++) {
                for (int oy = ys-50; oy < ys+50; oy++) {
                    if(fire.active[ox][oy] && Math.sqrt((xs-ox)*(xs-ox)+(ys-oy)*(ys-oy)) < closeDist ){
                        closeDist = Math.sqrt((xs-ox)*(xs-ox)+(ys-oy)*(ys-oy));// + checkNeighbor(ox, oy);
                        closeX = ox;
                        closeY = oy;
                    }
                }
            }
            //if(closeDist > 1){
                ap.xDestList.set(0, closeX);
                ap.yDestList.set(0, closeY);
            //}

        }
    }

    static void dropWater(apparatus ap, boolean atDestination) {
        if(
                fire.active[(int)ap.xDestList.get(0)][(int)ap.yDestList.get(0)] && 
                atDestination &&
                ap.water >= 9
            ){
                BufferedImage map = compile.map;
                Graphics2D g2d = map.createGraphics();
                //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Random random = new Random();   
                for (int ox = -1; ox <= 1; ox++) {
                    for (int oy = -1; oy <= 1; oy++) {
                            int x = ap.xDestList.get(0)+ox;
                            int y = ap.yDestList.get(0)+oy;
                        if(mapGrid[x][y].fire != 0 && fire.active[x][y]) {
                            int ii = 100 + random.nextInt(15);
                            g2d.setColor(new Color(ii, ii, ii));
                            g2d.fillRect(
                                (int) (x),
                                (int) (y),
                                (int) (1),
                                (int) (1)
                            );
                            fire.active[x][y] = false;
                        }
                        
                    }
                }
                ap.water = 0;
            }
    }

    static void fly(apparatus ap, boolean atDestination){
        if(!atDestination){
        if(ap.velocity < 1)ap.velocity += ap.maxAcceleration;
        ap.heading = (int) Math.toDegrees(Math.atan2((ap.xDestList.get(0))-ap.x, (ap.yDestList.get(0))-ap.y));
        ap.x += Math.sin(Math.toRadians(ap.heading))*(ap.speed*0.017*ap.velocity);
        ap.y += Math.cos(Math.toRadians(ap.heading))*(ap.speed*0.017*ap.velocity);
        }
        if(Math.sqrt(((ap.x-ap.xDestList.get(0))*(ap.x-ap.xDestList.get(0)))+((ap.y-ap.yDestList.get(0))*(ap.y-ap.yDestList.get(0)))) < (ap.velocity * ap.velocity)/(2*ap.maxAcceleration)){
            ap.velocity -= ap.maxAcceleration*2;
        }
    }
    
    static void move(apparatus ap, boolean atDestination){
        if(!atDestination){
        if(ap.velocity < 1)ap.velocity += ap.maxAcceleration;
        ap.heading = (int) Math.toDegrees(Math.atan2((ap.xDestList.get(0))-ap.x, (ap.yDestList.get(0))-ap.y));
        ap.x += Math.sin(Math.toRadians(ap.heading))*(ap.speed*0.017*(ap.active ? .5 : 1));
        ap.y += Math.cos(Math.toRadians(ap.heading))*(ap.speed*0.017*(ap.active ? .5 : 1));
        }
    }

    static void fillWater(apparatus ap, boolean atDestination){
        if(
                mapGrid[(int)ap.xDestList.get(0)][(int)ap.yDestList.get(0)].gID == "water" && 
                atDestination &&
                ap.maxWater > ap.water
                )ap.water = ap.maxWater;
    }

    static int checkNeighbor(int x, int y){
        int count = 0;
        for (int ox = x-2; ox < x+2; ox++) {
            for (int oy = y-2; oy < y+2; oy++) {
                if(mapGrid[ox][oy].fire > 0){
                    count++;
                }
            }
        }
        return count;
    }
    
    static int rand(int range){ return ThreadLocalRandom.current().nextInt(range);}

    public static int getMapCordX(int displayX){
        return (int)((((displayX-IM.ox)/IM.scale)));
    }
    public static int getMapCordY(int displayY){
        return (int)((((displayY-IM.oy)/IM.scale)));
    }

    public static void pause() {
        pause = true;
        intractable = false;
        if(pause){
            r.addButton(125, 0, "exit", imageGenerator.button(new Color(168 ,70, 70), "Exit", 50, 250, 90, 40, 40), "center");
            r.addButton(125, 100, "resume", imageGenerator.button(new Color(100 ,100, 100), "Resume", 50, 250, 50, 40, 40), "center");

        }
    }
    public static void resume() {
        r.removeButton("exit");
        r.removeButton("resume");
        pause = false;
    }
}
