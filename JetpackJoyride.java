/*
JetpackJoyride.java
Ingrid and Isabel Crant
*/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.geom.*;
import javax.imageio.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import javax.sound.sampled.Clip;

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
class JetpackJoyridePanel extends JPanel implements MouseListener, ActionListener, KeyListener {
	/**
	 * 
	 */
    private static Timer myTimer;
    public static final int WIDTH = 1000, HEIGHT = 750; // width and height of the panel
	public static final int TOPBORDERHEIGHT = 120, BOTTOMBORDERHEIGHT = 100;
	private static final Image background = new ImageIcon("Images/background.png").getImage(); // background image (the lab)
	private static Image laserBeamImage;
	private static Rectangle laserBeamRect;
	private static int backgroundX, backgroundY, reverseBackgroundX, reverseBackgroundY;
	public static int speedX; // speed of the background

	private static final int LEFT = 0, RIGHT = 1;

	private static Font myFont;

	private static boolean[] allKeys;
	private Point mouse = new Point();
	private Random rand = new Random();

	public static Barry barry;

	private ArrayList<Zapper> zappers;
	private ArrayList<Scientist> scientists;
	private ArrayList<Missile> missiles;
	private ArrayList<Laser[]> lasers;
	private ArrayList<Coin> coins;

	private String currentStretch;

	private int currentCoins; // current amount of coins (amounts after each game)
	public static int currentRun; // current distance ran
	private int longestRun; // longest run distance
	private String longestRunInfo; // information about the longest run (aka name and distance travelled in the form "name: distance")

	private int numOfShields; // current number of shields (amounts after each game)
	private double missileProbability; // the probabilities of a missile/laser appearing

	private String screen;
	private static boolean isGameOver;
	private Image startScreen;
	private static boolean newLongestRunPrompted; // if the player is prompted for their name when they set a new longest run
	private static String buyShieldsMessage; // the message that appears when the player is trying to buy shields
	private static int buyShieldMessageFrameCount; // the number of frames the the buy shield message appears for
	private static Rectangle buyShieldRect = new Rectangle(WIDTH/2 + 125, HEIGHT/2 - 130, 200, 100);
	private static Rectangle restartGameRect = new Rectangle(WIDTH/2 - 300, HEIGHT/2+50, 600, 180);

