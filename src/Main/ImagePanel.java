package Main;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel{

    private Image backGroundImage;

    public ImagePanel(String imagePath){
        backGroundImage = new ImageIcon(imagePath).getImage();

    }

    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        g.drawImage(backGroundImage, 0, 0, getWidth(), getHeight(), this);
    }

}