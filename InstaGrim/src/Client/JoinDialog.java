package Client;

import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

// 회원가입 다이얼로그
public class JoinDialog extends JDialog implements ActionListener {

	private Client client;

	private JLabel titleLabel = new JLabel("회원가입");

	private JLabel idLabel = new JLabel("아이디");
	private JTextField idField = new JTextField();

	private JLabel pwLabel = new JLabel("비밀번호");
	private JPasswordField pwField = new JPasswordField();

	private JLabel pwcLabel = new JLabel("비밀번호 확인");
	private JPasswordField pwcField = new JPasswordField(); // PW확인을 위한 컨포넌트들

	private JLabel nameLabel = new JLabel("이름");
	private JTextField nameField = new JTextField(); // 이름입력을 위한 컨포넌트들

	private JLabel emailLabel = new JLabel("이메일");
	private JTextField emailField = new JTextField();

	private JButton joinButton = new JButton("회원가입"); // 회원가입 버튼
	private JButton cancelButton = new JButton("취소"); // 취소 버튼

	public void addActionListener() {
		this.joinButton.addActionListener(this);
		this.cancelButton.addActionListener(this);
	}

	public JoinDialog(JFrame jframe) {
		super(jframe, null, ModalityType.DOCUMENT_MODAL);
		setTitle("회원가입");
		setSize(270, 246);
		setResizable(false);
		setUndecorated(true);
		setLocationRelativeTo(jframe);
		addActionListener();
		limitInputLength();
		addComponent();
		setBackground(InstagrimUtil.clearivory);
		setModal(true);
		setVisible(true);
	}

