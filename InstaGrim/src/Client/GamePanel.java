package Client;

import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GamePanel extends JPanel {

	public static boolean doGame = false; // 게임중 표시
	public static boolean endGame = false; // 게임끝 표시

	public String roomTitle;
	public static String userID;
	public String startGameCountdown; // 게임시작 카운트다운
	public static String quizWriter; // 출제자가 누구인지

	public boolean isReady = false;

	public CanvasPanel canvasPanel = new CanvasPanel();
	public ChattingPanel chattingPanel = new ChattingPanel();
	public TablePanel tablePanel = new TablePanel();

	private JPanel leftPanel = new JPanel();
	private JPanel topPanel = new JPanel();
	public JLabel turnLabel = new JLabel("", JTextField.CENTER); // 누구 차례인지 알려주는 Label
	public JLabel alarmLabel = new JLabel("인 스 타 그 림", JTextField.CENTER); // x초 후 시작됩니다. 하고 알려주는 Label
	public JLabel wordLabel = new JLabel("", JTextField.CENTER); // 제시어 Label

	public JPanel bottomPanel = new JPanel(null);
	URL blackURL = getClass().getClassLoader().getResource("black.png"); // "image/black.png"
	public JButton blackButton = new JButton(new ImageIcon(blackURL));
	URL blueURL = getClass().getClassLoader().getResource("blue.png"); // "image/blue.png"
	public JButton blueButton = new JButton(new ImageIcon(blueURL));
	URL greenURL = getClass().getClassLoader().getResource("green.png"); // "image/green.png"
	public JButton greenButton = new JButton(new ImageIcon(greenURL));
	URL redURL = getClass().getClassLoader().getResource("red.png"); // "image/red.png"
	public JButton redButton = new JButton(new ImageIcon(redURL));
	URL eraserURL = getClass().getClassLoader().getResource("eraser.png"); // "image/eraser.png"
	public JButton eraserButton = new JButton(new ImageIcon(eraserURL));
	URL clearURL = getClass().getClassLoader().getResource("clear.png"); // "image/clear.png"
	public JButton clearButton = new JButton(new ImageIcon(clearURL));
	public JLabel countdownLabel = new JLabel("", JTextField.CENTER); // 남은 시간 보여주는 label

	public JPanel bottomnullPanel = new JPanel();

	public JPanel buttonPanel = new JPanel(null);
	public JButton readyButton = new JButton("준비");
	public JButton soundButton = new JButton("음악 끄기");
	public JButton exitButton = new JButton("나가기");

	public GamePanel() {
		this.setSize(960, 640);
		this.addComponent();
	}

	public void addComponent() {
		setLayout(null);

		turnLabel.setSize(240, 14);
		turnLabel.setForeground(InstagrimUtil.blue);
		topPanel.add(turnLabel);
		alarmLabel.setLocation(240, 0);
		alarmLabel.setSize(240, 14);
		topPanel.add(alarmLabel);
		wordLabel.setSize(240, 14);
		wordLabel.setLocation(480, 0);
		wordLabel.setForeground(InstagrimUtil.blue);
		topPanel.add(wordLabel);
		topPanel.setBounds(6, 6, 708, 20);
		add(topPanel);

		leftPanel.add(canvasPanel);
		leftPanel.setBounds(6, 26, 708, 354);
		add(leftPanel);

		bottomPanel.add(clearButton);
		blackButton.setBorderPainted(false);
		blackButton.setFocusPainted(false);
		blackButton.setContentAreaFilled(false);
		blackButton.setSize(28, 28);
		blackButton.setLocation(108, 9);
		bottomPanel.add(blackButton);
		blueButton.setBorderPainted(false);
		blueButton.setFocusPainted(false);
		blueButton.setContentAreaFilled(false);
		blueButton.setLocation(74, 9);
		blueButton.setSize(28, 28);
		bottomPanel.add(blueButton);
		greenButton.setBorderPainted(false);
		greenButton.setFocusPainted(false);
		greenButton.setContentAreaFilled(false);
		greenButton.setLocation(40, 9);
		greenButton.setSize(28, 28);
		bottomPanel.add(greenButton);
		redButton.setBorderPainted(false);
		redButton.setFocusPainted(false);
		redButton.setContentAreaFilled(false);
		redButton.setLocation(6, 9);
		redButton.setSize(28, 28);
		bottomPanel.add(redButton);
		eraserButton.setBorderPainted(false);
		eraserButton.setFocusPainted(false);
		eraserButton.setContentAreaFilled(false);
		eraserButton.setSize(28, 28);
		eraserButton.setLocation(142, 9);
		bottomPanel.add(eraserButton);
		clearButton.setBorderPainted(false);
		clearButton.setFocusPainted(false);
		clearButton.setContentAreaFilled(false);
		clearButton.setSize(28, 28);
		clearButton.setLocation(176, 9);
		countdownLabel.setSize(340, 28);
		countdownLabel.setLocation(360, 6);
		countdownLabel.setHorizontalAlignment(JLabel.RIGHT);
		bottomPanel.add(countdownLabel);
		bottomPanel.setBounds(6, 380, 708, 40);
		add(bottomPanel);

		chattingPanel.setLocation(0, 426);
		add(chattingPanel);

		tablePanel.setLocation(720, 0);
		add(tablePanel);

		readyButton.setSize(234, 150);
		buttonPanel.add(readyButton);
		soundButton.setLocation(0, 151);
		soundButton.setSize(116, 50);
		buttonPanel.add(soundButton);
		exitButton.setLocation(118, 151);
		exitButton.setSize(116, 50);
		buttonPanel.add(exitButton);
		buttonPanel.setBounds(720, 432, 234, 202);
		add(buttonPanel);
	}

	public void setNumRows() {
		tablePanel.model.setNumRows(0);
	}

	public void setUserTable(String tableNick, String getScore) {
		boolean exist = false;
		String[] tmp = { tableNick, String.valueOf(getScore) };
		for (int i = 0; i < tablePanel.model.getRowCount(); i++) {
			// model에 아이디있는지 확인
			if (tablePanel.model.getValueAt(i, 0).equals(tableNick)) {
				exist = true;
			}
		}
		if (exist == false) { // 해당 아이디가 테이블에 존재하지 않을 경우에만 추가
			tablePanel.model.addRow(tmp);
		}
	}

	public void draw(int colorNum, int x1, int y1) {
		if (!userID.equals(quizWriter)) {
			canvasPanel.draw(colorNum, x1, y1);
		}
	}

	public void moved(int x1, int y1) {
		if (!userID.equals(quizWriter)) {
			canvasPanel.moved(x1, y1);
		}
	}

	public void clearBt() {
		canvasPanel.repaint();
	}

	public boolean isEndGame() {
		return endGame;
	}

	public void setEndGame(boolean endGame) {
		GamePanel.endGame = endGame;
	}

	public void inGameMsg(String st) {
		chattingPanel.chatArea.append(st + "\n");
		chattingPanel.chattingScroll.getVerticalScrollBar()
				.setValue(chattingPanel.chattingScroll.getVerticalScrollBar().getMaximum());
	}

	public void stopGameCountdown() {
		doGame = false;
		alarmLabel.setText("InstaGrim");
		isReady = false;
	}

	public void startCountdown(String countdownMsg) {
		doGame = true;
		countdownLabel.setText(countdownMsg);
	}

	public void getQuizWriter(String quizWriter) {
		GamePanel.quizWriter = quizWriter;
		turnLabel.setText("출제자 : " + quizWriter);

		if (userID.equals(quizWriter)) {
			blackButton.setEnabled(true);
			blueButton.setEnabled(true);
			greenButton.setEnabled(true);
			redButton.setEnabled(true);
			eraserButton.setEnabled(true);
			clearButton.setEnabled(true);
			canvasPanel.colorNum = 1;
			canvasPanel.othersColorNum = 1;
			canvasPanel.repaint();
			alarmLabel.setText("우측의 문제를 보고 그림을 그려주세요");
		} else if (!userID.equals(quizWriter)) {
			canvasPanel.setEnabled(true);
			blackButton.setEnabled(false);
			blueButton.setEnabled(false);
			greenButton.setEnabled(false);
			redButton.setEnabled(false);
			eraserButton.setEnabled(false);
			clearButton.setEnabled(false);
			bottomnullPanel.setVisible(true);
			canvasPanel.colorNum = 1;
			canvasPanel.othersColorNum = 1;
			canvasPanel.repaint();
			wordLabel.setText("");
			alarmLabel.setText("그림을 잘 보고 정답을 맞춰보세요");
		}
	}

	public void quizAnswer(String quizAnswer) {
		wordLabel.setText(quizAnswer);
	}

	public void quizCountdownMsg(String quizCountdownMsg) {
		countdownLabel.setText(quizCountdownMsg);
	}

	public void noQuizAnswer(String rightQuizAnswer) {
		canvasPanel.repaint();
		countdownLabel.setText("");
		turnLabel.setText("");
		wordLabel.setText("");
		alarmLabel.setText("[정답 : " + rightQuizAnswer + "] " + "정답자가 없습니다");
	}

	public void existQuizAnswer(String nick, String rightQuizAnswer) {
		canvasPanel.repaint();
		countdownLabel.setText("");
		turnLabel.setText("");
		wordLabel.setText("");
		alarmLabel.setText("[정답 : " + rightQuizAnswer + "] 정답자 : " + nick);
	}

	public void finishGame() {
		ArrayList<Ranking> ranking = new ArrayList<Ranking>();
		bottomPanel.setVisible(true);
		canvasPanel.colorNum = 1;
		canvasPanel.othersColorNum = 1;
		canvasPanel.repaint();
		doGame = false;
		alarmLabel.setText("인 스 타 그 림");
		isReady = false;
		readyButton.setText("준비");
		
		for (int i = 0; i < tablePanel.model.getRowCount(); i++) { // model에 아이디 있는지 확인
			Ranking rank = new Ranking();
			rank.setId(tablePanel.model.getValueAt(i, 0).toString());
			rank.setNum(Integer.parseInt(tablePanel.model.getValueAt(i, 1).toString()));
			ranking.add(rank);
		}

		for (int i = 0; i < ranking.size() - 1; i++) {
			for (int j = i + 1; j < ranking.size(); j++) {
				if (ranking.get(i).getNum() < ranking.get(j).getNum()) {
					Ranking rank;
					rank = ranking.get(i);
					ranking.set(i, ranking.get(j));
					ranking.set(j, rank);
				}
			}
		}
		JOptionPane.showMessageDialog(this,
				"게임 끝!\n" + "1등 : " + ranking.get(0).getId() + "\n" + "정답수 : " + ranking.get(0).getNum());

		try {
			Client.getDos().writeUTF("game¿" + "setAnswerNumZero¿" + canvasPanel.roomTitle);
			Client.getDos().flush();
		} catch (Exception e) {
		} // 모든 사람들 정답 갯수 초기화

	}
}