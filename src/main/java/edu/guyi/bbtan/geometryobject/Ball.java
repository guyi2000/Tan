package edu.guyi.bbtan.geometryobject;

import java.awt.*;
import java.util.ArrayList;

public class Ball {
    public double x;
    public double y;
    private int radius;
    public double speed_x;
    public double speed_y;
    private boolean valid;
    private boolean IsHitted;
    private boolean IsHanced;
    private final double gravity = 0.5 / 1000000 / 1000000 / 1000;

    public Ball(double x, double y, int radius,
                double speed_x, double speed_y) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.speed_x = speed_x / 1000000;
        this.speed_y = speed_y / 1000000;
        this.valid = true;
        this.IsHitted = false;
    }

    public synchronized int update(ArrayList<GeometryObject> boudingBox, long elapsedTime) {
        double pixelMovement_x = elapsedTime * speed_x;
        double pixelMovement_y = elapsedTime * speed_y;
        double temp_x = x + pixelMovement_x;
        double temp_y = y + pixelMovement_y;
        synchronized (boudingBox) {
            for (GeometryObject boud : boudingBox) {
                if (boud.IsIn(temp_x, temp_y)) {
                    boud.Reflect(this, temp_x, temp_y);
                    if (Math.abs(speed_x * 1000000) + Math.abs(speed_y * 1000000) < 0.03) speed_y = -0.4 / 1000000;
                    boolean ans = boud.Hit();
                    if(IsHanced) boud.Hit();
                    this.IsHitted = true;
                    pixelMovement_x = elapsedTime * speed_x;
                    pixelMovement_y = elapsedTime * speed_y;
                    if (IsHitted) speed_y += gravity * elapsedTime;
                    x += pixelMovement_x;
                    y += pixelMovement_y;
                    if (y > 650) setValid(false);
                    return ans ? (IsHanced? 2 : 1) : 0;
                }
            }
        }
        if (IsHitted) speed_y += gravity * elapsedTime;
        x += pixelMovement_x;
        y += pixelMovement_y;
        if(y > 650) setValid(false);
        return 0;
    }

    public synchronized void  draw(Graphics g) {
        synchronized (g) {
            if(IsHanced)
                g.setColor(Color.CYAN);
            else
                g.setColor(Color.WHITE);
            g.fillOval((int) x - radius, (int) y - radius, 2 * radius, 2 * radius);
        }
    }

    public synchronized boolean IsValid() {
        return valid;
    }

    public synchronized void setValid(boolean v) {
        valid = v;
    }

    public synchronized void setHanced(boolean v) {
        IsHanced = v;
    }
}
