/*
JetpackJoyride.java
Ingrid and Isabel Crant
*/

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

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
    private static final Image background = new ImageIcon("Images/background.png").getImage();
	private static int backgroundX = 0, backgroundY = 0, reversebackgroundX = 1000, reversebackgroundY = 0;
	public static final int dx = -20;

	private static final int LEFT = 0, RIGHT = 1;

	private static Font myFont;

	private static boolean[] allKeys;
	private Random rand = new Random();

	public static Barry barry;
	private Zapper zapper;
	private ArrayList<Scientist> scientists;
	private ArrayList<Missile> missiles;

	private int currentCoins;

	private int currentRun;
	private int longestRun;

	private String screen = "start";
	private boolean isGameOver = false;
	private Image startScreen;
	private boolean writtenToFiles = false;

	// Coin.GAP
	private final Coin[] COINFormation = {new Coin(Coin.GAP,0), new Coin(Coin.GAP*2,0), new Coin(Coin.GAP*3,0),
									new Coin(0,Coin.GAP), new Coin(0,Coin.GAP*2), new Coin(0,Coin.GAP*3), 
									new Coin(Coin.GAP,Coin.GAP*4), new Coin(Coin.GAP*2,Coin.GAP*4), new Coin(Coin.GAP*3,Coin.GAP*4), // the "C" part

									new Coin(Coin.GAP*5,Coin.GAP), new Coin(Coin.GAP*5,Coin.GAP*2), new Coin(Coin.GAP*5,Coin.GAP*3),
									new Coin(Coin.GAP*8,Coin.GAP), new Coin(Coin.GAP*8,Coin.GAP*2), new Coin(Coin.GAP*8,Coin.GAP*3),
									new Coin(Coin.GAP*6,0), new Coin(Coin.GAP*7,0), new Coin(Coin.GAP*6,Coin.GAP*4), new Coin(Coin.GAP*7,Coin.GAP*4),	// the "O" part

									new Coin(Coin.GAP*10,0), new Coin(Coin.GAP*11,0), new Coin(Coin.GAP*12,0),
									new Coin(Coin.GAP*10,Coin.GAP*4), new Coin(Coin.GAP*11,Coin.GAP*4), new Coin(Coin.GAP*12,Coin.GAP*4),
									new Coin(Coin.GAP*11,Coin.GAP), new Coin(Coin.GAP*11,Coin.GAP*2), new Coin(Coin.GAP*11,Coin.GAP*3),	// the "I" part

									new Coin(Coin.GAP*14,0), new Coin(Coin.GAP*14,Coin.GAP), new Coin(Coin.GAP*14,Coin.GAP*2),
									new Coin(Coin.GAP*14,Coin.GAP*3), new Coin(Coin.GAP*14,Coin.GAP*4),
									new Coin(Coin.GAP*17,0), new Coin(Coin.GAP*17,Coin.GAP), new Coin(Coin.GAP*17,Coin.GAP*2),
									new Coin(Coin.GAP*17,Coin.GAP*3), new Coin(Coin.GAP*17,Coin.GAP*4),
									new Coin(Coin.GAP*15,Coin.GAP), new Coin(Coin.GAP*16,Coin.GAP*2),	// the "N" part
									
									new Coin(Coin.GAP*20,0), new Coin(Coin.GAP*21,0), new Coin(Coin.GAP*22,0),
									new Coin(Coin.GAP*19,Coin.GAP*2), new Coin(Coin.GAP*20,Coin.GAP*2), new Coin(Coin.GAP*21,Coin.GAP*2), new Coin(Coin.GAP*22,Coin.GAP*2),
									new Coin(Coin.GAP*19,Coin.GAP*4), new Coin(Coin.GAP*20,Coin.GAP*4), new Coin(Coin.GAP*21,Coin.GAP*4), new Coin(Coin.GAP*22,Coin.GAP*4),
									new Coin(Coin.GAP*19,Coin.GAP), new Coin(Coin.GAP*22,Coin.GAP*3)
								};
	private final Coin[] CLUMPFormation = {new Coin(Coin.GAP, 0), new Coin(Coin.GAP*2, 0), new Coin(Coin.GAP*3, 0), 
		                             new Coin(Coin.GAP*4, 0), new Coin(Coin.GAP*5, 0), new Coin(Coin.GAP*6, 0),
		                             new Coin(0, Coin.GAP), new Coin(Coin.GAP, Coin.GAP), new Coin(Coin.GAP*2, Coin.GAP), 
									 new Coin(Coin.GAP*3, Coin.GAP), new Coin(Coin.GAP*4, Coin.GAP), new Coin(Coin.GAP*5, Coin.GAP), 
									 new Coin(Coin.GAP*6, Coin.GAP), new Coin(Coin.GAP*7, Coin.GAP), new Coin(Coin.GAP, Coin.GAP*2), 
									 new Coin(Coin.GAP*2, Coin.GAP*2), new Coin(Coin.GAP*3, Coin.GAP*2), new Coin(Coin.GAP*4, Coin.GAP*2), 
									 new Coin(Coin.GAP*5, Coin.GAP*2), new Coin(Coin.GAP*6, Coin.GAP*2)
									};
	private final Coin[] CURVEFormation = {new Coin(0, Coin.GAP*4), 
		
		                             new Coin(Coin.GAP, Coin.GAP*3), new Coin(Coin.GAP*2, Coin.GAP*2), new Coin(Coin.GAP*3, Coin.GAP*2), 
									 new Coin(Coin.GAP*4, Coin.GAP), new Coin(Coin.GAP*5, Coin.GAP), new Coin(Coin.GAP*6, Coin.GAP), 
									 new Coin(Coin.GAP*7, 0), new Coin(Coin.GAP*8, 0), new Coin(Coin.GAP*9, 0), 
									 new Coin(Coin.GAP*10, 0), new Coin(Coin.GAP*11, Coin.GAP), new Coin(Coin.GAP*12, Coin.GAP), 
									 new Coin(Coin.GAP*13, Coin.GAP), new Coin(Coin.GAP*14, Coin.GAP*2), new Coin(Coin.GAP*15, Coin.GAP*2), new Coin(Coin.GAP*16, Coin.GAP*3), 
									 
									 new Coin(Coin.GAP*17, Coin.GAP*4), 

									 new Coin(Coin.GAP, Coin.GAP*5), new Coin(Coin.GAP*2, Coin.GAP*6), new Coin(Coin.GAP*3, Coin.GAP*6), 
									 new Coin(Coin.GAP*4, Coin.GAP*7), new Coin(Coin.GAP*5, Coin.GAP*7), new Coin(Coin.GAP*6, Coin.GAP*7), 
									 new Coin(Coin.GAP*7, Coin.GAP*8), new Coin(Coin.GAP*8, Coin.GAP*8), new Coin(Coin.GAP*9, Coin.GAP*8), 
									 new Coin(Coin.GAP*10, Coin.GAP*8), new Coin(Coin.GAP*11, Coin.GAP*7), new Coin(Coin.GAP*12, Coin.GAP*7), 
									 new Coin(Coin.GAP*13, Coin.GAP*7), new Coin(Coin.GAP*14, Coin.GAP*6), new Coin(Coin.GAP*15, Coin.GAP*6), new Coin(Coin.GAP*16, Coin.GAP*5)
									};

	private final Coin[] BARRYFormation = {
								new Coin(0,0), new Coin(0,Coin.GAP), new Coin(0,Coin.GAP*2), new Coin(0,Coin.GAP*3), new Coin(0,Coin.GAP*4),
								new Coin(Coin.GAP,0), new Coin(Coin.GAP*2,0), new Coin(Coin.GAP*3,Coin.GAP),
								new Coin(Coin.GAP*2,Coin.GAP*2), new Coin(Coin.GAP,Coin.GAP*2),
								new Coin(Coin.GAP*3,Coin.GAP*3), new Coin(Coin.GAP*2,Coin.GAP*4), new Coin(Coin.GAP,Coin.GAP*4),		// the "B" part

								new Coin(Coin.GAP*4,Coin.GAP), new Coin(Coin.GAP*4,Coin.GAP*2), new Coin(Coin.GAP*4,Coin.GAP*3), new Coin(Coin.GAP*4,Coin.GAP*4),
								new Coin(Coin.GAP*7,Coin.GAP), new Coin(Coin.GAP*7,Coin.GAP*2), new Coin(Coin.GAP*7,Coin.GAP*3), new Coin(Coin.GAP*7,Coin.GAP*4),
								new Coin(Coin.GAP*5,0), new Coin(Coin.GAP*6,0), new Coin(Coin.GAP*5,Coin.GAP*2), new Coin(Coin.GAP*6,Coin.GAP*2),	// the "A" part

								new Coin(Coin.GAP*9,0), new Coin(Coin.GAP*9,Coin.GAP), new Coin(Coin.GAP*9,Coin.GAP*2), new Coin(Coin.GAP*9,Coin.GAP*3), new Coin(Coin.GAP*9,Coin.GAP*4),
								new Coin(Coin.GAP*12,Coin.GAP), new Coin(Coin.GAP*12,Coin.GAP*2), new Coin(Coin.GAP*12,Coin.GAP*4),
								new Coin(Coin.GAP*10,0), new Coin(Coin.GAP*11,0), new Coin(Coin.GAP*10,Coin.GAP*2), new Coin(Coin.GAP*11,Coin.GAP*2),	// the "R" part

								new Coin(Coin.GAP*14,0), new Coin(Coin.GAP*14,Coin.GAP), new Coin(Coin.GAP*14,Coin.GAP*2), new Coin(Coin.GAP*14,Coin.GAP*3), new Coin(Coin.GAP*14,Coin.GAP*4),
								new Coin(Coin.GAP*17,Coin.GAP), new Coin(Coin.GAP*17,Coin.GAP*2), new Coin(Coin.GAP*17,Coin.GAP*4),
								new Coin(Coin.GAP*15,0), new Coin(Coin.GAP*16,0), new Coin(Coin.GAP*15,Coin.GAP*2), new Coin(Coin.GAP*16,Coin.GAP*2),	// the "R" part

								new Coin(Coin.GAP*19,0), new Coin(Coin.GAP*19,Coin.GAP), new Coin(Coin.GAP*19,Coin.GAP*2),
								new Coin(Coin.GAP*22,0), new Coin(Coin.GAP*22,Coin.GAP), new Coin(Coin.GAP*22,Coin.GAP*2),
								new Coin(Coin.GAP*20,Coin.GAP*2), new Coin(Coin.GAP*21,Coin.GAP*2),
								new Coin(Coin.GAP*21,Coin.GAP*3), new Coin(Coin.GAP*21,Coin.GAP*4)		// the "Y" part
							};
	private Coin[][] coinFormations = {COINFormation, CLUMPFormation, CURVEFormation, BARRYFormation};
	private ArrayList<Coin> coins = new ArrayList<Coin>();
	private ArrayList<Coin> removedCoins = new ArrayList<Coin>();

	public JetpackJoyridePanel(){
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		addMouseListener(this);
		addKeyListener(this);

		InputStream is = JetpackJoyridePanel.class.getResourceAsStream("NewAthleticM54.ttf");
		try {
			myFont = Font.createFont(Font.TRUETYPE_FONT, is);
		} catch(Exception e) {
			myFont = new Font("Courier New", 1, 30);
		}

		allKeys = new boolean[KeyEvent.KEY_LAST+1];
		barry = new Barry("barry");
		zapper = new Zapper("diagonal2", 700, 200);
		scientists = new ArrayList<Scientist>();
		missiles = new ArrayList<Missile>();
		
		currentCoins = getScore("Coins.txt");

		currentRun = 0;
		longestRun = getScore("LongestRun.txt");

		startScreen = new ImageIcon("Images/start_screen.png").getImage();

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

	public Integer getScore(String fileName) {
		File file = new File(fileName);

		try {
			if(file.length() == 0) return 0;
			else {
				Scanner myReader = new Scanner(file);
				int data = Integer.parseInt(myReader.nextLine());
				myReader.close();
				return data;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}
	public void setScore(int score, String fileName) {
		File file = new File(fileName);
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {}
		}

		try {
			FileWriter myWriter = new FileWriter(file);
			myWriter.write(String.valueOf(score));
			myWriter.close();
		  } catch (IOException e) {
			e.printStackTrace();
		  }
	}

 	// Main Game Loop
	@Override
	public void actionPerformed(ActionEvent e){
		if(screen.equals("game")) {
			move();
		}
		repaint();
	}

	public void addScientists() {
		boolean canSpawn = new Random().nextDouble() < 0.04;
		if(canSpawn) {
			int randDir = rand.nextInt(2);
			int canCrouch = rand.nextInt(2);
			scientists.add(new Scientist(randDir, canCrouch));
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
	public void resetCoins() {
		coins.removeAll(removedCoins);
		removedCoins.clear();
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

	public void drawScores(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(myFont.deriveFont(Font.BOLD, 40f));
		g.drawString(currentRun+"M", 10, 40);

		Color silver = new Color(232, 232, 232);
		g.setColor(silver);
		g.setFont(myFont.deriveFont(Font.BOLD, 30f));
		g.drawString("BEST: "+longestRun+"M", 10, 70);

		Color gold = new Color(255, 255, 26);
		g.setColor(gold);
		g.setFont(myFont.deriveFont(Font.BOLD, 25f));
		g.drawString(currentCoins+"", 10, 95);
	}

	public void checkRun() {
		if(currentRun > longestRun) {
			longestRun = currentRun;
		}
	}
	
    public void move(){
		if(isGameOver) {
			screen = "game over";
			return;
		}
		backgroundX += dx;
		reversebackgroundX += dx;
		if(backgroundX <= -WIDTH) backgroundX = WIDTH;
		if(reversebackgroundX <= -WIDTH) reversebackgroundX = WIDTH;

		if(coins.isEmpty()) {
			Coin[] randFormation = coinFormations[rand.nextInt(coinFormations.length)];
			for(Coin coin: randFormation) {
				Coin newCoin = new Coin((int) coin.getX()+700, (int) coin.getY()+200);
				coins.add(newCoin);
			}
		}
		
		for(Coin coin: coins) {
			coin.move();
			if(coin.getX() < 0) {
				removedCoins.add(coin);
			} else if(barry.intersects(coin)) {
				removedCoins.add(coin);
				currentCoins++;
			}
		}
		resetCoins();

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
				if(JetpackJoyridePanel.barry.getY() > scientist.getY() - JetpackJoyridePanel.barry.getHeight() && scientist.canCrouch()) { // if Barry is on the same plane as the scientist
					scientist.crouch();
				}
				else {
					scientist.walk();
				}
			}
			if(scientist.intersects(barry)) {
				scientist.faint(RIGHT);
			}
			for(Missile missile : missiles) {
				if(scientist.intersects(missile)) {
					scientist.faint(missile.getDirection());
				}
			}
		}
		removeScientists();

		if(barry.collidesWith(zapper)) {
			isGameOver = true;
		}
		for(Missile missile: missiles) {
			if(missile.isFiring()) {
				if(barry.intersects(missile)) {
					System.out.println("barry tumbles");
					isGameOver = true;
				}
			}
		}

		currentRun++;
    }
    
	@Override
    public void paint(Graphics g) {
		if(screen.equals("start")) {
			g.drawImage(startScreen, 0, 0, null);
		}
		else if(screen.equals("game")) {
			g.drawImage(background, backgroundX, backgroundY, null);
			g.drawImage(background, reversebackgroundX+WIDTH, reversebackgroundY, -WIDTH, HEIGHT, null);

			for(Coin coin: coins) {
				coin.draw(g);
			}

			zapper.draw(g);

			for(Scientist scientist : scientists) {
				scientist.draw(g);
			}

			for(Missile missile : missiles) {
				missile.draw(g);
			}

			barry.draw(g);

			drawScores(g);
		}
		else if(screen.equals("game over")) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, WIDTH, HEIGHT);

			if(!writtenToFiles) {
				checkRun();
				setScore(currentCoins, "Coins.txt");
				setScore(longestRun, "LongestRun.txt");
				writtenToFiles = true;
			}
		}
	}

	@Override
	public void	mousePressed(MouseEvent e){
	}

	public void	mouseClicked(MouseEvent e){}
	public void	mouseEntered(MouseEvent e){}
	public void	mouseExited(MouseEvent e){}
	public void	mouseReleased(MouseEvent e){}
	
	public void	keyPressed(KeyEvent e) {
		if (screen.equals("start") && e.getKeyCode() == KeyEvent.VK_SPACE) {
            screen = "game";
        }
		allKeys[e.getKeyCode()] = true;
	}
	public void	keyReleased(KeyEvent e){ 
		allKeys[e.getKeyCode()] = false;
	}
		
	public void	keyTyped(KeyEvent e){}
}
