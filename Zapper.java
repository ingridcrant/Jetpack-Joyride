import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.*;

public class Zapper {
    private static final BufferedImage diagonalZapper = JetpackJoyridePanel.loadBuffImg("zapper_diagonal.png");
    private static final BufferedImage normalZapper = JetpackJoyridePanel.loadBuffImg("zapper.png");
    
    private String type;
    private Rectangle bounds;
    
    private static final int dx = JetpackJoyridePanel.dx;

    public Zapper(String type, int X, int Y) {
        this.type = type;
        if(type == "diagonal") {
            bounds = new Rectangle(X, Y, diagonalZapper.getWidth(null), diagonalZapper.getHeight(null));
        } else {
            bounds = new Rectangle(X, Y, normalZapper.getWidth(null), normalZapper.getHeight(null));
        }
    }

    public boolean intersects(Barry barry) {
        // Check if the boundires intersect
        if (barry.intersects(bounds)) {
            // Calculate the collision overlay
            Rectangle intersectBounds = getCollision(bounds, barry);

            if (!intersectBounds.isEmpty()) {
                // Check all the pixels in the collision overlay to determine
                // if there are any non-alpha pixel collisions...
                for (int x = intersectBounds.x; x < intersectBounds.x + intersectBounds.width; x++) {
                    for (int y = intersectBounds.y; y < intersectBounds.y + intersectBounds.height; y++) {
                        if (collision(barry, x, y)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    protected Rectangle getCollision(Rectangle rect1, Rectangle rect2) {
        Area a1 = new Area(rect1);
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

    private boolean collision(Barry barry, int x, int y) {
        boolean collision = false;

        BufferedImage barryImage = barry.getImage();

        int barryPixel = barryImage.getRGB(x - (int) barry.getX(), y - (int) barry.getY());
        
        int zapperPixel;
        if (type == "diagonal") {
            zapperPixel = diagonalZapper.getRGB(x - bounds.x, y - bounds.y);
        } else {
            zapperPixel = normalZapper.getRGB(x - bounds.x, y - bounds.y);
        }
        // 255 is completely transparent, you might consider using something
        // a little less absolute, like 225, to give you a sligtly
        // higher hit right, for example...
        if (((barryPixel >> 24) & 0xFF) < 255 && ((zapperPixel >> 24) & 0xFF) < 255) {
            collision = true;
        }
        return collision;
    }

    public void move() {
        bounds.translate(-dx, 0);
    }

    public void draw(Graphics g) {
        if(type == "diagonal") {
            g.drawImage(diagonalZapper, (int) bounds.getX(), (int) bounds.getY(), null);
        }
        else {
            g.drawImage(normalZapper, (int) bounds.getX(), (int) bounds.getY(), null);
        }
    }
}
