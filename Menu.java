import java.awt.*;

public class Menu {
    // Calculate center coordinates
    public static int centerX = GamePanel.WIDTH / 2;
    public static int centerY = GamePanel.HEIGHT / 2;

    // Adjust playButton coordinates relative to the center
    public Rectangle playButton = new Rectangle(centerX - 50, centerY - 25, 100, 50);
    public Rectangle quitButton = new Rectangle(centerX - 50, centerY + 55, 100, 50);

    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        Font fnt0 = new Font("arial", Font.BOLD, 50);
        g.setFont(fnt0);
        g.setColor(Color.YELLOW);
        g.drawString("TETRIS", centerX - 180, centerY - 50);
        g.setColor(Color.RED);
        g.drawString("GAME", centerX + 10, centerY - 50);
        g.setColor(Color.WHITE);
        Font fnt1 = new Font("arial", Font.BOLD, 30);
        g.setFont(fnt1);
        g.drawString("Play", playButton.x + 19, playButton.y + 35);
        g2d.draw(playButton);
        g.drawString("Quit", quitButton.x + 19, quitButton.y + 35);
        g2d.draw(quitButton);
    }
}
