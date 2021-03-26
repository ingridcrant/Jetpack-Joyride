import java.awt.*;
import java.awt.image.*;

public class Missile extends Rectangle {
    private static final int LEFT = 0, RIGHT = 1;
    private static final int maxFramesBeforeFiring = 80;
    private static final int maxFramesBeforeWarning = 60;

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

    public void move() {
        currentFrames++;

        if(currentFrames == maxFramesBeforeWarning) {
            warn();
        }
        else if(currentFrames == maxFramesBeforeFiring) {
            fire();
        }

        if(firing) {
            if(dir == LEFT) {
                translate(-40, 0);
                x -= 40;
            }
            else {
                translate(20, 0);
                x += 20;
            }
        }
        if(targeting) {
            y = (int) JetpackJoyridePanel.barry.getY();
        }
    }

    public void fire() {
        if(dir == LEFT) {
            x = JetpackJoyridePanel.WIDTH;
        }
        else {
            x = 0 - width;
        }

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