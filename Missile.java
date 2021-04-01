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

    private boolean warning, targeting, firing;   // booleans for the missile's phases: targeting, warning, or firing

    private int currentFrames = 0;

    private BufferedImage missilePic = JetpackJoyridePanel.loadBuffImg("missile.png");
    private BufferedImage targetingPic = JetpackJoyridePanel.loadBuffImg("missile_target.png");
    private BufferedImage warningPic = JetpackJoyridePanel.loadBuffImg("missile_coming.png");

    public Missile(int ddir) {
        super();
        dir = ddir;

        warning = false;
        targeting = true;                                                 // initially, the missile is targeting Barry
        firing = false;

        width = missilePic.getWidth();
        height = missilePic.getHeight();

        if(dir == LEFT) {                                                 // if the missile is facing left
            x = JetpackJoyridePanel.WIDTH - width - GAPFROMEDGE;          // places the signal to the right of the screen
        }
        else {                                                            // if the missile is facing right
            // flips images to face right:
            missilePic = JetpackJoyridePanel.flipImage(missilePic);
            targetingPic = JetpackJoyridePanel.flipImage(targetingPic);

            x = GAPFROMEDGE;                                              // places the signal to the left of the screen
        }

        y = (int) JetpackJoyridePanel.barry.getY();

        setBounds(x, y, width, height);
    }

    // Getter and setter methods:
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
            warn();                                         // warns Barry
        }
        else if(currentFrames == MAXFRAMESBEFOREFIRING) {
            fire();                                         // fires toward Barry
        }

        if(firing) {                                   // if the missile is firing
            int dx;
            if(dir == LEFT) {                          // if the missile is facing left
                dx = 3*JetpackJoyridePanel.speedX;     // moves the missile left
            }
            else {                                     // if the missile is facing right
                dx = (-2)*JetpackJoyridePanel.speedX;  // moves the missile right
            }
            x += dx;
        }
        if(targeting) {                                  // if the missile is targeting Barry
            y = (int) JetpackJoyridePanel.barry.getY();  // the missile follows Barry
        }
        setLocation(x, y);
    }

    // fires the missile:
    public void fire() {
        if(dir == LEFT) {                                           // if the missile is facing left
            x = JetpackJoyridePanel.WIDTH;                          // places the missile off screen (to the right)
        }
        else {                                                      // if the missile is facing right
            x = 0 - width;                                          // places the missile off screen (to the left)
        }
        setBounds(x, y, width, height);
        
        warning = false;
        targeting = false;
        firing = true;

        SoundPlayer.playSoundEffect(SoundPlayer.missileLaunch, 0);
    }
    // activates the warning signal:
    public void warn() {
        warning = true;
        targeting = false;
        firing = false;

        SoundPlayer.playSoundEffect(SoundPlayer.missileWarning, 0);
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