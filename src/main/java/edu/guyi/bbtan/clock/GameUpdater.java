package edu.guyi.bbtan.clock;

import edu.guyi.bbtan.gamedata.GameData;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.*;

public class GameUpdater {
    private final GameData gameData;
    private final BlockingQueue<BufferStrategy> bufferStrategyQueue;
    private BufferStrategy bufferStrategy;
    private Component componentToDraw;
    private final Rectangle drawAreaBounds;
    private final BufferedImage drawing;

    public GameUpdater(GameData gameData, BlockingQueue<BufferStrategy>
            bufferStrategyQueue) {
        this.gameData = gameData;
        this.bufferStrategyQueue = bufferStrategyQueue;
        this.drawAreaBounds = new Rectangle(7, 30, 380,
                700);

        drawing = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice().getDefaultConfiguration()
                .createCompatibleImage(380, 700);

    }

    public synchronized void setComponentToDraw(Component componentToDraw) {
        this.componentToDraw = componentToDraw;
    }

    public void updateLoop() {
        try {
            bufferStrategy = bufferStrategyQueue.poll(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        gameData.resetTimeOfLastUpdate();

        while (true) {
            long nanoTimeAtStartOfUpdate = System.nanoTime();

            gameData.updateData();
            try {
                Graphics2D g = (Graphics2D) bufferStrategy
                        .getDrawGraphics();
                drawGame(g);
                g.dispose();
                if (!bufferStrategy.contentsLost()) {
                    bufferStrategy.show();
                }
            }
            catch (IllegalStateException e) {
                e.printStackTrace();
            }

            waitUntilNextUpdate(nanoTimeAtStartOfUpdate);
        }
    }

    private synchronized void drawGame(Graphics2D g) {
        Graphics2D drawingBoard = drawing.createGraphics();
        gameData.drawGameData(drawingBoard, drawing.getWidth(),
                drawing.getHeight());
        drawingBoard.dispose();

        final Graphics swingAndOtherGuiGraphics = g.create();
        synchronized (drawAreaBounds) {
            swingAndOtherGuiGraphics.translate(drawAreaBounds.x,
                    drawAreaBounds.y);

            Graphics gameGraphics = g.create();
            gameGraphics.drawImage(drawing, drawAreaBounds.x,
                    drawAreaBounds.y, drawAreaBounds.width,
                    drawAreaBounds.height, null);
            gameGraphics.dispose();
        }
        if (componentToDraw != null) {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        if (componentToDraw instanceof JComponent) {
                            ((JComponent) componentToDraw).paintComponents
                                    (swingAndOtherGuiGraphics);
                        } else {
                            componentToDraw.paintAll
                                    (swingAndOtherGuiGraphics);
                        }
                    }
                });
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        swingAndOtherGuiGraphics.dispose();
    }

    private void waitUntilNextUpdate(long nanoTimeCurrentUpdateStartedOn) {
        long currentUpdateSpeed = gameData
                .getCurrentWaitTimeBetweenUpdates();
        if (currentUpdateSpeed > 0) {
            long timeToSleep = currentUpdateSpeed -
                    ((System.nanoTime() -
                            nanoTimeCurrentUpdateStartedOn)
                            / 10000000);
            timeToSleep = Math.max(timeToSleep, 0);
            if (timeToSleep > 0) {
                try {
                    Thread.sleep(timeToSleep);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
