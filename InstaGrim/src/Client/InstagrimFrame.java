package Client;

import java.awt.CardLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.FontUIResource;

public class InstagrimFrame extends JFrame implements ActionListener, MouseListener, MouseMotionListener {
	public static Client client;

	private InstagrimFrame self = this;
	public InstagrimManager manager = new InstagrimManager();

	private int x;
	private int y;
	// "image/background.png"
	URL backgroundURL = getClass().getClassLoader().getResource("background.png");
	private ImageIcon backgroundImage = new ImageIcon(backgroundURL);
	public CardLayout cards = new CardLayout();
	public static LoginPanel loginPanel;
	public static WaitingRoomPanel waitingroomPanel;
	public static GamePanel gameroomPanel;
	public MakeRoomDialog makeroomDialog;
	public InstagrimUtil instagrimUtil;

	public String userID;
	private String clientId;
	private String clientLv;
	private String clientRecord;
	private String clientWin;
	private String clientLose;
	private String clientExp;
	private int winningRate = 0;
	private String removeClientId;

	public String getScore = "0";
	private String roomTitle = "";
	private String roomPw;
	private String userCount;
	private String totalPerson;
	private String statusGame;

	public static HashMap<String, ArrayList<LoginUserInfo>> userDataMap = new HashMap<String, ArrayList<LoginUserInfo>>();
	public static ArrayList<LoginUserInfo> userData = new ArrayList<LoginUserInfo>();

	private String makeroom_title;
	private String makeroom_pw;
	private String makeroom_personnel;

	private void addContentPane() {
		getContentPane().setLayout(cards);
		getContentPane().add("login", loginPanel);
		getContentPane().add("waitingroom", waitingroomPanel);
		getContentPane().add("gameroom", gameroomPanel);
	}

	public void addEvent() {
		addMouseListener(this);
		addMouseMotionListener(this);
		loginPanel.joinLabel.addMouseListener(this);
		loginPanel.findidpwLabel.addMouseListener(this);
		loginPanel.loginButton.addActionListener(this);
		loginPanel.exitButton.addActionListener(this);
		loginPanel.soundButton.addActionListener(this);
		waitingroomPanel.gameListPane.gameTable.addMouseListener(this);
		waitingroomPanel.chatPane.chatField.addActionListener(this);
		waitingroomPanel.chatPane.sendButton.addActionListener(this);
		waitingroomPanel.createRoomButton.addActionListener(this);
		waitingroomPanel.joinRoomButton.addActionListener(this);
		waitingroomPanel.userInfoPane.soundButton.addActionListener(this);
		waitingroomPanel.userInfoPane.exitButton.addActionListener(this);
		gameroomPanel.chattingPanel.chatField.addActionListener(this);
		gameroomPanel.chattingPanel.sendButton.addActionListener(this);
		gameroomPanel.canvasPanel.addMouseListener(this);
		gameroomPanel.canvasPanel.addMouseMotionListener(this);
		gameroomPanel.redButton.addActionListener(this);
		gameroomPanel.greenButton.addActionListener(this);
		gameroomPanel.blueButton.addActionListener(this);
		gameroomPanel.blackButton.addActionListener(this);
		gameroomPanel.eraserButton.addActionListener(this);
		gameroomPanel.clearButton.addActionListener(this);
		gameroomPanel.readyButton.addActionListener(this);
		gameroomPanel.soundButton.addActionListener(this);
		gameroomPanel.exitButton.addActionListener(this);
	}

