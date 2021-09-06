import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game extends JPanel {

    private static final long serialVersionUID = 110;
    int crx, cry;
    int car_x, car_y;
    int speedX, speedY;
    int nOpponent;
    String imageLoc[];
    int lx[], ly[];
    int score;
    int speedOpponent[];
    boolean isFinished;
    boolean isUp, isDown, isRight, isLeft;
    Image img;

    public Game() {
        crx = cry = -999;
        addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent e) {
                moveCar(e);
            }

            public void keyReleased(KeyEvent e) {
                stopCar(e);
            }

            public void keyTyped(KeyEvent arg0) {
            }
        });
        setFocusable(true);
        car_x = 5;
        car_y = 350;
        isUp = isDown = isLeft = isRight = false;
        speedX = speedY = 0;
        nOpponent = 0;
        lx = new int[20];
        ly = new int[20];
        imageLoc = new String[20];
        speedOpponent = new int[20];
        isFinished = false;
        score = 0;
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D obj = (Graphics2D) g;
        obj.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        try {
            Image img_st = new ImageIcon(this.getClass().getResource("/img/st_road.png")).getImage();
            Image img_cross = new ImageIcon(this.getClass().getResource("/img/cross_road.png")).getImage();
            Image img_carself = new ImageIcon(this.getClass().getResource("/img/car_self.png")).getImage();
            Image img_boom = new ImageIcon(this.getClass().getResource("/img/boom.png")).getImage();
            Image img_carleft;
            String str;
            obj.drawImage(img_st, 0, 0, this);
            if (cry >= -499 && crx >= -499)
                obj.drawImage(img_cross, crx, cry, this);
            obj.drawImage(img_carself, car_x, car_y, this);
            if (isFinished) {
                obj.drawImage(img_boom, car_x - 60, car_y - 60, this);
            }
            if (this.nOpponent > 0) {
                for (int i = 0; i < this.nOpponent; i++) {
                    str = this.imageLoc[i];
                    img_carleft = new ImageIcon(this.getClass().getResource(str)).getImage();
                    obj.drawImage(img_carleft, this.lx[i], this.ly[i], this);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void moveRoad(int count) {
        if (crx == -999 && cry == -999) {

            crx = 499;
            cry = 0;

        } else {
            crx--;
        }
        if (crx == -499 && cry == 0) {
            crx = cry = -999;
        }
        car_x += speedX;
        car_y += speedY;

        if (car_x < 0)
            car_x = 0;

        if (car_x + 93 >= 500)
            car_x = 500 - 93;

        if (car_y <= 124)
            car_y = 124;

        if (car_y >= 364 - 50)
            car_y = 364 - 50;

        for (int i = 0; i < this.nOpponent; i++) {
            this.lx[i] -= speedOpponent[i];
        }

        int index[] = new int[nOpponent];
        for (int i = 0; i < nOpponent; i++) {
            if (lx[i] >= -127) {
                index[i] = 1;
            }
        }
        int c = 0;
        for (int i = 0; i < nOpponent; i++) {
            if (index[i] == 1) {
                imageLoc[c] = imageLoc[i];
                lx[c] = lx[i];
                ly[c] = ly[i];
                speedOpponent[c] = speedOpponent[i];
                c++;
            }
        }

        score += nOpponent - c;
        nOpponent = c;

        for (int i = 0; i < nOpponent; i++) {
            if ((ly[i] >= car_y && ly[i] <= car_y + 46) || (ly[i] + 46 >= car_y && ly[i] + 46 <= car_y + 46)) {
                if (car_x + 87 >= lx[i] && !(car_x >= lx[i] + 87)) {
                    this.finish();
                }
            }
        }
    }

    void finish() {
        isFinished = true;
        this.repaint();
        JOptionPane.showMessageDialog(this, "Game Over!!!\nYour Score : " + score, "Game Over",
                JOptionPane.YES_NO_OPTION);
        System.exit(ABORT);
    }

    public void moveCar(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            isUp = true;
            speedX = 1;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            isDown = true;
            speedX = -2;
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            isRight = true;
            speedY = 1;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            isLeft = true;
            speedY = -1;
        }
    }

    public void stopCar(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            isUp = false;
            speedX = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            isDown = false;
            speedX = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            isLeft = false;
            speedY = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            isRight = false;
            speedY = 0;
        }
    }

    public static void main(String args[]) {
        Game game = new Game();
        JFrame frame = new JFrame("Race");
        frame.add(game);
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int count = 1, c = 1;
        while (true) {
            game.moveRoad(count);
            while (c <= 1) {
                game.repaint();
                try {
                    Thread.sleep(5);
                } catch (Exception e) {
                    System.out.println(e);
                }
                c++;
                count++;
            }
            c = 1;

            if (game.nOpponent < 4 && count % 200 == 0) {
                game.imageLoc[game.nOpponent] = "/img/car_left_" + ((int) ((Math.random() * 100) % 3) + 1) + ".png";
                game.lx[game.nOpponent] = 499;
                int p = (int) (Math.random() * 100) % 4;
                if (p == 0) {
                    p = 250;
                } else if (p == 1) {
                    p = 300;
                } else if (p == 2) {
                    p = 185;
                } else {
                    p = 130;
                }
                game.ly[game.nOpponent] = p;
                game.speedOpponent[game.nOpponent] = (int) (Math.random() * 100) % 2 + 2;
                game.nOpponent++;
            }
        }
    }
}
