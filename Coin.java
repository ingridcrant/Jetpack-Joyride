import java.awt.*;
import java.awt.image.*;
import java.util.Random;

public class Coin extends Rectangle {
    private static BufferedImage coinSpriteImage = JetpackJoyridePanel.loadBuffImg("coinsprite.png");
	private static final int NUMSPRITES = 6;
    private static BufferedImage[] sprites = getSprites(coinSpriteImage, NUMSPRITES);
    private static final Random rand = new Random();
    
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
        int spriteWidth = spriteSheet.getWidth(null)/numSprites;

        for(int i = 0; i < numSprites; i++) {
            sprites[i] = spriteSheet.getSubimage(i*spriteWidth, 0, spriteWidth, spriteSheet.getHeight(null));
        }

        return sprites;
    }
    public void move() {
        currentSprite = (currentSprite+1)%NUMSPRITES;

        X += JetpackJoyridePanel.dx;
        translate(JetpackJoyridePanel.dx, 0);
    }
    public void draw(Graphics g) {
        g.drawImage(sprites[currentSprite], X, Y, null);
    }
}
