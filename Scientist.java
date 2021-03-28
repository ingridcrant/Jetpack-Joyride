import java.awt.*;
import java.awt.image.*;

public class Scientist extends Rectangle {
    private static final int LEFT = 0, RIGHT = 1;

    private BufferedImage scientistWalking1 = JetpackJoyridePanel.loadBuffImg("scientist_stationary.png");
    private BufferedImage scientistWalking2 = JetpackJoyridePanel.loadBuffImg("scientist_moving.png");
    private BufferedImage scientistCrouching = JetpackJoyridePanel.loadBuffImg("scientist_crouching.png");
    private BufferedImage scientistFainting = JetpackJoyridePanel.loadBuffImg("scientist_fainting.png");
    private BufferedImage scientistFaintingUpsideDown = JetpackJoyridePanel.loadBuffImg("scientist_fainting_upsidedown.png");

    private int x, y;
    private int dir;
    private static final int speed = 5;
    private int width, height;

    private static boolean isMoving = true;
    private boolean walking, crouching, fainting;
    private boolean flipped = false;
    private boolean crouch = false; // only the smart scientists about 50% can crouch
    private static int maxWalkingPoseCount = 4;
    private int walkingPoseCount = 0;

    public Scientist(int ddir, int ccrouch) {
        super();
        walking = true;
        crouching = false;
        fainting = false;

        dir = ddir;

        if(ccrouch == 0) {
            crouch = true;
        }

        if(dir == RIGHT) {
            scientistWalking1 = JetpackJoyridePanel.flipImage(scientistWalking1);
            scientistWalking2 = JetpackJoyridePanel.flipImage(scientistWalking2);
            scientistCrouching = JetpackJoyridePanel.flipImage(scientistCrouching);
            scientistFainting = JetpackJoyridePanel.flipImage(scientistFainting);
            scientistFaintingUpsideDown = JetpackJoyridePanel.flipImage(scientistFaintingUpsideDown);
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

    public void crouch() { // makes scientist crouch
        walking = false;
        crouching = true;
        fainting = false;
    }
    public void walk() { // makes scientist walk
        walking = true;
        crouching = false;
        fainting = false;
    }
    public void faint(int direction) { // makes scientist faint
        if(dir == direction) {
            flipped = true;
        }
        walking = false;
        crouching = false;
        fainting = true;
    }

    public boolean isFainted() {
        return fainting;
    }
    public boolean canCrouch() {
        return crouch;
    }
    public static void stopMoving() {
        isMoving = false;
    }
    public void draw(Graphics g) {
        if(walking && isMoving) {
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
            if(flipped) {
                g.drawImage(scientistFaintingUpsideDown, x, y, null);
            }
            else {
                g.drawImage(scientistFainting, x, y, null);
            }
        }
    }
}
