package Client;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class LoginPanel extends JPanel {

	URL backgroundURL = getClass().getClassLoader().getResource("background.png");
	private ImageIcon backgroundImage = new ImageIcon(backgroundURL);

	public JLabel titleLabel = new JLabel("계정 로그인");
	public JLabel idLabel = new JLabel("아이디");
	public JButton loginButton = new JButton("로그인");
	public JTextField idField = new JTextField();
	public JLabel pwLabel = new JLabel("비밀번호");
	public JPasswordField pwField = new JPasswordField();
	public JButton exitButton = new JButton("나가기");
	public JLabel findidpwLabel = new JLabel("계정을 잊으셨나요?");
	public JLabel askLabel = new JLabel("아직 계정이 없으신가요?");
	public JLabel joinLabel = new JLabel("지금 가입하세요!");
	URL soundonURL = getClass().getClassLoader().getResource("soundon.png");
	public JButton soundButton = new JButton(new ImageIcon(soundonURL));
	private final JPanel panel = new JPanel();

	public void paintComponent(Graphics g) {
		g.drawImage(backgroundImage.getImage(), 0, 0, null);
		setOpaque(false);
		super.paintComponent(g);
	}

	protected void addComponent() {
		idLabel.setBounds(52, 234, 57, 24);
		add(idLabel);

		idField.setBounds(50, 257, 237, 24);
		add(idField);
		idField.setColumns(10);

		pwLabel.setBounds(52, 290, 57, 24);
		add(pwLabel);

		pwField.setBounds(50, 313, 237, 24);
		add(pwField);

		loginButton.setBounds(123, 358, 76, 33);
		add(loginButton);

		exitButton.setBounds(211, 358, 76, 33);
		add(exitButton);

		askLabel.setBounds(52, 410, 147, 24);
		add(askLabel);

		findidpwLabel.setForeground(InstagrimUtil.blue);
		findidpwLabel.setBounds(52, 435, 135, 24);
		add(findidpwLabel);

		joinLabel.setForeground(InstagrimUtil.blue);
		joinLabel.setBounds(181, 415, 108, 15);
		add(joinLabel);

		soundButton.setBorderPainted(false);
		soundButton.setFocusPainted(false);
		soundButton.setContentAreaFilled(false);
		soundButton.setBackground(InstagrimUtil.clear);
		soundButton.setLocation(912, 17);
		soundButton.setSize(30, 30);
		add(soundButton);

		panel.setBackground(InstagrimUtil.clearivory);
		panel.setBounds(39, 187, 262, 308);
		add(panel);
	}

	public LoginPanel() {
		setSize(960, 640);
		setLayout(null);
		addComponent();
		limitInputLength();
		idField.grabFocus();
	}

	// 글자 수 제한
	private void limitInputLength() {
		idField.setDocument(new PlainDocument() {
			public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
				if (getLength() + str.length() <= 12)
					super.insertString(offs, str, a);
			}
		});
		pwField.setDocument(new PlainDocument() {
			public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
				if (getLength() + str.length() <= 12)
					super.insertString(offs, str, a);
			}
		});
	}
}
