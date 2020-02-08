
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class DB extends JFrame {

	private static final long serialVersionUID = 1L;
	
	Connection con = null;
	Statement stmt = null;
	ResultSet rs;
	
	public DB() { 		//Configure this before trying to launch application.  
		try {
			String dbType = "mysql";
			String dbServer = "XXXXXXXXX";
			String port = "XXXXXXXXXXX";
			String dbName = "XXXXXXXXX";
			String username = "XXXXXXXXX";
			String pw = "XXXXXXXXX";
			con = DriverManager.getConnection("jdbc:" + dbType + "://" + dbServer + ":" + port + "/" + dbName, username,
					pw);
			stmt = con.createStatement();
			System.out.println("Successful DB connect");
		} catch (Exception e) {
			System.out.println(e);
			System.exit(0);
		}
	}

	void saveScore(String user, String score, int game) {
		String query;
		try {
			if(game == 0) {
				 query = "INSERT INTO `blockgameScore` ( `username`, `score`) VALUES  ('" + user + "','" + score
						+ "')";
			}else {
				 query = "INSERT INTO `treasureScore` ( `username`, `score`) VALUES  ('" + user + "','" + score
						+ "')";
			}
			
			stmt.executeUpdate(query);

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error at saving score to database!");
		}

	}
	
	void returnScore(int game) {

		JFrame score = new JFrame();
		score.setAlwaysOnTop(true);
		score.setBackground(Color.white);
		score.setSize(400, 400);
		score.setLocationRelativeTo(null);
		score.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		score.setResizable(false);
		
		score.setVisible(true);
		JPanel scoresPanel = new JPanel();
		scoresPanel.setLayout(null);
		scoresPanel.setBackground(Color.WHITE);
		scoresPanel.setVisible(true);
		scoresPanel.setOpaque(true);
				
		JLabel text = new JLabel();
		text.setBounds(95, 5, 205, 30);
		text.setOpaque(true);
		text.setText("        Username                Time(s)");
		text.setVisible(true);
		text.setBackground(Color.ORANGE);
		
		JButton closeScores = new JButton("Close");
		closeScores.setBounds(150, 330, 100, 30);
		closeScores.setBackground(Color.cyan);
		closeScores.setVisible(true);
		scoresPanel.add(closeScores);
		
		closeScores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				score.dispose();
			}

		});
		
		
		scoresPanel.add(text);

		int yy = 40;

		try {
			String sql;
			 if(game==0) {
				 sql = "SELECT * FROM blockgameScore ORDER BY score+0 ASC LIMIT 8 ";
			 }else {
				 sql = "SELECT * FROM treasureScore ORDER BY score+0 ASC LIMIT 8 ";
			 }
			PreparedStatement stmt = con.prepareStatement(sql);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {

				JLabel nickLabel = new JLabel(new ImageIcon("scoreTable.png"));
				nickLabel.setBounds(95, yy, 100, 30);
				nickLabel.setOpaque(true);
				nickLabel.setText(rs.getString("username"));
				nickLabel.setVisible(true);
				nickLabel.setBackground(Color.green);
				scoresPanel.add(nickLabel);
				nickLabel.setHorizontalTextPosition(JLabel.CENTER);
				nickLabel.setVerticalTextPosition(JLabel.CENTER);

				JLabel scoreLabel = new JLabel(new ImageIcon("scoreTable.png"));
				scoreLabel.setBounds(200, yy, 100, 30);
				scoreLabel.setOpaque(true);
				scoreLabel.setText(rs.getString("score"));
				scoreLabel.setVisible(true);
				scoreLabel.setBackground(Color.green);
				scoreLabel.setHorizontalTextPosition(JLabel.CENTER);
				scoreLabel.setVerticalTextPosition(JLabel.CENTER);
				scoresPanel.add(scoreLabel);

				yy += 35;

			}

			score.add(scoresPanel);

		} catch (Exception e) {
			System.out.println(e);
			System.out.println("Can't read data from database!");
		}

	}

	

	public void close() {
		try {
			con.close();
		} catch (Exception e) {

		}
	}

}
