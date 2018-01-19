package Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DBManager {

	static final String url = "jdbc:oracle:thin:@oracledbinstance.cghdlhexhsme.ap-northeast-2.rds.amazonaws.com:1521:ORCL";
	static final String dbId = "soyeon";
	static final String dbPw = "thth9512";

	private Connection getConnection() throws Exception {
		Class.forName("oracle.jdbc.driver.OracleDriver");

		Connection con = DriverManager.getConnection(url, dbId, dbPw);
		return con;
	}

	// ↓ 회원가입이 가능할 때 회원의 정보를 입력하기 위한 메소드
	public int insertData(LoginInfo l) throws Exception {
		Connection con = this.getConnection();

		String sql = "insert into userList values(seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement pstat = con.prepareStatement(sql);
		pstat.setString(1, l.getLoginId());
		pstat.setString(2, l.getLoginPw());
		pstat.setString(3, l.getName());
		pstat.setString(4, l.getEmailAddress());
		pstat.setInt(5, l.getLevel());
		pstat.setInt(6, l.getRecord());
		pstat.setInt(7, l.getWin());
		pstat.setInt(8, l.getLose());
		pstat.setInt(9, l.getExp());

		int result = pstat.executeUpdate();
		con.commit();
		con.close();
		return result;
	}

	// ↓ 회원가입할 때 중복된 아이디가 있는지 없는지 검사하기 위한 메소드
	public boolean isIdExist(String id) throws Exception {
		Connection con = this.getConnection();
		boolean result;
		String sql = "select * from userList where id = ?";
		PreparedStatement pstat = con.prepareStatement(sql);
		pstat.setString(1, id);
		ResultSet rs = pstat.executeQuery();
		result = rs.next();
		return result;
	}

	// ↓ 아이디 찾기할 때 이름과 이메일로 가입된 아이디정보가 있는지 없는지 확인
	public boolean findID(String name, String email) throws Exception {
		Connection con = this.getConnection();
		boolean result;
		String sql = "select * from userList where name = ? and email = ?";
		PreparedStatement pstat = con.prepareStatement(sql);
		pstat.setString(1, name);
		pstat.setString(2, email);
		ResultSet rs = pstat.executeQuery();
		result = rs.next();
		return result;
	}

	// ↓ 비밀번호 찾기할 때 아이다, 이름과 이메일로 가입된 아이디정보가 있는지 없는지 확인
	public boolean findPW(String id, String name, String email) throws Exception {
		Connection con = this.getConnection();
		boolean result;
		String sql = "select * from userList where id = ? and name = ? and email = ?";
		PreparedStatement pstat = con.prepareStatement(sql);
		pstat.setString(1, id);
		pstat.setString(2, name);
		pstat.setString(3, email);
		ResultSet rs = pstat.executeQuery();
		result = rs.next();
		return result;
	}

	// ↓ 데이터베이스 목록을 가져와서 ID/PW찾기를 할 때 받아온 정보와 일치하는 id, pw정보를 전달해주기 위한 메소드
	public ArrayList<LoginInfo> AllgetData() throws Exception {
		Connection con = this.getConnection();
		Statement stat = con.createStatement();
		ResultSet rs = stat.executeQuery("select * from userList order by seq asc");
		ArrayList<LoginInfo> memberList = new ArrayList<LoginInfo>();

		while (rs.next()) {
			String memberList_Id = rs.getString("id");
			String memberList_Pw = rs.getString("pw");
			String memberList_Name = rs.getString("name");
			String memberList_Email = rs.getString("email");
			int memberList_level = rs.getInt("LV");
			int memberList_record = rs.getInt("record");
			int memberList_win = rs.getInt("win");
			int memberList_lose = rs.getInt("lose");
			int memberList_exp = rs.getInt("exp");
			memberList.add(new LoginInfo(memberList_Id, memberList_Pw, memberList_Name, memberList_Email,
					memberList_level, memberList_record, memberList_win, memberList_lose, memberList_exp));
		}

		con.close();
		return memberList;
	}

	// 로그인할 때 입력한 아이디, 비밀번호가 일치하는 결과가 데이터베이스 내에 존재하는지 확인하는 메소드
	public boolean login(String id, String pw) throws Exception {
		Connection con = this.getConnection();
		boolean result = false;
		String sql = "select * from userList where id = ? and pw = ?";
		PreparedStatement pstat = con.prepareStatement(sql);
		pstat.setString(1, id);
		pstat.setString(2, pw);
		ResultSet rs = pstat.executeQuery();
		result = rs.next();
		return result;
	}

}
