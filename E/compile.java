package E;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.Color;
import java.awt.Font;

import G.fire;
import G.world;

public class compile {
    public static BufferedImage map = null;
    public static BufferedImage bell205 = Load.image("Bell_205.png");
    public static BufferedImage fire_icon = Load.image("fire-2-64.png");

    public static apparatus clicked_utility;
    public static void map(Graphics2D g){
//draws map image
        double s = IM.scale;
        if(world.mapGrid != null && world.mapGrid[0][0] != null){
            g.drawImage(map,(int) IM.ox,(int) IM.oy, (int)(world.mapGrid.length*2*s),(int)(world.mapGrid[0].length*2*s), null);
        }
        {   
        final BufferedImage rendered = new BufferedImage(display.c.getWidth(), display.c.getHeight(), 2);
        if(UIdistributor.selectedApparatus != null){

            g.drawLine(
                getDisplayX(UIdistributor.selectedApparatus.x),
                getDisplayY(UIdistributor.selectedApparatus.y),
                getDisplayX(UIdistributor.selectedApparatus.xDestList.get(0)),
                getDisplayY(UIdistributor.selectedApparatus.yDestList.get(0)));
            g.fillOval(
                getDisplayX(UIdistributor.selectedApparatus.xDestList.get(0))-10,
                getDisplayY(UIdistributor.selectedApparatus.yDestList.get(0))-10,
                20, 
                20
                );
            for (int a = 1; a < UIdistributor.selectedApparatus.xDestList.size(); a++){
                g.fillOval(
                    getDisplayX(UIdistributor.selectedApparatus.xDestList.get(a))-10,
                    getDisplayY(UIdistributor.selectedApparatus.yDestList.get(a))-10,
                    20, 
                    20
                    );
                g.drawLine(
                    getDisplayX(UIdistributor.selectedApparatus.xDestList.get(a-1)),
                    getDisplayY(UIdistributor.selectedApparatus.yDestList.get(a-1)),
                    getDisplayX(UIdistributor.selectedApparatus.xDestList.get(a)),
                    getDisplayY(UIdistributor.selectedApparatus.yDestList.get(a))

                    );


            }
        }
        //draws apparatus

        for (int i = 0; i < world.apparatus.size(); i++) {

            BufferedImage image = world.apparatus.get(i).apparatusImage;
            final double rads = Math.toRadians(-world.apparatus.get(i).heading-180);
            final AffineTransform at = new AffineTransform();
            at.translate((image.getWidth() / 2), image.getHeight() / 2);
            at.translate(((world.apparatus.get(i).x * s) + IM.ox) - (image.getWidth() / 2),((world.apparatus.get(i).y * s) + IM.oy) - (image.getHeight() / 2));
            at.rotate(rads,0, 0);
            at.translate(-image.getWidth() / 2, -image.getHeight() / 2);
            //at.scale(.75, .75);
            final AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
            rotateOp.filter(image,rendered);
            
        }
        g.drawImage(rendered,0,0,
        null);
    }
    }
//Map UI
    public static void mapUI(Graphics2D g) {
        if(world.running){
            if(IM.newApparatus != null)g.drawImage(IM.newApparatus.apparatusImage, IM.mx, IM.my, null);
    
        //Bottom info bar
        {
        int bar_Y = display.c.getHeight()-100;
        g.setColor(new Color(50,50,50, 200));
        g.fillRect(0, display.c.getHeight()-100, display.c.getWidth(), 100);

        //Text font
        Font font = new Font("Trebuchet MS", Font.PLAIN, 18);
        g.setFont(font);

        Color background_color = new Color(20,20,20);
        Color text_color = new Color(200,200,200);
        int textbox_height = 30;
        int textbox_bevel = 10;
        int textbox_spacing_side = 20;
        int textbox_spacing_top = 60;
        int text_spacing_top = 21;

        int box_X = textbox_spacing_side;
    
        //time
        {
            int width = 150; //Width of the box
            String text = fire.time +" hr";
            
            g.setColor(background_color);
            g.fillRoundRect(box_X, bar_Y + textbox_spacing_top, width, textbox_height, textbox_bevel, textbox_bevel);
            g.setColor(text_color);
            //icon
            g.drawString("T+ ", box_X + 10, bar_Y + textbox_spacing_top + text_spacing_top);
            int textWidth = g.getFontMetrics().stringWidth(text);
            g.drawString(text, box_X-textWidth+ width-10, bar_Y + textbox_spacing_top + text_spacing_top);
            box_X = box_X + width + textbox_spacing_side; 
        }
        
        //acres burned
        {
            int width = 150; //Width of the box
            String text = world.ACRES_BURNED +" ac";
            
            g.setColor(background_color);
            g.fillRoundRect(box_X, bar_Y + textbox_spacing_top, width, textbox_height, textbox_bevel, textbox_bevel);
            g.setColor(text_color);
            //icon
            g.drawImage(fire_icon, box_X+5, bar_Y + textbox_spacing_top + 3, 24, 24, null);
            int textWidth = g.getFontMetrics().stringWidth(text);
            g.drawString(text, box_X-textWidth+ width-10, bar_Y + textbox_spacing_top + text_spacing_top);
            box_X = box_X + width + textbox_spacing_side; 

        }

        //Buildings Destroyed
        {
            int width = 200; //Width of the box
            String text = world.BUILDINGS_DESTROYED +" ac";
            
            g.setColor(background_color);
            g.fillRoundRect(box_X, bar_Y + textbox_spacing_top, width, textbox_height, textbox_bevel, textbox_bevel);
            g.setColor(text_color);
            //icon
            g.drawImage(fire_icon, box_X+5, bar_Y + textbox_spacing_top + 3, 24, 24, null);
            int textWidth = g.getFontMetrics().stringWidth(text);
            g.drawString(text, box_X-textWidth+ width-10, bar_Y + textbox_spacing_top + text_spacing_top);
            box_X = box_X + width + textbox_spacing_side; 
        }
        }
        
        //apparatus info box
            if(UIdistributor.selectedApparatus != null){
            
            Font font_A = new Font("Arial", Font.PLAIN, 12);
            Font font_B = new Font("Arial", Font.PLAIN, 20);
            Color background_color = new Color(20,20,20,200);
            Color text_color_A = new Color(180,180,180);
            Color text_color_B = new Color(230,230,230);
            int info_box_height = 210;
            int info_box_width = 210;
            int line_spacing = 40;
            int boxX = 10; 
            int boxY = 10;

            g.setColor(background_color);
            g.fillRect(boxX, boxY, info_box_width, info_box_height);
            int offset = 0;

            //Callsign
            g.setFont(font_A);
            g.setColor(text_color_A);
            g.drawString("Callsign", boxX + 10, boxY + offset + 20);

            g.setFont(font_B);
            g.setColor(text_color_B);
            g.drawString(UIdistributor.selectedApparatus.name, boxX + 10, boxY + offset + 40);
            offset = offset + line_spacing;

            //Type
            g.setFont(font_A);
            g.setColor(text_color_A);
            g.drawString("Type", boxX + 10, boxY + offset + 20);

            g.setFont(font_B);
            g.setColor(text_color_B);
            g.drawString(UIdistributor.selectedApparatus.type, boxX + 10, boxY + offset + 40);
            offset = offset + line_spacing;
            
            //Water
            g.setFont(font_A);
            g.setColor(text_color_A);
            g.drawString("Water", boxX + 10, boxY + offset + 20);

            g.setFont(font_B);
            g.setColor(text_color_B);
            g.drawString(UIdistributor.selectedApparatus.water + "", boxX + 10, boxY + offset + 40);
            offset = offset + line_spacing;


            g.drawImage(UIdistributor.selectedApparatus.apparatusImage, boxX + (200-UIdistributor.selectedApparatus.apparatusImage.getWidth()), boxY + 10, UIdistributor.selectedApparatus.apparatusImage.getWidth(), UIdistributor.selectedApparatus.apparatusImage.getHeight(),null);
            }
    }}

//creates suffix for big numbers ie. 2348 to 2.34K
    public static String withSuffix(long count) {
    if (count < 1000) return "" + count;
    int exp = (int) (Math.log(count) / Math.log(1000));
    return String.format("%.1f %c",count / Math.pow(1000, exp),"kMGTPE".charAt(exp-1));
}

//displays debug info
    public static void debug(Graphics2D g) {
    g.drawString("scale: " + IM.scale, 10, 100);
    for(int i = 0; i < IM.keycode.length; i++){
        //IM.keycode[i]=false;
        g.setColor(new Color(20,20,20));
        if(IM.keycode[i])g.setColor(new Color(200,20,20));
        g.drawString((char)i+"", i*10, 10);
    }
    try{
        g.setColor(new Color(20,20,20));
        g.drawString("" + world.mapGrid[(int)((((IM.mx-IM.ox)/IM.scale)))][(int)((((IM.my-IM.oy)/IM.scale)))].fire  + " X: " +(int)((((IM.mx-IM.ox)/IM.scale))) + " Y: " + (int)((((IM.my-IM.oy)/IM.scale)))
        ,IM.mx,IM.my);
        g.drawString(String.join(",", UIdistributor.latest_button_clicks), 50, 50);
        g.drawString("Active tile?" + G.fire.active[(int)((((IM.mx-IM.ox)/IM.scale)))][(int)((((IM.my-IM.oy)/IM.scale)))] 
        ,IM.mx,IM.my+20);
        g.drawString(UIdistributor.selectedApparatus.name, 400, 400);
    } catch ( Exception e){

    }
    
}
    private static int getDisplayX(double x){
        return (int)((x * IM.scale) + IM.ox);
    }
    private static int getDisplayY(double y){
        return (int)((y * IM.scale) + IM.oy);
    }
}