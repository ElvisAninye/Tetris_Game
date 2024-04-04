
import java.awt.*;
import java.util.Random;
import java.util.ArrayList;

public class PlayManager {
    // Main Play Area
    final int WIDTH = 360;
    final int HEIGHT = 600;
    public static int left_x;
    public static int right_x;
    public static int top_y;
    public static int bottom_y;

    // Mino
    Mino currentMino;
    final int MINO_START_x;
    final int MINO_START_Y;
    Mino nextMino;
    final int NEXTMINO_X;
    final int NEXTMINO_Y;
    public static ArrayList<Block> staticBlocks = new ArrayList<>();

    // Others
    public static int dropInterval = 60; // mino drops every 60 frames
    boolean gameOver;
    int gameOverCnt = 0;

    // Effects
    boolean effectCounterOn;
    int effectCounter;

    ArrayList<Integer> effectY = new ArrayList<>();

    // Score

    int level = 1;
    int lines;
    int score;
    public int x;
    public int y;

    public PlayManager() {

        // Main Playe Area Frame
        left_x = (GamePanel.WIDTH / 2) - (WIDTH / 2); // 1280/2 - 360/2 = 460
        right_x = left_x + WIDTH;
        top_y = 50;
        bottom_y = top_y + HEIGHT;

        MINO_START_x = left_x + (WIDTH / 2) - Block.SIZE;
        MINO_START_Y = top_y + Block.SIZE;

        NEXTMINO_X = right_x + 175;
        NEXTMINO_Y = top_y + 500;

        currentMino = pickMino();
        currentMino.setXY(MINO_START_x, MINO_START_Y);
        nextMino = pickMino();
        nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);
    }

    private Mino pickMino() {
        // pick a random mino
        Mino mino = null;
        int i = new Random().nextInt(7);

        switch (i) {
            case 0:
                mino = new Mino_L1();
                break;
            case 1:
                mino = new Mino_L2();
                break;
            case 2:
                mino = new Mino_Square();
                break;
            case 3:
                mino = new Mino_Bar();
                break;
            case 4:
                mino = new Mino_T();
                break;
            case 5:
                mino = new Mino_Z1();
                break;
            case 6:
                mino = new Mino_Z2();
                break;
        }
        return mino;
    }
    

    public void update() {
        if (currentMino.active == false) {
            // if the mino is not active, put it into the staticBlock
            staticBlocks.add(currentMino.b[0]);
            staticBlocks.add(currentMino.b[1]);
            staticBlocks.add(currentMino.b[2]);
            staticBlocks.add(currentMino.b[3]);

            // Check if the game is over
            if (currentMino.b[0].x == MINO_START_x && currentMino.b[0].y == MINO_START_Y) {
                // This means that the currentMino has collided with a block and cant move at
                // all
                // This means x and y are the same with the next Mino
                gameOver = true; 
                GamePanel.music.stop(); 
                GamePanel.se.play(2, false);
            }

            currentMino.deactivating = false;

            // replace current mino with nextmino
            currentMino = nextMino;
            currentMino.setXY(MINO_START_x, MINO_START_Y);
            nextMino = pickMino();
            nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);

            checkDelete(); // If the mino is not active, use this to check if the lines(s) need to be
                           // deleted
        } else {
            currentMino.update();
        }

    }

    private void checkDelete() {
        int x = left_x;
        int y = top_y;
        int blockCount = 0; 
        int lineCount = 0;

        while (x < right_x && y < bottom_y) {

            for (int i = 0; i < staticBlocks.size(); i++) {
                if ((staticBlocks.get(i).x == x) && (staticBlocks.get(i).y == y)) {
                    blockCount++; // Increase if static block

                } 
            } 
          
            x += Block.SIZE;

            if (x == right_x) {

                if (blockCount == 12) { // If the block Count is equal to 12, this means that the line is full so we
                                        // can delete it

                    effectCounterOn = true;
                    effectY.add(y);

                    for (int i = staticBlocks.size() - 1; i > -1; i--) {
                        if (staticBlocks.get(i).y == y) { // Remove all of the blocks in the current y line
                            staticBlocks.remove(i);
                        }

                    } 
                    lineCount++; 
                    lines++; 
                    // Drop Speed 
                    // if the line score  hits a certain number, increase the drop speed 
                    // 1 is the fastest here 
                    if (lines % 10 == 0 && dropInterval > 1) {
                    	
                    	level++; 
                    	if (dropInterval > 10) {
                    		dropInterval -=10;
                    	}else {
                    		dropInterval -=1;
                    	}
                    }
                    
                    // a line has been delete so we need to move down the blocks above it
                    for (int i = 0; i < staticBlocks.size(); i++) {
                        if (staticBlocks.get(i).y < y) {
                            staticBlocks.get(i).y += Block.SIZE;
                        }

                    }
                }

                blockCount = 0;
                x = left_x;
                y += Block.SIZE;
            }

        } 
        if (lineCount > 0) { 
        	GamePanel.se.play(1, false);
        	int singleLineScore = 10 * level; 
        	score += singleLineScore * lineCount;
        }

    }

    public void draw(Graphics2D g2) {
        // Draw Player Area Grid
    g2.setColor(Color.WHITE);
    g2.setStroke(new BasicStroke(1f));

    // Calculate the number of horizontal and vertical lines needed based on block size
    int numHorizontalLines = HEIGHT / Block.SIZE;
    int numVerticalLines = WIDTH / Block.SIZE;

    // Draw horizontal grid lines
    for (int i = 0; i <= numHorizontalLines; i++) {
        int y = top_y + i * Block.SIZE;
        g2.drawLine(left_x, y, right_x, y);
    }

    // Draw vertical grid lines
    for (int j = 0; j <= numVerticalLines; j++) {
        int x = left_x + j * Block.SIZE;
        g2.drawLine(x, top_y, x, bottom_y);
    }
        // Draw Player Area
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(left_x - 4, top_y - 4, WIDTH + 8, HEIGHT + 8);

        // Draw Next Mino Frame
        int x = right_x + 100;
        int y = bottom_y - 200;
        g2.drawRect(x, y, 200, 200);
        g2.setFont(new Font("Arial", Font.PLAIN, 30));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("NEXT", x + 60, y + 60);

        // Draw score frame
        g2.drawRect(x, top_y, 250, 300); 
        x += 40; 
        y = top_y + 90; 
        g2.drawString("LEVEL: " + level,x, y); y+=70; 
        g2.drawString("LINES: " + lines, x, y); y+=70; 
        g2.drawString("SCORE: " + score, x, y); y+=70;

        // Draw currentMino
        if (currentMino != null) {
            currentMino.draw(g2);
        }

        // draw the enst mino
        nextMino.draw(g2);

        // draw static blocks
        for (int i = 0; i < staticBlocks.size(); i++) {
            staticBlocks.get(i).draw(g2);
        }

        // Draw Effect
        if (effectCounterOn) {
            effectCounter++;

            g2.setColor(Color.red);
            for (int i = 0; i < effectY.size(); i++) {
                g2.fillRect(left_x, effectY.get(i), WIDTH, Block.SIZE);
            }

            if (effectCounter == 10) {
                effectCounterOn = false;
                effectCounter = 0;
                effectY.clear();
            }
        }

        // Pause game or game over
        g2.setColor(Color.yellow);
        g2.setFont(g2.getFont().deriveFont(50f));
        if (gameOver) {
            x = left_x + 25;
            y = top_y + 320;
            g2.drawString("GAME OVER", x, y);
            // Introduce a delay before setting inMenu to true
            new Thread(() -> {
                try {
                    Thread.sleep(3000); // Delay for 3 seconds 
                } catch (InterruptedException e) {}
                GamePanel.inMenu = true;
            }).start();
        } else if (KeyHandler.pausePressed) {
            x = left_x + 70;
            y = top_y + 320;
            g2.drawString("PAUSED", x, y);
        }

    }
}
