package E;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import G.start;
import G.world;
import java.awt.Graphics2D;


public class UIdistributor {
    public static tile mapClickID = null;
    public static apparatus selectedApparatus = null;
    String clickedButton;
    public static String[] latest_button_clicks = {"","","",""};
    public static ArrayList<Button> buttons = new ArrayList<Button>();
//checks if click in on a button
    public static boolean checkClick(int x, int y) {
        boolean clicked = false;
       
        for (int i = 0; i < buttons.size(); i++) {
            if (buttons.get(i).click(x, y)) {
                //if the click is on a button run the action
                buttonAction(buttons.get(i).name, x, y);
                clicked = true;
            }
        }
        return clicked;
    }

//renders buttons, called from display.java
    public static void render(Graphics2D g) {
        for (int i = 0; i < buttons.size(); i++) {
            switch (buttons.get(i).alignment){
                case "top left":
                    g.drawImage(buttons.get(i).txtr, buttons.get(i).x1, buttons.get(i).y1, buttons.get(i).txtr.getWidth(),
                    buttons.get(i).txtr.getHeight(), null);
                    break;
                case "center":
                    g.drawImage(buttons.get(i).txtr,(display.c.getWidth()/2) - buttons.get(i).x1, (display.c.getHeight()/2) - buttons.get(i).y1, buttons.get(i).txtr.getWidth(),
                    buttons.get(i).txtr.getHeight(), null);
                    break;
                case "bottom center":
                    g.drawImage(buttons.get(i).txtr, (display.c.getWidth()/2) - buttons.get(i).x1, display.c.getHeight() - buttons.get(i).y1, buttons.get(i).txtr.getWidth(),
                    buttons.get(i).txtr.getHeight(), null);
                    break;
            }
        }
    }

//checks if you clicked on a button apparatus
    public static boolean apparatusClick(int x, int y) {
        selectedApparatus = null;
        for(int i = 0; i < world.apparatus.size(); i++){
            int dx = (int) (IM.ox + (world.apparatus.get(i).x*IM.scale) - x);
            int dy = (int) (IM.oy + (world.apparatus.get(i).y*IM.scale) - y);
            if(Math.sqrt((dx*dx)+(dy*dy))<30){
                selectedApparatus = world.apparatus.get(i);
                break;
            }
            //System.out.println(Math.abs(Math.pow(dx, 2)+Math.pow(dy, 2)));
        }
        if(selectedApparatus == null)return true;
        return false;
    }


//adds button
    public static void add(Button button) {
        buttons.add(button);
    }

//removes movement buttons

    public static void remove_movement_buttons() {
        start.r.removeButton("go here");
        start.r.removeButton("drop water");
        start.r.removeButton("cut fire line");
    }

//code for all buttons is here:

    public static void buttonAction(String name, int x, int y) {
        System.arraycopy(latest_button_clicks,0,latest_button_clicks,1,3);
        System.out.println(name);
        String button_name = name;
        switch (button_name) {
            case "new world":
                buttons.clear();
                world.start(750);
                world.running = true;
                start.r.addButton(-50, 100,  "buy bulldozer", Load.image("Bulldozer_icon.png"), "bottom center");
                start.r.addButton(10, 100,  "buy helicopter", Load.image("Bell_205_icon.png"), "bottom center");
                break;

            case "exit":
                System.exit(0);
                break;

            case "settings":
                buttons.clear();
                //start.r.addbutton(100, 160, "full screen", imageGenerator.button(new Color(180 ,180, 180), "full screen", 50, 250, 52, 40, 40));
                break;
            case "drop water":
                selectedApparatus.active = true;
                selectedApparatus.xDestList.clear();
                selectedApparatus.xDestList.add((int) (((IM.lastClick[3][0]-IM.ox)/IM.scale)));
                selectedApparatus.yDestList.clear();
                selectedApparatus.yDestList.add((int) (((IM.lastClick[3][1]-IM.oy)/IM.scale)));
                remove_movement_buttons();
                break;
            case "cut fire line":
                selectedApparatus.active = true;
                selectedApparatus.xDestList.add((int) (((IM.lastClick[3][0]-IM.ox)/IM.scale)));
                selectedApparatus.yDestList.add((int) (((IM.lastClick[3][1]-IM.oy)/IM.scale)));
                remove_movement_buttons();
                break;
            case "go here":
                if(IM.keycode[16]){
                    selectedApparatus.xDestList.clear();
                    selectedApparatus.xDestList.add((int) (((IM.lastClick[3][0]-IM.ox)/IM.scale)));
                    selectedApparatus.yDestList.clear();
                    selectedApparatus.yDestList.add((int) (((IM.lastClick[3][1]-IM.oy)/IM.scale)));

                } else {
                    selectedApparatus.active = false;
                    selectedApparatus.xDestList.add((int) (((IM.lastClick[3][0]-IM.ox)/IM.scale)));
                    selectedApparatus.yDestList.add((int) (((IM.lastClick[3][1]-IM.oy)/IM.scale)));
                }
                remove_movement_buttons();
                break;
            case "buy bulldozer":
                IM.newApparatus = new apparatus(
                "B" + ThreadLocalRandom.current().nextInt(100), //callsign
                "Bulldozer", //type
                "cut fire line", //action
                Load.image("Bulldozer.png"), //image
                0, //heading
                2, //max speed
                .01, //max acceleration
                0 //max water
                );
                break;

            case "buy helicopter":
                IM.newApparatus = new apparatus(
                "H" + ThreadLocalRandom.current().nextInt(100), //callsign
                "bell 204", //type
                "drop water", //action
                Load.image("Bell_205.png"), //image
                0, //heading
                20, //max speed
                .01, //max acceleration
                9 //max water
                );
                break;
            case "resume":
                
                world.resume();
            default:
          }
    }
    public static void OBJbutton(tile id){
        if(mapClickID != id){
            mapClickID = id;
        }else{ 
            mapClickID = null;
            mapClickID.gID = "null";

        }
    }
    
}
