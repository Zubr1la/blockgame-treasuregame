import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class menuFrame extends JFrame {

	public static String nickName = "username";
	public static int inputMode;
	public static int gameMode;

	private static final long serialVersionUID = 1L;

	JFrame menu = new JFrame();

	JFrame bOpt = new JFrame();

	DB db;

	menuFrame() {

		db = new DB();

		menu.setTitle("Menu");
		menu.setSize(360, 600);
		menu.setLocationRelativeTo(null);
		menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menu.setResizable(false);
		menu.setLayout(null);
		menu.setBackground(Color.LIGHT_GRAY);

		JTextField nick = new JTextField();
		nick.setBounds(360 / 2 - 50, 80, 100, 20);
		menu.add(nick);

		JLabel textLabel = new JLabel(
				"<html><div style='text-align: center;'>Enter your nickname<br> Max length 12 symbols, <br> if longer, it will be cropped to 12  </div></html>");
		textLabel.setBounds(90, 20, 200, 60);
		menu.add(textLabel);

		JButton submit = new JButton("Save");
		submit.setBounds(360 / 2 - 50, 100, 100, 20);
		menu.add(submit);

		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (nick.getText().length() > 12) {
					nickName = nick.getText().substring(0, 12);
				} else {
					nickName = nick.getText();
				}

				if (nick.getText().length() == 0) {
					nickName = "username";
				}

				System.out.println(nickName);
			}
		});

		JButton startBlockGame = new JButton("LAUNCH BLOCK GAME");
		startBlockGame.setBounds(360 / 2 - 150, 150, 300, 50);
		startBlockGame.setBackground(Color.orange);
		startBlockGame.setVisible(true);
		menu.add(startBlockGame);

		startBlockGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				new blockGame(nickName, inputMode, gameMode);

				menu.dispose();
				bOpt.dispose();

			}
		});

		JButton blockScores = new JButton("Show BlockGame top 8");
		blockScores.setBounds(360 / 2 - 150, 200, 300, 50);
		blockScores.setBackground(Color.orange);
		blockScores.setVisible(true);
		menu.add(blockScores);

		blockScores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				db.returnScore(0);

			}
		});

		JButton startTreasuresGame = new JButton("LAUNCH Treasure game");
		startTreasuresGame.setBounds(360 / 2 - 150, 330, 300, 50);
		startTreasuresGame.setBackground(Color.green);
		startTreasuresGame.setVisible(true);
		menu.add(startTreasuresGame);

		startTreasuresGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new findTreasures(nickName);
				menu.dispose();
				bOpt.dispose();
			}
		});

		JButton treasureScores = new JButton("Show Treasure game top 8");
		treasureScores.setBounds(360 / 2 - 150, 380, 300, 50);
		treasureScores.setBackground(Color.green);
		treasureScores.setVisible(true);
		menu.add(treasureScores);

		treasureScores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				db.returnScore(1);

			}
		});

		JButton exit = new JButton("EXIT");
		exit.setBounds(360 / 2 - 150, 450, 300, 50);
		exit.setBackground(Color.red);
		exit.setVisible(true);
		menu.add(exit);

		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		JButton blockOptions = new JButton("Block game options");
		blockOptions.setBounds(360 / 2 - 150, 250, 300, 50);
		blockOptions.setBackground(Color.orange);
		blockOptions.setVisible(true);
		menu.add(blockOptions);

		menu.setVisible(true);

		blockOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				bOpt.setTitle("Block Game Options");
				bOpt.setSize(400, 200);
				bOpt.setLocationRelativeTo(null);
				bOpt.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				bOpt.setResizable(false);
				bOpt.setLayout(null);

				JButton keyboard = new JButton("Keyboard");
				keyboard.setBounds(100, 40, 100, 30);
				keyboard.setBackground(Color.green);
				keyboard.setVisible(true);
				bOpt.add(keyboard);

				JButton mouse = new JButton("Mouse");
				mouse.setBounds(200, 40, 100, 30);
				mouse.setBackground(Color.green);
				mouse.setVisible(true);
				bOpt.add(mouse);

				JLabel inputText = new JLabel("Choose your input mode");
				inputText.setBounds(130, 20, 300, 20);
				bOpt.add(inputText);
				inputText.setVisible(true);

				JButton levelMode = new JButton("Level");
				levelMode.setBounds(100, 100, 100, 30);
				levelMode.setBackground(Color.green);
				levelMode.setVisible(true);
				bOpt.add(levelMode);

				JButton creatorMode = new JButton("Creative");
				creatorMode.setBounds(200, 100, 100, 30);
				creatorMode.setBackground(Color.green);
				creatorMode.setVisible(true);
				bOpt.add(creatorMode);

				JLabel textMode = new JLabel("Choose your game mode");
				textMode.setBounds(130, 80, 300, 20);
				bOpt.add(textMode);
				textMode.setVisible(true);

				bOpt.setVisible(true);

				keyboard.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						inputMode = 1;
					}
				});
				mouse.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						inputMode = 0;
					}
				});

				levelMode.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						gameMode = 0;
					}
				});
				creatorMode.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						gameMode = 1;

					}
				});

				JButton closeOption = new JButton("Close");
				closeOption.setBounds(400 / 2 - 50, 135, 100, 30);
				closeOption.setBackground(Color.cyan);
				closeOption.setVisible(true);
				bOpt.add(closeOption);

				closeOption.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {

						bOpt.dispose();
					}

				});

			}
		});

	}

}
