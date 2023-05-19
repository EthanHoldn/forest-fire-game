package E;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.Color;


import G.start;
import G.world;

public class IM implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener{
    static int mx = 0, my = 0, mcb = 0, rotation = 0; //mouse clicked button
    boolean dragged = false;
    public static double ox;
    public static double oy;
    public static double scale = 1;
    public static int lastClick [][] = new int[5][2];
    static double MW_Rotation = 1;
    static boolean keycode[] = new boolean[200];
    static apparatus newApparatus;
    static boolean newApparatusDelay = true;

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }

    int mdx = 0;
    int mdy = 0;
    @Override
    public void mouseDragged(MouseEvent e) {
        int x = (int) (e.getX());
        int y = (int) (e.getY());
      
        if(mcb == 3){
        
            ox += x - mdx;
            if(ox < 5-(world.mapSize*2*scale)){ox = 5-(world.mapSize*2*scale);}
            if(ox > display.c.getWidth()-5){ox = display.c.getWidth()-5;}
            oy += y - mdy;
            if(oy < 5-(world.mapSize*2*scale)){oy = 5-(world.mapSize*2*scale);}
            if(oy > display.c.getHeight()-5){oy = display.c.getHeight()-5;}
            mdx = x;
            mdy = y;
        }
            mx = x;
            my = y;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        mx = x;
        my = y;
        mdx = x;
        mdy = y;

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mcb = e.getButton();

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int x = (int) (e.getX());
        int y = (int) (e.getY());
        int cordX = world.getMapCordX(x);
        int cordY = world.getMapCordY(y);
        lastClick[e.getButton()][0] = x;
        lastClick[e.getButton()][1] = y;
        if(newApparatus != null){
            if(cordX > cordY && cordX < world.mapSize-cordY)
                newApparatus.x = cordX;
            if(cordY > cordX && cordY < world.mapSize-cordX)
                newApparatus.y = cordY;
            if(cordX > cordY && cordX > world.mapSize-cordY){
                newApparatus.y = cordY;
                newApparatus.x = world.mapSize;

            }
            if(cordY > cordX && cordY > world.mapSize-cordX){
                newApparatus.x = cordX;
                newApparatus.y = world.mapSize;
            }
            newApparatus.xDestList.clear();
            newApparatus.yDestList.clear();
            newApparatus.xDestList.add(cordX);
            newApparatus.yDestList.add(cordY);
            world.apparatus.add(newApparatus);
            newApparatus = null;
            newApparatusDelay = true;
        }
        
        if(e.getButton()==1){
        if(!UIdistributor.checkClick(x,y)){
            UIdistributor.apparatusClick(x, y);
            }
        }
        if(UIdistributor.selectedApparatus != null && e.getButton()==3){
            UIdistributor.remove_movement_buttons();
            start.r.addButton(x, y, "go here", imageGenerator.button(new Color(100 ,100, 100), "go here", 30, 88, 10, 22, 20), "top left");
            start.r.addButton(x, y+35, UIdistributor.selectedApparatus.action, imageGenerator.button(new Color(100 ,100, 100), UIdistributor.selectedApparatus.action, 30, 95, 10, 22, 20), "top left");

        }
        dragged = false;
        mcb = -1;

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {
        if(e.getKeyChar() == 'e')rotation++;
        if(rotation >= 4) rotation = 0;
        if(e.getKeyChar() == '`')start.debug = true;
        


    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == 27)world.pause();
        keycode[e.getKeyCode()]= true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keycode[e.getKeyCode()]= false;
        
    }

    public static void keyFunctions() {
        //while(true);
        double tox = ox;
        double toy = oy;
//WASD movement
        if(world.running){
        if(keycode[(char)'A']){
            tox+=10;
            UIdistributor.remove_movement_buttons();
        }
        if(keycode[(char)'D']){
            tox-=10;
            UIdistributor.remove_movement_buttons();
        }
        if(keycode[(char)'S']){
            toy-=10;
            UIdistributor.remove_movement_buttons();
        }
        if(keycode[(char)'W']){
            toy+=10;
            UIdistributor.remove_movement_buttons();
        }

//zoom in and out
        if(keycode[(char)'Q'] ){
            scale+=.02*scale; 
            tox -= ((.02*scale)*world.mapSize*2*((((display.c.getWidth()/2)-tox)/(world.mapSize*2*scale))));
            toy -= ((.02*scale)*world.mapSize*2*((((display.c.getHeight()/2)-toy)/(world.mapSize*2*scale))));
            UIdistributor.remove_movement_buttons();
        }
        if(keycode[(char)'E'] && display.c.getWidth() < (scale-((.02*scale)))*world.mapSize){
            scale-=.02*scale; 
            tox += ((.02*scale)*world.mapSize*2*((((display.c.getWidth()/2)-ox)/(world.mapSize*2*scale))));
            toy += ((.02*scale)*world.mapSize*2*((((display.c.getHeight()/2)-toy)/(world.mapSize*2*scale))));
            UIdistributor.remove_movement_buttons();
        }
        }
        if(tox > 0){
            tox = 0;
        }
        if(toy > 0){
            toy = 0;
        }
        if(toy +(world.mapSize*scale) < display.c.getHeight()){
            toy = display.c.getHeight() - (world.mapSize*scale);
        }
        if(tox +(world.mapSize*scale) < display.c.getWidth()){
            tox = display.c.getWidth() - (world.mapSize*scale);
        }
        oy = toy;
        ox = tox;

    }
    

}
