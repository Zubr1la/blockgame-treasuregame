
import java.awt.Color;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;


public class blockGame extends JFrame implements MouseListener {

	private static final long serialVersionUID = 1L;

	//Used everywhere
	public static int timeCount = 0;

	boolean restartG = false;

	boolean gameOver = false;

	boolean pause = false;

	boolean gaming = false;

	boolean gameModeSet = false;

	ArrayList<JLabel> bricks;

	String nickName;

	int input;

	int gameMode;

	// Used in level game mode
	int levelModeLevel = 1;

	boolean firstOpen = true;

	// Used in creator game mode
	boolean brickPlacing = false;

	boolean brickRemoving = false;

	boolean restartGM1 = true;

	boolean firstLaunchGM1 = true;

	String fileName = "level";

	public static String levelName;

	ArrayList<Path> levels = new ArrayList<Path>();

	JComboBox chooseLevel;

	Path[] goodLevels;

	File wavFile;

	AudioClip sound;

	// Game window
	int maxX = 800;
	int maxY = 600;

	// Game ball
	JLabel ball;
	int ballX = 200;
	int ballY = 270;
	int speedX = 1;
	int speedY = 1;

	// Game paddle
	JLabel block;
	int blockX = 350;
	int blockY = 530;

	// Game labels/panels
	JFrame slickMenu = new JFrame();

	JPanel menuPanel = new JPanel();

	JPanel brickPane = new JPanel();

	JLabel menuBackground = new JLabel(new ImageIcon("menuscreen.png"));

	JLabel nickText;

	JLabel gameTime;

	JLabel gameOverLabel = new JLabel(new ImageIcon("gameover.png"));;

	JLabel pauseGameLabel = new JLabel(new ImageIcon("pauseGame.png"));

	JLabel levelCompleteLabel = new JLabel(new ImageIcon("levelComplete.png"));

	JButton exitGame = new JButton("Exit game");

	JButton startGame = new JButton("Start/restart game");

	JButton mainMenu = new JButton("Main menu");

	JButton nextLevel = new JButton("NEXT LEVEL");

	JButton reset = new JButton("Play again!");

	DB db;

