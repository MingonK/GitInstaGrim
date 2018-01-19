package Client;

import java.awt.Graphics;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class UserInfoPane extends JPanel {
	//getClass().getResource("/image/avatar.png")
	URL avatarURL = getClass().getClassLoader().getResource("avatar.png");
	private ImageIcon avatarImage = new ImageIcon(avatarURL);

	public JPanel avatarPanel;
	public JLabel idLabel = new JLabel();
	public JLabel scoreLabel = new JLabel();
	public JLabel lvLabel = new JLabel("LV");
	public JProgressBar expBar = new JProgressBar();
	public JButton soundButton = new JButton();
	public JButton exitButton = new JButton();

	private void addComponent() {
		setLayout(null);

		avatarPanel = new JPanel() {
			public void paintComponent(Graphics g) {
				g.drawImage(avatarImage.getImage(), 0, 0, null);
				setOpaque(false);
				super.paintComponent(g);
			}
		};

		idLabel.setForeground(InstagrimUtil.black);
		idLabel.setBounds(126, 36, 87, 24);
		add(idLabel);

		lvLabel.setForeground(InstagrimUtil.black);
		lvLabel.setBounds(126, 70, 23, 24);
		add(lvLabel);

		expBar.setMinimum(0);
		expBar.setMaximum(100);

		expBar.setBounds(151, 70, 65, 24);
		add(expBar);

		scoreLabel.setForeground(InstagrimUtil.black);
		scoreLabel.setBounds(126, 104, 87, 24);
		add(scoreLabel);

		avatarPanel.setBounds(24, 38, 90, 90);
		add(avatarPanel);

		soundButton.setText("음악 끄기");
		soundButton.setLocation(34, 151);
		soundButton.setSize(76, 33);

		add(soundButton);
		exitButton.setText("나가기");
		exitButton.setBounds(122, 151, 76, 33);
		add(exitButton);
	}

	public UserInfoPane() {
		setSize(228, 202);
		addComponent();
		setBackground(InstagrimUtil.clear);
	}
}