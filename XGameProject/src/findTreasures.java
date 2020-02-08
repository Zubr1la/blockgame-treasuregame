
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.io.*;

public class findTreasures extends JFrame implements MouseListener {

	private static final long serialVersionUID = 1L;

	boolean gaming = false;

	boolean isPlaying = false;

	boolean menu = true;

	boolean gameOver = false;

	boolean gameStarted = false;

	boolean mouseOuttaWindow = false;

	boolean firstGameLaunch = true;

	int mineCount = 5;

	int treasureCount = 5;

	int timeCount = 0;

	int treasureCountC = 0;

	ArrayList<JLabel> mines = new ArrayList<JLabel>();

	ArrayList<JLabel> tempMines = new ArrayList<JLabel>();

	ArrayList<JLabel> treasures = new ArrayList<JLabel>();

	Clip MouseHover = null;

	AudioInputStream audioInputStream = null;

	JSpinner mineNumberChooser;

	JSpinner treasureNumberChooser;

	DB db;

	String nickName;

	// Game window
	int maxX = 800;
	int maxY = 600;

	JLabel player;
	int playerX = 10;
	int playerY = 550;

	File wavFile;
	AudioClip sound;

	// Extra labels

	JLabel gameTime = new JLabel();

	JLabel menuBackground = new JLabel(new ImageIcon("menuscreen.png"));

	JLabel firstLaunch = new JLabel();

	JFrame menuScreen;

	JButton startGame = new JButton("START GAME");

	JLabel infoLabel = new JLabel();

	JLabel nickText;

	JButton exitGame = new JButton("Exit game");

	JPanel gameWonPanel = new JPanel();

	JPanel gameOverPanel = new JPanel();

	JLabel gameOverLabel = new JLabel(new ImageIcon("gameOverFT.png"));

	JLabel gameWonLabel = new JLabel(new ImageIcon("gameWonFT.png"));;

