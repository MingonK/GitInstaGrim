package Client;

import java.awt.Dimension;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class MakeRoomDialog extends JDialog {
	
	public JTextField titleField = new JTextField();
	URL lockURL = getClass().getClassLoader().getResource("unlock.png"); // image/unlock.png
	public JButton lockButton = new JButton(new ImageIcon(lockURL));
	public JPasswordField pwField = new JPasswordField();
	public JComboBox<String> personnelBox = new JComboBox<String>();
	public JButton makeButton = new JButton("만들기");
	public JButton exitButton = new JButton("닫기");


	public MakeRoomDialog(JFrame jframe, String joinUser, String roomTitle) {
		dispose();
	}

	public MakeRoomDialog(JFrame jframe) {
		this.setSize(266, 202);
		this.setLocationRelativeTo(jframe);
		this.setResizable(false);
		this.limitInputLength();
		this.compInit();
		this.setUndecorated(true);
		this.setBackground(InstagrimUtil.clearivory);
		this.setVisible(true);
		this.setModal(true);
	}

	private void compInit() {
		setLayout(null);
		makeButton.setBounds(47, 161, 80, 23);
		add(makeButton);
		exitButton.setBounds(137, 161, 80, 23);
		add(exitButton);
		personnelBox.setBounds(75, 118, 170, 23);
		add(personnelBox);
		pwField.setBounds(102, 83, 143, 23);
		add(pwField);

		this.pwField.setPreferredSize(new Dimension(100, 20));
		this.pwField.setEditable(false);
		
		lockButton.setBorderPainted(false);
		lockButton.setFocusPainted(false);
		lockButton.setContentAreaFilled(false);
		lockButton.setBackground(InstagrimUtil.clear);
		lockButton.setBounds(75, 83, 23, 23);
		add(lockButton);

		JLabel titlelabel = new JLabel("방 만들기");
		titlelabel.setBounds(15, 15, 57, 15);
		add(titlelabel);

		JLabel roomnameLabel = new JLabel("방 제목");
		roomnameLabel.setBounds(20, 55, 45, 15);
		add(roomnameLabel);

		JLabel lroomLabel = new JLabel("비밀 방");
		lroomLabel.setBounds(20, 88, 45, 15);
		add(lroomLabel);

		JLabel personLabel = new JLabel("인원 수");
		personLabel.setBounds(20, 121, 45, 15);
		add(personLabel);

		this.personnelBox.addItem("2");
		this.personnelBox.addItem("3");
		this.personnelBox.addItem("4");
	}

	private void limitInputLength() {
		titleField.setBounds(75, 50, 170, 23);
		add(titleField);
		titleField.setDocument(new PlainDocument() {
			public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
				if (getLength() + str.length() <= 20)
					super.insertString(offs, str, a);
			}
		});
		pwField.setDocument(new PlainDocument() {
			public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
				if (getLength() + str.length() <= 4)
					super.insertString(offs, str, a);
			}
		});
	}

	public void createRoom() {
		dispose();
	}

}