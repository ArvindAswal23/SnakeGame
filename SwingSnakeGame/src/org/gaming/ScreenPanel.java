package org.gaming;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class ScreenPanel extends JPanel implements ActionListener {
    private final int SCREEN_WIDTH = 300;
    private final int SCREEN_HEIGHT = 300;
    private final int DOT_ICON_SIZE = 10;
    private final int DOT_ICONS = 900;
    private final int RAND_POS = 29;
    private final int DELAY = 140;

    private final int x[] = new int[DOT_ICONS];
    private final int y[] = new int[DOT_ICONS];

    private int parts;
    private int score=0;
    private static int pscore;
    private int apple_x;
    private int apple_y;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inSnakeGame = true;
    private Timer timer;
    private Image snakepart;
    private Image apple;
    private Image snakehead;

    public ScreenPanel() {
        
        initScreenPanel();
    }
    
    private void initScreenPanel() {

        addKeyListener(new GameKeyAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        loadIcons();
        gameScore();
        initSnakeGame();
        
    }

    private void loadIcons() {

        ImageIcon iisnakepart = new ImageIcon("src/resources/snakepart.png");
        snakepart = iisnakepart.getImage();

        ImageIcon iiapple = new ImageIcon("src/resources/apple.png");
        apple = iiapple.getImage();

        ImageIcon iisnakehead = new ImageIcon("src/resources/snakehead.png");
        snakehead = iisnakehead.getImage();
    }

    private void initSnakeGame() {

        parts = 5;

        for (int z = 0; z < parts; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }
        
        locateApple();

        timer = new Timer(DELAY, this);
        timer.start();
    }
    
    private void gameScore() {
    	JLabel scoreLabel = new JLabel("Score: 0");
    	
    	scoreLabel.setText("Score: " + score);
    	
    	
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    
    private void doDrawing(Graphics g) {
        
        if (inSnakeGame) {

            g.drawImage(apple, apple_x, apple_y, this);

            for (int z = 0; z < parts; z++) {
                if (z == 0) {
                    g.drawImage(snakehead, x[z], y[z], this);
                } else {
                    g.drawImage(snakepart, x[z], y[z], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();

        } else {
        	addKeyListener(new tryAgain());
        	tryAgain t = new tryAgain();
            t.endGame(g);
            

        }        
    }
    
    private void findAppleIcon() {
    	

        if ((x[0] == apple_x) && (y[0] == apple_y)) {

            parts++;
            score++;
            pscore++;
            locateApple();
            gameScore();
        }
    }

    private void shift() {

        for (int z = parts; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (leftDirection) {
            x[0] -= DOT_ICON_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_ICON_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_ICON_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_ICON_SIZE;
        }
    }

    private void findCollision() {

        for (int z = parts; z > 0; z--) {

            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inSnakeGame = false;
            }
        }

        if (y[0] >= SCREEN_HEIGHT) {
            inSnakeGame = false;
        }

        if (y[0] < 0) {
            inSnakeGame = false;
        }

        if (x[0] >= SCREEN_WIDTH) {
            inSnakeGame = false;
        }

        if (x[0] < 0) {
            inSnakeGame = false;
        }
        
        if (!inSnakeGame) {
            timer.stop();
        }
    }

    private void locateApple() {

        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * DOT_ICON_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_ICON_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if (inSnakeGame) {

            findAppleIcon();
            findCollision();
            shift();
        }

        repaint();
    }

    private class GameKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent event) {

            int key = event.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }

            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }

            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
            
            if (key == KeyEvent.VK_P) {
            	timer.stop();

            	
            }
            if (key == KeyEvent.VK_R) {
            	timer.start();
         
            }
            
        }
    }
    private class tryAgain extends KeyAdapter{
    	 @Override
         public void keyPressed(KeyEvent event) {

             int key = event.getKeyCode();
             
             if (key == KeyEvent.VK_Y) {
             
            		SnakeGame.main(null);
                   } 
             if(key ==  KeyEvent.VK_T) {
            	 
            	timer.start();
             }
    	 }
    	 public void endGame(Graphics g) {	
    	        String msg = "Game Over";
    	        String msg1 ="Try Again ? Y/N"; 
    	        String Score1 = Integer.toString(score);
    	        String Pscore = Integer.toString(pscore);
    	        Font small = new Font("Helvetica", Font.BOLD, 14);
    	        FontMetrics metr = getFontMetrics(small);
    	        g.setColor(Color.white);
    	        g.setFont(small);
    	        g.drawString(msg, (SCREEN_WIDTH - metr.stringWidth(msg)) / 2, SCREEN_HEIGHT / 2);
    	        g.drawString(msg1, (SCREEN_WIDTH - metr.stringWidth(msg)) / 2, (SCREEN_HEIGHT / 2)+10);
    	        g.drawString("Your Score",10,10);
    	        g.drawString(Score1,95,10);
    	        g.drawString(Pscore,10,20 );
    	 
    	 }
    	
    	
    	
    }
    
    
    
}
