import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

public class Scientist extends Rectangle {
    public static final int LEFT = 0, RIGHT = 1;
    private final int BLANK = 0x00000000;
    
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
    private boolean crouch = false; // only the smart scientists (about 50%) can crouch
    private boolean canPlayFaintingSound = true;
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
    public int getHitByLaserFallingDirection() {
        if(dir == RIGHT) {
            return LEFT;
        }
        else {
            return RIGHT;
        }
    }
    public int getDir() {
        return dir;
    }

    public boolean collidesWith(Zapper zapper) {
        // Check if the boundires intersect
        if (intersects(zapper)) {
            // Calculate the collision overlay
            Rectangle intersectBounds = getCollision(zapper);
            if (!intersectBounds.isEmpty()) {
                // Check all the pixels in the collision overlay to determine
                // if there are any non-alpha pixel collisions...
                for (int x = intersectBounds.x; x < intersectBounds.x + intersectBounds.width; x++) {
                    for (int y = intersectBounds.y; y < intersectBounds.y + intersectBounds.height; y++) {
                        int scientistPixel = getImage().getRGB(x - (int) getX(), y - (int) getY());
                        int zapperPixel = zapper.getImage().getRGB(x - (int)zapper.getX(), y - (int)zapper.getY());
                        
                        // 255 is completely transparent, you might consider using something
                        // a little less absolute, like 225, to give you a sligtly
                        // higher hit right, for example...
                        if (scientistPixel != BLANK && zapperPixel != BLANK) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    protected Rectangle getCollision(Rectangle rect2) {
        Area a1 = new Area(this);
        Area a2 = new Area(rect2);
        a1.intersect(a2);
        return a1.getBounds();
    }

    public void move() {
        int dx;
        if(walking) {
            if(dir == LEFT) {
                dx = JetpackJoyridePanel.speedX-speed;
            }
            else {
                dx = JetpackJoyridePanel.speedX+speed;
            }
        }
        else {
            dx = JetpackJoyridePanel.speedX;
        }
        translate(dx, 0);
        x += dx;
    }

    public BufferedImage getImage() {
        if(walking) {
            if(walkingPoseCount > maxWalkingPoseCount/2) {
                return scientistWalking1;
            }
            else {
                return scientistWalking2;
            }
        } else if (crouching) {
            return scientistCrouching;
        } else {
            if(flipped) {
                return scientistFaintingUpsideDown;
            }
            else {
                return scientistFainting;
            }
        }
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

        playFaintingSound();
    }

    public void playFaintingSound() {
        if(canPlayFaintingSound) {
            SoundPlayer.playSoundEffect(SoundPlayer.scientistFainting, 0);
            canPlayFaintingSound = false;
        }
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
        if(walking) {
            if(isMoving) {
                walkingPoseCount++;
            }
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
