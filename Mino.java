import java.awt.*;

public class Mino {
    public Block b[] = new Block[4];
    public Block tempB[] = new Block[4];
    int autoDropCounter = 0;
    public int direction = 1;
    boolean leftCollision, rightCollision, bottomCollision;
    public boolean active = true;
    public boolean deactivating;
    int deactivateCounter = 0;

    public void create(Color c) {
        b[0] = new Block(c);
        b[1] = new Block(c);
        b[2] = new Block(c);
        b[3] = new Block(c);
        tempB[0] = new Block(c);
        tempB[1] = new Block(c);
        tempB[2] = new Block(c);
        tempB[3] = new Block(c);
    }

    public void setXY(int x, int y) {
    }

    public void updateXY(int direction) {

        checkRotationCollision();

        if (leftCollision == false && rightCollision == false && bottomCollision == false) {

            this.direction = direction;
            b[0].x = tempB[0].x;
            b[0].y = tempB[0].y;
            b[1].x = tempB[1].x;
            b[1].y = tempB[1].y;
            b[2].x = tempB[2].x;
            b[2].y = tempB[2].y;
            b[3].x = tempB[3].x;
            b[3].y = tempB[3].y;
        }

    }

    public void getDirection1() {
    }

    public void getDirection2() {
    }

    public void getDirection3() {
    }

    public void getDirection4() {
    }

    public void checkMovementCollision() {
        // Reset collision flags
        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        // check static block collision
        checkStaticBlockCollision();

        // Check frame collision
        // Left wall
        for (int i = 0; i < b.length; i++) {
            if (b[i].x == PlayManager.left_x) {
                leftCollision = true;
            }
        }

        // Right wall
        for (int i = 0; i < b.length; i++) {
            if (b[i].x + Block.SIZE == PlayManager.right_x) {
                rightCollision = true;
            }
        }

        // Bottom floor
        for (int i = 0; i < b.length; i++) {
            if (b[i].y + Block.SIZE == PlayManager.bottom_y) {
                bottomCollision = true;
            }
        }
    }

    public void checkRotationCollision() {

        // Reset collision flags
        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        // check static block collision
        checkStaticBlockCollision();

        // Check frame collision
        // Left wall
        for (int i = 0; i < b.length; i++) {
            if (tempB[i].x < PlayManager.left_x) {
                leftCollision = true;
            }
        }

        // Right wall
        for (int i = 0; i < b.length; i++) {
            if (tempB[i].x + Block.SIZE > PlayManager.right_x) {
                rightCollision = true;
            }
        }

        // Bottom floor
        for (int i = 0; i < b.length; i++) {
            if (tempB[i].y + Block.SIZE > PlayManager.bottom_y) {
                bottomCollision = true;
            }
        }

    }

    private void checkStaticBlockCollision() {
        for (int ii = 0; ii < b.length; ii++) {
            int currentX = b[ii].x;
            int currentY = b[ii].y;
    
            // Check if there's a static block below the current block
            for (Block staticBlock : PlayManager.staticBlocks) {
                if (currentX == staticBlock.x && currentY + Block.SIZE == staticBlock.y) {
                    bottomCollision = true;
                }
            }
    
            // Check if there's a static block to the left of the current block
            for (Block staticBlock : PlayManager.staticBlocks) {
                if (currentX - Block.SIZE == staticBlock.x && currentY == staticBlock.y) {
                    leftCollision = true;
                }
            }
    
            // Check if there's a static block to the right of the current block
            for (Block staticBlock : PlayManager.staticBlocks) {
                if (currentX + Block.SIZE == staticBlock.x && currentY == staticBlock.y) {
                    rightCollision = true;
                }
            }
        }
    }
    

    public void update() {

        if (deactivating) {
            deactivating();
        }

        // move mino
        if (KeyHandler.upPressed) {
            switch (direction) {
                case 1:
                    getDirection2();
                    break;
                case 2:
                    getDirection3();
                    break;
                case 3:
                    getDirection4();
                    break;
                case 4:
                    getDirection1();
                    break;
            }

            KeyHandler.upPressed = false; 
            GamePanel.se.play(3, false);
        }

        checkMovementCollision();

        if (KeyHandler.downPressed) {
            // If the mino's bottom is not hitting,
            if (bottomCollision == false) {
                // Move each block down
                b[0].y += Block.SIZE;
                b[1].y += Block.SIZE;
                b[2].y += Block.SIZE;
                b[3].y += Block.SIZE;
                // Reset the autoDropCounter
                autoDropCounter = 0;
            }
            // Reset the downPressed flag
            KeyHandler.downPressed = false;
        }

        if (KeyHandler.leftPressed) {
            // If left collision is false, move each block to the left
            if (leftCollision == false) {
                b[0].x -= Block.SIZE;
                b[1].x -= Block.SIZE;
                b[2].x -= Block.SIZE;
                b[3].x -= Block.SIZE;
            }
            // Reset the leftPressed flag
            KeyHandler.leftPressed = false;
        }

        if (KeyHandler.rightPressed) {
            // If right collision is false, move each block to the right
            if (rightCollision == false) {
                b[0].x += Block.SIZE;
                b[1].x += Block.SIZE;
                b[2].x += Block.SIZE;
                b[3].x += Block.SIZE;
            }
            // Reset the rightPressed flag
            KeyHandler.rightPressed = false;
        }
        if (bottomCollision) { 
        	if (deactivating == false) {
        		GamePanel.se.play(4, false);
        	}
            deactivating = true;
        } else {
            autoDropCounter++;
            if (autoDropCounter == PlayManager.dropInterval) {
                b[0].y += Block.SIZE;
                b[1].y += Block.SIZE;
                b[2].y += Block.SIZE;
                b[3].y += Block.SIZE;
                autoDropCounter = 0;
            }

        }

    }

    public void deactivating() {
        deactivateCounter++;

        // Wait 45 frames until deactivation
        if (deactivateCounter == 45) {
            deactivateCounter = 0;
            checkMovementCollision(); // To check if bottom is still hitting

            // Deactivate the mino if it is still hitting after 45 frames
            if (bottomCollision) {
                active = false;
            }
        }

    }

    public void draw(Graphics2D g2) {

        int margin = 2;
        g2.setColor(b[0].c);
        g2.fillRect(b[0].x + margin, b[0].y + margin, Block.SIZE - (margin * 2), Block.SIZE - (margin * 2));
        g2.fillRect(b[1].x + margin, b[1].y + margin, Block.SIZE - (margin * 2), Block.SIZE - (margin * 2));
        g2.fillRect(b[2].x + margin, b[2].y + margin, Block.SIZE - (margin * 2), Block.SIZE - (margin * 2));
        g2.fillRect(b[3].x + margin, b[3].y + margin, Block.SIZE - (margin * 2), Block.SIZE - (margin * 2));
    }
}
