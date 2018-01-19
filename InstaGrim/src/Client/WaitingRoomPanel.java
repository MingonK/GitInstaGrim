package Client;

import javax.swing.JButton;
import javax.swing.JPanel;

public class WaitingRoomPanel extends JPanel {

	public GameListPane gameListPane = new GameListPane();
	public UserListPane userListPane = new UserListPane();
	public ChattingPane chatPane = new ChattingPane();
	public UserInfoPane userInfoPane = new UserInfoPane();

	private JPanel buttonPanel = new JPanel(null);
	public JButton joinRoomButton = new JButton("입장하기");
	public JButton createRoomButton = new JButton("방만들기");

	private void addComponent() {
		setLayout(null);

		gameListPane.setLocation(0, 0);
		add(gameListPane);

		userListPane.setLocation(720, 0);
		add(userListPane);

		chatPane.setLocation(0, 426);
		add(chatPane);

		createRoomButton.setBounds(0, 108, 240, 101);
		buttonPanel.add(createRoomButton);

		joinRoomButton.setBounds(0, 6, 240, 101);
		buttonPanel.add(joinRoomButton);

		buttonPanel.setBounds(480, 426, 240, 214);
		add(buttonPanel);

		userInfoPane.setLocation(726, 432);
		add(userInfoPane);

	}

	public WaitingRoomPanel() {
		setSize(960, 640);
		setBackground(InstagrimUtil.clearivory);
		addComponent();
	}

}
