import java.awt.*;
import java.awt.image.*;
import java.util.Random;

public class Coin extends Rectangle {
    private static BufferedImage coinSpriteImage = JetpackJoyridePanel.loadBuffImg("coinsprite.png");
	private static final int NUMSPRITES = 6;
    public static final int WIDTH = coinSpriteImage.getWidth()/6, HEIGHT = coinSpriteImage.getHeight(), GAP = WIDTH+2;
    private static BufferedImage[] sprites = getSprites(coinSpriteImage, NUMSPRITES);
    private static final Random rand = new Random();
    
    private static boolean isRotating = true;
    private int currentSprite, X, Y;

    public Coin(int X, int Y) {
        super(X, Y, sprites[0].getWidth(), sprites[0].getHeight());
        this.X = X;
        this.Y = Y;
        currentSprite = rand.nextInt(NUMSPRITES);
    }
    public BufferedImage getImage() {
        return sprites[currentSprite];
    }
    private static BufferedImage[] getSprites(BufferedImage spriteSheet, int numSprites) {
        BufferedImage[] sprites = new BufferedImage[numSprites];

        for(int i = 0; i < numSprites; i++) {
            sprites[i] = spriteSheet.getSubimage(i*WIDTH, 0, WIDTH, HEIGHT);
        }

        return sprites;
    }
    public void translateCoin(int xx, int yy) {
        X += xx;
        Y += yy;
        translate(xx, yy);
    }
    public static void stopRotating() {
        isRotating = false;
    }
    public void move() {
        if(isRotating) {
            currentSprite = (currentSprite+1)%NUMSPRITES;
        }
        translateCoin(JetpackJoyridePanel.dx, 0);
    }
    public void draw(Graphics g) {
        g.drawImage(sprites[currentSprite], X, Y, null);
    }
}
