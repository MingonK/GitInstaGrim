package Client;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

public class CanvasPanel extends Canvas {

	private int x;
	private int y;
	int colorNum = 0;
	int othersColorNum = 0;

	public Color red = new Color(226, 73, 77);
	public Color green = new Color(67, 140, 100);
	public Color blue = new Color(30, 115, 212);

	public String roomTitle;

	public CanvasPanel() {
		setSize(708, 354);
		setBackground(Color.WHITE);
	}

	public void sendCoor(MouseEvent e) {
		if (GamePanel.doGame == false) {
			// 게임이 진행되지 않았을 경우
			Graphics g = this.getGraphics();
			Graphics2D g2 = (Graphics2D) g;

			g2.setStroke(new BasicStroke(3)); // 초기 그래픽 설정
			if (colorNum == 0 || colorNum == 1) {
				g2.setColor(Color.BLACK);
			} else if (colorNum == 2) {
				g2.setColor(blue);
			} else if (colorNum == 3) {
				g2.setColor(green);
			} else if (colorNum == 4) {
				g2.setColor(red);
			} else if (colorNum == 5) {
				g2.setStroke(new BasicStroke(15));
				g2.setColor(Color.WHITE);
			}

			g2.drawLine(x, y, e.getX(), e.getY());
			x = e.getX();
			y = e.getY();
			
		} else if (GamePanel.doGame == true && GamePanel.quizWriter.equals(GamePanel.userID)) {
			// 게임이 진행 중이고 현재 유저가 게임 출제자인 경우
			Graphics g = this.getGraphics();
			Graphics2D g2 = (Graphics2D) g;

			g2.setStroke(new BasicStroke(3)); // 초기 그래픽 설정
			if (colorNum == 0 || colorNum == 1) {
				g2.setColor(Color.BLACK);
			} else if (colorNum == 2) {
				g2.setColor(blue);
			} else if (colorNum == 3) {
				g2.setColor(green);
			} else if (colorNum == 4) {
				g2.setColor(red);
			} else if (colorNum == 5) {
				g2.setStroke(new BasicStroke(15));
				g2.setColor(Color.WHITE);
			}

			g2.drawLine(x, y, e.getX(), e.getY());
			x = e.getX();
			y = e.getY();

			try {
				Client.getDos().writeUTF("game¿canvas¿" + roomTitle + "¿" + "draw");
				Client.getDos().writeUTF("draw¿" + colorNum + "¿" + x + "¿" + y);
				Client.getDos().flush();
			} catch (Exception e1) {
			}
		}
	}

	protected void draw(int othersColorNum, int x1, int y1) {
		this.othersColorNum = othersColorNum;

		Graphics g = this.getGraphics();
		Graphics2D g2 = (Graphics2D) g;

		g2.setStroke(new BasicStroke(3)); // 초기 그래픽 설정
		if (othersColorNum == 0 || othersColorNum == 1) {
			g2.setColor(Color.BLACK);
		} else if (othersColorNum == 2) {
			g2.setColor(blue);
		} else if (othersColorNum == 3) {
			g2.setColor(green);
		} else if (othersColorNum == 4) {
			g2.setColor(red);
		} else if (othersColorNum == 5) {
			g2.setStroke(new BasicStroke(15));
			g2.setColor(Color.WHITE);
		}
		g2.drawLine(x, y, x1, y1);

		this.x = x1;
		this.y = y1;
	}

	public void moved(int x1, int y1) {
		this.x = x1;
		this.y = y1;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}