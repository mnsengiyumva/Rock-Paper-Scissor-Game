package Main;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;


/**
 * ImagePanel class handles the background image instead of using default colors
 * Image can be changed depending on user preferences
 */

public class ImagePanel extends JPanel {

    private Image backgroundImage;

    ImagePanel(String imagePath){

        try{
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(imagePath)));
            backgroundImage = icon.getImage();
        } catch (Exception e){
            System.out.println("Background image not found: "+imagePath);
        }
        setOpaque(false);
    }

    /**
     * This method ensures that our image is not transparent
     *
     * @return false if we want our background image not to be seen through
     */

    public boolean isOpaque(){
        return false;
    }


    /**
     * This method ensures that we set appropriate colors for our features
     * if the background image is not provided
     * @param g the <code>Graphics</code> object to protect
     */

    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        if(backgroundImage != null){
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        else{
            g.setColor(new Color(59, 130, 246));
            g.fillRect(0,0,getWidth(),getHeight());
        }

        super.paintComponent(g);
    }
}