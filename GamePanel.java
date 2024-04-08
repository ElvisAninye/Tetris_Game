import java.awt.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable {
    public static final int WIDTH = 1280;
    public static int HEIGHT = 720;
    final int FPS = 60;
    Thread gameThread;
    PlayManager pm; 
    
    public static Sound music = new Sound(); 
    public static Sound se = new Sound();
    
    // New attributes for menu
    static boolean inMenu = true;
    static Menu menu;


    

    public GamePanel() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.blue.darker().darker().darker().darker().darker());
        this.setLayout(null);

        // KeyListener
        this.addKeyListener(new KeyHandler());
        this.addMouseListener(new MouseInput());
        this.setFocusable(true);

        pm = new PlayManager();
        
        // Initialize menu
        menu = new Menu();
    }

    public void launchGame() {
        gameThread = new Thread(this);
        gameThread.start(); 
        
        music.play(0, true); 
        music.loop();
    }

    @Override
    public void run() {
        // Game loop
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }

    }

    private void update() {
        if (inMenu) {} 
        else {
            if (KeyHandler.pausePressed == false && pm.gameOver == false) {
                pm.update();
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        if (inMenu) {
            menu.render(g2);
        } else {
            pm.draw(g2);
        }
    }
}
