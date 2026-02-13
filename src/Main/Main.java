package Main;

import javax.swing.*;

public class Main{

    public static void main(String[] args){

        ImagePanel panel = new ImagePanel("/images/background.jpg");

        SwingUtilities.invokeLater(() -> new RockPaperScissorGame());


    }
}