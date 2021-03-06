package Client;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChattingPane extends JPanel {

	public JTextArea chatArea = new JTextArea();
	public JScrollPane chattingScroll = new JScrollPane(chatArea);
	public JTextField chatField = new JTextField();
	public JButton sendButton = new JButton("입력");

	private void addComponent() {
		setLayout(null);
		chatArea.setEnabled(false);
		chatArea.setDisabledTextColor(InstagrimUtil.black);
		chattingScroll.setBounds(6, 6, 468, 177);
		add(chattingScroll);
		sendButton.setBounds(394, 186, 80, 22);
		add(sendButton);
		chatField.setBounds(3, 186, 390, 22);
		add(chatField);
	}

	public ChattingPane() {
		setSize(480, 214);
		addComponent();
		setBackground(InstagrimUtil.clear);
	}

}
