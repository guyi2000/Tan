package edu.guyi.bbtan.geometryobject;

import edu.guyi.bbtan.utilities.Utilities;

import java.awt.*;

public class AddBallTool extends ToolBox {
    public AddBallTool(double x, double y, double ball_radius) {
        super(x, y, ball_radius);
    }

    @Override
    public synchronized void draw(Graphics g) {
        synchronized (g) {
            if(IsTimeMachined == 0) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.RED);
                g2d.setStroke(new BasicStroke(3F));
                g2d.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );
                g2d.drawOval((int) x - 10, (int) y - 10, 2 * 10, 2 * 10);
                g2d.setColor(Color.WHITE);
                g2d.setFont(Utilities.MonospaceBasic);
                g2d.drawString("+", (int) x - 4, (int) y + 6);
            }
        }
    }
}
