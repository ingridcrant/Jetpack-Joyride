import java.awt.*;
import java.awt.image.*;

public class Scientist extends Rectangle {

    private BufferedImage scientistWalking1;
    private BufferedImage scientistWalking2;
    private BufferedImage scientistCrouching;
    private BufferedImage scientistFainting;

    private int x, y;
    private int dir;
    private int width, height;

    private boolean walking, crouching, fainting;
    private static int maxWalkingPoseCount = 4;
    private int walkingPoseCount = 0;

    public Scientist(int ddir) {
        super();
        walking = true;
        crouching = false;
        fainting = false;

        dir = ddir;

        if(dir == 0) {
            scientistWalking1 = JetpackJoyridePanel.loadBuffImg("scientist-stationary.png");
            scientistWalking2 = JetpackJoyridePanel.loadBuffImg("scientist-moving.png");
            scientistCrouching = JetpackJoyridePanel.loadBuffImg("scientist-crouching.png");
            scientistFainting = JetpackJoyridePanel.loadBuffImg("scientist-fainting.png");
        }
        else {
            scientistWalking1 = JetpackJoyridePanel.flipImage(JetpackJoyridePanel.loadBuffImg("scientist-stationary.png"));
            scientistWalking2 = JetpackJoyridePanel.flipImage(JetpackJoyridePanel.loadBuffImg("scientist-moving.png"));
            scientistCrouching = JetpackJoyridePanel.flipImage(JetpackJoyridePanel.loadBuffImg("scientist-crouching.png"));
            scientistFainting = JetpackJoyridePanel.flipImage(JetpackJoyridePanel.loadBuffImg("scientist-fainting.png"));
        }

        width = scientistWalking1.getWidth(null);
        height = scientistWalking1.getHeight(null);

        x = JetpackJoyridePanel.WIDTH;
        y = JetpackJoyridePanel.HEIGHT - height - 100;

        setBounds(x, y, width, height);
    }

    public void move() {
        if(walking) {
            if(dir == 0) {
                translate(-15, 0);
                x -= 15;
            }
            else {
                translate(-5, 0);
                x -= 5;
            }
        }
        else {
            translate(-10, 0);
            x -= 10;
        }
    }

    // if Barry is approaching the scientist
    public void barryApproaching() {
        if(fainting == false) {
            if(JetpackJoyridePanel.barry.getY() > y - JetpackJoyridePanel.barry.getHeight()) { // if Barry is on the same plane as the scientist
                walking = false;
                crouching = true; // makes scientist crouch
                fainting = false;
            }
            else {
                walking = true; // makes scientist walk normally
                crouching = false;
                fainting = false;
            }
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
