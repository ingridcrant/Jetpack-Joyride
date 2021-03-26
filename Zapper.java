import java.awt.*;
import java.awt.image.*;

public class Zapper {
    public static final BufferedImage diagonalZapper = JetpackJoyridePanel.loadBuffImg("zapper_diagonal.png");
    public static final BufferedImage normalZapper = JetpackJoyridePanel.loadBuffImg("zapper.png");

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

    public Rectangle getRect() {
        return bounds;
    }

    public String getType() {
        return type;
    }

    public int getX() {
        return (int) bounds.getX();
    }

    public int getY() {
        return (int) bounds.getY();
    }

    public void move() {
        bounds.translate(-dx, 0);
    }

    public void draw(Graphics g) {
        if(type == "diagonal") {
            g.drawImage(diagonalZapper, getX(), getY(), null);
        }
        else {
            g.drawImage(normalZapper, getX(), getY(), null);
        }
    }
}
