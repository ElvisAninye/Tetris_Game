import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseInput extends MouseAdapter {
    
    @Override
    public void mouseClicked(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        
        // Check if the mouse click occurred within the bounds of the play button
        if (GamePanel.inMenu && GamePanel.menu.playButton.contains(mouseX, mouseY)) {
            GamePanel.inMenu = false;
        }
        if (GamePanel.inMenu && GamePanel.menu.quitButton.contains(mouseX, mouseY)) {
            System.exit(0);
        }
    }
}
