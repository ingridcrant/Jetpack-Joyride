import java.awt.*;
import javax.swing.*;

public class Scientist extends Rectangle {

    private static final Image scientistWalking1 = new ImageIcon("images/scientist-stationary.png").getImage();
    private static final Image scientistWalking2 = new ImageIcon("images/scientist-moving.png").getImage();
    private static final Image scientistCrouching = new ImageIcon("images/scientist-crouching.png").getImage();
    private static final Image scientistFainting = new ImageIcon("images/scientist-fainting.png").getImage();

    private int x, y;
    private int width;

    private boolean walking, crouching, fainting;
    private static int maxWalkingPoseCount = 4;
    private int walkingPoseCount = 0;

    public Scientist() {
        walking = true;
        crouching = false;
        fainting = false;

        width = scientistWalking1.getWidth(null);
        height = scientistWalking1.getHeight(null);

        x = JetpackJoyridePanel.WIDTH;
        y = JetpackJoyridePanel.HEIGHT - width - 21;
    }

    public void move() {
        x -= 10;
    }

    // if Barry is approaching the scientist
    public void barryApproaching() {
        if(JetpackJoyridePanel.barry.getY() > y - JetpackJoyridePanel.barry.getHeight() && fainting == false) { // if Barry is on the same plane as the scientist and the scientist has not already fainted
            walking = false;
            crouching = true; // makes scientist crouch
            fainting = false;
        }
    }

    // if scientist is hit by Barry
    public void hitByBarry() {
        if(this.intersects(JetpackJoyridePanel.barry)) {
            walking = false;
            crouching = false;
            fainting = true; // makes scientist faint
        }
    }

    public void draw(Graphics g) {
        if(walking) {
            walkingPoseCount++;
            if(walkingPoseCount > maxWalkingPoseCount/2) {
                g.drawImage(scientistWalking1, x, y, null);
            }
            else {
                g.drawImage(scientistWalking2, x, y, null);
            }
            if(walkingPoseCount > maxWalkingPoseCount) {
                walkingPoseCount = 0;
            }
        }
        else if(crouching) {
            g.drawImage(scientistCrouching, x, y, null);
        }
        else if(fainting) {
            g.drawImage(scientistFainting, x, y, null);
        }
    }
}