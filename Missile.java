import java.awt.*;
import java.awt.image.*;

public class Missile extends Rectangle {
    private static final int LEFT = 0, RIGHT = 1;
    private static final int MAXFRAMESBEFOREFIRING = 15;
    private static final int MAXFRAMESBEFOREWARNING = 10;
    private static final int MISSILESPEED = 50;

    private int x, y;
    private int width, height;
    private int dir;

    private boolean warning, targeting, firing;

    private int currentFrames = 0;

    private BufferedImage missilePic = JetpackJoyridePanel.loadBuffImg("missile.png");
    private BufferedImage targetingPic = JetpackJoyridePanel.loadBuffImg("missile_target.png");
    private BufferedImage warningPic = JetpackJoyridePanel.loadBuffImg("missile_coming.png");

    public Missile(int ddir) {
        super();
        dir = ddir;

        warning = false;
        targeting = true;
        firing = false;

        width = missilePic.getWidth();
        height = missilePic.getHeight();

        if(dir == LEFT) {
            x = JetpackJoyridePanel.WIDTH - width;
        }
        else {
            missilePic = JetpackJoyridePanel.flipImage(missilePic);
            targetingPic = JetpackJoyridePanel.flipImage(targetingPic);
            x = 0;
        }

        y = (int) JetpackJoyridePanel.barry.getY();

        setBounds(x, y, width, height);
    }

    public int getDirection() {
        return dir;
    }

    public BufferedImage getImage() {
        return missilePic;
    }

    public boolean isFiring() {
        return firing;
    }

    public void move() {
        currentFrames++;

        if(currentFrames == MAXFRAMESBEFOREWARNING) {
            warn();
        }
        else if(currentFrames == MAXFRAMESBEFOREFIRING) {
            fire();
        }

        if(firing) {
            int dx;
            if(dir == LEFT) {
                dx = JetpackJoyridePanel.dx-MISSILESPEED;
            }
            else {
                dx = JetpackJoyridePanel.dx+MISSILESPEED;
            }
            x += dx;
        }
        if(targeting) {
            y = (int) JetpackJoyridePanel.barry.getY();
        }
        setLocation(x, y);
    }

    public void fire() {
        if(dir == LEFT) {
            x = JetpackJoyridePanel.WIDTH;
        }
        else {
            x = 0 - width;
        }
        setBounds(x, y, width, height);
        warning = false;
        targeting = false;
        firing = true;
    }
    public void warn() {
        warning = true;
        targeting = true;
        firing = false;
    }

    public void draw(Graphics g) {
        if(warning) {
            g.drawImage(warningPic, x, Math.abs(y - targetingPic.getWidth(null) - 10), null);
        }
        if(targeting) {
            g.drawImage(targetingPic, x, y, null);
        }
        if(firing) {
            g.drawImage(missilePic, x, y, null);
        }
    }
}