	public static void main(String[] args) {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
		}
		new InstagrimFrame();
	}

	public InstagrimFrame() {
		setUIFont(new FontUIResource(new Font("맑은 고딕", Font.PLAIN, 11)));
		loginPanel = new LoginPanel();
		gameroomPanel = new GamePanel();
		waitingroomPanel = new WaitingRoomPanel();
		setSize(960, 640);
		setLayout(null);
		setUndecorated(true);
		setLocationRelativeTo(null);
		addContentPane();
		addEvent();
		setVisible(true);
		instagrimUtil = new InstagrimUtil();
		instagrimUtil.sound();
	}

	public void listenMsg() { // 서버에서 오는 메세지를 기다리는 스레드
		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						String recieveMsg; // 서버에서 오는 메세지를 받음
						while ((recieveMsg = Client.getDis().readUTF()) != null) { // 서버로부터 전달받은 메세지가 있다면
							String[] delimiter = recieveMsg.split("¿"); // "¿"를 기준으로 메세지 구분 
							if (delimiter[0].equals("connect")) { // 로그인 시 서버로 부터 전달받는 신호
								clientId = delimiter[1];
								clientLv = delimiter[2];
								clientRecord = delimiter[3];
								clientWin = delimiter[4];
								clientLose = delimiter[5];
								clientExp = delimiter[6];
								userData.add(
										new LoginUserInfo(clientLv, clientRecord, clientWin, clientLose, clientExp));
								userDataMap.put(clientId, userData);
								if (Integer.parseInt(clientRecord) != 0) {
									winningRate = Integer.parseInt(clientWin) / Integer.parseInt(clientRecord);
								}
								waitingroomPanel.userListPane.updateUserList(winningRate);
								Client.getDos().writeUTF("updateRoomList¿");
								Client.getDos().flush();

								if (userID.equals(clientId)) {
									waitingroomPanel.userInfoPane.idLabel.setText(clientId);
									waitingroomPanel.userInfoPane.lvLabel.setText(clientLv);
									waitingroomPanel.userInfoPane.scoreLabel.setText(clientRecord + "전 " + clientWin
											+ "승 " + clientLose + "패 " + "(" + winningRate + "%)");
									waitingroomPanel.userInfoPane.expBar.setValue(clientExp.length());
								}

							} else if (delimiter[0].equals("chatMsg")) {
								String msg = delimiter[1];
								waitingroomPanel.chatPane.chatArea.append(msg);
								waitingroomPanel.chatPane.chattingScroll.getVerticalScrollBar().setValue(
										waitingroomPanel.chatPane.chattingScroll.getVerticalScrollBar().getMaximum());
							} else if (delimiter[0].equals("remove")) {
								removeClientId = delimiter[1];
								userDataMap.remove(removeClientId);
								userData.remove(
										new LoginUserInfo(clientLv, clientRecord, clientWin, clientLose, clientExp));
								waitingroomPanel.userListPane.updateUserList(winningRate);

							} else if (delimiter[0].equals("createRoom")) {
								waitingroomPanel.gameListPane.setGameListNumRow();
								Client.getDos().writeUTF("updateRoomList¿");
								Client.getDos().flush();
								GamePanel.userID = userID;
								gameroomPanel.roomTitle = makeroom_title;
								gameroomPanel.canvasPanel.roomTitle = makeroom_title;
								cards.show(getContentPane(), "gameroom");
							} else if (delimiter[0].equals("uncreatedRoom")) {
								JOptionPane.showMessageDialog(self, "이미 존재하는 방입니다.", "enterRoom",
										JOptionPane.PLAIN_MESSAGE, null);

							} else if (delimiter[0].equals("updateroom")) {
								// 방리스트들을 업데이트 하라는 신호를 보내면
								roomTitle = delimiter[1];
								roomPw = delimiter[2];
								userCount = delimiter[3];
								totalPerson = delimiter[4];
								statusGame = delimiter[5];
								// 순서대로 정보를 저장해서
								waitingroomPanel.gameListPane.updateRoomList(roomTitle, roomPw, userCount, totalPerson,
										statusGame);
								// 룸리스트패널의 updateRoomList라는 메소드를 실행시켜 JTable에 전달받은 정보를 추가함
							} else if (delimiter[0].equals("setGameListInit")) {
								// 테이블의 리스트를 초기화 시키지 않으면 전달 받은 데이터가 추가되기 때문에 일단 초기화시킴
								waitingroomPanel.gameListPane.setGameListNumRow();

							} else if (delimiter[0].equals("comein")) {
								// comein은 입장해도 좋다는 메세지 이므로 입장을 성공한다.
								if (waitingroomPanel.gameListPane.roomTitle.equals(null)) {
									gameroomPanel.roomTitle = makeroom_title;
									gameroomPanel.canvasPanel.roomTitle = makeroom_title;
									GamePanel.userID = userID;
								} else {
									gameroomPanel.roomTitle = waitingroomPanel.gameListPane.roomTitle;
									gameroomPanel.canvasPanel.roomTitle = waitingroomPanel.gameListPane.roomTitle;
									GamePanel.userID = userID;
								}
								cards.show(getContentPane(), "gameroom");
								// makeRoomDialog를 통해 입장된 유저의 창을 전환시켜준다.
							} else if (delimiter[0].equals("fail")) {
								// 유저가 가득차서 입장에 실패했으므로 아래와 같은 메세지를 출력시킨다.
								JOptionPane.showMessageDialog(self, "최대 허용인원을 초과하였습니다.", "enterRoom",
										JOptionPane.PLAIN_MESSAGE, null);

							} else if (delimiter[0].equals("insertPw")) {
								// 비밀번호를 입력하라는 메세지 이므로 inputDialog를 실행시켜 비밀번호를 입력받는다.
								String password = JOptionPane.showInputDialog(self, "비밀번호를 입력해주세요", null,
										JOptionPane.PLAIN_MESSAGE);
								Client.getDos().writeUTF("enteredroomPw¿" + waitingroomPanel.gameListPane.getRoomTitle()
										+ "¿" + password);
								Client.getDos().flush();
								// 입력받은 비밀번호를 입장하려는 방제목과 비밀번호를 다시 서버에게 전송한다.
							} else if (delimiter[0].equals("notmatch")) {
								// 비밀번호가 일치하지 않을 경우 전달되는 메세지
								JOptionPane.showMessageDialog(self, "비밀번호가 일치하지 않습니다.", "enterRoom",
										JOptionPane.PLAIN_MESSAGE, null);
							} else if (delimiter[0].equals("playing")) {
								JOptionPane.showMessageDialog(self, "게임중에 접근할 수 없습니다.", "enterRoom",
										JOptionPane.PLAIN_MESSAGE, null);
							} else if (delimiter[0].equals("setNumRows")) { // 여기부터 아래로 쭉 게임기능
								gameroomPanel.setNumRows();

							} else if (delimiter[0].equals("userTable")) {
								String tableNick = delimiter[1];
								getScore = delimiter[2];
								gameroomPanel.setUserTable(tableNick, getScore);

							} else if (delimiter[0].equals("draw")) {
								int colorNum = Integer.parseInt(delimiter[1]);
								int x1 = Integer.parseInt(delimiter[2]);
								int y1 = Integer.parseInt(delimiter[3]);
								gameroomPanel.draw(colorNum, x1, y1);

							} else if (delimiter[0].equals("move")) {
								int x1 = Integer.parseInt(delimiter[1]);
								int y1 = Integer.parseInt(delimiter[2]);
								gameroomPanel.moved(x1, y1);

							} else if (delimiter[0].equals("clearBt")) {
								gameroomPanel.clearBt();

							} else if (delimiter[0].equals("inGameMsg")) {
								String inGameMsg = delimiter[1]; // <<-- msg
								gameroomPanel.inGameMsg(inGameMsg);

							} else if (delimiter[0].equals("startCountdown")) {
								String countdownMsg = delimiter[1];
								gameroomPanel.startCountdown(countdownMsg);

							} else if (delimiter[0].equals("stopGameCountdown")) {
								gameroomPanel.stopGameCountdown();

							} else if (delimiter[0].equals("quizWriter")) {
								String quizWriter = delimiter[1];
								gameroomPanel.getQuizWriter(quizWriter);

							} else if (delimiter[0].equals("quizCountdown")) {
								String quizCountdownMsg = delimiter[1];
								gameroomPanel.quizCountdownMsg(quizCountdownMsg);

							} else if (delimiter[0].equals("noQuizAnswer")) {
								String rightQuizAnswer = delimiter[1];
								gameroomPanel.noQuizAnswer(rightQuizAnswer);

							} else if (delimiter[0].equals("quiz")) {
								String quizAnswer = delimiter[1];
								gameroomPanel.quizAnswer(quizAnswer);

							} else if (delimiter[0].equals("existQuizAnswer")) {
								String answer = delimiter[1];
								String rightQuizAnswer = delimiter[2];
								gameroomPanel.existQuizAnswer(answer, rightQuizAnswer);

							} else if (delimiter[0].equals("finishGame")) {
								gameroomPanel.finishGame();
							}
						}
					} catch (Exception e) {
						break;
					}
					try {
						Thread.sleep(1);
					} catch (Exception e) {
					}
				}
			}
		}.start();
	}

	// 전체 폰트 변경 메소드
	public static void setUIFont(FontUIResource f) {
		Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {

			Object key = keys.nextElement();
			Object value = UIManager.get(key);

			if (value instanceof FontUIResource) {
				FontUIResource orig = (FontUIResource) value;
				Font font = new Font(f.getFontName(), orig.getStyle(), f.getSize());
				UIManager.put(key, new FontUIResource(font));
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loginPanel.pwField || e.getSource() == loginPanel.loginButton) {
			if (loginPanel.idField.getText().trim().length() == 0) {
				JOptionPane.showMessageDialog(this, "아이디를 입력해주세요", null, JOptionPane.PLAIN_MESSAGE);
				loginPanel.idField.grabFocus();
			} else if (!InstagrimUtil.checkInputOnlyNumberAndAlphabet(loginPanel.idField.getText())) {
				JOptionPane.showMessageDialog(this, "아이디는 영문자 또는 숫자만 입력해주세요", null, JOptionPane.PLAIN_MESSAGE);
				loginPanel.idField.grabFocus();
			} else if (loginPanel.idField.getText().length() < 4) {
				JOptionPane.showMessageDialog(this, "아이디는 4자 이상 입력해주세요", null, JOptionPane.PLAIN_MESSAGE);
				loginPanel.idField.grabFocus();
			} else if (String.valueOf(loginPanel.pwField.getPassword()).trim().length() == 0) {
				JOptionPane.showMessageDialog(this, "비밀번호를 입력해주세요", null, JOptionPane.PLAIN_MESSAGE);
				loginPanel.pwField.grabFocus();
			} else if (!InstagrimUtil
					.checkInputOnlyNumberAndAlphabet(String.valueOf(loginPanel.pwField.getPassword()))) {
				JOptionPane.showMessageDialog(this, "비밀번호는 영문자 또는 숫자만 입력해주세요", null, JOptionPane.PLAIN_MESSAGE);
				loginPanel.pwField.grabFocus();
			} else if (String.valueOf(loginPanel.pwField.getPassword()).length() < 4) {
				JOptionPane.showMessageDialog(this, "비밀번호는 4자 이상 입력해주세요", null, JOptionPane.PLAIN_MESSAGE);
				loginPanel.pwField.grabFocus();
			} else {
				client = new Client();
				String result = manager.login(loginPanel.idField.getText(),
						String.valueOf(loginPanel.pwField.getPassword()));
				if (result.equals("success")) {
					listenMsg();
					userID = loginPanel.idField.getText();
					try {
						Thread.sleep(3);
					} catch (Exception e1) {
					}
					cards.show(getContentPane(), "waitingroom");
					loginPanel.pwField.setText("");
				} else if (result.equals("already connected")) {
					JOptionPane.showMessageDialog(this, "이미 접속된 아이디 입니다", null, JOptionPane.PLAIN_MESSAGE);
					loginPanel.idField.grabFocus();
					loginPanel.pwField.setText("");
				} else if (result.equals("check id or pw")) {
					JOptionPane.showMessageDialog(this, "아이디 또는 비밀번호를 확인해주세요.", null, JOptionPane.PLAIN_MESSAGE);
					loginPanel.idField.grabFocus();
				} else {
					JOptionPane.showMessageDialog(this, "서버 연결 실패", null, JOptionPane.PLAIN_MESSAGE);
				}

			}
		} else if (e.getSource() == loginPanel.exitButton) {
			int select = JOptionPane.showConfirmDialog(null, "게임을 종료하시겠습니까?", null, JOptionPane.YES_NO_OPTION,
					JOptionPane.PLAIN_MESSAGE, null);
			if (select == JOptionPane.YES_OPTION) {
				try {
					Client.getDis().close();
					Client.getDos().close();
				} catch (Exception e1) {
				}
				System.exit(0);
			}
		} else if (e.getSource() == loginPanel.soundButton) {
			if (InstagrimUtil.clip.isActive()) {
				// getClass().getResource("/image/soundoff.png")
				URL soundoffURL = getClass().getClassLoader().getResource("soundoff.png");
				loginPanel.soundButton.setIcon(new ImageIcon(soundoffURL));
				InstagrimUtil.soundOff();
			} else {
				// getClass().getResource("/image/soundon.png")
				URL soundonURL = getClass().getClassLoader().getResource("soundon.png");
				loginPanel.soundButton.setIcon(new ImageIcon(soundonURL));
				InstagrimUtil.soundOn();
			}
		} // ====================================================================
		else if (e.getSource() == waitingroomPanel.chatPane.sendButton
				|| e.getSource() == waitingroomPanel.chatPane.chatField) {
			if (waitingroomPanel.chatPane.chatField.getText().length() > 0) {
				manager.sendMessage(userID, waitingroomPanel.chatPane.chatField.getText());
				waitingroomPanel.chatPane.chatField.setText("");
				waitingroomPanel.chatPane.chatField.grabFocus();
			}
		} else if (e.getSource() == waitingroomPanel.createRoomButton) {
			makeroomDialog = new MakeRoomDialog(this);
			makeroomDialog.lockButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (makeroomDialog.pwField.isEditable() == true) {
						// getClass().getResource("/image/unlock.png")
						URL unlockURL = getClass().getClassLoader().getResource("unlock.png");
						makeroomDialog.lockButton.setIcon(new ImageIcon(unlockURL));
						makeroomDialog.pwField.setEditable(false);
					} else {
						// getClass().getResource("/image/lock.png")
						URL lockURL = getClass().getClassLoader().getResource("lock.png");
						makeroomDialog.lockButton.setIcon(new ImageIcon(lockURL));
						makeroomDialog.pwField.setEditable(true);
					}
				}
			});
			makeroomDialog.makeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (makeroomDialog.titleField.getText().trim().length() == 0) {
						JOptionPane.showMessageDialog(self, "제목을 입력해주세요", null, JOptionPane.PLAIN_MESSAGE, null);
					} else if (makeroomDialog.pwField.isEditable() == true
							&& String.valueOf(makeroomDialog.pwField.getPassword()).trim().length() == 0) {
						JOptionPane.showMessageDialog(self, "비밀번호를 입력해주세요", null, JOptionPane.PLAIN_MESSAGE, null);
					} else {
						if (checkInputOnlyNumber(String.valueOf(makeroomDialog.pwField.getPassword()))) {
							makeroom_title = makeroomDialog.titleField.getText();
							makeroom_pw = String.valueOf(makeroomDialog.pwField.getPassword());
							makeroom_personnel = String.valueOf(makeroomDialog.personnelBox.getSelectedItem());
							try {
								Client.getDos().writeUTF("makeroom¿" + userID + "¿" + makeroom_title + "¿" + makeroom_pw
										+ "¿" + makeroom_personnel);
								Client.getDos().flush();
								makeroomDialog.dispose();
							} catch (Exception e1) {
							}
						} else {
							JOptionPane.showMessageDialog(self, "비밀번호는 숫자만 입력하세요.", "Room", JOptionPane.PLAIN_MESSAGE,
									null);
						}
					}
				}
			});
			makeroomDialog.exitButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					makeroomDialog.dispose();
				}
			});
		} else if (e.getSource() == waitingroomPanel.joinRoomButton) {
			makeroom_title = waitingroomPanel.gameListPane.roomTitle;
			if (makeroom_title.length() > 0) {
				try {
					Client.getDos().writeUTF("enteredroom¿" + makeroom_title + "¿" + userID);
					Client.getDos().flush();
				} catch (Exception e1) {
				}
			}
		} else if (e.getSource() == waitingroomPanel.userInfoPane.soundButton) {
			if (InstagrimUtil.clip.isActive()) {
				waitingroomPanel.userInfoPane.soundButton.setText("음악 켜기");
				InstagrimUtil.soundOff();
			} else {
				waitingroomPanel.userInfoPane.soundButton.setText("음악 끄기");
				InstagrimUtil.soundOn();
			}
		} else if (e.getSource() == waitingroomPanel.userInfoPane.exitButton) {
			int select = JOptionPane.showConfirmDialog(this, "로그아웃 하시겠습니까?", null, JOptionPane.YES_NO_OPTION,
					JOptionPane.PLAIN_MESSAGE, null);
			if (select == JOptionPane.YES_OPTION) {
				manager.logout();
				cards.show(getContentPane(), "login");
			}
		} // ====================================================================
		else if (e.getSource() == gameroomPanel.chattingPanel.chatField
				|| e.getSource() == gameroomPanel.chattingPanel.sendButton) {
			if (GamePanel.doGame == false) {
				if (gameroomPanel.chattingPanel.chatField.getText().length() != 0) {
					try {
						Client.getDos().writeUTF("game¿inGameMsg¿" + gameroomPanel.roomTitle + "¿"
								+ gameroomPanel.chattingPanel.chatField.getText());
						Client.getDos().flush();
					} catch (Exception e1) {
					}
					gameroomPanel.chattingPanel.chatField.setText("");
					gameroomPanel.chattingPanel.chatField.grabFocus();
				}
			} else if (GamePanel.doGame == true) {
				if (gameroomPanel.chattingPanel.chatField.getText().length() != 0) {
					if (userID.equals(GamePanel.quizWriter)) {
						try {
							Client.getDos()
									.writeUTF("game¿inGameMsg¿" + gameroomPanel.roomTitle + "¿" + "출제자는 말할 수 없습니다.");
							Client.getDos().flush();
						} catch (Exception e1) {
						}
					} else if (!userID.equals(GamePanel.quizWriter)) {
						try {
							Client.getDos().writeUTF("game¿inGameMsg¿" + gameroomPanel.roomTitle + "¿"
									+ gameroomPanel.chattingPanel.chatField.getText());
							Client.getDos().flush();
						} catch (Exception e1) {
						}
					}
					gameroomPanel.chattingPanel.chatField.setText("");
					gameroomPanel.chattingPanel.chatField.grabFocus();
				}
			}
		}
		if (e.getSource() == gameroomPanel.clearButton) {
			gameroomPanel.canvasPanel.repaint();
			try {
				Client.getDos().writeUTF("game¿clearBt¿" + gameroomPanel.roomTitle);
				Client.getDos().flush();
			} catch (Exception e1) {
			}
		} else if (e.getSource() == gameroomPanel.blackButton) {
			gameroomPanel.canvasPanel.colorNum = 1;
		} else if (e.getSource() == gameroomPanel.blueButton) {
			gameroomPanel.canvasPanel.colorNum = 2;
		} else if (e.getSource() == gameroomPanel.greenButton) {
			gameroomPanel.canvasPanel.colorNum = 3;
		} else if (e.getSource() == gameroomPanel.redButton) {
			gameroomPanel.canvasPanel.colorNum = 4;
		} else if (e.getSource() == gameroomPanel.eraserButton) {
			gameroomPanel.canvasPanel.colorNum = 5;
		} else if (e.getSource() == gameroomPanel.readyButton) {
			if (GamePanel.doGame == false) { // 게임중이 아닐 경우에만 활성화
				if (gameroomPanel.isReady == false) {
					try {
						Client.getDos().writeUTF("game¿ready¿" + gameroomPanel.roomTitle + "¿" + userID + "¿false");
						Client.getDos().flush();
						gameroomPanel.readyButton.setText("준비 완료");
						gameroomPanel.isReady = true;
					} catch (Exception e1) {
					}
				} else if (gameroomPanel.isReady == true) {
					try {
						Client.getDos().writeUTF("game¿ready¿" + gameroomPanel.roomTitle + "¿" + userID + "¿true");
						Client.getDos().flush();
						gameroomPanel.readyButton.setText("준비");
						gameroomPanel.isReady = false;
					} catch (Exception e1) {
					}
				}
			}
		} else if (e.getSource() == gameroomPanel.soundButton) {
			if (InstagrimUtil.clip.isActive()) {
				waitingroomPanel.userInfoPane.soundButton.setText("음악 켜기");
				InstagrimUtil.soundOff();
			} else {
				waitingroomPanel.userInfoPane.soundButton.setText("음악 끄기");
				InstagrimUtil.soundOn();
			}
		} else if (e.getSource() == gameroomPanel.exitButton) {
			if (GamePanel.doGame == true) {
				JOptionPane.showMessageDialog(this, "게임 중에는 나갈 수 없습니다!", null, JOptionPane.PLAIN_MESSAGE);
			} else {
				//////////////////////////////////////////////////////////////////////////////////////////////
				int exitOK = JOptionPane.showConfirmDialog(this, "정말 나가시겠습니까?", null, JOptionPane.YES_NO_OPTION);
				if (exitOK == JOptionPane.YES_OPTION) {
					try {
						Client.getDos().writeUTF("exitroom¿" + userID + "¿" + gameroomPanel.roomTitle);
						// ------------------------ 여기 룸 타이틀 널값 문제 발견, 방리스트 갱신은 되지만 유저리스트 갱신안됨.
						// 방에 접속하는 유저는 잘 추가되나 퇴장할 때 문제 발생
						Client.getDos().flush();
						waitingroomPanel.gameListPane.setGameListNumRow();
						waitingroomPanel.userListPane.updateUserList(winningRate);
						waitingroomPanel.chatPane.chatArea.setText("");
						gameroomPanel.chattingPanel.chatArea.setText("");
						Client.getDos().writeUTF("updateRoomList¿");
						Client.getDos().flush();
						cards.show(getContentPane(), "waitingroom");
					} catch (Exception e1) {
					}
				}
				////////////////////////////////////////////////////////////////////////////////////////
			}
		}
	}

	private boolean checkInputOnlyNumber(String textInput) {
		for (int i = 0; i < textInput.length(); i++) {
			char chrInput = textInput.charAt(i);
			if (chrInput >= 0x30 && chrInput <= 0x39) {
			} else {
				return false;
			}
		}
		return true;
	}

	// 마우스 리스너 할당 - 클릭했을 때
	public void mouseClicked(MouseEvent e) {
		if (e.getSource() == loginPanel.joinLabel) {
			new JoinDialog(this);
		} else if (e.getSource() == loginPanel.findidpwLabel) {
			new FindIDPW(this);
		} else if (e.getSource() == waitingroomPanel.gameListPane.gameTable) {
			waitingroomPanel.gameListPane.row = waitingroomPanel.gameListPane.gameTable.getSelectedRow();
			waitingroomPanel.gameListPane.col = waitingroomPanel.gameListPane.gameTable.getSelectedColumn();
			waitingroomPanel.gameListPane.roomTitle = waitingroomPanel.gameListPane.gameTable
					.getValueAt(waitingroomPanel.gameListPane.row, waitingroomPanel.gameListPane.col).toString();
			if (e.getClickCount() == 2) {
				try {
					makeroom_title = waitingroomPanel.gameListPane.roomTitle;
					Client.getDos().writeUTF("enteredroom¿" + makeroom_title + "¿" + userID);
					Client.getDos().flush();
				} catch (Exception e1) {
				}
			}
		}
	}

	// 마우스 리스너 할당 - 눌려졌을 때
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == this) { // 프레임 x, y 값 받아오기
			x = e.getX();
			y = e.getY();
		}
	}

	// 마우스 리스너 할당 - 떼어졌을 때
	public void mouseReleased(MouseEvent e) {
		if (e.getSource() == gameroomPanel.canvasPanel) {
			gameroomPanel.canvasPanel.sendCoor(e);
		}
	}

	// 마우스 리스너 할당 - 위에 올라갈 때
	public void mouseEntered(MouseEvent e) {
		if (e.getSource() == loginPanel.joinLabel) { // joinLabel 하얀색으로 변경
			loginPanel.joinLabel.setForeground(InstagrimUtil.black);
		} else if (e.getSource() == loginPanel.findidpwLabel) { // findidLabel 하얀색으로 변경
			loginPanel.findidpwLabel.setForeground(InstagrimUtil.black);
		}
	}

	// 마우스 리스너 할당 - 내려올 때
	public void mouseExited(MouseEvent e) {
		if (e.getSource() == loginPanel.joinLabel) { // joinLabel 파란색으로 변경
			loginPanel.joinLabel.setForeground(InstagrimUtil.blue);
		} else if (e.getSource() == loginPanel.findidpwLabel) { // findidLabel 파란색으로 변경
			loginPanel.findidpwLabel.setForeground(InstagrimUtil.blue);
		}
	}

	// 마우스 모션 리스너 할당 - 드래그할 때
	public void mouseDragged(MouseEvent e) {
		if (e.getSource() == this) { // 프레임 위치 이동
			setLocation(e.getLocationOnScreen().x - x, e.getLocationOnScreen().y - y);
		} else if (e.getSource() == gameroomPanel.canvasPanel) {
			gameroomPanel.canvasPanel.sendCoor(e);
		}
	}

	// 마우스 모션 리스너 할당 - 움직일 때
	public void mouseMoved(MouseEvent e) {
		if (e.getSource() == gameroomPanel.canvasPanel) {
			gameroomPanel.canvasPanel.setX(e.getX());
			gameroomPanel.canvasPanel.setY(e.getY());
			
			if (GamePanel.doGame == true) {
				try {
					Client.getDos().writeUTF("game¿canvas¿" + roomTitle + "¿" + "move");
					Client.getDos().writeUTF(
							"move¿" + gameroomPanel.canvasPanel.getX() + "¿" + gameroomPanel.canvasPanel.getY());
					Client.getDos().flush();
				} catch (Exception e1) {
				}
			}
		}
	}

}