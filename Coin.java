import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;

public class Coin extends Rectangle {
    private static BufferedImage coinSpriteImage = JetpackJoyridePanel.loadBuffImg("coinsprite.png");
	private static BufferedImage[] sprites;
    private static int currentSprite, X, Y;
    private static final int NUMSPRITES = 6;

    public Coin() {
        super();
        X = 500;
        Y = 300;

        sprites = getSprites(coinSpriteImage, NUMSPRITES);
        currentSprite = 0;

        setBounds(X, Y, sprites[0].getWidth(), sprites[0].getHeight());
    }
    private static BufferedImage[] getSprites(BufferedImage spriteSheet, int numSprites) {
        BufferedImage[] sprites = new BufferedImage[numSprites];
        int spriteWidth = spriteSheet.getWidth(null)/numSprites;

        for(int i = 0; i < numSprites; i++) {
            sprites[i] = spriteSheet.getSubimage(i*spriteWidth, 0, spriteWidth, spriteSheet.getHeight(null));
        }

        return sprites;
    }
    public void move() {
        currentSprite = (currentSprite+1)%NUMSPRITES;

        X -= 10;
        translate(-10, 0);
    }
    public void draw(Graphics g) {
        g.drawImage(sprites[currentSprite], X, Y, null);
    }
}
