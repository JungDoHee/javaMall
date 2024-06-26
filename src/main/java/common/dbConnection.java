package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletContext;

public class dbConnection {
	public Connection con;
	public Statement stmt;
	public PreparedStatement pstmt;
	public ResultSet rs;

	public dbConnection(ServletContext application) {
		try {
			String strDriver = application.getInitParameter("MssqlDriver");
			String strDbUrl = application.getInitParameter("MssqlUrl");
			String strDbUser = application.getInitParameter("MssqlUser");
			String strDbPassword = application.getInitParameter("MssqlPw");
			String strDbName = application.getInitParameter("mainDatabase");

			String strConnection = strDbUrl + "databaseName=" + strDbName + ";user=" + strDbUser + ";password="
					+ strDbPassword;

			Class.forName(strDriver);
			this.con = DriverManager.getConnection(strConnection);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void dbClose() {
		try {
			if (this.rs != null)
				this.rs.close();
			if (this.stmt != null)
				this.stmt.close();
			if (this.pstmt != null)
				this.pstmt.close();
			if (this.con != null)
				this.con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}