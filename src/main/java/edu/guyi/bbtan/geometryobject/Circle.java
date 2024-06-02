package edu.guyi.bbtan.geometryobject;

import edu.guyi.bbtan.utilities.Utilities;

import java.awt.*;

public class Circle extends GeometryObject{
    private int radius;
    private double cond;

    public Circle(int radius, double x, double y, int ball_radius, int HitTime) {
        super(x, y, ball_radius, HitTime);
        this.radius = radius;
        cond = (radius + ball_radius) * (radius + ball_radius);
    }

    @Override
    public synchronized boolean IsIn(double p_x, double p_y) {
        if(IsTimeMachined == 0)
            return (p_x - x) * (p_x - x) + (p_y - y) * (p_y - y) <= cond;
        else return false;
    }

    @Override
    public synchronized void Reflect(Ball ball, double temp_x, double temp_y) {
        synchronized (ball) {
            double delta_x = ball.x - x;
            double delta_y = ball.y - y;
            double speed_x = ((delta_y * delta_y - delta_x * delta_x) * ball.speed_x - 2 * delta_x * delta_y * ball.speed_y) / (delta_x * delta_x + delta_y * delta_y);
            double speed_y = ((delta_x * delta_x - delta_y * delta_y) * ball.speed_y - 2 * delta_x * delta_y * ball.speed_x) / (delta_x * delta_x + delta_y * delta_y);
            ball.speed_x = loss * speed_x;
            ball.speed_y = loss * speed_y;
        }
    }

    @Override
    public synchronized void draw(Graphics g) {
        synchronized (g) {
            if(IsTimeMachined == 0) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.RED);
                g2d.setStroke(new BasicStroke(5F));
                g2d.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );
                g2d.drawOval((int) x - radius, (int) y - radius, 2 * radius, 2 * radius);
                g2d.setColor(Color.WHITE);
                g2d.setFont(Utilities.MonospaceBasic);
                if (RestHitTime >= 10) {
                    g2d.drawString(String.valueOf(RestHitTime), (int) x - 8, (int) y + 6);
                } else {
                    g2d.drawString(String.valueOf(RestHitTime), (int) x - 4, (int) y + 6);
                }
            }
        }
    }
}