	protected void addComponent() {
		getContentPane().setLayout(null);

		titleLabel.setForeground(InstagrimUtil.black);
		titleLabel.setBounds(15, 15, 57, 15);
		getContentPane().add(titleLabel);

		idLabel.setForeground(InstagrimUtil.black);
		idLabel.setBounds(15, 48, 36, 15);
		getContentPane().add(idLabel);

		idField.setBounds(96, 42, 160, 25);
		getContentPane().add(idField);

		pwLabel.setForeground(InstagrimUtil.black);
		pwLabel.setBounds(15, 80, 69, 15);
		getContentPane().add(pwLabel);

		pwField.setBounds(96, 74, 160, 25);
		getContentPane().add(pwField);

		pwcLabel.setForeground(InstagrimUtil.black);
		pwcLabel.setBounds(15, 112, 95, 15);
		getContentPane().add(pwcLabel);

		pwcField.setBounds(96, 106, 162, 25);
		getContentPane().add(pwcField);
		nameLabel.setSize(80, 15);
		nameLabel.setLocation(15, 144);

		nameLabel.setForeground(InstagrimUtil.black);
		getContentPane().add(nameLabel);
		nameField.setSize(162, 25);
		nameField.setLocation(96, 139);

		nameField.setForeground(InstagrimUtil.black);
		getContentPane().add(nameField);

		emailLabel.setForeground(InstagrimUtil.black);
		emailLabel.setBounds(15, 176, 85, 15);
		getContentPane().add(emailLabel);

		emailField.setBounds(96, 170, 160, 25);
		getContentPane().add(emailField);

		joinButton.setBounds(44, 200, 83, 30);
		getContentPane().add(joinButton);

		cancelButton.setBounds(140, 200, 83, 30);
		getContentPane().add(cancelButton);
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
				if (getLength() + str.length() <= 15)
					super.insertString(offs, str, a);
			}
		});
		pwcField.setDocument(new PlainDocument() {
			public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
				if (getLength() + str.length() <= 15)
					super.insertString(offs, str, a);
			}
		});
		emailField.setDocument(new PlainDocument() {
			public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
				if (getLength() + str.length() <= 24)
					super.insertString(offs, str, a);
			}
		});
		nameField.setDocument(new PlainDocument() {
			public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
				if (getLength() + str.length() <= 20)
					super.insertString(offs, str, a);
			}
		});
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == joinButton) {
			if (idField.getText().trim().length() == 0) {
				JOptionPane.showMessageDialog(this, "아이디를 입력해주세요", null, JOptionPane.PLAIN_MESSAGE, null);
				idField.grabFocus();
			} else if (idField.getText().length() < 4) {
				JOptionPane.showMessageDialog(this, "아이디는 4자 이상 입력해주세요", null, JOptionPane.PLAIN_MESSAGE, null);
				idField.grabFocus();
			} else if (InstagrimUtil.checkInputOnlyNumberAndAlphabet(idField.getText()) == false) {
				JOptionPane.showMessageDialog(this, "아이디는 영문자 또는 숫자만 입력해주세요", null, JOptionPane.PLAIN_MESSAGE, null);
				idField.grabFocus();
			} else if (String.valueOf(pwField.getPassword()).trim().length() == 0) {
				JOptionPane.showMessageDialog(this, "비밀번호를 입력해주세요", null, JOptionPane.PLAIN_MESSAGE, null);
				pwField.grabFocus();
			} else if (String.valueOf(pwField.getPassword()).length() < 4) {
				JOptionPane.showMessageDialog(this, "비밀번호는 4자 이상 입력해주세요", null, JOptionPane.PLAIN_MESSAGE, null);
				pwField.grabFocus();
			} else if (InstagrimUtil.checkInputOnlyNumberAndAlphabet(String.valueOf(pwField.getPassword())) == false) {
				JOptionPane.showMessageDialog(this, "비밀번호는 영문자 또는 숫자만 입력해주세요", null, JOptionPane.PLAIN_MESSAGE, null);
				pwField.grabFocus();
			} else if (!String.valueOf(pwField.getPassword()).equals(String.valueOf(pwcField.getPassword()))) {
				JOptionPane.showMessageDialog(this, "비밀번호가 일치하지 않습니다", null, JOptionPane.PLAIN_MESSAGE, null);
				pwcField.setText("");
				pwcField.grabFocus();
			} else if (nameField.getText().trim().length() == 0) {
				JOptionPane.showMessageDialog(this, "이름을 입력해주세요", null, JOptionPane.PLAIN_MESSAGE, null);
				nameField.grabFocus();
			} else if (nameField.getText().length() < 2) {
				JOptionPane.showMessageDialog(this, "이름은 2자 이상 입력해주세요", null, JOptionPane.PLAIN_MESSAGE, null);
				nameField.grabFocus();
			} else if (emailField.getText().trim().length() == 0) {
				JOptionPane.showMessageDialog(this, "이메일을 입력해주세요", null, JOptionPane.PLAIN_MESSAGE, null);
				emailField.grabFocus();
			} else if (InstagrimUtil.checkEmailFormat(emailField.getText()) == false) {
				JOptionPane.showMessageDialog(this, "이메일 형식이 올바르지않습니다", null, JOptionPane.PLAIN_MESSAGE, null);
				emailField.grabFocus();
			} else {
				client = new Client();
				try {
					Client.getDos().writeUTF("joinMember¿");
					Client.getDos().flush();
					Client.getDos().writeUTF(idField.getText());
					Client.getDos().flush();
					if (Client.getDis().readBoolean()) {
						JOptionPane.showMessageDialog(this, "이미 사용중인 아이디 입니다", null, JOptionPane.PLAIN_MESSAGE, null);
						idField.grabFocus();
					} else {
						Client.getDos().writeUTF(String.valueOf(pwField.getPassword()));
						Client.getDos().writeUTF(nameField.getText());
						Client.getDos().writeUTF(emailField.getText());
						Client.getDos().flush();
						if (Client.getDis().readInt() > 0) {
							JOptionPane.showMessageDialog(this, "축하합니다! 가입이 완료되었습니다!", null, JOptionPane.PLAIN_MESSAGE,
									null);
							dispose();
						}
					}
				} catch (IOException ioe) {
				}
			}
		} else if (e.getSource() == cancelButton) {
			int select = JOptionPane.showConfirmDialog(this, "가입을 취소하시겠습니까?", null, JOptionPane.YES_NO_OPTION,
					JOptionPane.PLAIN_MESSAGE, null);
			if (select == JOptionPane.YES_OPTION) {
				dispose();
			}
		}
	}
}
