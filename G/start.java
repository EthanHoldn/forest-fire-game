package G;

import java.awt.Color;

import E.*;
public class start {
  public static display r;
  public static boolean debug = false;
  
    public static void main(String[] args) throws InterruptedException {

      
      r =  new  display ();
		  r.setName("renderer");
      r.start();
//main menu button
      r.addButton(100, 100, "new world", imageGenerator.button(new Color(100, 100, 100), "New World", 50, 250, 30, 40, 40), "top left");
      r.addButton(100, 160, "settings", imageGenerator.button(new Color(100 ,100, 100), "Settings", 50, 250, 52, 40, 40), "top left");
      r.addButton(100, 220, "exit", imageGenerator.button(new Color(168 ,70, 70), "Exit", 50, 250, 90, 40, 40), "top left");
      
      long fTickEnd = 0;
      long fTickStart = 0;
      world.mainMenu(750);
      if(debug)UIdistributor.buttonAction("new world", 0, 0);
      while (true){
        Thread.sleep(1);
        if(world.running){
        fTickStart = System.currentTimeMillis();
        if(fTickStart-fTickEnd>=17){
          if(world.running == true)world.runApparatus();
          fTickEnd = System.currentTimeMillis();
        }
      }
      }
    }
  }