	// coin formations:
	// spells out COIN
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
	// 3 clumps of coins
	private final Coin[] CLUMPFormation = {new Coin(Coin.GAP, 0), new Coin(Coin.GAP*2, 0), new Coin(Coin.GAP*3, 0), 
		                             new Coin(Coin.GAP*4, 0), new Coin(Coin.GAP*5, 0), new Coin(Coin.GAP*6, 0),
		                             new Coin(0, Coin.GAP), new Coin(Coin.GAP, Coin.GAP), new Coin(Coin.GAP*2, Coin.GAP), 
									 new Coin(Coin.GAP*3, Coin.GAP), new Coin(Coin.GAP*4, Coin.GAP), new Coin(Coin.GAP*5, Coin.GAP), 
									 new Coin(Coin.GAP*6, Coin.GAP), new Coin(Coin.GAP*7, Coin.GAP), new Coin(Coin.GAP, Coin.GAP*2), 
									 new Coin(Coin.GAP*2, Coin.GAP*2), new Coin(Coin.GAP*3, Coin.GAP*2), new Coin(Coin.GAP*4, Coin.GAP*2), 
									 new Coin(Coin.GAP*5, Coin.GAP*2), new Coin(Coin.GAP*6, Coin.GAP*2), // 1st clump

									 new Coin(Coin.GAP*10, Coin.GAP*3), new Coin(Coin.GAP*11, Coin.GAP*3), new Coin(Coin.GAP*12, Coin.GAP*3), 
		                             new Coin(Coin.GAP*13, Coin.GAP*3), new Coin(Coin.GAP*14, Coin.GAP*3), new Coin(Coin.GAP*15, Coin.GAP*3),
		                             new Coin(Coin.GAP*9, Coin.GAP*4), new Coin(Coin.GAP*10, Coin.GAP*4), new Coin(Coin.GAP*11, Coin.GAP*4), 
									 new Coin(Coin.GAP*12, Coin.GAP*4), new Coin(Coin.GAP*13, Coin.GAP*4), new Coin(Coin.GAP*14, Coin.GAP*4), 
									 new Coin(Coin.GAP*15, Coin.GAP*4), new Coin(Coin.GAP*16, Coin.GAP*4), new Coin(Coin.GAP*10, Coin.GAP*5), 
									 new Coin(Coin.GAP*11, Coin.GAP*5), new Coin(Coin.GAP*12, Coin.GAP*5), new Coin(Coin.GAP*13, Coin.GAP*5), 
									 new Coin(Coin.GAP*14, Coin.GAP*5), new Coin(Coin.GAP*15, Coin.GAP*5), // 2nd clump

									 new Coin(Coin.GAP*19, Coin.GAP*6), new Coin(Coin.GAP*20, Coin.GAP*6), new Coin(Coin.GAP*21, Coin.GAP*6), 
		                             new Coin(Coin.GAP*22, Coin.GAP*6), new Coin(Coin.GAP*23, Coin.GAP*6), new Coin(Coin.GAP*24, Coin.GAP*6),
		                             new Coin(Coin.GAP*18, Coin.GAP*7), new Coin(Coin.GAP*19, Coin.GAP*7), new Coin(Coin.GAP*20, Coin.GAP*7), 
									 new Coin(Coin.GAP*21, Coin.GAP*7), new Coin(Coin.GAP*22, Coin.GAP*7), new Coin(Coin.GAP*23, Coin.GAP*7), 
									 new Coin(Coin.GAP*24, Coin.GAP*7), new Coin(Coin.GAP*25, Coin.GAP*7), new Coin(Coin.GAP*19, Coin.GAP*8), 
									 new Coin(Coin.GAP*20, Coin.GAP*8), new Coin(Coin.GAP*21, Coin.GAP*8), new Coin(Coin.GAP*22, Coin.GAP*8), 
									 new Coin(Coin.GAP*23, Coin.GAP*8), new Coin(Coin.GAP*24, Coin.GAP*8) // 3rd clump
									};
	// 2 overlapping curves of coins
	private final Coin[] CURVEFormation = {new Coin(0, Coin.GAP*4),
		
		                             new Coin(Coin.GAP, Coin.GAP*3), new Coin(Coin.GAP*2, Coin.GAP*2), new Coin(Coin.GAP*3, Coin.GAP*2), 
									 new Coin(Coin.GAP*4, Coin.GAP), new Coin(Coin.GAP*5, Coin.GAP), new Coin(Coin.GAP*6, Coin.GAP), 
									 new Coin(Coin.GAP*7, 0), new Coin(Coin.GAP*8, 0), new Coin(Coin.GAP*9, 0), 
									 new Coin(Coin.GAP*10, 0), new Coin(Coin.GAP*11, Coin.GAP), new Coin(Coin.GAP*12, Coin.GAP), 
									 new Coin(Coin.GAP*13, Coin.GAP), new Coin(Coin.GAP*14, Coin.GAP*2), new Coin(Coin.GAP*15, Coin.GAP*2), new Coin(Coin.GAP*16, Coin.GAP*3), 

									 new Coin(Coin.GAP, Coin.GAP*5), new Coin(Coin.GAP*2, Coin.GAP*6), new Coin(Coin.GAP*3, Coin.GAP*6), 
									 new Coin(Coin.GAP*4, Coin.GAP*7), new Coin(Coin.GAP*5, Coin.GAP*7), new Coin(Coin.GAP*6, Coin.GAP*7), 
									 new Coin(Coin.GAP*7, Coin.GAP*8), new Coin(Coin.GAP*8, Coin.GAP*8), new Coin(Coin.GAP*9, Coin.GAP*8), 
									 new Coin(Coin.GAP*10, Coin.GAP*8), new Coin(Coin.GAP*11, Coin.GAP*7), new Coin(Coin.GAP*12, Coin.GAP*7), 
									 new Coin(Coin.GAP*13, Coin.GAP*7), new Coin(Coin.GAP*14, Coin.GAP*6), new Coin(Coin.GAP*15, Coin.GAP*6), new Coin(Coin.GAP*16, Coin.GAP*5),

									 new Coin(Coin.GAP*17, Coin.GAP*4), 

									 new Coin(Coin.GAP*18, Coin.GAP*3), new Coin(Coin.GAP*19, Coin.GAP*2), new Coin(Coin.GAP*20, Coin.GAP*2), 
									 new Coin(Coin.GAP*21, Coin.GAP), new Coin(Coin.GAP*22, Coin.GAP), new Coin(Coin.GAP*23, Coin.GAP), 
									 new Coin(Coin.GAP*24, 0), new Coin(Coin.GAP*25, 0), new Coin(Coin.GAP*26, 0), 
									 new Coin(Coin.GAP*27, 0), new Coin(Coin.GAP*28, Coin.GAP), new Coin(Coin.GAP*29, Coin.GAP), 
									 new Coin(Coin.GAP*30, Coin.GAP), new Coin(Coin.GAP*31, Coin.GAP*2), new Coin(Coin.GAP*32, Coin.GAP*2), new Coin(Coin.GAP*33, Coin.GAP*3),

									 new Coin(Coin.GAP*18, Coin.GAP*5), new Coin(Coin.GAP*19, Coin.GAP*6), new Coin(Coin.GAP*20, Coin.GAP*6), 
									 new Coin(Coin.GAP*21, Coin.GAP*7), new Coin(Coin.GAP*22, Coin.GAP*7), new Coin(Coin.GAP*23, Coin.GAP*7), 
									 new Coin(Coin.GAP*24, Coin.GAP*8), new Coin(Coin.GAP*25, Coin.GAP*8), new Coin(Coin.GAP*26, Coin.GAP*8), 
									 new Coin(Coin.GAP*27, Coin.GAP*8), new Coin(Coin.GAP*28, Coin.GAP*7), new Coin(Coin.GAP*29, Coin.GAP*7), 
									 new Coin(Coin.GAP*30, Coin.GAP*7), new Coin(Coin.GAP*31, Coin.GAP*6), new Coin(Coin.GAP*32, Coin.GAP*6), new Coin(Coin.GAP*33, Coin.GAP*5),

									 new Coin(Coin.GAP*34, Coin.GAP*4)
									};
	// spells out BARRY
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

