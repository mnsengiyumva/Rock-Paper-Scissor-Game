package Main;

import java.awt.*;

public class Particle {
    double x, y;
    double vx, vy;
    Color color;
    int size;
    int life;

    /**
     * Class constructor
     * @param x
     * @param y
     */

    Particle(double x, double y) {
        this.x = x;
        this.y = y;
        this.vx = (Math.random() - 0.5) * 10;
        this.vy = (Math.random() - 0.5) * 10 - 5;
        this.color = new Color(
                (int)(Math.random() * 255),
                (int)(Math.random() * 255),
                (int)(Math.random() * 255)
        );
        this.size = (int)(Math.random() * 8) + 3;
        this.life = 100;
    }

    void update() {
        x += vx;
        y += vy;
        vy += 0.3; // gravity
        life--;
    }

    boolean isAlive() {
        return life > 0;
    }

    void draw(Graphics g) {
        g.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(),
                Math.min(255, life * 2)));
        g.fillOval((int)x, (int)y, size, size);
    }
}