package git_study2;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import git_study2.conn.JdbcUtil;

public class ConnectionTest {
	public static void main(String[] args) {

		// try - catch - finally
//		connection_test01();
		
		// try - catch - resource
//		connection_test02();
		
		connection_test03();
		
	}
	
		private static void connection_test03() {
			Connection con = JdbcUtil.getConnection();
			System.out.println(con);
			
			try (Connection conn = JdbcUtil.getConnection();
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery("select dept_no, dept_name, floor from Department");) {
				while (rs.next()) {
					int deptNo = rs.getInt("dept_no");
					String deptName = rs.getString("dept_name");
					int floor = rs.getInt("floor");
					System.out.printf("%d %s %d%n", deptNo, deptName, floor);
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		
	
	private static void connection_test02() {
		String url = "jdbc:oracle:thin:@localhost:1521:orcl?useSSL=false";
		String user = "user_erp";
		String pwd = "rootroot";

		// close()가 자동으로 호출 된다.
		try (Connection conn = DriverManager.getConnection(url, user, pwd);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("select dept_no, dept_name, floor from Department");) {
			while (rs.next()) {
				int deptNo = rs.getInt("dept_no");
				String deptName = rs.getString("dept_name");
				int floor = rs.getInt("floor");
				System.out.printf("%d %s %d%n", deptNo, deptName, floor);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	
	
	private static void connection_test01() {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			// 1. JDBC 드라이버로딩
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// 2. 데이터베이스커넥션생성
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl?useSSL=false", "user_erp", "rootroot");
			// 3. Statement 생성
			stmt = conn.createStatement();
			// 4. 쿼리실행
			rs = stmt.executeQuery("select dept_no, dept_name, floor from Department");
			// 5. 쿼리실행결과출력
			while (rs.next()) {
				int deptNo = rs.getInt("dept_no");
				String deptName = rs.getString("dept_name");
				int floor = rs.getInt("floor");
				System.out.printf("%d %s %d%n", deptNo, deptName, floor);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException ex) {
				} // 6. 사용한 ResultSet종료(select인 경우)
			if (stmt != null)
				try {
					stmt.close();
				} catch (SQLException ex) {
				} // 7. 사용한 Statement 종료
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException ex) {
				} // 8. 커넥션종료
		}
	}

}
