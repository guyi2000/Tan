package edu.guyi.bbtan.gamedata;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

import edu.guyi.bbtan.geometryobject.*;
import edu.guyi.bbtan.utilities.Utilities;

import javax.imageio.ImageIO;

public class GameData {
    private static final long slowWaitTimeBetweenUpdates = 10;
    private ArrayList<GeometryObject> boundingBox;
    private ArrayList<Ball> balls;
    private int ball_nums;
    private boolean valid, IsDoneGen, IsEnhanced, IsFutureEnhanced;
    public boolean IsGameOver;
    private LinkedList<boolean[]> container;
    public static int[] xCoord = {
            65, 115, 165, 215, 265, 315
    };
    public static int[] yCoord = {
            130, 180, 230, 280, 330, 380, 430, 480, 530, 580
    };
    private long currentUpdateSpeed;
    private long oldTime;
    public boolean showTool;

    private int score;

    private BufferedImage background, tool;

    public GameData() {
        boundingBox = new ArrayList<>();
        balls = new ArrayList<>();
        ball_nums = 0;
        boundingBox.add(new Border(true, true, 20, 7));
        boundingBox.add(new Border(false, true, 30, 7));
        boundingBox.add(new Border(false, false, 350, 7));
        currentUpdateSpeed = slowWaitTimeBetweenUpdates;
        oldTime = System.nanoTime();
        try {
            background = ImageIO.read(
                    Objects.requireNonNull(GameData.class.getResource("/img/background1.png"))
            );
            tool = ImageIO.read(
                    Objects.requireNonNull(GameData.class.getResource("/img/jiaocheng.png"))
            );
        } catch (Exception e) { System.out.println(e); }
        valid = false; IsDoneGen = false; IsGameOver = false; IsEnhanced = false; IsFutureEnhanced = false;
        score = 0;
        container = new LinkedList<>();
        for(int i = 0; i < 10; i++)
            container.add(new boolean[]{false, false, false, false, false, false});
        showTool = false;
        addBall();
    }

    public synchronized void genBoundingBox() {
        synchronized (boundingBox) {
            for (boolean k : container.getFirst()) {
                if (k) IsGameOver = true;
            }
            if(container.size() == 10) {
                container.remove(0);
                container.add(new boolean[]{false, false, false, false, false, false});
                for (GeometryObject bound : boundingBox) bound.update(-50);
                for (int i = 0; i < 6; i++) {
                    if (Utilities.Property(0.45)) {
                        container.getLast()[i] = true;
                        switch (Utilities.rand.nextInt(3)) {
                            case 0:
                                boundingBox.add(new Circle(
                                        20, xCoord[i], yCoord[9], 7,
                                        Utilities.rand.nextInt(4 * ball_nums) + 2 * ball_nums
                                ));
                                break;
                            case 1:
                                boundingBox.add(new Square(
                                        40, 0.5 * Utilities.rand.nextDouble() - 0.25,
                                        xCoord[i], yCoord[9], 7,
                                        Utilities.rand.nextInt(4 * ball_nums) + 2 * ball_nums
                                ));
                                break;
                            case 2:
                                boundingBox.add(new Triangle(
                                        40, 0.5 * Utilities.rand.nextDouble() - 0.25,
                                        xCoord[i], yCoord[9], 7,
                                        Utilities.rand.nextInt(4 * ball_nums) + 2 * ball_nums
                                ));
                                break;
                        }
                    } else if (Utilities.Property(0.1)) {
                        container.getLast()[i] = true;
                        boundingBox.add(new AddBallTool(
                                xCoord[i], yCoord[9], 7
                        ));
                    } else if (Utilities.Property(0.05)) {
                        container.getLast()[i] = true;
                        boundingBox.add(new EnhanceBallTool(
                                xCoord[i], yCoord[9], 7
                        ));
                    } else if (Utilities.Property(0.01)) {
                        container.getLast()[i] = true;
                        boundingBox.add(new TimeMachine(
                                xCoord[i], yCoord[9], 7
                        ));
                    } else if (Utilities.Property(0.01)) {
                        container.getLast()[i] = true;
                        boundingBox.add(new Boom(
                                xCoord[i], yCoord[9], 7
                        ));
                    }
                }
            } else {
                container.remove(0);
                for (GeometryObject bound : boundingBox) bound.update(-50);
            }
        }
    }

    public synchronized void reset() {
        boundingBox = new ArrayList<>();
        balls = new ArrayList<>();
        ball_nums = 0;
        boundingBox.add(new Border(true, true, 20, 7));
        boundingBox.add(new Border(false, true, 30, 7));
        boundingBox.add(new Border(false, false, 350, 7));
        currentUpdateSpeed = slowWaitTimeBetweenUpdates;
        oldTime = System.nanoTime();
        valid = false; IsDoneGen=false; IsGameOver = false; IsEnhanced = false; IsFutureEnhanced = false;
        score = 0;
        container = new LinkedList<>();
        for(int i = 0; i < 10; i++)
            container.add(new boolean[]{false, false, false, false, false, false});
        addBall();

    }

