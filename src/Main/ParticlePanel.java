package Main;

import Main.Particle;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ParticlePanel extends JPanel {
    private final List<Particle> particles = new ArrayList<>();
    private final Timer animationTimer;

    ParticlePanel() {
        setOpaque(false);
        animationTimer = new Timer(30, e -> {
            particles.removeIf(p -> !p.isAlive());
            particles.forEach(Particle::update);
            repaint();
            if (particles.isEmpty()) {
                ((Timer)e.getSource()).stop();
            }
        });
    }

    void explode(int x, int y) {
        for (int i = 0; i < 50; i++) {
            particles.add(new Particle(x, y));
        }
        animationTimer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        particles.forEach(p -> p.draw(g));
    }
}