	findTreasures(String nick) {

		timeCount = 0;

		try {
			audioInputStream = AudioSystem
					.getAudioInputStream(new File("treasuregame/tempmineSound.wav").getAbsoluteFile());
			MouseHover = AudioSystem.getClip();
			MouseHover.open(audioInputStream);
		} catch (Exception e) {

			System.out.println("Something wrong with loop sound!");
		}

		// Game window
		this.setTitle("Find Treasures");
		this.setSize(maxX, maxY);
		this.setLocation(maxX, maxY);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(null);

		// moving blockGame player
		player = new JLabel();
		player.setBounds(playerX, playerY, 15, 15);
		player.setVisible(true);
		this.add(player);

		nickName = nick;
		nickText = new JLabel("Your nickname: " + nickName);
		nickText.setBounds(400 / 2 - 100, 20, 300, 15);
		nickText.setOpaque(true);
		nickText.setVisible(true);

		infoLabel.setBounds(70, 130, 300, 100);
		infoLabel.setOpaque(true);
		infoLabel.setVisible(true);
		infoLabel.setText("<html><div style='text-align: center;'>" + "To start game <br>1.Press start button <br>"
				+ " 2.Move mouse to bottom left corner <br>" + "3.DON'T MOVE MOUSE OUTSIDE GAME WINDOW!</div></html>");

		gameWonPanel.setVisible(false);
		gameWonPanel.setBounds(0, 0, 800, 600);
		gameWonPanel.setOpaque(false);
		gameWonPanel.setLayout(null);

		gameWonLabel.setVisible(true);
		gameWonLabel.setBounds(maxX / 2 - 250, 50, 500, 120);
		gameWonLabel.setOpaque(false);
		gameWonPanel.add(gameWonLabel);
		
		
		gameTime = new JLabel();
		gameTime.setBounds(maxX/2-70, 350, 300, 50);
		gameTime.setFont(new Font("Serif", Font.PLAIN, 30));
		gameTime.setForeground(Color.white);
		gameTime.setVisible(true);
		gameWonPanel.add(gameTime);

		JButton winRestartGameButton = new JButton("Restart game");
		winRestartGameButton.setFont(new Font("Arial", Font.PLAIN, 25));
		winRestartGameButton.setBounds(maxX / 2 - 100, 200, 200, 100);
		winRestartGameButton.setVisible(true);
		winRestartGameButton.setOpaque(true);
		gameWonPanel.add(winRestartGameButton);

		winRestartGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (mineCount == 5 && treasureCountC == 5) {
					db.saveScore(nickName, "" + timeCount, 1);
				}
				dispose();
				new findTreasures(nickName);

			}
		});

		gameOverPanel.setBounds(gameWonPanel.getBounds());
		gameOverPanel.setVisible(false);
		gameOverPanel.setOpaque(false);
		gameOverPanel.setLayout(null);

		gameOverLabel.setVisible(true);
		gameOverLabel.setBounds(maxX / 2 - 250, 50, 500, 120);
		gameOverLabel.setOpaque(false);
		gameOverPanel.add(gameOverLabel);

		JButton restartGameButton = new JButton("Play again!");
		restartGameButton.setBounds(maxX / 2 - 100, 200, 200, 100);
		restartGameButton.setVisible(true);
		restartGameButton.setOpaque(true);
		gameOverPanel.add(restartGameButton);

		restartGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				dispose();
				new findTreasures(nickName);

			}
		});

		

		this.add(gameWonPanel);
		this.add(gameOverPanel);

		this.getContentPane().setBackground(Color.black);
		this.setMouseListener();
		this.setVisible(true);
		this.setFocusable(true);
		this.addMouseListener(this);
		drawMenuScreen();

		menuScreen.setVisible(true);

	}

	void drawMenuScreen() {
		menuScreen = new JFrame();

		menuScreen.setTitle("Find treasures menu");
		menuScreen.setBackground(Color.black);
		menuScreen.setSize(400, 400);
		menuScreen.setLocationRelativeTo(null);
		menuScreen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menuScreen.setResizable(false);
		menuScreen.setLayout(null);

		startGame.setBounds(140, 220, 120, 30);
		startGame.setBackground(Color.green);
		startGame.setVisible(true);

		JLabel numberChooserText = new JLabel("Choose mine count             Choose treasure count");
		numberChooserText.setBounds(70, 70, 300, 30);
		numberChooserText.setVisible(true);
		menuScreen.add(numberChooserText);

		SpinnerNumberModel numberModel = new SpinnerNumberModel(new Integer(5), new Integer(1), new Integer(10),
				new Integer(1));
		mineNumberChooser = new JSpinner(numberModel);
		mineNumberChooser.setBounds(100, 100, 50, 30);
		mineNumberChooser.setVisible(true);
		((JSpinner.DefaultEditor) mineNumberChooser.getEditor()).getTextField().setEditable(false);
		menuScreen.add(mineNumberChooser);

		SpinnerNumberModel numberModel1 = new SpinnerNumberModel(new Integer(5), new Integer(1), new Integer(10),
				new Integer(1));

		treasureNumberChooser = new JSpinner(numberModel1);
		treasureNumberChooser.setBounds(250, 100, 50, 30);
		treasureNumberChooser.setVisible(true);
		((JSpinner.DefaultEditor) treasureNumberChooser.getEditor()).getTextField().setEditable(false);
		menuScreen.add(treasureNumberChooser);

		menuScreen.add(startGame);
		menuScreen.add(nickText);
		menuScreen.add(infoLabel);

		JButton mainMenu = new JButton("Main menu");
		mainMenu.setBounds(140, 260, 120, 30);
		mainMenu.setBackground(Color.green);
		mainMenu.setVisible(true);
		menuScreen.add(mainMenu);

		JButton exitGame = new JButton("Exit game");
		exitGame.setBounds(140, 300, 120, 30);
		exitGame.setBackground(Color.green);
		exitGame.setVisible(true);
		menuScreen.add(exitGame);

		if (firstGameLaunch) {

			mainMenu.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					dispose();
					menuScreen.dispose();
					new menuFrame();

				}
			});

			exitGame.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					System.exit(0);

				}
			});

			startGame.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (!isPlaying && !gameOver) {

						mineCount = (Integer) mineNumberChooser.getValue();
						treasureCount = (Integer) treasureNumberChooser.getValue();

						treasureCountC = treasureCount;
						drawMines();
						drawTreasures();
						timeCounter();

						try {
							Thread.sleep(500);
						} catch (InterruptedException e1) {

							e1.printStackTrace();
						}
						menu = false;
						gameOver = false;
						isPlaying = true;
						firstGameLaunch = false;

						Runnable r1 = () -> {
							refresh();
						};
						Thread t1 = new Thread(r1);

						t1.start();

						Runnable r2 = () -> {
							intersectsTempMines();
						};
						Thread t2 = new Thread(r2);

						t2.start();

						db = new DB();

					} else {

						dispose();
						new findTreasures(nickName);
					}
					MouseHover.stop();
					menuScreen.dispose();
				}
			});
		}
	}

	void intersectsTempMines() {

		while (!gameOver) {

			if (gaming) {
				for (JLabel tempMine : tempMines) {
					if (player.getBounds().intersects(tempMine.getBounds())) {
						MouseHover.loop(1);
						break;

					} else {

						MouseHover.loop(0);

					}

				}

			}

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
	}

	void refresh() {
		try {

			while (!gameOver) {

				if (gameStarted && mouseOuttaWindow) {
					gameOver();
				}

				if (treasureCount == 0) {
					player.setBounds(0, 0, 1000, 1000);

					gameWin();
				}

				if (gameOver) {

				} else {

					move();
					intersectsTreasure();
					intersectsMines();

				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			System.out.println("Something important broke, restart game!");
			System.exit(0);
		}

	}

	void drawMines() {
		Random rand = new Random();

		for (int i = 0; i < mineCount; i++) {
			int elemPosX = rand.nextInt(700) + 40;
			int elemPosY = rand.nextInt(540);

			JLabel mine = new JLabel(new ImageIcon("mine.jpg"));

			mine.setBackground(Color.blue);
			mine.setBounds(elemPosX, elemPosY, 30, 30);
			mine.setOpaque(true);
			mine.setVisible(true);

			for (JLabel goodMine : mines) {
				while (goodMine.getBounds().intersects(mine.getBounds())) {
					elemPosX = rand.nextInt(700) + 40;
					elemPosY = rand.nextInt(540);
					mine.setBounds(elemPosX, elemPosY, 30, 30);

				}
			}

			add(mine);
			mines.add(mine);

			JLabel tempMine = new JLabel();
			tempMine.setBounds(elemPosX - 30, elemPosY - 30, 90, 90);
			tempMine.setOpaque(true);
			tempMine.setVisible(false);
			tempMine.setBackground(Color.orange);
			add(tempMine);
			tempMines.add(tempMine);

		}

	}

	void intersectsMines() {

		try {
			for (JLabel mine : mines) {

				if (player.getBounds().intersects(mine.getBounds())) {

					player.setBounds(0, 0, 1000, 1000);

					gameOver();

					playSound("treasuregame/gameover.wav");

					break;
				}

			}
		} catch (Exception e) {
			System.out.println("Something broke at intersectsMines()");
			e.printStackTrace();
			System.exit(1);
		}

	}

	void gameOver() {
		gameOver = true;
		gaming = false;

		gameOverPanel.setVisible(true);

	}

	void gameWin() {
		gameOver = true;
		gaming = false;
		gameTime.setText("Time: " + timeCount);
		gameWonPanel.setVisible(true);

	}

	void drawTreasures() {
		Random rand = new Random();

		for (int i = 1; i <= treasureCount; i++) {
			int elemPosX = rand.nextInt(700) + 40;
			int elemPosY = rand.nextInt(540);
			JLabel treasure = new JLabel(new ImageIcon("treasure.png"));
			treasure.setBackground(Color.yellow);
			treasure.setBounds(elemPosX, elemPosY, 30, 30);

			ArrayList<JLabel> elements = new ArrayList<JLabel>();

			elements.addAll(tempMines);
			elements.addAll(treasures);

			for (JLabel all : elements) {
				while (all.getBounds().intersects(treasure.getBounds())) {
					elemPosX = rand.nextInt(700) + 40;
					elemPosY = rand.nextInt(540);
					treasure.setBounds(elemPosX, elemPosY, 30, 30);
				}
			}

			treasure.setOpaque(true);
			treasure.setVisible(true);
			add(treasure);
			treasures.add(treasure);

		}

	}

	void intersectsTreasure() {

		try {
			for (JLabel treasure : treasures) {

				if (player.getBounds().intersects(treasure.getBounds())) {

					playSound("treasuregame/gen.wav");
					treasures.remove(treasure);
					treasureCount--;
					break;
				}

			}
		} catch (Exception e) {
			System.out.println("Something broke at intersectsMines()");
			e.printStackTrace();
			System.exit(1);
		}

	}

	public void paint(Graphics g) {

	}

	void move() {

		player.setLocation(playerX, playerY);

	}

	@SuppressWarnings("deprecation")
	void playSound(String fileName) {
		wavFile = new File(fileName);

		try {
			sound = Applet.newAudioClip(wavFile.toURL());
			sound.play();
		} catch (Exception e) {
			System.out.println("Gamesound: " + fileName + " is missing");
			// e.printStackTrace();
		}
	}

	public final void setMouseListener() {
		this.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {

				if (!menu && !gaming && e.getX() < 20 && e.getY() > 500) {

					gameStarted = true;
					gaming = true;

				}

				if (gaming) {
					playerX = e.getX() - 7;
					playerY = e.getY() - 32;
				}

			}

			public void mouseDragged(MouseEvent e) {

				playerX = e.getX() - 7;
				playerY = e.getY() - 32;
			}

		});
	}

	void timeCounter() {

		new Timer().schedule(new TimerTask() {

			public void run() {

				if (gaming) {

					timeCount++;


				}

			}
		}, 0, 1000);

	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

		mouseOuttaWindow = false;
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		mouseOuttaWindow = true;
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

}