	// zapper formations:
	// zapper formation #1: 3 vertical zappers (up, down, middle), 2 horizontal zapper (up, down)
	private final Zapper[] zapperFormation1 = {new Zapper("vertical", 0, TOPBORDERHEIGHT+50), new Zapper("vertical", Zapper.verticalZapper.getWidth()+20, HEIGHT-BOTTOMBORDERHEIGHT-(int)this.getHeight()-50), new Zapper("vertical", 2*Zapper.verticalZapper.getWidth()+2*20, HEIGHT/2-(int)this.getHeight()/2), new Zapper("horizontal", 3*Zapper.verticalZapper.getWidth()+3*20, TOPBORDERHEIGHT+50), new Zapper("horizontal", 3*Zapper.verticalZapper.getWidth()+Zapper.horizontalZapper.getWidth()+4*20, HEIGHT-BOTTOMBORDERHEIGHT-(int)this.getHeight()-50)};
	// zapper formtion #2: 4 vertical zapper (up, down, middle, middle)
	private final Zapper[] zapperFormation2 = {new Zapper("vertical", 0, TOPBORDERHEIGHT+50), new Zapper("vertical", Zapper.verticalZapper.getWidth()+20, HEIGHT-BOTTOMBORDERHEIGHT-(int)this.getHeight()-50), new Zapper("vertical", 2*Zapper.verticalZapper.getWidth()+2*20, HEIGHT/2-(int)this.getHeight()/2), new Zapper("vertical", 2*Zapper.verticalZapper.getWidth()+Zapper.horizontalZapper.getWidth()+3*20, HEIGHT/2-(int)this.getHeight()/2)};
	// zapper formation #3: 1 diagonal zapper (up), 1 vertical zapper (middle), 1 diagonal zapper (middle), 2 horizontal zapper (up, down)
	private final Zapper[] zapperFormation3 = {new Zapper("diagonal1", 0, TOPBORDERHEIGHT+50), new Zapper("vertical", Zapper.diagonal1Zapper.getWidth()+20, HEIGHT/2-(int)this.getHeight()/2), new Zapper("diagonal2", Zapper.diagonal1Zapper.getWidth()+Zapper.verticalZapper.getWidth()+2*20, HEIGHT/2-(int)this.getHeight()/2), new Zapper("horizontal", Zapper.diagonal1Zapper.getWidth()+Zapper.verticalZapper.getWidth()+Zapper.diagonal2Zapper.getWidth()+3*20, TOPBORDERHEIGHT+50), new Zapper("horizontal", Zapper.diagonal1Zapper.getWidth()+Zapper.verticalZapper.getWidth()+Zapper.diagonal2Zapper.getWidth()+Zapper.horizontalZapper.getWidth()+4*20, HEIGHT-BOTTOMBORDERHEIGHT-(int)this.getHeight()-50)};
	// zapper formation #4: 1 diagonal zapper (up), 3 vertical zappers (up, down middle)
	private final Zapper[] zapperFormation4 = {new Zapper("diagonal1", 0, TOPBORDERHEIGHT+50), new Zapper("vertical", Zapper.diagonal1Zapper.getWidth()+20, TOPBORDERHEIGHT+50), new Zapper("vertical", Zapper.diagonal1Zapper.getWidth()+Zapper.verticalZapper.getWidth()+2*20, HEIGHT-BOTTOMBORDERHEIGHT-(int)this.getHeight()-50), new Zapper("vertical", Zapper.diagonal1Zapper.getWidth()+2*Zapper.verticalZapper.getWidth()+3*20, HEIGHT/2-(int)this.getHeight()/2)};

	private Coin[][] coinFormations = {COINFormation, CLUMPFormation, CURVEFormation, BARRYFormation};
	private Zapper[][] zapperFormations = {zapperFormation1, zapperFormation2, zapperFormation3, zapperFormation4};

	public JetpackJoyridePanel() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		addMouseListener(this);
		addKeyListener(this);

