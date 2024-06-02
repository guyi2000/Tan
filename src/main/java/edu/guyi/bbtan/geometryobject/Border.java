package edu.guyi.bbtan.geometryobject;

import java.awt.*;

public class Border extends GeometryObject{
    boolean isHorizontal;
    boolean isLeftOrUp;

    @Override
    public boolean Hit() { return false; }

    @Override
    public void update(double y) {}

    @Override
    public boolean IsValid() {
        return true;
    }

    public Border(boolean is_horizontal, boolean isLeftOrUp, double location, double ball_radius) {
        super(location, location, ball_radius, Integer.MAX_VALUE);
        this.isHorizontal = is_horizontal;
        this.isLeftOrUp = isLeftOrUp;
    }

    @Override
    public synchronized void Reflect(Ball ball, double temp_x, double temp_y) {
        synchronized (ball) {
            if (isHorizontal) {
                ball.speed_y = -0.9 * ball.speed_y;
            } else {
                ball.speed_x = -0.9 * ball.speed_x;
            }
        }
    }

    @Override
    public synchronized boolean IsIn(double p_x, double p_y) {
        if(isHorizontal) {
            if(isLeftOrUp) {
                return p_y < y;
            } else {
                return p_y > y;
            }
        } else {
            if(isLeftOrUp) {
                return p_x < x;
            } else {
                return p_x > x;
            }
        }
    }

    @Override
    public void draw(Graphics g) {
    }
}
