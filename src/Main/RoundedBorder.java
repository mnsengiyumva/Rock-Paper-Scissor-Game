package Main;

import javax.swing.border.AbstractBorder;
import java.awt.*;



/**
 * This class makes round borders for buttons
 * using the specified radius
 */

public class RoundedBorder extends AbstractBorder {
    private final int radius;
    private final Color color;

    RoundedBorder(int radius, Color color) {
        this.color = color;
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(color);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }
}