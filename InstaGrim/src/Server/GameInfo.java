package Server;

import java.io.DataOutputStream;

public class GameInfo {

	private String roomTitle = null;
	private String id = null;
	private int clientLv = 0;
	private int clientRecord = 0;
	private int clientWin = 0;
	private int clientLose = 0;
	private int clientExp = 0;
	private DataOutputStream dos;
	private boolean ready = false; // 현재 이 유저가 레디중인지 알기 위함
	private boolean doGame = false; // 현재 게임중인지 아닌지 확인하기 위함
	private String quiz = null; // 현재 맞춰야하는 퀴즈가 뭔지 저장해두기 위함
	private String quizWriter = null; // 출제자가 누구인지 알기 위함
	private int writerNum =0 ;
	private boolean isQuizAnswer = false; // 내가 정답을 맞췄다면 true로 변환
	private int rightAnswerNum = 0; // 맞춘 갯수 저장
	private boolean stopGame = false;
	private boolean nextQuiz = false;
	private boolean stopGameCountdown = false;
	private boolean notEnoughUser = false;
	//private int quizCountdown = 180;

	public int getWriterNum() {
		return writerNum;
	}

	public void setWriterNum(int writerNum) {
		this.writerNum = writerNum;
	}

	public int getClientLv() {
		return clientLv;
	}

	public void setClientLv(int clientLv) {
		this.clientLv = clientLv;
	}

	public int getClientRecord() {
		return clientRecord;
	}

	public void setClientRecord(int clientRecord) {
		this.clientRecord = clientRecord;
	}

	public int getClientWin() {
		return clientWin;
	}

	public void setClientWin(int clientWin) {
		this.clientWin = clientWin;
	}

	public int getClientLose() {
		return clientLose;
	}

	public void setClientLose(int clientLose) {
		this.clientLose = clientLose;
	}

	public int getClientExp() {
		return clientExp;
	}

	public void setClientExp(int clientExp) {
		this.clientExp = clientExp;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DataOutputStream getDos() {
		return dos;
	}

	public void setDos(DataOutputStream dos) {
		this.dos = dos;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public boolean isDoGame() {
		return doGame;
	}

	public void setDoGame(boolean doGame) {
		this.doGame = doGame;
	}

	public String getQuiz() {
		return quiz;
	}

	public void setQuiz(String quiz) {
		this.quiz = quiz;
	}

	public String getQuizWriter() {
		return quizWriter;
	}

	public void setQuizWriter(String quizWriter) {
		this.quizWriter = quizWriter;
	}

//	public int getQuizCountdown() {
//		return quizCountdown;
//	}
//
//	public void setQuizCountdown(int quizCountdown) {
//		this.quizCountdown = quizCountdown;
//	}

	public boolean isQuizAnswer() {
		return isQuizAnswer;
	}

	public void setQuizAnswer(boolean isQuizAnswer) {
		this.isQuizAnswer = isQuizAnswer;
	}

	public int getRightAnswerNum() {
		return rightAnswerNum;
	}

	public void setRightAnswerNum(int rightAnswerNum) {
		this.rightAnswerNum = rightAnswerNum;
	}

	public boolean isStopGame() {
		return stopGame;
	}

	public void setStopGame(boolean stopGame) {
		this.stopGame = stopGame;
	}

	public boolean isNextQuiz() {
		return nextQuiz;
	}

	public void setNextQuiz(boolean nextQuiz) {
		this.nextQuiz = nextQuiz;
	}

	public boolean isStopGameCountdown() {
		return stopGameCountdown;
	}

	public void setStopGameCountdown(boolean stopStartGameCountdown) {
		this.stopGameCountdown = stopStartGameCountdown;
	}

	public boolean isNotEnoughUser() {
		return notEnoughUser;
	}

	public void setNotEnoughUser(boolean notEnoughUser) {
		this.notEnoughUser = notEnoughUser;
	}

	public String getRoomTitle() {
		return roomTitle;
	}

	public void setRoomTitle(String roomTitle) {
		this.roomTitle = roomTitle;
	}

}
