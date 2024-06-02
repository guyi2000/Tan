package edu.guyi.bbtan.view;

import edu.guyi.bbtan.bgm.Music;
import edu.guyi.bbtan.clock.GameUpdater;
import edu.guyi.bbtan.gamedata.GameData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferStrategy;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class MainWindowView extends JFrame implements ActionListener {
    public JButton startGame;
    public JPanel contentPane;
    public JPanel mainPanel;
    public JPanel gameControlPanel;
    public JPanel gamePanel;
    private GameUpdater gameUpdater;


    public MainWindowView() {
        setTitle("最强弹一弹");
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        setLayout(null);
        // Add background picture
        ImageIcon background = new ImageIcon(
                Objects.requireNonNull(MainWindowView.class.getResource("/img/background.png"))
        );
        JLabel jl_bg = new JLabel(background);
        jl_bg.setLayout(null);
        jl_bg.setBounds(0, 0, 380, 700);
        getLayeredPane().add(jl_bg, Integer.valueOf(Integer.MIN_VALUE));
        contentPane = (JPanel)getContentPane();
        contentPane.setOpaque(false);
        contentPane.setLayout(null);

        // Add some basic panel
        mainPanel = getMainPanel();
        mainPanel.setOpaque(false);
        contentPane.add(mainPanel);

        gameControlPanel = getGameControlPanel();
        gameControlPanel.setOpaque(false);
        contentPane.add(gameControlPanel);

        // Set size and cannot modify
        setSize(380, 700);
        Insets insets = this.getInsets();
        int insetWide = insets.left + insets.right;
        int insetTall = insets.top + insets.bottom;
        setSize(getWidth() + insetWide,
                getHeight() + insetTall);
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        new Music().playMusic();
                    }
                }
        ).start();
    }

    private JPanel getMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        ImageIcon title = new ImageIcon(
                Objects.requireNonNull(MainWindowView.class.getResource("/img/title.png"))
        );
        JLabel jl_tt = new JLabel(title);
        jl_tt.setLayout(null);
        jl_tt.setBounds(0,0, title.getIconWidth(), title.getIconHeight());
        jl_tt.setOpaque(false);
        mainPanel.add(jl_tt);
        mainPanel.setBounds(0, 100, 380, 150);
        return mainPanel;
    }

    private JPanel getGameControlPanel() {
        JPanel gameControlPanel = new JPanel();
        gameControlPanel.setLayout(new BorderLayout(0, 5));

        ImageIcon start = new ImageIcon(
                Objects.requireNonNull(MainWindowView.class.getResource("/img/start.png"))
        );
        startGame = new JButton(start);
        gameControlPanel.add(startGame);
        gameControlPanel.setBounds(95, 400, 190, 75);
        startGame.setOpaque(false);
        startGame.setBorderPainted(false);
        startGame.setContentAreaFilled(false);
        startGame.setFocusPainted(false);

        startGame.addActionListener(this);
        return gameControlPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj == (Object) startGame) {
            configGamePanel();
            start();
        }
    }

    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                gameUpdater.updateLoop();
            }
        }).start();
    }

    private void configGamePanel() {
        // Remove all panel
        contentPane.remove(gameControlPanel);
        contentPane.remove(mainPanel);
        contentPane.repaint();

        // Create gameData
        GameData gameData = new GameData();
        BlockingQueue<BufferStrategy> bufferStrategyQueue = new ArrayBlockingQueue<>(1);
        gameUpdater = new GameUpdater(gameData, bufferStrategyQueue);

        // Create main game panel
        setIgnoreRepaint(true);
        gamePanel = new GamePanel(gameData);
        gamePanel.setOpaque(false);
        contentPane.add(gamePanel);

        // Double buffer
        createBufferStrategy(2);
        try {
            bufferStrategyQueue.put(this.getBufferStrategy());
        } catch (Exception e) {
            e.printStackTrace();
        }
        gameUpdater.setComponentToDraw(contentPane);
    }
}