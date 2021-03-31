import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

public class Barry extends Rectangle {
    private static final BufferedImage barryWalking1 = JetpackJoyridePanel.loadBuffImg("barry.png");
    private static final BufferedImage barryWalking2 = JetpackJoyridePanel.loadBuffImg("barry2.png");
    private static final BufferedImage barryRising = JetpackJoyridePanel.loadBuffImg("barry_rising.png");
    private static final BufferedImage barryFalling = JetpackJoyridePanel.loadBuffImg("barry_falling.png");
    private static final BufferedImage barryForwards = JetpackJoyridePanel.loadBuffImg("barry_forwards.png");
    private static final BufferedImage barryDead = JetpackJoyridePanel.loadBuffImg("barry_dead.png");

    private static final BufferedImage barryWalkingShield1 = JetpackJoyridePanel.loadBuffImg("barry_shield.png");
    private static final BufferedImage barryWalkingShield2 = JetpackJoyridePanel.loadBuffImg("barry2_shield.png");
    private static final BufferedImage barryRisingShield = JetpackJoyridePanel.loadBuffImg("barry_rising_shield.png");
    private static final BufferedImage barryFallingShield = JetpackJoyridePanel.loadBuffImg("barry_falling_shield.png");

    private final int BLANK = 0x00000000;

    private boolean RISING, FALLING, WALKING;
    private boolean shield, hit;
    public boolean TUMBLING = false, DYING = false;
    private static int maxWalkingPoseCount = 4;
    private static boolean isMoving = true;
    private int walkingPoseCount = 0;

    private int WIDTH, HEIGHT;
    private static final int X = JetpackJoyridePanel.WIDTH/3;
    private int Y;
    private double risingYSpeed = 20, fallingYSpeed = 25;

    public static double GRAVITY = 0.99;
    public boolean hitFloor = false;
    private boolean canPlayBarrySlidingSound = true;
    private int barryRotationAngle = 270;

    public Barry(String picName) {
        super();
        WALKING = true;
        FALLING = false;
        RISING = false;

        WIDTH = barryWalking1.getWidth(null);
        HEIGHT = barryWalking1.getHeight(null);
        Y = JetpackJoyridePanel.WIDTH-HEIGHT-21;
        setBounds(X, Y, WIDTH, HEIGHT);
    }
    public void activateShield() {
        shield = true;
    }
    public void deactivateShield() {
        shield = false;
    }
    public boolean hasShield() {
        return shield;
    }
    public boolean isHit() {
        return hit;
    }
    public void resetHit() {
        hit = false;
    }
    public void gotHit() {
        hit = true;
    }
    public void accelerate(double accelerationX, double accelerationY) {
        JetpackJoyridePanel.speedX += accelerationX;
        fallingYSpeed += accelerationY;
    }

    public void move(boolean spacePressed) {
        if(spacePressed) {
            RISING = true; FALLING = false; WALKING = false;
            translate(0, (int) -risingYSpeed);
            Y -= risingYSpeed;
        }
        else {
            RISING = false; FALLING = true; WALKING = false;
            translate(0, (int) fallingYSpeed);
            Y += fallingYSpeed;
        }

        if(Y > JetpackJoyridePanel.HEIGHT-HEIGHT-JetpackJoyridePanel.BOTTOMBORDERHEIGHT) {
            Y = JetpackJoyridePanel.HEIGHT-HEIGHT-JetpackJoyridePanel.BOTTOMBORDERHEIGHT;
            setLocation(X, JetpackJoyridePanel.HEIGHT-HEIGHT-JetpackJoyridePanel.BOTTOMBORDERHEIGHT);
        } else if (Y < JetpackJoyridePanel.TOPBORDERHEIGHT) {
            Y = JetpackJoyridePanel.TOPBORDERHEIGHT;
            setLocation(X, JetpackJoyridePanel.TOPBORDERHEIGHT);
        }
        if(Y == JetpackJoyridePanel.HEIGHT-HEIGHT-JetpackJoyridePanel.BOTTOMBORDERHEIGHT) {
            RISING = false; FALLING = false; WALKING = true;
            SoundPlayer.playSoundEffect(SoundPlayer.barryWalking, false);
        }
        setBounds(X, Y, getImage().getWidth(), getImage().getHeight());
    }
    public static void stopMoving() {
        isMoving = false;
    }
    public void dying() {
        RISING = false; FALLING = false; WALKING = false; TUMBLING = true;
        Y += fallingYSpeed;
        accelerate(0, GRAVITY); // gravity accelerates the object downwards each tick
        if(Y >= JetpackJoyridePanel.HEIGHT-HEIGHT-JetpackJoyridePanel.BOTTOMBORDERHEIGHT) { 
            playBarrySlidingSound(); 
            TUMBLING = false;
            DYING = true;
            Y = JetpackJoyridePanel.HEIGHT-HEIGHT-JetpackJoyridePanel.BOTTOMBORDERHEIGHT;
            setLocation(X, Y);
            hitFloor = true;
        }
        fallingYSpeed = Math.ceil(fallingYSpeed);
        setBounds(X, Y, getImage().getWidth(), getImage().getHeight());
    }

