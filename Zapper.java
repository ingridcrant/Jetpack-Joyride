import java.awt.*;
import java.awt.image.*;

public class Zapper {
    public static final BufferedImage diagonal1Zapper = JetpackJoyridePanel.loadBuffImg("zapper_diagonal.png");
    public static final BufferedImage diagonal2Zapper = JetpackJoyridePanel.loadBuffImg("zapper_diagonal_2.png");
    public static final BufferedImage verticalZapper = JetpackJoyridePanel.loadBuffImg("zapper.png");
    public static final BufferedImage horizontalZapper = JetpackJoyridePanel.loadBuffImg("zapper_horizontal.png");
    private BufferedImage Zapper;

    private String type;
    private Rectangle bounds;

    public Zapper(String type, int X, int Y) {
        this.type = type;
        if(type == "diagonal1") {
            Zapper = diagonal1Zapper;
        } else if (type == "diagonal2") {
            Zapper = diagonal2Zapper;
        } else if (type == "vertical") {
            Zapper = verticalZapper;
        } else if (type == "horizontal") {
            Zapper = horizontalZapper;
        }
        bounds = new Rectangle(X, Y, Zapper.getWidth(), Zapper.getHeight());
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

    public BufferedImage getImage() {
        return Zapper;
    }

    public void move() {
        bounds.translate(JetpackJoyridePanel.speedX, 0);
    }

    public void draw(Graphics g) {
        g.drawImage(Zapper, getX(), getY(), null);
    }
}
