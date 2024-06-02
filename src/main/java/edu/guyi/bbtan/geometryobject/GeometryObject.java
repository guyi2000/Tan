package edu.guyi.bbtan.geometryobject;

import java.awt.*;

abstract public class GeometryObject {
    public int RestHitTime;
    public double x;
    public double y;
    protected double ball_radius;
    protected final double loss = 0.75;
    public boolean IsTool;
    public int IsTimeMachined;

    public GeometryObject(double x, double y, double ball_radius, int HitTime) {
        this.x = x;
        this.y = y;
        this.ball_radius = ball_radius;
        RestHitTime = HitTime;
        IsTool = false;
        IsTimeMachined = 0;
    }

    public synchronized boolean Hit() {
        RestHitTime -= 1;
        return true;
    }

    public synchronized void update(double y) {
        if(IsTimeMachined != 0) IsTimeMachined--;
        else {
            this.y += y;
        }
    }

    public synchronized boolean IsValid() {
        return RestHitTime > 0;
    }

    abstract public void Reflect(Ball ball, double temp_x, double temp_y);

    abstract public boolean IsIn(double p_x, double p_y);

    abstract public void draw(Graphics g);
}
