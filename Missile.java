import java.awt.*;
import java.awt.image.*;

public class Missile extends Rectangle {
    private static final int LEFT = 0, RIGHT = 1;
    private static final int MAXFRAMESBEFOREFIRING = 15;
    private static final int MAXFRAMESBEFOREWARNING = 10;
    // private static final int MISSILESPEED = 50;

    private int x, y;
    private int width, height;
    private int dir;
    private static final int GAPFROMEDGE = 5;

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
            x = JetpackJoyridePanel.WIDTH - width - GAPFROMEDGE;
        }
        else {
            missilePic = JetpackJoyridePanel.flipImage(missilePic);
            targetingPic = JetpackJoyridePanel.flipImage(targetingPic);
            x = GAPFROMEDGE;
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
                dx = 3*JetpackJoyridePanel.speedX;
            }
            else {
                dx = (-2)*JetpackJoyridePanel.speedX;
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
        targeting = false;
        firing = false;
    }

    public void draw(Graphics g) {
        if(warning) {
            g.drawImage(warningPic, x - warningPic.getWidth()/2 + targetingPic.getWidth()/2, y - warningPic.getHeight()/2 + targetingPic.getHeight()/2, null);
        }
        if(targeting) {
            g.drawImage(targetingPic, x, y, null);
        }
        if(firing) {
            g.drawImage(missilePic, x, y, null);
        }
    }
}