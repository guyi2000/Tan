package edu.guyi.bbtan.geometryobject;

public class Line {
    private double A, B, C;
    public double x1, y1, x2, y2;

    public Line(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.A = y2 - y1;
        this.B = x1 - x2;
        this.C = x2 * y1 - x1 * y2;
    }
    public synchronized void slip() {
        this.A = - this.A;
        this.B = - this.B;
        this.C = - this.C;
        double temp;
        temp = x1;
        x1 = x2;
        x2 = temp;
        temp = y1;
        y1 = y2;
        y2 = temp;
    }
    public synchronized boolean IsIn(double px, double py) {
        return (this.A * px + this.B * py + this.C) >= 0;
    }
    public synchronized boolean IsSeg(double p1x, double p1y, double p2x, double p2y) {
        Line l = new Line(p1x, p1y, p2x, p2y);
        return (IsIn(p1x, p1y) ^ IsIn(p2x, p2y)) && (l.IsIn(x1, y1) ^ l.IsIn(x2, y2));
    }

    public synchronized void update(double y) {
        y1 += y;
        y2 += y;
        this.A = y2 - y1;
        this.B = x1 - x2;
        this.C = x2 * y1 - x1 * y2;
    }
}
