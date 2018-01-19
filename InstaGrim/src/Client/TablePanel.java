package Client;

import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TablePanel extends JPanel {

	JTable userTable; // 게임 참여자 테이블
	private JScrollPane listJs; // 게임 참여자 테이블

	Vector<String> userColumn = new Vector<String>();
	DefaultTableModel model;
	Vector<String> userRow;

	private void addComponent() {
		setLayout(null);
		userColumn.addElement("아이디");
		userColumn.addElement("점수");

		model = new DefaultTableModel(userColumn, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		userTable = new JTable(model);
		userTable.getTableHeader().setReorderingAllowed(false);
		userTable.getTableHeader().setResizingAllowed(false);
		listJs = new JScrollPane(userTable);

		userTable.getColumnModel().getColumn(0).setMaxWidth(100);
		userTable.getColumnModel().getColumn(0).setMinWidth(100);
		userTable.getColumnModel().getColumn(0).setWidth(100);

		userTable.getColumnModel().getColumn(1).setMaxWidth(134);
		userTable.getColumnModel().getColumn(1).setMinWidth(134);
		userTable.getColumnModel().getColumn(1).setWidth(134);

		userTable.setRowHeight(110);

		this.listJs.setBounds(0, 6, 234, 414); // 게임참여자 창 크기 조절

		add(listJs);
	}

	public TablePanel() {
		setSize(240, 426);
		setBackground(InstagrimUtil.clear);
		addComponent();
	}
}
