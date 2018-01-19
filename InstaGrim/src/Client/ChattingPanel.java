package Client;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChattingPanel extends JPanel {

	public JTextArea chatArea = new JTextArea();
	public JScrollPane chattingScroll = new JScrollPane(chatArea);
	public JTextField chatField = new JTextField();
	public JButton sendButton = new JButton("입력");

	private void addComponent() {
		setLayout(null);
		chatArea.setEditable(false);
		chattingScroll.setBounds(6, 6, 708, 177);
		add(chattingScroll);
		sendButton.setBounds(634, 186, 80, 22);
		add(sendButton);
		chatField.setBounds(3, 186, 630, 22);
		add(chatField);
	}
	
	public ChattingPanel() {
		setSize(720, 214);
		addComponent();
		setBackground(InstagrimUtil.clear);
	}

}
