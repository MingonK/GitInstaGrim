package Client;

import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class GameListPane extends JPanel {
	public int row;
	public int col;
	public String roomTitle;
	public DefaultTableModel gamemodel;
	public JPanel gameListPane = new JPanel(new BorderLayout());
	public JTable gameTable;
	private TableColumn column;
	private DefaultTableCellRenderer colrend;
	private DefaultTableCellRenderer hearend;
	private JScrollPane gameListScroll;
	private Vector<String> gameColumn = new Vector<String>();
	private Vector<String> gameRow;

	public void addComponent() {
		this.setLayout(null);

		gameColumn.addElement("잠금");
		gameColumn.addElement("방 제목");
		gameColumn.addElement("인원");
		gameColumn.addElement("Play");
		colrend = new DefaultTableCellRenderer();
		hearend = new DefaultTableCellRenderer();
		
		gamemodel = new DefaultTableModel(gameColumn, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		gameTable = new JTable(gamemodel);
		gameTable.getTableHeader().setReorderingAllowed(false);
		gameTable.getTableHeader().setResizingAllowed(false);
		gameTable.setRowHeight(20);
		for(int i = 0; i < gamemodel.getColumnCount(); i++) {
			column = gameTable.getColumnModel().getColumn(i);
			colrend.setHorizontalAlignment(JLabel.CENTER);
			hearend.setHorizontalAlignment(JLabel.CENTER);
			column.setCellRenderer(colrend);
			column.setHeaderRenderer(hearend);
		}
		gameTable.setAutoCreateRowSorter(true);
		gameListScroll = new JScrollPane(gameTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		gameListPane.add(gameListScroll, BorderLayout.CENTER);
		gameListPane.setBounds(6, 6, 714, 420);
		this.add(gameListPane);
	}

	public GameListPane() {
		setSize(720, 426);
		addComponent();
	}

	public void updateRoomList(String roomTitle, String roomPw, String userCount, String totalPerson, String statusGame) {
		if (roomPw.isEmpty()) {
			gameRow = new Vector<String>();
			gameRow.addElement("free");
			gameRow.addElement(roomTitle);
			gameRow.addElement(userCount + "/" + totalPerson);
			gameRow.addElement(statusGame);
			gamemodel.addRow(gameRow);
		} else {
			gameRow = new Vector<String>();
			gameRow.addElement("lock");
			gameRow.addElement(roomTitle);
			gameRow.addElement(userCount + "/" + totalPerson);
			gameRow.addElement(statusGame);
			gamemodel.addRow(gameRow);
		}
	}

	public void setGameListNumRow() {
		gamemodel = (DefaultTableModel) gameTable.getModel();
		this.gamemodel.setNumRows(0);
	}

	public JTable getGameTable() {
		return gameTable;
	}

	public void setGameTable(JTable gameTable) {
		this.gameTable = gameTable;
	}

	public String getRoomTitle() {
		return roomTitle;
	}

	public void setRoomTitle(String roomTitle) {
		this.roomTitle = roomTitle;
	}

}