    public synchronized void boom() {
        synchronized (container) {
            container = new LinkedList<>();
            for(int i = 0; i < 10; i++)
                container.add(new boolean[]{false, false, false, false, false, false});
        }
        synchronized (boundingBox) {
            Iterator<GeometryObject> iterator = boundingBox.iterator();
            while(iterator.hasNext()) {
                GeometryObject o = iterator.next();
                if(!(o instanceof Border) && o.IsTimeMachined == 0) {
                    score += o.RestHitTime;
                    iterator.remove();
                }
            }
        }
    }

    public synchronized void addBall() {
        ball_nums += 1;
    }

    public synchronized void EnhanceBall() {
        IsFutureEnhanced = true;
    }

    public synchronized void ActivateTimeMachine() {
        synchronized (boundingBox) {
            for (GeometryObject bound : boundingBox) {
                if(bound.IsTimeMachined != 0) {
                    bound.IsTimeMachined += 2;
                } else if(bound.y == yCoord[9]) {
                    bound.IsTimeMachined = 2;
                } else if(bound.y == yCoord[8]) {
                    bound.update(50);
                    bound.IsTimeMachined = 1;
                } else {
                    bound.update(100);
                }
            }
        }
        container.add(0, new boolean[]{false, false, false, false, false, false});
        container.add(0, new boolean[]{false, false, false, false, false, false});
    }

    public synchronized void startBall(int x, int y) {
        valid = false;
        double speed_x = 0.7 * (x - 190) / Math.sqrt((x - 190) * (x - 190) + (y - 21) * (y - 21));
        double speed_y = 0.7 * (y - 21) / Math.sqrt((x - 190) * (x - 190) + (y - 21) * (y - 21));
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (balls) {
                    balls.clear();
                }
                if(IsFutureEnhanced) IsEnhanced = true;
                for(int i = 0; i < ball_nums; i++) {
                    synchronized (balls) {
                        Ball ball = new Ball(190, 21, 7, speed_x, speed_y);
                        ball.setHanced(IsEnhanced);
                        balls.add(ball);
                    }
                    try {
                        Thread.sleep(100);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
                IsDoneGen = false;
                if(IsEnhanced) IsFutureEnhanced = false;
                IsEnhanced = false;
            }
        }).start();
    }

    public synchronized void resetTimeOfLastUpdate() {
        oldTime = System.nanoTime();
    }

    public synchronized long getCurrentWaitTimeBetweenUpdates() {
        return currentUpdateSpeed;
    }

    public synchronized void updateData() {
        long elapsedTime = System.nanoTime() - oldTime;
        oldTime = oldTime + elapsedTime;

        synchronized (balls) {
            balls.removeIf(ball -> !ball.IsValid());
            if (balls.size() == 0) {
                valid = true;
                if(!IsDoneGen) {
                    IsDoneGen = true;
                    genBoundingBox();
                }
            }
            for (Ball ball : balls) {
                score += ball.update(boundingBox, elapsedTime);
            }
        }

        synchronized (boundingBox) {
            Iterator<GeometryObject> iterator = boundingBox.iterator();
            while(iterator.hasNext()) {
                GeometryObject o = iterator.next();
                if(!o.IsValid()) {
                    container.get(((int) o.y - 130) / 50)[((int) o.x - 65) / 50] = false;
                    if(o.IsTool) {
                        if(o instanceof AddBallTool) {
                            addBall();
                        } else if(o instanceof Boom) {
                            boom();
                            break;
                        } else if(o instanceof TimeMachine) {
                            iterator.remove();
                            ActivateTimeMachine();
                            break;
                        } else if(o instanceof EnhanceBallTool) {
                            EnhanceBall();
                        }
                    }
                    iterator.remove();
                }
            }
        }
    }

    public synchronized boolean IsValid() {
        return valid;
    }

    public synchronized void drawGameData(Graphics2D drawingBoard,
                                          int drawAreaWidth,
                                          int drawAreaHeight) {
        drawingBoard.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        drawingBoard.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        drawingBoard.drawImage(background, 0, 0, 380, 700, null);

        Graphics circleGraphics = drawingBoard.create();
        if (!showTool) {
            if (!IsGameOver) {
                circleGraphics.setColor(Color.WHITE);
                circleGraphics.setFont(Utilities.MonospaceBasic);
                circleGraphics.drawString("分数：" + score, 10, 20);
                circleGraphics.drawString("小球数量：" + ball_nums, 10, 45);
                for (Ball ball : balls) {
                    ball.draw(circleGraphics);
                }
                for (GeometryObject boud : boundingBox) {
                    boud.draw(circleGraphics);
                }
            } else {
                circleGraphics.setColor(Color.WHITE);
                circleGraphics.setFont(Utilities.GameOverFont);
                circleGraphics.drawString("Game Over", 100, 330);
                circleGraphics.setFont(Utilities.MonospaceBasic);
                circleGraphics.drawString("最终得分：" + score, 130, 370);
                circleGraphics.drawString("点击任意位置开始下一局", 90, 420);
            }
        } else {
            drawingBoard.drawImage(tool,0, 0, 380, 700, null);
        }
        circleGraphics.dispose();
    }
}
