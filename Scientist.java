import java.awt.*;
import java.awt.image.*;

public class Scientist {
    private int x, y;
    private int width, height;

    private BufferedImage pic;
    private Rectangle scientistBox;

    public Scientist(int xx, String picName) {

        pic = JetpackJoyridePanel.loadBuffImg(picName); // we need scientists to face different directions (currently only have facing left, I sugest we do the reflect thing like we did for the background)

        width = pic.getWidth(null);
        height = pic.getHeight(null);

        x = xx;
        y = JetpackJoyridePanel.WIDTH - width - 20;

        scientistBox = new Rectangle(x, y, width, height);
    }

    public static void move(String dir) {
        if(dir.equals("right")) {
            pic = JetpackJoyridePanel.loadBuffImg("Images/scientist-moving.png"); // but make it face right
        }
        else {
            pic = JetpackJoyridePanel.loadBuffImg("Images/scientist-moving.png");
        }
    }

    // if scientist is hit by bullets flying out of Barry's Jetpack
    public static void hitByBullets() {
        for(Bullet bullet : SpaceInvadersPanel.bullets) {
            if(getRect().intersects(bullet.getRect())) {

            }
        }
    }

    // if scientist is hit by Barry
    public static void hitByBarry() {
        if(getRect().intersects(SpaceInvadersPanel.barry.getRect())) {

        }
    }

    // if scientist walks into a zapper
    public static void hitByZapper() {
        for(Zapper zapper : SpaceInvadersPanel.zappers) {
            if(getRect().intersects(zapper.getRect())) {

            }
        }
    }

    // Getter methods:
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public Rectangle getRect() {
        return scientistBox;
    }

    public static void draw(Graphics g) {
        g.drawImage(pic, x, y, null);
    }
}