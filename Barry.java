import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;

public class Barry extends Rectangle {
    private static final Image barryWalking1 = new ImageIcon("images/barry.png").getImage();
    private static final Image barryWalking2 = new ImageIcon("images/barry2.png").getImage();
    private static final Image barryRising = new ImageIcon("images/barry_rising.png").getImage();
    private static final Image barryFalling = new ImageIcon("images/barry_falling.png").getImage();

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
    public void draw(Graphics g) {
        if(WALKING) {
            walkingPoseCount++;
            if(walkingPoseCount > maxWalkingPoseCount/2) {
                g.drawImage(barryWalking1, X, Y, null);
            }
            else {
                g.drawImage(barryWalking2, X, Y, null);
            }
            if(walkingPoseCount > maxWalkingPoseCount) {
                walkingPoseCount = 0;
            }
        } else if (RISING) {
            g.drawImage(barryRising, X, Y, null);
        } else if (FALLING) {
            g.drawImage(barryFalling, X, Y, null);
        }
    }
}