	public blockGame(String nick, int inputMode, int mode) {

		nickName = nick;
		nickText = new JLabel("Your nickname: " + nickName);
		gameMode = mode;
		input = inputMode;

		// Game window
		this.setTitle("Block Game");
		this.setSize(maxX, maxY);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLayout(null);
		this.getContentPane().setBackground(Color.black);
		this.setMouseListener();
		this.setKeyListener();
		this.setVisible(true);
		this.setFocusable(true);

		gameTime = new JLabel();
		gameTime.setBounds(20, 20, 50, 50);
		gameTime.setFont(new Font("Serif", Font.PLAIN, 30));
		gameTime.setForeground(Color.blue);
		gameTime.setVisible(true);
		add(gameTime);

		// Paddle
		block = new JLabel(new ImageIcon("block.png"));
		block.setBackground(Color.blue);
		block.setOpaque(true);
		block.setBounds(blockX, blockY, 100, 20);
		this.add(block);

		// Game ball
		ball = new JLabel(new ImageIcon("ball.png"));
		ball.setBounds(ballX, ballY, 15, 15);
		ball.setVisible(true);
		this.add(ball);

		// Menu panel in game
		menuPanel.setLayout(null);
		menuPanel.setBounds(0, 0, 800, 600);
		menuPanel.setOpaque(false);
		menuPanel.setVisible(false);
		this.add(menuPanel);

		gameOverLabel.setBounds(300, 20, 200, 200);
		gameOverLabel.setVisible(false);
		menuPanel.add(gameOverLabel);

		pauseGameLabel.setBounds(150, 20, 500, 200);
		pauseGameLabel.setVisible(false);
		menuPanel.add(pauseGameLabel);

		levelCompleteLabel.setBounds(150, 20, 500, 200);
		levelCompleteLabel.setVisible(false);
		menuPanel.add(levelCompleteLabel);

		brickPane.setLayout(null);
		brickPane.setBounds(0, 0, 800, 600);
		brickPane.setBackground(Color.black);
		this.add(brickPane);

		bricks = new ArrayList<JLabel>();

		if (gameMode == 0) {
			addBricks(levelModeLevel);

		} else {
			drawCreatorMenu();
		}

		menuBackground.setBounds(270, 250, 250, 200);
		menuBackground.setOpaque(true);
		menuBackground.setVisible(true);

		nickText.setBounds(320, 300, 150, 15);
		nickText.setOpaque(true);
		nickText.setVisible(true);
		menuPanel.add(nickText);

		startGame.setBounds(320, 330, 150, 50);
		startGame.setBackground(Color.red);
		startGame.setVisible(true);
		menuPanel.add(startGame);

		reset.setBounds(320, 330, 150, 50);
		reset.setBackground(Color.red);
		reset.setVisible(false);
		menuPanel.add(reset);

		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				restartG = true;
				
				reset.setVisible(false);
			}

		});

		exitGame.setBounds(320, 380, 150, 50);
		exitGame.setBackground(Color.red);
		exitGame.setVisible(true);
		menuPanel.add(exitGame);

		mainMenu.setBounds(320, 470, 150, 50);
		mainMenu.setBackground(Color.red);
		mainMenu.setVisible(true);
		menuPanel.add(mainMenu);

		mainMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (gameMode == 1) {
					restartGM1 = false;

				} else {
					gameOver = true;
					dispose();
					new menuFrame();
				}
			}
		});

		nextLevel.setBounds(320, 330, 150, 50);
		nextLevel.setBackground(Color.red);
		nextLevel.setOpaque(true);
		nextLevel.setVisible(false);
		menuPanel.add(nextLevel);
		menuPanel.add(menuBackground);

		timeCount = 0;

		if (gameMode == 0) {
			gameModeSet = true;
			startLaunch();
		}

	}

	
	void startLaunch() {

		menuPanel.setVisible(true);
		repaint();
		db = new DB();
		
		startGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (gameMode == 0) {
					pause = true;
					automaticUpdates();
					menuPanel.setVisible(false);
					repaint();
					timeCounter();
					gaming = true;

					startGame.setVisible(false);
					nextLevel.setVisible(true);
					
				}
			}
			
		});
		exitGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);

			}
		});

	}

	void handleGameplay() {
	
		move();
		intersectsBrick(bricks);
		gameStatusHandler();
		revalidate();

	}

	void gameStatusHandler() {

		if (bricks.size() == 0 && gameMode == 0) {

			levelCompleteLabel.setVisible(true);

			brickPane.removeAll();
			brickPane.repaint();

			bricks.clear();

			menuPanel.setVisible(true);
			ballX = 300;
			ballY = 300;
			speedX = 1;
			speedY = 1;
			ball.setLocation(ballX, ballY);

			gameOver = true;
			gaming = false;
			pause = false;

			if (firstOpen) {

				nextLevel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						levelModeLevel++;
						addBricks(levelModeLevel);
						levelCompleteLabel.setVisible(false);

						firstOpen = false;

						pause = true;
						gaming = true;
						gameOver = false;

						menuPanel.setVisible(false);

					}

				});
			}

		}

		if (bricks.size() == 0 && gameMode == 1) {
			levelCompleteLabel.setVisible(true);
			brickPane.removeAll();
			brickPane.repaint();

			bricks.clear();

			gameOver = true;
			gaming = false;
			pause = false;

			drawCreatorMenu();

		}

		if (bricks.size() == 0 && levelModeLevel == 5 && gameMode == 0) {

			db.saveScore(nickName, "" + timeCount, 0);

			nextLevel.setVisible(false);

			reset.setVisible(true);

		}

		if (ballY >= maxY - 70) {

			gameOverLabel.setVisible(true);
			nextLevel.setVisible(false);
			startGame.setVisible(true);

			gameOver = true;
			gaming = false;
			pause = false;
			ball.setVisible(false);
			menuPanel.setVisible(true);

			brickPane.removeAll();

			brickPane.repaint();
			brickPane.setVisible(false);
			remove(brickPane);
			repaint();

			startGame.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					restartG = true;
					
				}

			});
			exitGame.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});

		}

	}

	void automaticUpdates() {
		new Thread(new Runnable() {
			public void run() {
				Timer t = new Timer();
				t.scheduleAtFixedRate(new TimerTask() {
					public void run() {

						if (gameMode == 0) {
							if (restartG) {

								dispose();
								new blockGame(nickName, input, gameMode);
								restartG = false;
								gameOver = true;

							} else if (gameOver) {

							} else {

								handleGameplay();
							}
						} else if (gameMode == 1) {

							if (!restartGM1) {

								dispose();

								new menuFrame();

								restartGM1 = true;

							}
							if (gameMode == 1) {
								if (restartG) {
									dispose();
									slickMenu.dispose();
									new blockGame(nickName, input, gameMode);
									restartG = false;

								}
							}
							if (gameOver) {

							} else {
								handleGameplay();
							}

						}

					}
				}, 0, 5);
			}
		}).start();
	}

	void pauseGame() {

		if (pause == true) {

			pause = false;
			menuPanel.setVisible(true);
			nextLevel.setVisible(false);
			startGame.setVisible(true);
			pauseGameLabel.setVisible(true);
			levelCompleteLabel.setVisible(false);

			repaint();

			exitGame.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});

			startGame.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					restartG = true;
					
				}
			});

		} else {
			nextLevel.setVisible(true);
			startGame.setVisible(false);
			menuPanel.setVisible(false);
			repaint();
			pause = true;
			pauseGameLabel.setVisible(false);
		}
	}

	void intersectsBrick(ArrayList<JLabel> blocks) {

		try {
			for (JLabel block : blocks) {

				int x = block.getBounds().x;
				int y = block.getBounds().y;

				JLabel tempLabel = new JLabel();
				tempLabel.setBounds(x - 2, y - 2, 75, 36);

				if (ball.getBounds().intersects(tempLabel.getBounds())) {

					playSound("hit.wav");

					int getX = block.getBounds().x;
					int getY = block.getBounds().y;

					if ((ballY + 3 >= getY + 29) && (ballX >= getX - 10)) {
						speedY = -speedY;

					} else if (getY > ballY + 8) {
						speedY = -speedY;

					} else if (getX > ballX) {
						speedX = -speedX;
					} else {
						speedX = -speedX;
					}

					this.getContentPane().repaint();

					block.remove(block);
					blocks.remove(block);
					block.setVisible(false);

					break;
				}

			}
		} catch (Exception e) {
			System.out.println("Something broke at intersectsBrick()");
			e.printStackTrace();
			System.exit(1);
		}

	}

	void move() {
		int moving = pause ? 1 : 0;
		int isPausedX = speedX * moving;
		int isPausedY = speedY * moving;
		ballX += isPausedX;
		ballY += isPausedY;

		if (ballX >= maxX - 20) {
			speedX = -speedX;

		} else if (ballX <= 0) {
			speedX = -speedX;

		} else if (ballY <= 0) {
			speedY = -speedY;

		}

		ball.setLocation(ballX, ballY);
		hitBlock();

	}

	void hitBlock() {
		if (block.getBounds().intersects(ball.getBounds())) {

			int x = block.getBounds().x;
			int xx = ball.getBounds().x + 7;
			if (x + 20 >= xx) {
				speedY = 1;

			} else if (x + 40 >= xx) {

				speedY = 2;

			} else if (x + 60 >= xx) {
				speedY = 4;
			} else if (x + 80 >= xx) {
				speedY = 2;
			} else {
				speedY = 1;
			}

			speedY = -speedY;

		}
	}

	void addBricks(int levelM) {
		int elemPosX = 0;
		int elemPosY = 60;

		int X = 2 * levelM;
		bricks.clear();

		for (int rows = 0; rows < 3; rows++) {
			elemPosX = maxX / 2 - 35 - 35 * X - 65;

			for (int elements = 0; elements < X; elements++) {

				JLabel brick = new JLabel(new ImageIcon("bricks.png"));

				elemPosX += 75;
				brick.setBackground(Color.WHITE); // if missing original texture
				brick.setBounds(elemPosX, elemPosY, 70, 30);
				brick.setOpaque(true);
				brick.setVisible(true);
				brickPane.add(brick);
				brickPane.repaint();
				repaint();
				bricks.add(brick);

			}

			elemPosY += 50;
		}

	}

	@SuppressWarnings("deprecation")
	void playSound(String fileName) {
		wavFile = new File(fileName);

		try {
			sound = Applet.newAudioClip(wavFile.toURL());
			sound.play();
		} catch (Exception e) {
			System.out.println("Gamesound: " + fileName + " is missing");

		}
	}

	void timeCounter() {

		new Timer().schedule(new TimerTask() {

			public void run() {

				if (!gameOver) {
					if (pause) {
						timeCount++;
					}

					String text = "" + timeCount;
					if (!pause) {

						text = "" + timeCount;

					}

					gameTime.setText(text);

				}

			}
		}, 0, 1000);

	}

	public final void setKeyListener() {
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (input == 1) {
					if (key == KeyEvent.VK_LEFT && blockX >= 0) {
						blockX -= 20;
						block.setLocation(blockX, blockY);
					} else if (key == KeyEvent.VK_RIGHT && blockX < 685) {
						blockX += 20;
						block.setLocation(blockX, blockY);
					}
				}

				if (key == KeyEvent.VK_SPACE) {

					if (gaming) {
						pauseGame();
					}
				}
			}

		});
	}

	public final void setMouseListener() {
		this.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				if (input == 0 && gameModeSet) {
					blockX = e.getX() - 50;

					block.setLocation(blockX, blockY);
					repaint();
				}
			}
		});
	}

	public void drawCreatorMenu() {

		slickMenu.setAlwaysOnTop(true);
		slickMenu.setBackground(Color.white);
		slickMenu.setSize(800, 200);
		slickMenu.setLocation(560, 650);
		slickMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		slickMenu.setResizable(false);
		slickMenu.setLayout(null);

		JPanel main = new JPanel();
		main.setSize(800, 200);
		main.setLayout(null);
		addMouseListener(this);
		
		JButton placeBrick = new JButton("Add brick");
		placeBrick.setBounds(210, 30, 120, 30);
		placeBrick.setBackground(Color.green);
		placeBrick.setVisible(true);
		main.add(placeBrick);

		JButton removeBrick = new JButton("Remove brick");
		removeBrick.setBounds(410, 30, 120, 30);
		removeBrick.setBackground(Color.green);
		removeBrick.setVisible(true);
		main.add(removeBrick);

		JButton saveBricks = new JButton("Save bricks");
		saveBricks.setBounds(640, 130, 120, 30);
		saveBricks.setBackground(Color.green);
		saveBricks.setVisible(true);
		main.add(saveBricks);

		JButton exitGameCreator = new JButton("Exit game");
		exitGameCreator.setBounds(30, 120, 120, 30);
		exitGameCreator.setBackground(Color.green);
		exitGameCreator.setVisible(true);
		main.add(exitGameCreator);
		exitGameCreator.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		JButton mainMenuCreator = new JButton("Main menu");
		mainMenuCreator.setBounds(30, 30, 120, 30);
		mainMenuCreator.setBackground(Color.green);
		mainMenuCreator.setVisible(true);
		main.add(mainMenuCreator);
		mainMenuCreator.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				slickMenu.dispose();
				new menuFrame();
			
			}
		});

		JButton showInfo = new JButton("Information");
		showInfo.setBounds(640, 30, 120, 30);
		showInfo.setBackground(Color.green);
		showInfo.setVisible(true);
		main.add(showInfo);
		showInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProcessBuilder pb = new ProcessBuilder("Notepad.exe", "readMe.txt");
				try {
					pb.start();
				} catch (IOException e1) {

					System.out.println("COULD NOT OPEN NOTEPAD!!!");
					e1.printStackTrace();
				}
			}
		});

		JButton startCustomLevel = new JButton("Start level");
		startCustomLevel.setBounds(410, 120, 120, 30);
		startCustomLevel.setBackground(Color.green);
		startCustomLevel.setVisible(true);
		main.add(startCustomLevel);

		JTextField fileNameLabel = new JTextField();
		JLabel textLabel = new JLabel("Enter Filename");
		textLabel.setBounds(640, 70, 120, 30);
		fileNameLabel.setBounds(640, 100, 120, 30);
		main.add(fileNameLabel);
		main.add(textLabel);

		if (firstLaunchGM1) {
			levelLister();
			goodLevels = levels.toArray(new Path[0]);
			chooseLevel = new JComboBox(goodLevels);
		}

		chooseLevel.setBounds(180, 120, 200, 30);
		chooseLevel.setVisible(true);

		chooseLevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileName = chooseLevel.getSelectedItem().toString();
				loadCustomLevel();
			
			}
		});

		main.add(chooseLevel);

		slickMenu.add(main);
		slickMenu.setVisible(true);

		main.setVisible(true);

		placeBrick.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				brickRemoving = false;
				brickPlacing = true;

			}
		});

		removeBrick.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				brickRemoving = true;
				brickPlacing = false;
			}
		});

		startCustomLevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (bricks.size() >= 5) {

					brickRemoving = false;
					brickPlacing = false;
					pause = true;
					gameModeSet = true;
					if (firstLaunchGM1) {
						automaticUpdates();
						firstLaunchGM1 = false;
					}

					gaming = true;
					gameOver = false;
					// slickMenu.setLocation(560, 830);
					main.setVisible(false);
					slickMenu.dispose();

				} else {
					System.out.println("You need atleast 5 bricks to start game!");
				}

			}
		});

		saveBricks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (bricks.size() >= 5) {

					fileName = fileNameLabel.getText();
					if (fileName.isEmpty()) {
						fileName = "defaultLevel";
					}

				

					levelName = "customLevel\\" + fileName + ".txt";

					cleanFile(levelName, "");
					String enter = System.lineSeparator();
					for (JLabel creatorBrick : bricks) {
						int x = creatorBrick.getBounds().x;
						int y = creatorBrick.getBounds().y;
						String text = "" + x + "," + y + enter;
						addToFile(levelName, text);
					}

					levelLister();
					main.repaint();

				} else {
					System.out.println("You need atleast 5 bricks to start game!");
				}

			}
		});

	}

	public void placeBrick(int X, int Y) {
		JLabel creatorBrick = new JLabel(new ImageIcon("bricks.png"));
		boolean inters = true;
		creatorBrick.setBackground(Color.WHITE); // if missing original texture
		creatorBrick.setBounds(X, Y, 70, 30);

		for (JLabel brick : bricks) {
			if (creatorBrick.getBounds().intersects(brick.getBounds())) {
				System.out.println("Intersects existing brick!");
				inters = false;
				break;
			}

		}

		if (inters) {
			creatorBrick.setOpaque(true);
			creatorBrick.setVisible(true);
			brickPane.add(creatorBrick);
			bricks.add(creatorBrick);
			repaint();
			brickPane.repaint();
		}
	}

	public void removeBrick(int X, int Y) {
		JLabel tempp = new JLabel();
		tempp.setBounds(X, Y, 10, 10);

		for (JLabel creatorBrick : bricks) {
			if (tempp.getBounds().intersects(creatorBrick.getBounds())) {
				brickPane.remove(creatorBrick);
				bricks.remove(creatorBrick);
				brickPane.repaint();
				repaint();

				break;
			}

		}

	}

	public void levelLister() {

		try {
			Files.walkFileTree(Paths.get("customLevel\\"), new SimpleFileVisitor<Path>() {

				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {

				
					levels.add(file);

					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e1) {
			System.out.println("Couldn't load levels");
		}

	}

	void loadCustomLevel() {

		brickPane.removeAll();
		brickPane.repaint();
		bricks.clear();

		readFile(fileName, 1);

		brickPane.repaint();

		this.repaint();

	}

	public void mouseClicked(MouseEvent e) {
		if (brickPlacing && e.getY() < 400) {
			placeBrick(e.getX() - 30, e.getY() - 30);

		} else if (brickPlacing && e.getY() > 400) {
			System.out.println("You can't place brick there!");

		} else if (brickRemoving) {
			removeBrick(e.getX() - 7, e.getY() - 30);

		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	static void cleanFile(String filename, String text) { 
		try {
			PrintStream out = new PrintStream(new FileOutputStream(filename));
			out.print(text);
			out.close();
		} catch (Exception e) {
			System.err.println("Something went wrong @ cleanFile()");
		}
	}

	static void addToFile(String filename, String text) {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
			out.print(text);
			out.close();
		} catch (FileNotFoundException e) {
			System.err.println("Something went wrong, File missing or file is in 'read only' mode");
		} catch (Exception e) {
			System.err.println("Something went wrong @ addToFile()");
		}
	}

	public void readFile(String filename, int choose) {
	
		try {
			File fileIn = new File(filename); 
			if (!fileIn.exists()) {
				System.out.println("File doesn't exist");

			}
			if (!fileIn.isFile()) {
		

			}
			Scanner sc = new Scanner(fileIn);
			while (sc.hasNext()) {
				String str = sc.nextLine();
				switch (choose) {

				case 1:
					
					int x = Integer.parseInt(str.split(",")[0]);
					int y = Integer.parseInt(str.split(",")[1]);
					placeBrick(x, y);
				

				}

			}
			sc.close();
		} catch (IOException e) {
			// e.printStackTrace();
			System.err.println("Something went wrong @ readFile()");
		}

	}

}
