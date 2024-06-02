package edu.guyi.bbtan.geometryobject;

import edu.guyi.bbtan.utilities.Utilities;

import java.awt.*;
import java.util.ArrayList;

public class Square extends GeometryObject{
    private double rotate;
    private double sideLength;
    private double p1x, p1y, p2x, p2y, p3x, p3y, p4x, p4y;
    private ArrayList<Line> allLines;
    private Line l1, l2, l3, l4;
    private Line tl1, tl2, tl3, tl4;
    private double cond;

    public Square(double sideLength, double rotate, double x, double y, int ball_radius, int HitTime) {
        super(x, y, ball_radius, HitTime);
        this.rotate = rotate;
        this.sideLength = sideLength;
        double diagonal_2 = sideLength / Math.sqrt(2);
        final double cos = Math.cos(1 * Math.PI / 4 + rotate);
        final double sin = Math.sin(1 * Math.PI / 4 + rotate);
        p1x = x + diagonal_2 * cos;
        p1y = y + diagonal_2 * sin;
        p2x = x + diagonal_2 * (-sin);
        p2y = y + diagonal_2 * cos;
        p3x = x + diagonal_2 * (-cos);
        p3y = y + diagonal_2 * (-sin);
        p4x = x + diagonal_2 * sin;
        p4y = y + diagonal_2 * (-cos);
        l1 = new Line(
                p1x + ball_radius * cos,
                p1y + ball_radius * sin,
                p2x + ball_radius * (-sin),
                p2y + ball_radius * cos
        );
        l2 = new Line(
                p2x + ball_radius * (-sin),
                p2y + ball_radius * cos,
                p3x + ball_radius * (-cos),
                p3y + ball_radius * (-sin)
        );
        l3 = new Line(
                p3x + ball_radius * (-cos),
                p3y + ball_radius * (-sin),
                p4x + ball_radius * sin,
                p4y + ball_radius * (-cos)
        );
        l4 = new Line(
                p4x + ball_radius * sin,
                p4y + ball_radius * (-cos),
                p1x + ball_radius * cos,
                p1y + ball_radius * sin
        );
        if (!l1.IsIn(p3x, p3y)) l1.slip();
        if (!l2.IsIn(p4x, p4y)) l2.slip();
        if (!l3.IsIn(p1x, p1y)) l3.slip();
        if (!l4.IsIn(p2x, p2y)) l4.slip();

        tl1 = new Line(p1x, p1y, p2x, p2y);
        tl2 = new Line(p2x, p2y, p3x, p3y);
        tl3 = new Line(p3x, p3y, p4x, p4y);
        tl4 = new Line(p4x, p4y, p1x, p1y);
        if (!tl1.IsIn(p3x, p3y)) tl1.slip();
        if (!tl2.IsIn(p4x, p4y)) tl2.slip();
        if (!tl3.IsIn(p1x, p1y)) tl3.slip();
        if (!tl4.IsIn(p2x, p2y)) tl4.slip();
        cond = ball_radius * ball_radius;
        allLines = new ArrayList<>();
        allLines.add(l1);
        allLines.add(l2);
        allLines.add(l3);
        allLines.add(l4);
    }

    @Override
    public synchronized boolean IsIn(double p_x, double p_y) {
        if(IsTimeMachined == 0) {
            boolean IsInl1 = l1.IsIn(p_x, p_y);
            boolean IsInl2 = l2.IsIn(p_x, p_y);
            boolean IsInl3 = l3.IsIn(p_x, p_y);
            boolean IsInl4 = l4.IsIn(p_x, p_y);
            if (!(IsInl1 && IsInl2 && IsInl3 && IsInl4))
                return false;
            if (tl2.IsIn(p_x, p_y) && tl4.IsIn(p_x, p_y))
                return true;
            if (tl1.IsIn(p_x, p_y) && tl3.IsIn(p_x, p_y))
                return true;
            return ((p_x - p1x) * (p_x - p1x) + (p_y - p1y) * (p_y - p1y) <= cond) ||
                    ((p_x - p2x) * (p_x - p2x) + (p_y - p2y) * (p_y - p2y) <= cond) ||
                    ((p_x - p3x) * (p_x - p3x) + (p_y - p3y) * (p_y - p3y) <= cond) ||
                    ((p_x - p4x) * (p_x - p4x) + (p_y - p4y) * (p_y - p4y) <= cond);
        } else return false;
    }

    @Override
    public synchronized void Reflect(Ball ball, double temp_x, double temp_y) {
        synchronized (ball) {
            double hit_x = 0, hit_y = 0;
            if ((temp_x - p1x) * (temp_x - p1x) + (temp_y - p1y) * (temp_y - p1y) <= cond) {
                hit_x = p1x;
                hit_y = p1y;
            } else if ((temp_x - p2x) * (temp_x - p2x) + (temp_y - p2y) * (temp_y - p2y) <= cond) {
                hit_x = p2x;
                hit_y = p2y;
            } else if ((temp_x - p3x) * (temp_x - p3x) + (temp_y - p3y) * (temp_y - p3y) <= cond) {
                hit_x = p3x;
                hit_y = p3y;
            } else if ((temp_x - p4x) * (temp_x - p4x) + (temp_y - p4y) * (temp_y - p4y) <= cond) {
                hit_x = p4x;
                hit_y = p4y;
            } else {
                for (Line l : allLines) {
                    if (l.IsSeg(ball.x, ball.y, temp_x, temp_y)) {
                        double dot = ((ball.x - l.x1) * (l.x2 - l.x1) + (ball.y - l.y1) * (l.y2 - l.y1)) /
                                ((l.x2 - l.x1) * (l.x2 - l.x1) + (l.y2 - l.y1) * (l.y2 - l.y1));
                        hit_x = l.x1 + dot * (l.x2 - l.x1);
                        hit_y = l.y1 + dot * (l.y2 - l.y1);
                        break;
                    }
                }
            }

            double delta_x = ball.x - hit_x;
            double delta_y = ball.y - hit_y;
            double speed_x = ((delta_y * delta_y - delta_x * delta_x) * ball.speed_x - 2 * delta_x * delta_y * ball.speed_y) / (delta_x * delta_x + delta_y * delta_y);
            double speed_y = ((delta_x * delta_x - delta_y * delta_y) * ball.speed_y - 2 * delta_x * delta_y * ball.speed_x) / (delta_x * delta_x + delta_y * delta_y);
            ball.speed_x = loss * speed_x;
            ball.speed_y = loss * speed_y;
        }
    }

    @Override
    public synchronized void update(double y) {
        if(IsTimeMachined == 0) {
            this.y += y;
            p1y += y;
            p2y += y;
            p3y += y;
            p4y += y;
            l1.update(y);
            l2.update(y);
            l3.update(y);
            l4.update(y);
            tl1.update(y);
            tl2.update(y);
            tl3.update(y);
            tl4.update(y);
        } else IsTimeMachined--;
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

                int[] xPoints = {(int) p1x, (int) p2x, (int) p3x, (int) p4x};
                int[] yPoints = {(int) p1y, (int) p2y, (int) p3y, (int) p4y};

                g2d.drawPolygon(xPoints, yPoints, xPoints.length);
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