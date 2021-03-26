/*
JetpackJoyride.java
Ingrid and Isabel Crant
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.Random;

public class JetpackJoyride extends JFrame{
	/**
	 *
	 */
    JetpackJoyridePanel game;
		
    public JetpackJoyride() {
		super("Jetpack Joyride");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JetpackJoyridePanel game = new JetpackJoyridePanel();
		add(game);
		pack();
		setVisible(true);
		setResizable(false);
    }
	
    public static void main(String[] arguments) {
		JetpackJoyride frame = new JetpackJoyride();
    }
}

// INTERFACE
class JetpackJoyridePanel extends JPanel implements MouseListener, ActionListener, KeyListener{
	/**
	 * 
	 */
    private static Timer myTimer;
    public static final int WIDTH=1000, HEIGHT=750;
    private static final Image background = new ImageIcon("images/background.png").getImage();
	private static int backgroundX = 0, backgroundY = 0, reversebackgroundX = 1000, reversebackgroundY = 0;
	public static final int dx = -20;
	
	private static boolean[] allKeys;
	private Random rand = new Random();

	public static Barry barry;
	private Coin coin1;
	private Zapper zapper;
	private ArrayList<Scientist> scientists;
	private ArrayList<Missile> missiles;

	public JetpackJoyridePanel(){
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		addMouseListener(this);
		addKeyListener(this);

		allKeys = new boolean[KeyEvent.KEY_LAST+1];
		barry = new Barry("barry");
		coin1 = new Coin(400, 200);
		zapper = new Zapper("diagonal", 700, 200);
		scientists = new ArrayList<Scientist>();
		missiles = new ArrayList<Missile>();

		Timer myTimer = new Timer(100, this);
		setFocusable(true);
		requestFocus();
		myTimer.start();
 	}

	public static BufferedImage loadBuffImg(String n) { 													// used to load BufferedImages
        try {
            return ImageIO.read(new File("Images/" + n));
        }
        catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }

 	// Main Game Loop
	@Override
	public void actionPerformed(ActionEvent e){
		move();
		repaint();
	}

	public void addScientists() {
		boolean canSpawn = new Random().nextDouble() < 0.04;
		if(canSpawn) {
			int randDir = rand.nextInt(2);
			scientists.add(new Scientist(randDir));
		}
	}
	public void removeScientists() {
		ArrayList<Scientist> removedScientists = new ArrayList<Scientist>();

		for(Scientist scientist : scientists) {
			if(scientist.getX() + scientist.getWidth() <= 0) {
				removedScientists.add(scientist);
			}
		}

		scientists.removeAll(removedScientists);
	}
	public void addMissiles() {
		boolean canSpawn = new Random().nextDouble() < 0.01;
		if(canSpawn && missiles.isEmpty()) {
			int randDir = rand.nextInt(2);
			missiles.add(new Missile(randDir));
		}
	}
	public void removeMissiles() {
		ArrayList<Missile> removedMissiles = new ArrayList<Missile>();

		for(Missile missile : missiles) {
			if(missile.getX() + missile.getWidth() <= 0 || missile.getX() >= WIDTH) {
				removedMissiles.add(missile);
			}
		}

		missiles.removeAll(removedMissiles);
	}

	public static BufferedImage flipImage(BufferedImage pic) {
		BufferedImage reversedPic = new BufferedImage(pic.getWidth(), pic.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for(int xx = pic.getWidth()-1; xx > 0; xx--){
            for(int yy = 0; yy < pic.getHeight(); yy++){
                reversedPic.setRGB(pic.getWidth()-xx, yy, pic.getRGB(xx, yy));
            }
        }
    	return reversedPic;
	}

	
    public void move(){
		backgroundX += dx;
		reversebackgroundX += dx;
		if(backgroundX <= -WIDTH) backgroundX = WIDTH;
		if(reversebackgroundX <= -WIDTH) reversebackgroundX = WIDTH;

		coin1.move();
		zapper.move();

		addMissiles();
		for(Missile missile : missiles) {
			missile.move();
		}
		removeMissiles();

		barry.move(allKeys[KeyEvent.VK_SPACE]);

		addScientists();
		for(Scientist scientist : scientists) {
			scientist.move();
			if(!scientist.isFainted()) {
				if(JetpackJoyridePanel.barry.getY() > scientist.getY() - JetpackJoyridePanel.barry.getHeight()) { // if Barry is on the same plane as the scientist
					scientist.crouch();
				}
				else {
					scientist.walk();
				}
			}
			if(scientist.intersects(barry)) {
				scientist.faint();
			}
			for(Missile missile : missiles) {
				if(scientist.intersects(missile)) {
					scientist.faint();
				}
			}
		}
		removeScientists();

		if(barry.intersects(coin1)) {
			System.out.println("got coin!");
		}
		if(barry.collidesWith(zapper)) {
			System.out.println("hit zapper");
		}
		for(Missile missile: missiles) {
			if(missile.isFiring()) {
				if(barry.intersects(missile)) {
					System.out.println("barry tumbles");
				}
			}
		}
    }
    
	@Override
    public void paint(Graphics g) {
		g.drawImage(background, backgroundX, backgroundY, null);
		g.drawImage(background, reversebackgroundX+WIDTH, reversebackgroundY, -WIDTH, HEIGHT, null);

		coin1.draw(g);
		zapper.draw(g);

		for(Scientist scientist : scientists) {
			scientist.draw(g);
		}

		for(Missile missile : missiles) {
			missile.draw(g);
		}

		barry.draw(g);
	}

	@Override
	public void	mousePressed(MouseEvent e){
	}

	public void	mouseClicked(MouseEvent e){}
	public void	mouseEntered(MouseEvent e){}
	public void	mouseExited(MouseEvent e){}
	public void	mouseReleased(MouseEvent e){}
	
	public void	keyPressed(KeyEvent e){
		allKeys[e.getKeyCode()] = true;
	}
	public void	keyReleased(KeyEvent e){ 
		allKeys[e.getKeyCode()] = false;
	}
		
	public void	keyTyped(KeyEvent e){}
}
