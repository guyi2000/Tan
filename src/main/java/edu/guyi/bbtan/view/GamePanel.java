package edu.guyi.bbtan.view;

import javax.swing.*;
import java.awt.event.*;

import edu.guyi.bbtan.gamedata.GameData;
import edu.guyi.bbtan.utilities.Utilities;

public class GamePanel extends JPanel {
    private GameData gameData;

    public GamePanel(GameData gameData) {
        this.gameData = gameData;
        setLayout(null);
        setBounds(0,0,380,700);
        setOpaque(false);

        JButton menu = new JButton("显示菜单");
        menu.setBounds(280, 0, 100, 30);
        menu.setOpaque(false);
        add(menu);

        JPanel gameMenu = new JPanel();
        gameMenu.setLayout(null);
        gameMenu.setBounds(0, 0, 380, 700);
        gameMenu.setVisible(false);
        gameMenu.setOpaque(false);
        add(gameMenu);

        JButton gameReset = new JButton("重新开始");
        gameReset.setBounds(140,250, 100, 30);
        gameReset.setOpaque(false);
        gameMenu.add(gameReset);

        JButton gameTool = new JButton("操作提示");
        gameTool.setBounds(140, 330, 100, 30);
        gameTool.setOpaque(false);
        gameMenu.add(gameTool);

        menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(menu.getText().equals("显示菜单")) {
                    gameMenu.setVisible(true);
                    menu.setText("隐藏菜单");
                } else {
                    gameMenu.setVisible(false);
                    menu.setText("显示菜单");
                }
            }
        });

        gameReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameData.reset();
                gameMenu.setVisible(false);
                menu.setText("显示菜单");
            }
        });

        gameTool.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameData.showTool = true;
                gameMenu.setVisible(false);
                menu.setText("显示菜单");
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(gameData.showTool) {
                    gameData.showTool = false;
                } else {
                    if (e.getButton() == MouseEvent.BUTTON1 &&
                            !gameData.IsGameOver &&
                            gameData.IsValid()) {
                        int x = e.getX();
                        int y = e.getY();
                        if (y > 21)
                            gameData.startBall(x, y);
                    }

                    if (e.getButton() == MouseEvent.BUTTON3) {
                        gameData.showTool = true;
                    }
                    if (gameData.IsGameOver) {
                        gameData.reset();
                    }
                }
            }
        });
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE) {
                    if(!gameData.IsGameOver && gameData.IsValid())
                        gameData.startBall(
                                Utilities.rand.nextInt(360) + 10,
                                Utilities.rand.nextInt(580) + 60
                        );
                    if(gameData.IsGameOver) {
                        gameData.reset();
                    }
                }
            }
        });
    }
}
