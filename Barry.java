import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;

public class Barry extends Rectangle {
    private Image barryIcon;
    private int WIDTH, HEIGHT;
    private static final int TOPBORDERHEIGHT = 120, BOTTOMBORDERHEIGHT = 100;
    private static final int X = 50;
    private int Y;

    public Barry(String picName) {
        super();
        barryIcon = new ImageIcon("images/"+picName+".png").getImage();
        WIDTH = barryIcon.getWidth(null);
        HEIGHT = barryIcon.getHeight(null);
        Y = JetpackJoyridePanel.WIDTH-HEIGHT-21;
        setBounds(X, Y, WIDTH, HEIGHT);
    }

    public void move(boolean spacePressed) {
        if(spacePressed) {
            translate(0,-10);
            Y -= 10;
        }
        else {
            translate(0,5);
            Y += 5;
        }
        if(Y > JetpackJoyridePanel.HEIGHT-HEIGHT-BOTTOMBORDERHEIGHT) {
            Y = JetpackJoyridePanel.HEIGHT-HEIGHT-BOTTOMBORDERHEIGHT;
            setLocation(X, JetpackJoyridePanel.HEIGHT-HEIGHT-BOTTOMBORDERHEIGHT);
        } else if (Y < TOPBORDERHEIGHT) {
            Y = TOPBORDERHEIGHT;
            setLocation(X, TOPBORDERHEIGHT);
        }
    }
    public void draw(Graphics g) {
        g.drawImage(barryIcon, X, Y, null);
    }
}
