import java.awt.*;
import java.awt.image.*;

public class Scientist extends Rectangle {
    private static final int LEFT = 0, RIGHT = 1;

    private BufferedImage scientistWalking1 = JetpackJoyridePanel.loadBuffImg("scientist_stationary.png");
    private BufferedImage scientistWalking2 = JetpackJoyridePanel.loadBuffImg("scientist_moving.png");
    private BufferedImage scientistCrouching = JetpackJoyridePanel.loadBuffImg("scientist_crouching.png");
    private BufferedImage scientistFainting = JetpackJoyridePanel.loadBuffImg("scientist_fainting.png");

    private int x, y;
    private int dir;
    private static final int speed = 5;
    private int width, height;

    private boolean walking, crouching, fainting;
    private boolean flipped = false;
    private static int maxWalkingPoseCount = 4;
    private int walkingPoseCount = 0;

    public Scientist(int ddir) {
        super();
        walking = true;
        crouching = false;
        fainting = false;

        dir = ddir;

        if(dir == RIGHT) {
            scientistWalking1 = JetpackJoyridePanel.flipImage(scientistWalking1);
            scientistWalking2 = JetpackJoyridePanel.flipImage(scientistWalking2);
            scientistCrouching = JetpackJoyridePanel.flipImage(scientistCrouching);
            scientistFainting = JetpackJoyridePanel.flipImage(scientistFainting);
        }

        width = scientistWalking1.getWidth(null);
        height = scientistWalking1.getHeight(null);

        x = JetpackJoyridePanel.WIDTH;
        y = JetpackJoyridePanel.HEIGHT - height - 100;

        setBounds(x, y, width, height);
    }

    public void move() {
        int dx;
        if(walking) {
            if(dir == LEFT) {
                dx = JetpackJoyridePanel.dx-speed;
            }
            else {
                dx = JetpackJoyridePanel.dx+speed;
            }
        }
        else {
            dx = JetpackJoyridePanel.dx;
        }
        translate(dx, 0);
        x += dx;
    }
    public void crouch() {
        walking = false;
        crouching = true;
        fainting = false;
    }
    public void walk() {
        walking = true;
        crouching = false;
        fainting = false;
    }
    // if scientist is hit by Barry
    public void faint(int direction) {
        walking = false;
        crouching = false;
        fainting = true; // makes scientist faint
    }
    public boolean isFainted() {
        return fainting;
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
