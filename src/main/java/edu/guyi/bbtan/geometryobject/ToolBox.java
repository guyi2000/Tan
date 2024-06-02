package edu.guyi.bbtan.geometryobject;

import java.awt.*;

abstract public class ToolBox extends GeometryObject {
    protected double cond;

    public ToolBox(double x, double y, double ball_radius) {
        super(x, y, ball_radius, 1);
        IsTool = true;
        cond = (10 + ball_radius) * (10 + ball_radius);
    }

    @Override
    public void Reflect(Ball ball, double temp_x, double temp_y) {

    }

    @Override
    public boolean IsIn(double p_x, double p_y) {
        if(IsTimeMachined == 0)
            return (p_x - x) * (p_x - x) + (p_y - y) * (p_y - y) <= cond;
        else return false;
    }

    @Override
    public void draw(Graphics g) {

    }

}
