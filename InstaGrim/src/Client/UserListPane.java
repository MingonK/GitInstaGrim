package Client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class UserListPane extends JPanel {

	private JPanel userListPane = new JPanel(new BorderLayout());
	private JTable userTable;
	private JScrollPane userListScroll;
	private Vector<String> userColumn = new Vector<String>();
	private Vector<String> userRow;
	private DefaultTableModel usermodel;
	private TableColumn column;
	private DefaultTableCellRenderer colrend;
	private DefaultTableCellRenderer hearend;

	private void compInit() {
		this.setLayout(null);

		userColumn.addElement("LV");
		userColumn.addElement("아이디");
		userColumn.addElement("전적(승률)");
		colrend = new DefaultTableCellRenderer();
		hearend = new DefaultTableCellRenderer();
		
		usermodel = new DefaultTableModel(userColumn, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		userTable = new JTable(usermodel);
		userTable.getTableHeader().setReorderingAllowed(false);
		userTable.getTableHeader().setResizingAllowed(false);
		userTable.setRowHeight(20);
		for(int i = 0; i < usermodel.getColumnCount(); i++) {
			column = userTable.getColumnModel().getColumn(i);
			colrend.setHorizontalAlignment(JLabel.CENTER);
			hearend.setHorizontalAlignment(JLabel.CENTER);
			column.setCellRenderer(colrend);
			column.setHeaderRenderer(hearend);
		}
		userTable.setAutoCreateRowSorter(true);
		userTable.getColumn("LV").setPreferredWidth(28);
		userTable.getColumn("아이디").setPreferredWidth(75);
		userTable.getColumn("전적(승률)").setPreferredWidth(125);
		
		userListScroll = new JScrollPane(userTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		userListPane.add(userListScroll, BorderLayout.CENTER);

		userListPane.setBounds(6, 6, 228, 420);
		this.add(userListPane);
	}

	public UserListPane() {
		setSize(240, 426);
		compInit();
		setBackground(InstagrimUtil.clear);
	}

	public void updateUserList(int winningRate) {
		usermodel = (DefaultTableModel) userTable.getModel();
		usermodel.setNumRows(0);
		Iterator<String> iterator = InstagrimFrame.userDataMap.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			for (int i = 0; i < InstagrimFrame.userDataMap.get(key).size(); i++) {
				userRow = new Vector<String>();
				userRow.addElement(InstagrimFrame.userData.get(i).getClientLv());
				userRow.addElement(key);
				userRow.addElement(InstagrimFrame.userData.get(i).getClientRecord() + "전 "
						+ InstagrimFrame.userData.get(i).getClientWin() + "승 "
						+ InstagrimFrame.userData.get(i).getClientLose() + "패 " + "(" + winningRate + "%)");
			}
			usermodel.addRow(userRow);
		}
	}
}