		InputStream is = JetpackJoyridePanel.class.getResourceAsStream("NewAthleticM54.ttf");
		try {
			myFont = Font.createFont(Font.TRUETYPE_FONT, is);
		} catch(Exception e) {
			myFont = new Font("Courier New", 1, 30);
		}

		screen = "start";
		initialize();

		Timer myTimer = new Timer(100, this);
		setFocusable(true);
		requestFocus();
		myTimer.start();
 	}
	public void initialize() {
		speedX = -20;
		backgroundX = 0;
		backgroundY = 0;
		reverseBackgroundX = WIDTH;
		reverseBackgroundY = 0;

		isGameOver = false;

		newLongestRunPrompted = false;
		buyShieldsMessage = "";
		buyShieldMessageFrameCount = 0;

		laserBeamImage = JetpackJoyridePanel.loadBuffImg("laserbeam.png");

		allKeys = new boolean[KeyEvent.KEY_LAST+1];
		coins = new ArrayList<Coin>();
		barry = new Barry("barry");
		zappers = new ArrayList<Zapper>();
		scientists = new ArrayList<Scientist>();
		missiles = new ArrayList<Missile>();
		lasers = new ArrayList<Laser[]>();
		laserBeamRect = new Rectangle();

		currentStretch = "";

		currentCoins = getCoins();
		currentRun = 0;
		longestRun = Integer.parseInt((getLongestRun().split(": "))[1]);
		longestRunInfo = getLongestRun();

		numOfShields = getNumOfShields();
		missileProbability = 0.01;

		startScreen = new ImageIcon("Images/start_screen.png").getImage();
	}

	// loads BufferedImages:
	public static BufferedImage loadBuffImg(String n) {
        try {
            return ImageIO.read(new File("Images/" + n));
        }
        catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }

	// gets the current amount of coins from the "Coins.txt" file:
	public Integer getCoins() {
		File file = new File("Coins.txt");

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
	// writes the current amount of coins to the "Coins.txt" file:
	public void setCoins() {
		File file = new File("Coins.txt");
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {}
		}

		try {
			FileWriter myWriter = new FileWriter(file);
			myWriter.write(String.valueOf(currentCoins));
			myWriter.close();
		  } catch (IOException e) {
			e.printStackTrace();
		  }
	}
	// gets the distance of the longest run from the "LongestRun.txt" file:
	public String getLongestRun() {
		File file = new File("LongestRun.txt");

		try {
			if(file.length() == 0) return "nobody: 0";
			else {
				Scanner myReader = new Scanner(file);
				String line = myReader.nextLine(); // line is the form name: score
				myReader.close();
				return line;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "nobody: 0";
		}
	}
	// writes the distance of the longest run to the "LongestRun.txt" file:
	public void setLongestRun() {
		File file = new File("LongestRun.txt");
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {}
		}

		try {
			FileWriter myWriter = new FileWriter(file);
			String name = JOptionPane.showInputDialog("You set the longest run! What is your name?");
			myWriter.write(name + ": " + String.valueOf(currentRun));
			myWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// gets the current number of shields from the "NumOfShields.txt" file:
	public Integer getNumOfShields() {
		File file = new File("NumOfShields.txt");

		try {
			if(file.length() == 0) return 0;
			else {
				Scanner myReader = new Scanner(file);
				Integer num = Integer.parseInt(myReader.nextLine()); // line is the form name: score
				myReader.close();
				return num;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}
	// writes the current number of shields to the "NumOfShields.txt" file:
	public void setNumOfShields() {
		File file = new File("NumOfShields.txt");
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {}
		}

		try {
			FileWriter myWriter = new FileWriter(file);
			myWriter.write(String.valueOf(numOfShields));
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

	// adds scientists randomly:
	public void addScientists() {
		boolean canSpawn = new Random().nextDouble() < 0.04; // scientists have a 4% chance of appearing
		if(canSpawn) {
			int randDir = rand.nextInt(2); // chooses a random direction for the scientist to face (left: 0, right: 1)
			int canCrouch = rand.nextInt(2); // chooses whether the scientist is smart enought to crouch or not (50% chance each)
			scientists.add(new Scientist(randDir, canCrouch));
		}
	}
	// removes scientists if they are off the screen:
	public void removeScientists() {
		ArrayList<Scientist> removedScientists = new ArrayList<Scientist>();

		for(Scientist scientist : scientists) {
			if(scientist.getX() + scientist.getWidth() <= 0) {
				removedScientists.add(scientist);
			}
		}

		scientists.removeAll(removedScientists);
	}
	// adds missiles randomly:
	public void addMissiles() {
		boolean canSpawn = new Random().nextDouble() < missileProbability;
		if(canSpawn && lasers.isEmpty() && missiles.isEmpty()) {
			int randDir = rand.nextInt(2); // chooses a random direction for the missile to face (left: 0, right: 1)
			missiles.add(new Missile(randDir));
		}
	}
	// removes missiles if they are off the screen:
	public void removeMissiles() {
		ArrayList<Missile> removedMissiles = new ArrayList<Missile>();

		for(Missile missile : missiles) {
			if(missile.getX() + missile.getWidth() <= 0 || missile.getX() >= WIDTH) {
				removedMissiles.add(missile);
			}
		}

		missiles.removeAll(removedMissiles);
	}

	// adds lasers:
	public void addLaser() {
		boolean validLaserY = false;
		int[] randYList = {120, 320, 520};
		int randY = 0;
		while(!validLaserY) {
			validLaserY = true;
			randY = randYList[rand.nextInt(3)];
			for(Laser[] laserPair : lasers) {
				if(laserPair[0].getY() == randY && laserPair[1].getY() == randY) {
					validLaserY = false;
				}
			}
		}
		Laser[] randLaserPair = {new Laser(Laser.RIGHT, randY), new Laser(Laser.LEFT, randY)};
		lasers.add(randLaserPair);
	}
	// removes laser if they are off the screen:
	public void removeLasers() {
		ArrayList<Laser[]> removedLasers = new ArrayList<Laser[]>();

		for(Laser[] laserPair : lasers) {
			Laser laser1 = laserPair[0];
			Laser laser2 = laserPair[1];
			if(laser1.isOff() && laser2.isOff()) {
				removedLasers.add(laserPair);
			}
		}

		lasers.removeAll(removedLasers);
	}

	public void addZappers() {
		Zapper[] randFormation = zapperFormations[rand.nextInt(zapperFormations.length)];
		for(Zapper zapper : randFormation) {
			Zapper newZapper = new Zapper(zapper.getType(), (int) zapper.getX()+WIDTH+20, (int) zapper.getY());
			zappers.add(newZapper);
		}
	}
	public void removeZappers() {
		ArrayList<Zapper> removedZappers = new ArrayList<Zapper>();
		for(Zapper zapper : zappers) {
			if(zapper.getX() + zapper.getWidth() <= 0) {
				removedZappers.add(zapper);
			}
		}
		zappers.removeAll(removedZappers);
	}

	// adds coins:
	public void addCoins() {
		Coin[] randFormation = coinFormations[rand.nextInt(coinFormations.length)];
		for(Coin coin: randFormation) {
			Coin newCoin = new Coin((int) coin.getX()+WIDTH+20, (int) coin.getY()+200);
			coins.add(newCoin);
		}
	}
	// removes coins if they are off the screen or if barry collected them:
	public void removeCoins() {
		ArrayList<Coin> removedCoins = new ArrayList<Coin>();

		for(Coin coin : coins) {
			if(coin.getX() + coin.getWidth() <= 0 || barry.intersects(coin)) {
				removedCoins.add(coin);
			}
		}

		coins.removeAll(removedCoins);
	}

	public static void resetlaserBeamRect() {
		laserBeamRect = new Rectangle();
	}

	// flips images horizontally:
	public static BufferedImage flipImage(BufferedImage pic) {
		BufferedImage reversedPic = new BufferedImage(pic.getWidth(), pic.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for(int xx = pic.getWidth()-1; xx > 0; xx--){
            for(int yy = 0; yy < pic.getHeight(); yy++){
                reversedPic.setRGB(pic.getWidth()-xx, yy, pic.getRGB(xx, yy));
            }
        }
    	return reversedPic;
	}

	// draws the scores:
	public void drawScores(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(myFont.deriveFont(Font.BOLD, 40f));
		g.drawString(currentRun + "M", 10, 40); // draws the distance of the current run

		g.setFont(myFont.deriveFont(Font.BOLD, 20f));
		g.drawString("NUMBER OF SHIELDS " + numOfShields, WIDTH - 200, 40); // draws the number of shields the player has

		Color silver = new Color(232, 232, 232);
		g.setColor(silver);
		g.setFont(myFont.deriveFont(Font.BOLD, 30f));
		g.drawString("BEST: " + longestRun + "M", 10, 70); // draws the distance of the longest run

		Color gold = new Color(255, 255, 26);
		g.setColor(gold);
		g.setFont(myFont.deriveFont(Font.BOLD, 25f));
		g.drawString(currentCoins + "", 10, 95); // draws the current amount of coins the player has
	}
	// draws the final scores (shows up on the game over screen):
	public void drawFinalScores(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(myFont.deriveFont(Font.BOLD, 50f));
		g.drawString("YOU FLEW", WIDTH/2 - 430, HEIGHT/2 - 250);

		Color gold = new Color(255, 255, 26);
		g.setColor(gold);
		g.setFont(myFont.deriveFont(Font.BOLD, 100f));
		g.drawString(currentRun + "M", WIDTH/2 - 430, HEIGHT/2 - 150); // draws the distance of the player's run

		if(newLongestRun()) { // if the player set a new longest run
			g.setColor(Color.BLUE);
			g.setFont(myFont.deriveFont(Font.BOLD, 30f));
			g.drawString("NEW BEST", WIDTH/2 - 180, HEIGHT/2 - 250); // draws "new best"
		}

		g.setColor(Color.WHITE);
		g.setFont(myFont.deriveFont(Font.BOLD, 50f));
		g.drawString("AND COLLECTED", WIDTH/2 - 430, HEIGHT/2 - 90);

		g.setColor(gold);
		g.setFont(myFont.deriveFont(Font.BOLD, 40f));
		g.drawString(currentCoins + " COINS", WIDTH/2 - 430, HEIGHT/2 - 40); // draws the amount of coins the player collected
	}
	// draws the leaderboard (aka the player with the longest run):
	public void drawLeaderBoard(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(myFont.deriveFont(Font.BOLD, 30f));
		g.drawString("HIGH SCORE:", WIDTH/2 + 50, HEIGHT/2 - 250);

		Color gold = new Color(255, 255, 26);
		g.setColor(gold);
		g.setFont(myFont.deriveFont(Font.BOLD, 50f));
		if(longestRunInfo.split(": ")[0].length() > 8) { // if the top scoring player has more than 8 letters in their name
			g.drawString(longestRunInfo.split(": ")[0].substring(0, 7) + ": " + longestRunInfo.split(": ")[1], WIDTH/2 + 50, HEIGHT/2 - 195); // draws the first 8 letters of the players name and the distance they ran
		}
		else {
			g.drawString(longestRunInfo, WIDTH/2 + 50, HEIGHT/2 - 195); // draws the player's full name and the distance they ran
		}
	}

	// lets the player buy shields:
	public void buyShield() {
		if(currentCoins >= 600) { // if the player has enough coins to buy a shield, the player is allowed to buy a shield
			currentCoins -= 600;
			numOfShields++;
			buyShieldsMessage = "You bought a shield! You now have "+numOfShields+" shields and "+currentCoins+" coins left.";
			setNumOfShields();
		}
		else { // the player is not allowed to buy a shield
			buyShieldsMessage = "Sorry, you don't have enough coins to buy a shield. Shields cost 600 coins!";
		}
	}

	// if the player has beat the previous longest run:
	public boolean newLongestRun() {
		if(currentRun > longestRun) {
			return true;
		}
		return false;
	}
	
    public void move(){
		if(isGameOver) { // if the game is over
			if(!barry.hitFloor) { // if barry is on the floor
				barry.dying(); // barry is dying
			} else {
				speedX *= 0.9999; // slows the background down
				if(speedX == 0) {
					screen = "game over";
					setCoins();
					setNumOfShields();
					return;
				}
			}
		}

		// moves the background
		backgroundX += speedX;
		reverseBackgroundX += speedX;

		// if either background is completely off the screen, add WIDTH onto it to make it reappear to the right of the other background
		if(backgroundX <= -WIDTH) backgroundX = reverseBackgroundX+WIDTH;
		if(reverseBackgroundX <= -WIDTH) reverseBackgroundX = backgroundX + WIDTH;

		boolean barryCollided = false;  // deactivates shield only when barry is not colliding with anything

		for(Coin coin: coins) {
			coin.move();
			if(coin.getX() + coin.getWidth() <= 0 || barry.intersects(coin)) {
				if(barry.intersects(coin)) {
					SoundPlayer.playSoundEffect(SoundPlayer.coin, 1);
					currentCoins++;
				}
			}
		}
		removeCoins();

		if(!currentStretch.equals("lasers")) {
			addMissiles();
		}
		for(Missile missile : missiles) {
			missile.move();
		}
		removeMissiles();

		for(Zapper zapper : zappers) {
			zapper.move();
		}
		removeZappers();

		for(Laser[] laserPair : lasers) {
			laserPair[0].move();
			laserPair[1].move();

			if(laserPair[0].isFiring() && laserPair[1].isFiring()) {
				Point2D firingEndPoint1 = laserPair[0].getFiringEndPoint();
				Point2D firingEndPoint2 = laserPair[1].getFiringEndPoint();

				laserBeamImage = laserBeamImage.getScaledInstance((int) Math.abs(firingEndPoint1.getX() - firingEndPoint2.getX())+3, laserBeamImage.getHeight(null), Image.SCALE_DEFAULT);			// the +3 is there to fill in some pixels since the scaling isn't perfect
				laserBeamRect = new Rectangle((int) Math.min(firingEndPoint1.getX(), firingEndPoint2.getX()), (int) firingEndPoint1.getY(), laserBeamImage.getWidth(null), laserBeamImage.getHeight(null));
			}
		}
		removeLasers();

		if(!isGameOver) {
			barry.move(allKeys[KeyEvent.VK_SPACE]);
		}

		addScientists();
		for(Scientist scientist : scientists) {
			scientist.move();
			if(!scientist.isFainted()) { // if the scientist has not already fainted
				if(JetpackJoyridePanel.barry.getY() > scientist.getY() - JetpackJoyridePanel.barry.getHeight() && scientist.canCrouch()) { // if Barry is on the same plane as the scientist
					scientist.crouch(); // the scientist crouches (if they can)
				}
				else {
					scientist.walk(); // the scientist walks
				}
			}
			if(scientist.intersects(barry) && !scientist.isFainted()) { // if barry hits a scientist
				scientist.faint(RIGHT); // the scientist faints
			}
			if(scientist.intersects(laserBeamRect) && !scientist.isFainted()) {	// if the laser beam hits a scientist
				scientist.faint(scientist.getHitByLaserFallingDirection()); // the scientist faints in the opposite direction they are walking in
			}
			for(Zapper zapper : zappers) {
				if(scientist.collidesWith(zapper) && !scientist.isFainted()) {
					if(scientist.getDir() == LEFT) {
						scientist.faint(RIGHT);
					}
					else {
						scientist.faint(LEFT);
					}
				}
			}
			for(Laser[] laserPair : lasers) {
				if(laserPair[0].isFiring() && laserPair[1].isFiring()){
					if(scientist.intersects(laserPair[0])) {
						scientist.faint(scientist.getHitByLaserFallingDirection()); // the scientist faints in the opposite direction they are walking in
					}
					else if(scientist.intersects(laserPair[1])) {
						scientist.faint(scientist.getHitByLaserFallingDirection()); // the scientist faints in the opposite direction they are walking in
					}
				}
			}
			for(Missile missile : missiles) {
				if(scientist.intersects(missile)) { // if a missile hits a scientist
					scientist.faint(missile.getDirection()); // the scientist faints
				}
			}
		}
		removeScientists();
		if(!isGameOver) {
			for(Zapper zapper : zappers) {
				if(barry.collidesWith(zapper)) { // if barry hits a zapper
					if(!barry.hasShield()) { // if barry doesn't have a shield
						isGameOver = true; // the game is over
						SoundPlayer.playSoundEffect(SoundPlayer.barryZapped, 0);
						SoundPlayer.playSoundEffect(SoundPlayer.barryHurt, 0);
					}
					barryCollided = true;
					barry.gotHit(); // barry got hit
				}
			}
	
			for(Laser[] laserPair : lasers) {
				if(laserPair[0].isFiring() && laserPair[1].isFiring()) {
					if(barry.intersects(laserBeamRect) || barry.intersects(laserPair[0]) || barry.intersects(laserPair[1])) { // if barry hits a laser
						if(!barry.hasShield()) { // if barry doesn't have a shield
							isGameOver = true; // the game is over
							SoundPlayer.playSoundEffect(SoundPlayer.barryHurt, 0);
						}
						barryCollided = true;
						barry.gotHit(); // barry got hit
					}
				}
			}
			for(Missile missile: missiles) {
				if(missile.isFiring()) {
					if(barry.intersects(missile)) { // if barry hits a missile
						if(!barry.hasShield()) { // if barry doesn't have a shield
							isGameOver = true; // the game is over
							SoundPlayer.playSoundEffect(SoundPlayer.barryHurt, 0);
						}
						barryCollided = true;
						barry.gotHit(); // barry got hit
					}
				}
			}
			if(!barryCollided && barry.isHit() && barry.hasShield()) { // the frame right after barry is hit
				barry.resetHit();
				barry.deactivateShield();
				numOfShields--;
			}
		}

		currentRun++;

		// for every stretch of 100 metres, the speed of the background and the probability of the missile increases:
		if(currentRun % 100 == 0) {
			speedX -= 3;
			missileProbability += 0.001;
		}

		// for every stretch of 100 metres, a new obstacle appears
		if(currentRun % 80 == 0 && currentRun != 0) {
			double randSelection = new Random().nextDouble();

			if(randSelection < 0.4) { // coins have a 40% chance of appearing
				addCoins();
			}
			else if(randSelection >= 0.4 && randSelection < 0.8) { // zappers have a 40% chance of appearing
				addZappers();
			}
			else { // lasers have a 20% chance of appearing
				int randLaserAmount = rand.nextInt(2)+1;
				for(int i = 0; i < randLaserAmount; i++) {
					addLaser();
				}
			}
		}
    }
    
	@Override
    public void paint(Graphics g) {
		if(screen.equals("start")) {
			g.drawImage(startScreen, 0, 0, null); // draws the starting screen
		}
		else if(screen.equals("game") || screen.equals("game over")) {
			g.drawImage(background, backgroundX, backgroundY, null); // draws the background
			g.drawImage(background, reverseBackgroundX+WIDTH, reverseBackgroundY, -WIDTH, HEIGHT, null);
			
			for(Coin coin: coins) {
				coin.draw(g);
			}

			for(Zapper zapper : zappers) {
				zapper.draw(g);
			}

			for(Scientist scientist : scientists) {
				scientist.draw(g);
			}

			for(Missile missile : missiles) {
				missile.draw(g);
			}

			for(Laser[] laserPair : lasers) {
				laserPair[0].draw(g);
				laserPair[1].draw(g);

				if(laserPair[0].isAtPosition() && laserPair[1].isAtPosition()) {
					Line2D.Double warningBeam = new Line2D.Double(laserPair[0].getLoadingLineEndPoint(), laserPair[1].getLoadingLineEndPoint());
					
					Graphics2D g2d = (Graphics2D) g;
					g2d.setColor(Color.RED);
					g2d.draw(warningBeam);
				}
				else if(laserPair[0].isFiring() && laserPair[1].isFiring()) {
					Point2D firingEndPoint1 = laserPair[0].getFiringEndPoint();
					Point2D firingEndPoint2 = laserPair[1].getFiringEndPoint();

					laserBeamImage = laserBeamImage.getScaledInstance((int) Math.abs(firingEndPoint1.getX() - firingEndPoint2.getX())+3, laserBeamImage.getHeight(null), Image.SCALE_DEFAULT);			// the +3 is there to fill in some pixels since the scaling isn't perfect
					g.drawImage(laserBeamImage, (int) Math.min(firingEndPoint1.getX(), firingEndPoint2.getX()), (int) firingEndPoint1.getY(), null);
				}
			}

			barry.draw(g);

			if(screen.equals("game")) {
				drawScores(g);
			}

			if(screen.equals("game over")) {
				// the objects stop moving:
				Scientist.stopMoving();
				Coin.stopRotating();

				// dims the background
				Color transparentBlack = new Color(0, 0, 0, 190);
				g.setColor(transparentBlack);
				g.fillRect(0, 0, WIDTH, HEIGHT);

				drawFinalScores(g);
				
				// prompts the player for their name ONCE if the player has set a new longest run
				if(newLongestRun() && !newLongestRunPrompted) {
					newLongestRunPrompted = true;
					setLongestRun();
					longestRunInfo = getLongestRun();
				}
				if(!longestRunInfo.equals("nobody: 0")) {
					drawLeaderBoard(g);
				}

				g.setColor(Color.BLUE);

				Graphics2D g2d = (Graphics2D) g;
				g2d.fill(buyShieldRect);
				g2d.fill(restartGameRect);

				Color gold = new Color(255, 255, 26);
				g.setColor(gold);
				g.setFont(myFont.deriveFont(Font.BOLD, 20f));
				g.drawString("600 COINS", WIDTH/2 + 180, HEIGHT/2 - 50);

				g.setColor(Color.WHITE);
				g.setFont(myFont.deriveFont(Font.BOLD, 30f));
				g.drawString("BUY SHIELDS", WIDTH/2 + 145, HEIGHT/2 - 80);

				g.setFont(myFont.deriveFont(Font.BOLD, 80f));
				g.drawString("RESTART GAME", WIDTH/2 - 245, HEIGHT/2 + 170);

				if(buyShieldMessageFrameCount > 0) {
					buyShieldMessageFrameCount++;
					if(buyShieldMessageFrameCount == 20) {
						buyShieldMessageFrameCount = 0;
						buyShieldsMessage = "";
					}
				}
				
				g.setFont(myFont.deriveFont(Font.BOLD, 25f));
				g.drawString(buyShieldsMessage, 150, HEIGHT/2 + 30);
			}
		}
	}

	@Override
	public void	mousePressed(MouseEvent e){
	}

	public void	mouseClicked(MouseEvent e){}
	public void	mouseEntered(MouseEvent e){}
	public void	mouseExited(MouseEvent e){}
	public void	mouseReleased(MouseEvent e){
		if(screen == "game over") {
			mouse = new Point(e.getX(),e.getY());
			if(buyShieldRect.contains(mouse)) {
				buyShieldMessageFrameCount++;
				buyShield();
			}
			if(restartGameRect.contains(mouse)) {
				initialize();
				screen = "game";
			}
			mouse = new Point();
		}
	}
	
	public void	keyPressed(KeyEvent e) {
		if (screen.equals("start") && e.getKeyCode() == KeyEvent.VK_SPACE) {
			SoundPlayer.playSoundEffect(SoundPlayer.background, Clip.LOOP_CONTINUOUSLY);
            screen = "game";
        }
		if (screen.equals("game") && e.getKeyCode() == KeyEvent.VK_ENTER && numOfShields > 0 && !barry.hasShield()) {
			barry.activateShield();
		}

		allKeys[e.getKeyCode()] = true;
	}
	public void	keyReleased(KeyEvent e){ 
		allKeys[e.getKeyCode()] = false;
	}
		
	public void	keyTyped(KeyEvent e){}
}
