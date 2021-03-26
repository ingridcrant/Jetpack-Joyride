import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

public class Barry extends Rectangle {
    private static final BufferedImage barryWalking1 = JetpackJoyridePanel.loadBuffImg("barry.png");
    private static final BufferedImage barryWalking2 = JetpackJoyridePanel.loadBuffImg("barry2.png");
    private static final BufferedImage barryRising = JetpackJoyridePanel.loadBuffImg("barry_rising.png");
    private static final BufferedImage barryFalling = JetpackJoyridePanel.loadBuffImg("barry_falling.png");

    private boolean RISING, FALLING, WALKING;
    private static int maxWalkingPoseCount = 4;
    private int walkingPoseCount = 0;

    private int WIDTH, HEIGHT;
    private static final int TOPBORDERHEIGHT = 120, BOTTOMBORDERHEIGHT = 100;
    private static final int X = 50;
    private int Y, risingDy = 12, fallingDy =15;

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

    public void move(boolean spacePressed) {
        if(spacePressed) {
            RISING = true; FALLING = false; WALKING = false;
            translate(0,-risingDy);
            Y -= risingDy;
        }
        else {
            RISING = false; FALLING = true; WALKING = false;
            translate(0,fallingDy);
            Y += fallingDy;
        }
        if(Y > JetpackJoyridePanel.HEIGHT-HEIGHT-BOTTOMBORDERHEIGHT) {
            Y = JetpackJoyridePanel.HEIGHT-HEIGHT-BOTTOMBORDERHEIGHT;
            setLocation(X, JetpackJoyridePanel.HEIGHT-HEIGHT-BOTTOMBORDERHEIGHT);
        } else if (Y < TOPBORDERHEIGHT) {
            Y = TOPBORDERHEIGHT;
            setLocation(X, TOPBORDERHEIGHT);
        }
        if(Y == JetpackJoyridePanel.HEIGHT-HEIGHT-BOTTOMBORDERHEIGHT) {
            RISING = false; FALLING = false; WALKING = true;
        }
    }

    public boolean collidesWith(Coin coin) {
        // Check if the boundires intersect
        if (intersects(coin)) {
            // Calculate the collision overlay
            Rectangle intersectBounds = getCollision(coin);

            if (!intersectBounds.isEmpty()) {
                // Check all the pixels in the collision overlay to determine
                // if there are any non-alpha pixel collisions...
                for (int x = intersectBounds.x; x < intersectBounds.x + intersectBounds.width; x++) {
                    for (int y = intersectBounds.y; y < intersectBounds.y + intersectBounds.height; y++) {
                        if (collision(coin, x, y)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean collidesWith(Zapper zapper) {
        // Check if the boundires intersect
        if (intersects(zapper.getRect())) {
            // Calculate the collision overlay
            Rectangle intersectBounds = getCollision(zapper.getRect());

            if (!intersectBounds.isEmpty()) {
                // Check all the pixels in the collision overlay to determine
                // if there are any non-alpha pixel collisions...
                for (int x = intersectBounds.x; x < intersectBounds.x + intersectBounds.width; x++) {
                    for (int y = intersectBounds.y; y < intersectBounds.y + intersectBounds.height; y++) {
                        if (collision(zapper, x, y)) {
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

    private boolean collision(Zapper zapper, int x, int y) {
        boolean collision = false;

        BufferedImage barryImage = getImage();
        int barryPixel = barryImage.getRGB(x - (int) getX(), y - (int) getY());
        
        int zapperPixel;
        if (zapper.getType() == "diagonal") {
            zapperPixel = Zapper.diagonalZapper.getRGB(x - zapper.getX(), y - zapper.getY());
        } else {
            zapperPixel = Zapper.normalZapper.getRGB(x - zapper.getX(), y - zapper.getY());
        }
        // 255 is completely transparent, you might consider using something
        // a little less absolute, like 225, to give you a sligtly
        // higher hit right, for example...
        if (((barryPixel >> 24) & 0xFF) < 255 && ((zapperPixel >> 24) & 0xFF) < 255) {
            collision = true;
        }
        return collision;
    }

    private boolean collision(Coin coin, int x, int y) {
        boolean collision = false;

        BufferedImage barryImage = getImage();
        int barryPixel = barryImage.getRGB(x - (int) getX(), y - (int) getY());
        
        BufferedImage coinImage = coin.getImage();
        int coinPixel = coinImage.getRGB(x - (int) coin.getX(), y - (int) coin.getY());
        // 255 is completely transparent, you might consider using something
        // a little less absolute, like 225, to give you a sligtly
        // higher hit right, for example...
        if (((barryPixel >> 24) & 0xFF) < 255 && ((coinPixel >> 24) & 0xFF) < 255) {
            collision = true;
        }
        return collision;
    }

    public BufferedImage getImage() {
        if(WALKING) {
            if(walkingPoseCount > maxWalkingPoseCount/2) {
                return barryWalking1;
            }
            else {
                return barryWalking2;
            }
        } else if (RISING) {
            return barryRising;
        } else {
            return barryFalling;
        }
    }

    public void draw(Graphics g) {
        if(WALKING) {
            walkingPoseCount++;
            if(walkingPoseCount > maxWalkingPoseCount/2) {
                setBounds(X, Y, barryWalking1.getWidth(null), barryWalking1.getHeight(null));
                g.drawImage(barryWalking1, X, Y, null);
            }
            else {
                setBounds(X, Y, barryWalking2.getWidth(null), barryWalking2.getHeight(null));
                g.drawImage(barryWalking2, X, Y, null);
            }
            if(walkingPoseCount > maxWalkingPoseCount) {
                walkingPoseCount = 0;
            }
        } else if (RISING) {
            setBounds(X, Y, barryRising.getWidth(null), barryWalking2.getHeight(null));
            g.drawImage(barryRising, X, Y, null);
        } else if (FALLING) {
            setBounds(X, Y, barryFalling.getWidth(null), barryWalking2.getHeight(null));
            g.drawImage(barryFalling, X, Y, null);
        }
    }
}
