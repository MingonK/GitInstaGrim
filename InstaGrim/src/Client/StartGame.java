package Client;

public class StartGame {

	boolean quizResult = false;
	boolean isQuizWriter = false;

	public StartGame(GamePanel parent, String id, TablePanel tablePanel, GamePanel gamePanel,
			ChattingPanel chattingPanel) {

		while (true) { // 문제 갯수
			if (id.equals(GamePanel.quizWriter)) { // 출제자인지 아닌지 저장함
				isQuizWriter = true; // 출제자o
			} else if (!id.equals(GamePanel.quizWriter)) {
				isQuizWriter = false; // 출제자x
			}
			if (isQuizWriter == true) {
				gamePanel.alarmLabel.setText("우측의 문제를 보고 그림을 그려주세요");
				gamePanel.canvasPanel.enable();

			} else if (isQuizWriter == false) {
				gamePanel.wordLabel.setText("");
				gamePanel.alarmLabel.setText("그림을 잘 보고 정답을 맞춰보세요");
				gamePanel.canvasPanel.disable();

			}
		}
	}

}