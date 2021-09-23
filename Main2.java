import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.*;

public class Main2 {
    
    public static void main(String[] args) {
        Program program = new Program();
        program.run();
    }
}

class Program {
    private JFrame mainFrame;
    private DrawPanel drawPanel;
    private java.util.List<Ball> balls;
    private java.util.List<Nanost> nanosts;

    private int windowWidth = 960;
    private int windowHeight = 700;
    private String windowLabel = "Brownian Reaction";

    void run() {
        long timeNow = System.currentTimeMillis();
        balls = new ArrayList<>();
        nanosts = new ArrayList<>();
        Random r = new Random ();
        r.setSeed ( timeNow );

        /* Generate balls */
        for (int i = 0; i < 250; i++) {
            
            Ball ball = new Ball(
                    /* Random positions from 0 to windowWidth or windowHeight */
                    (int) Math.floor((Math.random() * windowWidth)),
                    (int) Math.floor((Math.random() * windowHeight/2)),
                    /*(r.nextInt () * windowWidth/1000),
                     (r.nextInt () * windowHeight/1000),*/
                    
                    /* Random size from 10 to 30 */
                    /*(int) Math.floor(Math.random() * 20) + 10,*/
                    /* Modificado para Gerardinho */
                    10,
                    /* Random RGB colors*/
                    new Color(
                            /*(int) Math.floor((Math.random() * 256)),
                            (int) Math.floor((Math.random() * 256)),
                            (int) Math.floor((Math.random() * 256))*/
                            /* Modificado para Gerardinho */
                            10,10,10

                    ),
                    /* Random velocities from -5 to 5 */
                    /*(int) Math.floor((Math.random() * 10) - 5),
                    (int) Math.floor((Math.random() * 10) - 5) */
                    /* velocities )*/
                    5, 5
                );

            balls.add(ball);
        }
        /* Generate nanostuctures */
        for (int i = 0; i < 500; i++) {
            
            Nanost nanost = new Nanost(
                    /* Random positions from 0 to windowWidth or windowHeight */
                     (int)((1 - Math.random() )* windowWidth),
                    (int) ( windowHeight*(0.95 - (0.2* Math.random())) ),
                    
                    10,new Color( 10,10,255)
            );
            nanosts.add(nanost);
        } 

     /* Initialize program */
        mainFrame = new JFrame();
        drawPanel = new DrawPanel();
        mainFrame.getContentPane().add(drawPanel);
        mainFrame.setTitle(windowLabel);
        mainFrame.setSize(windowWidth, windowHeight+40);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        while (true) {
            for (Ball b: balls){ // Ball es la clase, balls el array definido antes
                 b.update();
            }

            for (Nanost ns: nanosts) {
                ns.update();
            } 
           
            /* Give Swing 10 milliseconds to see the update! */
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mainFrame.repaint();
        }
    }

    class DrawPanel extends JPanel {
        @Override
        public void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            for (Ball b: balls) {
                b.draw(graphics);
            }
            for (Nanost ns: nanosts) {
                ns.draw(graphics);
            }
        }
    }

    class Ball {
        public int posX, posY, size;
        public Color color;

        private int vx = 2;
        private int vy = 2;

        public Ball(int posX, int posY, int size, Color color, int vx, int vy) {
            this.posX = posX;
            this.posY = posY;
            this.size = size;
            this.color = color;
            this.vx = vx;
            this.vy = vy;
        }

        void update() {
           
            /* ....  */
            if (posX > windowWidth || posX < 0) {
                vx *= - 1 ;
            }

            if (posY > windowHeight || posY < 0) {
                vy *= -1 ;
            }
            if (posX < 0) {
                posX = 0;
            }

            if (posY > windowHeight) {
                posY = windowHeight;
            }

            if (posX > windowWidth) {
                posX = windowWidth;
            }
            
            if (posY < 0) {
                posY = 0;
            }

            this.posY += vy;
            this.posX += vx;         
            for (Ball b: balls) {
                if  (this != b ){
                    if  ( this.posX == b.posX  && this.posY == b.posY) {
                        this.posX -= this.vx ;  
                        this.posY -= this.vy ; 
                        b.posX -= b.vx ;  
                        b.posY -= b.vy ; 
                        this.vx= b.vx;
                        this.vy= b.vy;
                        b.vx= this.vx;
                        b.vy= this.vy;
                    }
                }
            }
        }

        void draw(Graphics g) {
            g.setColor(color);
            g.fillOval(posX, posY, size, size);
        }
    } // Ball

    class Nanost {
        public int posX, posY, size;
        public Color color;

        public Nanost (int posX, int posY, int size, Color color) {
            this.posX = posX;
            this.posY = posY;
            this.size = size;
            this.color = color;
        }

        void update() {
         /* ....  */
            for (Ball b: balls) {
                if  (Math.abs(this.posX - b.posX)<1 && Math.abs(this.posY - b.posY)<1  && this.color != b.color){
                        this.color = new Color(255,0,0 );
                        b.color = new Color(0,255,0 );
                        b.vx=0; b.vy=0;
                        //balls.remove(Objects.equals(b, get(i)));
                }
            }
        }

        void draw(Graphics g) {
            g.setColor(color);
            g.fillOval(posX, posY, size, size);
        }
    } //Nanost
}