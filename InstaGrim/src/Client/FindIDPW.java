package Client;

import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

// ID / PW 찾기 다이얼로그 
public class FindIDPW extends JDialog {

	private Client client;
	private FindIDPW self = this;
	private JTabbedPane tab = new JTabbedPane();

	private JPanel idFindPanel;
	private JPanel pwFindPanel;

	public FindIDPW(JFrame jframe) {
		super(jframe, null, ModalityType.DOCUMENT_MODAL);
		setTitle("ID / PW 찾기");
		setSize(255, 198);
		setUndecorated(true);
		setLocationRelativeTo(jframe);
		addComponent();
		setBackground(InstagrimUtil.clearivory);
		setModal(true);
		setVisible(true);
	}

	protected void addComponent() {
		idPanel();
		pwPanel();
		tab.addTab("아이디 찾기", idFindPanel);
		tab.addTab("비밀번호 찾기", pwFindPanel);
		tab.setBackground(InstagrimUtil.clearivory);
		getContentPane().add(tab);
	}

	// ID찾기 탭의 컨포넌트 설정을 위한 함수
	protected void idPanel() {
		idFindPanel = new JPanel(null);

		JLabel nameLabel = new JLabel("이름");
		nameLabel.setSize(80, 15);
		nameLabel.setLocation(15, 18);

		JTextField nameField = new JTextField();
		nameField.setLocation(61, 12);
		nameField.setSize(160, 25);

		JLabel emailLabel = new JLabel("이메일");
		emailLabel.setLocation(15, 50);
		emailLabel.setSize(50, 25);

		JTextField emailField = new JTextField();
		emailField.setLocation(61, 49);
		emailField.setSize(160, 25);

		JButton findButton = new JButton("찾기");
		findButton.setLocation(36, 110);
		findButton.setSize(83, 30);

		JButton cancelButton = new JButton("취소");
		cancelButton.setLocation(120, 110);
		cancelButton.setSize(83, 30);

		idFindPanel.add(nameLabel);
		idFindPanel.add(nameField);
		idFindPanel.add(emailLabel);
		idFindPanel.add(emailField);

		idFindPanel.add(findButton);
		idFindPanel.add(cancelButton);

		findButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				client = new Client();
				if (nameField.getText().trim().length() == 0) {
					JOptionPane.showMessageDialog(self, "이름을 입력해주세요", null, JOptionPane.PLAIN_MESSAGE, null);
					nameField.grabFocus();
				} else if (nameField.getText().length() < 2) {
					JOptionPane.showMessageDialog(self, "이름은 2자 이상 입력해주세요", null, JOptionPane.PLAIN_MESSAGE, null);
					nameField.grabFocus();
				} else if (emailField.getText().trim().length() == 0) {
					JOptionPane.showMessageDialog(self, "이메일을 입력해주세요", null, JOptionPane.PLAIN_MESSAGE, null);
					emailField.grabFocus();
				} else if (InstagrimUtil.checkEmailFormat(emailField.getText()) == false) {
					JOptionPane.showMessageDialog(self, "이메일 형식이 올바르지않습니다", null, JOptionPane.PLAIN_MESSAGE, null);
					emailField.grabFocus();
				} else {
					try {
						Client.getDos().writeUTF("findID¿");
						Client.getDos().flush();
						Client.getDos().writeUTF(nameField.getText());
						Client.getDos().writeUTF(emailField.getText());
						Client.getDos().flush();
						if (Client.getDis().readBoolean()) {
							String getID = Client.getDis().readUTF();
							JOptionPane.showMessageDialog(self, "아이디는 " + getID + "입니다", null,
									JOptionPane.PLAIN_MESSAGE, null);
						} else {
							JOptionPane.showMessageDialog(self, "정보를 찾을 수 없습니다", null, JOptionPane.PLAIN_MESSAGE, null);
						}
					} catch (Exception e1) {
					}
				}
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
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

	protected void pwPanel() {
		pwFindPanel = new JPanel(null);

		JLabel idLabel = new JLabel("아이디");
		idLabel.setSize(80, 15);
		idLabel.setLocation(15, 18);

		JTextField idField = new JTextField();
		idField.setSize(160, 25);
		idField.setLocation(61, 12);

		JLabel nameLabel = new JLabel("이름");
		nameLabel.setSize(50, 15);
		nameLabel.setLocation(15, 50);

		JTextField nameField = new JTextField();
		nameField.setSize(160, 25);
		nameField.setLocation(61, 44);

		JLabel emailLabel = new JLabel("이메일");
		emailLabel.setSize(50, 15);
		emailLabel.setLocation(15, 82);

		JTextField emailField = new JTextField();
		emailField.setLocation(61, 76);
		emailField.setSize(160, 25);

		JButton findButton = new JButton("찾기");
		findButton.setSize(83, 30);
		findButton.setLocation(36, 110);

		JButton cancelButton = new JButton("취소");
		cancelButton.setSize(83, 30);
		cancelButton.setLocation(120, 110);

		pwFindPanel.add(idLabel);
		pwFindPanel.add(idField);
		pwFindPanel.add(nameLabel);
		pwFindPanel.add(nameField);
		pwFindPanel.add(emailLabel);
		pwFindPanel.add(emailField);

		pwFindPanel.add(findButton);
		pwFindPanel.add(cancelButton);

		findButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				client = new Client();
				if (idField.getText().trim().length() == 0) {
					JOptionPane.showMessageDialog(self, "아이디를 입력해주세요", null, JOptionPane.PLAIN_MESSAGE, null);
					idField.grabFocus();
				} else if (idField.getText().length() < 4) {
					JOptionPane.showMessageDialog(self, "아이디는 4자 이상 입력해주세요", null, JOptionPane.PLAIN_MESSAGE, null);
					idField.grabFocus();
				} else if (InstagrimUtil.checkInputOnlyNumberAndAlphabet(idField.getText()) == false) {
					JOptionPane.showMessageDialog(self, "아이디는 영문자 또는 숫자만 입력해주세요", null, JOptionPane.PLAIN_MESSAGE,
							null);
					idField.grabFocus();
				} else if (nameField.getText().trim().length() == 0) {
					JOptionPane.showMessageDialog(self, "이름을 입력해주세요", null, JOptionPane.PLAIN_MESSAGE, null);
					nameField.grabFocus();
				} else if (nameField.getText().length() < 2) {
					JOptionPane.showMessageDialog(self, "이름은 2자 이상 입력해주세요", null, JOptionPane.PLAIN_MESSAGE, null);
					nameField.grabFocus();
				} else if (emailField.getText().trim().length() == 0) {
					JOptionPane.showMessageDialog(self, "이메일을 입력해주세요", null, JOptionPane.PLAIN_MESSAGE, null);
					emailField.grabFocus();
				} else if (InstagrimUtil.checkEmailFormat(emailField.getText()) == false) {
					JOptionPane.showMessageDialog(self, "이메일 형식이 올바르지않습니다", null, JOptionPane.PLAIN_MESSAGE, null);
					emailField.grabFocus();
				} else {
					try {
						Client.getDos().writeUTF("findPW¿");
						Client.getDos().flush();
						Client.getDos().writeUTF(idField.getText());
						Client.getDos().writeUTF(nameField.getText());
						Client.getDos().writeUTF(emailField.getText());
						Client.getDos().flush();
						if (Client.getDis().readBoolean()) {
							String getPW = Client.getDis().readUTF();
							JOptionPane.showMessageDialog(self, "비밀번호는 " + getPW + "입니다", null,
									JOptionPane.PLAIN_MESSAGE, null);
						} else {
							JOptionPane.showMessageDialog(self, "정보를 찾을 수 없습니다", null, JOptionPane.PLAIN_MESSAGE, null);
						}
					} catch (Exception e1) {
					}
				}
			}
		});
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		idField.setDocument(new PlainDocument() {
			public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
				if (getLength() + str.length() <= 12)
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
}