    private void playBarrySlidingSound() {
        if(canPlayBarrySlidingSound) {
            SoundPlayer.playSoundEffect(SoundPlayer.barrySliding, false);
            canPlayBarrySlidingSound = false;
        }
    }

    public int getDyingYSpeed() {
        return (int) fallingYSpeed;
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
                        int barryPixel = getImage().getRGB(x - (int) getX(), y - (int) getY());
                        int zapperPixel = zapper.getImage().getRGB(x - (int)zapper.getX(), y - (int)zapper.getY());
                        
                        // 255 is completely transparent, you might consider using something
                        // a little less absolute, like 225, to give you a sligtly
                        // higher hit right, for example...
                        if (barryPixel != BLANK && zapperPixel != BLANK) {
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

    /**
     * Test if a given x/y position of the images contains transparent
     * pixels or not...
     * @param x
     * @param y
     * @return 
     */

    public BufferedImage getImage() {
        if(WALKING) {
            if(walkingPoseCount > maxWalkingPoseCount/2) {
                if(shield) {
                    return barryWalkingShield1;
                }
                else {
                    return barryWalking1;
                }
            }
            else {
                if(shield) {
                    return barryWalkingShield2;
                }
                else {
                    return barryWalking2;
                }
            }
        } else if (RISING) {
            if(shield) {
                return barryRisingShield;
            }
            else {
                return barryRising;
            }
        } else if (FALLING) {
            if(shield) {
                return barryFallingShield;
            }
            else {
                return barryFalling;
            }
        } else if (TUMBLING) {
            return barryForwards;
        } else {
            return barryDead;
        }
    }
    public void draw(Graphics g) {
        if(WALKING) {
            if(isMoving) {
                walkingPoseCount++;
            }
            if(walkingPoseCount > maxWalkingPoseCount/2) {
                setBounds(X, Y, barryWalking1.getWidth(null), barryWalking1.getHeight(null));
                if(shield) {
                    g.drawImage(barryWalkingShield1, X - barryWalkingShield1.getWidth()/2 + (int)getWidth()/2, Y - barryWalkingShield1.getHeight()/2 + (int)getHeight()/2, null);
                }
                else {
                    g.drawImage(barryWalking1, X, Y, null);
                }
            }
            else {
                setBounds(X, Y, barryWalking2.getWidth(null), barryWalking2.getHeight(null));
                if(shield) {
                    g.drawImage(barryWalkingShield2, X - barryWalkingShield2.getWidth()/2 + (int)getWidth()/2, Y - barryWalkingShield2.getHeight()/2 + (int)getHeight()/2, null);
                }
                else {
                    g.drawImage(barryWalking2, X, Y, null);
                }
            }
            if(walkingPoseCount > maxWalkingPoseCount) {
                walkingPoseCount = 0;
            }
        } else if (RISING) {
            setBounds(X, Y, barryRising.getWidth(null), barryWalking2.getHeight(null));
            if(shield) {
                g.drawImage(barryRisingShield, X - barryRisingShield.getWidth()/2 + (int)getWidth()/2, Y - barryRisingShield.getHeight()/2 + (int)getHeight()/2, null);
            }
            else {
                g.drawImage(barryRising, X, Y, null);
            }
        } else if (FALLING) {
            setBounds(X, Y, barryFalling.getWidth(null), barryWalking2.getHeight(null));
            if(shield) {
                g.drawImage(barryFallingShield, X - barryFallingShield.getWidth()/2 + (int)getWidth()/2, Y - barryFallingShield.getHeight()/2 + (int)getHeight()/2, null);
            }
            else {
                g.drawImage(barryFalling, X, Y, null);
            }
        } else if(TUMBLING) {
            AffineTransform identity = new AffineTransform();

            Graphics2D g2d = (Graphics2D)g;
            AffineTransform trans = new AffineTransform();
            trans.setTransform(identity);
            trans.translate(X, Y);
            trans.rotate(Math.toRadians(barryRotationAngle));
            g2d.drawImage(getImage(), trans, null);

            barryRotationAngle += 10;
            barryRotationAngle = barryRotationAngle % 360;
        } else {
            g.drawImage(barryDead, X, Y, null);
        }
    }
}
