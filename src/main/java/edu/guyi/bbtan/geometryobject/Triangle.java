package edu.guyi.bbtan.geometryobject;

import edu.guyi.bbtan.utilities.Utilities;

import java.awt.*;
import java.util.ArrayList;

public class Triangle extends GeometryObject{
    private double rotate;
    private double sideLength;
    private double p1x, p1y, p2x, p2y, p3x, p3y;
    private Line l1, l2, l3;
    private Line tl1, tl2, tl3;
    private ArrayList<Line> allLines;
    private double cond;

    public Triangle(double sideLength, double rotate, double x, double y, int ball_radius, int HitTime) {
        super(x, y, ball_radius, HitTime);
        this.sideLength = sideLength;
        this.rotate = rotate;
        double diagonal_2 = sideLength / Math.sqrt(3);
        p1x = x + diagonal_2 * Math.cos(Math.PI / 2 + rotate);
        p1y = y + diagonal_2 * Math.sin(Math.PI / 2 + rotate);
        p2x = x + diagonal_2 * Math.cos(7 * Math.PI / 6 + rotate);
        p2y = y + diagonal_2 * Math.sin(7 * Math.PI / 6 + rotate);
        p3x = x + diagonal_2 * Math.cos(11 * Math.PI / 6 + rotate);
        p3y = y + diagonal_2 * Math.sin(11 * Math.PI / 6 + rotate);

        l1 = new Line(
                p1x + ball_radius * Math.cos(Math.PI / 2 + rotate),
                p1y + ball_radius * Math.sin(Math.PI / 2 + rotate),
                p2x + ball_radius * Math.cos(7 * Math.PI / 6 + rotate),
                p2y + ball_radius * Math.sin(7 * Math.PI / 6 + rotate)
        );
        l2 = new Line(
                p2x + ball_radius * Math.cos(7 * Math.PI / 6 + rotate),
                p2y + ball_radius * Math.sin(7 * Math.PI / 6 + rotate),
                p3x + ball_radius * Math.cos(11 * Math.PI / 6 + rotate),
                p3y + ball_radius * Math.sin(11 * Math.PI / 6 + rotate)
        );
        l3 = new Line(
                p3x + ball_radius * Math.cos(11 * Math.PI / 6 + rotate),
                p3y + ball_radius * Math.sin(11 * Math.PI / 6 + rotate),
                p1x + ball_radius * Math.cos(Math.PI / 2 + rotate),
                p1y + ball_radius * Math.sin(Math.PI / 2 + rotate)
        );

        if (!l1.IsIn(p3x, p3y)) l1.slip();
        if (!l2.IsIn(p1x, p1y)) l2.slip();
        if (!l3.IsIn(p2x, p2y)) l3.slip();

        tl1 = new Line(p1x, p1y, p2x, p2y);
        tl2 = new Line(p2x, p2y, p3x, p3y);
        tl3 = new Line(p3x, p3y, p1x, p1y);
        if (!tl1.IsIn(p3x, p3y)) tl1.slip();
        if (!tl2.IsIn(p1x, p1y)) tl2.slip();
        if (!tl3.IsIn(p2x, p2y)) tl3.slip();

        cond = ball_radius * ball_radius;
        allLines = new ArrayList<>();
        allLines.add(l1);
        allLines.add(l2);
        allLines.add(l3);
    }

    @Override
    public synchronized boolean IsIn(double x, double y) {
        if(IsTimeMachined == 0) {
            if (!(l1.IsIn(x, y) && l2.IsIn(x, y) && l3.IsIn(x, y)))
                return false;
            if (tl1.IsIn(x, y) && tl2.IsIn(x, y)) return true;
            if (tl2.IsIn(x, y) && tl3.IsIn(x, y)) return true;
            if (tl3.IsIn(x, y) && tl1.IsIn(x, y)) return true;
            return ((x - p1x) * (x - p1x) + (y - p1y) * (y - p1y) <= cond) ||
                    ((x - p2x) * (x - p2x) + (y - p2y) * (y - p2y) <= cond) ||
                    ((x - p3x) * (x - p3x) + (y - p3y) * (y - p3y) <= cond);
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
            l1.update(y);
            l2.update(y);
            l3.update(y);
            tl1.update(y);
            tl2.update(y);
            tl3.update(y);
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

                int[] xPoints = {(int) p1x, (int) p2x, (int) p3x};
                int[] yPoints = {(int) p1y, (int) p2y, (int) p3y};

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