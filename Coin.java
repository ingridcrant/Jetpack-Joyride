import java.awt.*;
import java.awt.image.*;
import java.util.Random;

public class Coin extends Rectangle {
    private static final BufferedImage coinSpriteImage = JetpackJoyridePanel.loadBuffImg("coinsprite.png");
    private static final int NUMSPRITES = 6;
    private static final BufferedImage[] sprites = getSprites(coinSpriteImage, NUMSPRITES);
    private int currentSprite, x, y;
    public static int WIDTH, HEIGHT, GAP;
    
    private static boolean isRotating = true;   // for if the coin is rotating or not

    private static final Random rand = new Random();

    public Coin(int xx, int yy) {
        super(xx, yy, sprites[0].getWidth(), sprites[0].getHeight());
        x = xx;
        y = yy;
        currentSprite = rand.nextInt(NUMSPRITES);
    }

    // Getter and setter methods:
    public BufferedImage getImage() {
        return sprites[currentSprite];
    }
    private static BufferedImage[] getSprites(BufferedImage spriteSheet, int numSprites) {
        BufferedImage[] sprites = new BufferedImage[numSprites];
        WIDTH = coinSpriteImage.getWidth()/6;
        HEIGHT = coinSpriteImage.getHeight();
        GAP = WIDTH+2;

        for(int i = 0; i < numSprites; i++) {
            sprites[i] = spriteSheet.getSubimage(i*WIDTH, 0, WIDTH, HEIGHT);
        }

        return sprites;
    }
    public static void stopRotating() {
        isRotating = false;
    }

    // translates the coin:
    public void translateCoin(int xx, int yy) {
        x += xx;
        y += yy;
        translate(xx, yy);
    }

    public void move() {
        if(isRotating) {
            currentSprite = (currentSprite+1)%NUMSPRITES;
        }
        translateCoin(JetpackJoyridePanel.speedX, 0);
    }

    public void draw(Graphics g) {
        g.drawImage(sprites[currentSprite], x, y, null);
    }
}
