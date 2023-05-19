package G;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;   
import java.awt.Color;
import java.awt.RenderingHints;

import E.IM;
import E.compile;
import E.display;

public class fire extends Thread{
    public static int tickCount = 0;
    public static int time = 0;

    public static boolean active[][], newfire[][];
//spawns initial fire
    public fire(int size, String string) {
        Random random = new Random();   
        active = new boolean[size][size];
        newfire = new boolean[size][size];
        // try 100 times to find a good place to spawn the initial fire
        int trysleft = 100;
        while(true){
            trysleft--;
            int x = random.nextInt(size/2)+(size/4);
            int y = random.nextInt(size/2)+(size/4);
            if(world.mapGrid[x][y].gID == "grass" && world.mapGrid[x+2][y+2].gID == "grass"){
                world.mapGrid[x][y].fire = 1;
                active[x][y] = true;
                world.mapGrid[x+2][y+2].fire = 1;
                active[x+2][y+2] = true;
                System.out.println(x + " | " + y);
                IM.scale=6;
                IM.ox = (-1 * x * 6) + (display.c.getWidth()/2);
                IM.oy = (-1 * y * 6) + (display.c.getHeight()/2);
                break;
            }
            if(trysleft < 0){
                
                break;
            }
        }
        for (int x = 0; x < 30; x++) {
            simulate();
        }
        
    }
    @Override
	public void run() {
        
        long Ftickend = 0;
        long Ftickstart = 0;
        
        while (true) { 
            Ftickstart = System.currentTimeMillis();
            if(Ftickstart-Ftickend>=1000){
                if(!world.pause)simulate();
            Ftickend = System.currentTimeMillis();
                //if(simulate){//|| !start.debug
                    

                //}
                
                
            }
        }
    }
    public void simulate(){
        tickCount++;
        time = tickCount;
        BufferedImage map = compile.map;
        Graphics2D g2d = map.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int tempmapGrid[][];
        tempmapGrid = new int[world.mapSize][world.mapSize];
        double burn_size = 0;
        double houses_burned = 0;

//loading the fire grid to tempmapGrid

        for (int x = 0; x < world.mapSize; x++) {
            for (int y = 0; y < world.mapSize; y++) {
                tempmapGrid[x][y] = world.mapGrid[x][y].fire;
                if(world.mapGrid[x][y].fire > 0)burn_size += .25;
                if(world.mapGrid[x][y].fire > 0 && world.mapGrid[x][y].gID == "house")houses_burned += 1;
            } 
        }
        world.ACRES_BURNED = burn_size;
        world.BUILDINGS_DESTROYED = houses_burned;

//read from tempmapGrid
//write to world.mapGrid

//simulating fire:

        for (int x = 0; x < world.mapSize; x++) {
            for (int y = 0; y < world.mapSize; y++) {
                if(tempmapGrid[x][y] > 0 && active[x][y]){
                    Random random = new Random();   
                    world.mapGrid[x][y].fire = tempmapGrid[x][y] + 1;
                    if(random.nextInt(4) == 0)switch (world.mapGrid[x][y].gID) {
        //grass 
        //can only spread to neighbor tiles
                        case "grass":
                        int rx = random.nextInt(3)-1;
                        int ry = random.nextInt(3)-1;
                        if(x+rx >= 0 && y+ry >= 0 && y+ry <= world.mapSize && x+rx <= world.mapSize && world.mapGrid[x+rx][y+ry].gID != "water" && world.mapGrid[x+rx][y+ry].gID != "dirt" && tempmapGrid[x+rx][y+ry] == 0){
                            g2d.setColor(new Color(224 + random.nextInt(15), 40, 48));
                            g2d.fillRect(
                                (int) (x+rx),
                                (int) (y+ry),
                                (int) (1),
                                (int) (1)
                            );
                            world.mapGrid[x+rx][y+ry].fire = 1;
                            active[x+rx][y+ry] = true;
                            
                        }
                        if(tempmapGrid[x][y] >= 20){
                            int i = 40 + random.nextInt(15);
                            g2d.setColor(new Color(i, i, i));
                            g2d.fillRect(
                                (int) (x),
                                (int) (y),
                                (int) (1),
                                (int) (1)
                            );
                            active[x][y] = false;
                        }
                        break;
        //brush
        //can spread to close tiles
                        case "brush":
                        rx = random.nextInt(3)-1;
                        ry = random.nextInt(3)-1;
                        if(x+rx >= 0 && y+ry >= 0 && y+ry <= world.mapSize && x+rx <= world.mapSize && world.mapGrid[x+rx][y+ry].gID != "water" && world.mapGrid[x+rx][y+ry].gID != "dirt" && tempmapGrid[x+rx][y+ry] == 0){
                            g2d.setColor(new Color(224 + random.nextInt(15), 40, 48));
                            g2d.fillRect(
                                (int) (x+rx),
                                (int) (y+ry),
                                (int) (1),
                                (int) (1)
                            );
                            world.mapGrid[x+rx][y+ry].fire = 1;
                            active[x+rx][y+ry] = true;
                            
                        }
                        if(tempmapGrid[x][y] >= 20){
                            int i = 100 + random.nextInt(15);
                            g2d.setColor(new Color(i, i, i));
                            g2d.fillRect(
                                (int) (x),
                                (int) (y),
                                (int) (1),
                                (int) (1)
                            );
                            active[x][y] = false;
                        }
                        break;
        //tree
        //can spread to far tiles
                        case "tree":
                        rx = random.nextInt(3)-1;
                        ry = random.nextInt(3)-1;
                        if(x+rx >= 0 && y+ry >= 0 && y+ry <= world.mapSize && x+rx <= world.mapSize && world.mapGrid[x+rx][y+ry].gID != "water" && world.mapGrid[x+rx][y+ry].gID != "dirt" && tempmapGrid[x+rx][y+ry] == 0){
                            if(random.nextInt(3) == 0){
                                rx =+ random.nextInt(3)-1;
                                ry =+ random.nextInt(3)-1;
                            }
                            g2d.setColor(new Color(224 + random.nextInt(15), 40, 48));
                            g2d.fillRect(
                                (int) (x+rx),
                                (int) (y+ry),
                                (int) (1),
                                (int) (1)
                            );
                            world.mapGrid[x+rx][y+ry].fire = 1;
                            active[x+rx][y+ry] = true;
                            
                        }
                        if(tempmapGrid[x][y] >= 20){
                            int i = 100 + random.nextInt(15);
                            g2d.setColor(new Color(i, i, i));
                            g2d.fillRect(
                                (int) (x),
                                (int) (y),
                                (int) (1),
                                (int) (1)
                            );
                            active[x][y] = false;
                        }
                        break;
        //house
                        case "house":
                        rx = random.nextInt(5)-2;
                        ry = random.nextInt(5)-2;
                        if(x+rx >= 0 && y+ry >= 0 && y+ry <= world.mapSize && x+rx <= world.mapSize && world.mapGrid[x+rx][y+ry].gID != "water" && tempmapGrid[x+rx][y+ry] == 0){
                            g2d.setColor(new Color(224 + random.nextInt(15), 40, 48));
                            g2d.fillRect(
                                (int) (x+rx),
                                (int) (y+ry),
                                (int) (1),
                                (int) (1)
                            );
                            world.mapGrid[x+rx][y+ry].fire = 1;
                            active[x+rx][y+ry] = true;
                            
                        }
                        if(tempmapGrid[x][y] >= 20){
                            int i = 100 + random.nextInt(15);
                            g2d.setColor(new Color(i, i, i));
                            g2d.fillRect(
                                (int) (x),
                                (int) (y),
                                (int) (1),
                                (int) (1)
                            );
                            active[x][y] = false;
                        }
                        break;
                        


                    }
                }

            } 
        }
       
            g2d.dispose();
        compile.map = map;

    }


//returns amount of fire in neighboring tiles (from 0-9)

    public static int checkNeighbor(int x1, int y1, int firetype, int tempmapGrid[][]){
        int number = 0;
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if(tempmapGrid[x1+x-1][y1+y-1] >= firetype)number++;
            }
        }
        return number;
    }
    public static void waterdrop(int x1, int y1){
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                if(x1+x-2 >= 0 && x1+x-2 <= world.mapSize && y1+x-2 >= 0 && y1+x-2 <= world.mapSize){
                    if(world.mapGrid[x1+x-2][y1+y-2].fire == 0) world.mapGrid[x1+x-2][y1+y-2].fire = -1;
                    if(world.mapGrid[x1+x-2][y1+y-2].fire > 0) world.mapGrid[x1+x-2][y1+y-2].fire = 25;
                }
            }

        }

    }
    
}
