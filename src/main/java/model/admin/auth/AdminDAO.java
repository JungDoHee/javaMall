package model.admin.auth;

import java.sql.SQLException;

import javax.servlet.ServletContext;

import common.dbConnection;
import common.encoding;

public class AdminDAO extends dbConnection {
	public String strMessage = "";

	public AdminDAO(ServletContext application) {
		super(application);
	}

	public int adminJoinProc(AdminDTO dto) {
		int nAdminNo = 0;
		try {
			this.con.setAutoCommit(false);

			String qryUniqueId = "SELECT admin_no FROM admin_general_info WHERE admin_id = ?";
			this.pstmt = this.con.prepareStatement(qryUniqueId);
			this.pstmt.setString(1, dto.getAdminID());
			this.rs = this.pstmt.executeQuery();
			if (this.rs.next()) {
				throw new Exception("사용할 수 없는 아이디입니다");
			}

			String rstAdminGeneralInfo = "INSERT INTO admin_general_info ";
			rstAdminGeneralInfo = rstAdminGeneralInfo + " (admin_id, admin_name) ";
			rstAdminGeneralInfo = rstAdminGeneralInfo + " VALUES ";
			rstAdminGeneralInfo = rstAdminGeneralInfo + " (?, ?) ";
			this.pstmt = this.con.prepareStatement(rstAdminGeneralInfo, 1);
			this.pstmt.setString(1, dto.getAdminID());
			this.pstmt.setString(2, dto.getAdminName());
			if (this.pstmt.executeUpdate() < 1) {
				throw new Exception("관리자 정보 등록에 실패했습니다 (1)");
			}

			this.rs = this.pstmt.getGeneratedKeys();
			if (!this.rs.next()) {
				throw new Exception("관리자 정보 등록에 실패했습니다 (2)");
			}
			nAdminNo = this.rs.getInt(1);

			String rstAdminPrivateInfo = "INSERT INTO admin_private_info ";
			rstAdminPrivateInfo = rstAdminPrivateInfo + " (admin_no, admin_password, password_salt) ";
			rstAdminPrivateInfo = rstAdminPrivateInfo + " VALUES ";
			rstAdminPrivateInfo = rstAdminPrivateInfo + " (?, ?, ?) ";
			this.pstmt = this.con.prepareStatement(rstAdminPrivateInfo);
			this.pstmt.setInt(1, nAdminNo);
			this.pstmt.setString(2, dto.getAdminPassword());
			this.pstmt.setString(3, dto.getPasswordSalt());
			if (this.pstmt.executeUpdate() < 1) {
				throw new Exception("관리자 정보 등록에 실패했습니다 (3)");
			}
			this.con.commit();
		} catch (Exception e) {
			this.strMessage = e.getMessage();
			try {
				this.con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return nAdminNo;
	}

	public AdminDTO loginProc(AdminDTO dto) {
		AdminDTO loginDto = new AdminDTO();
		try {
			this.con.setAutoCommit(false);

			String rstGeneralInfo = "SELECT admin_no, admin_id, admin_name FROM admin_general_info WHERE admin_id=?";
			this.pstmt = this.con.prepareStatement(rstGeneralInfo);
			this.pstmt.setString(1, dto.getAdminID());
			this.rs = this.pstmt.executeQuery();
			if (!this.rs.next()) {
				throw new Exception("아이디 또는 비밀번호를 확인하세요");
			}
			String strAdminNo = this.rs.getString("admin_no");
			String strAdminId = this.rs.getString("admin_id");
			String strAdminName = this.rs.getString("admin_name");

			String rstPrivateInfo = "SELECT admin_password, password_salt FROM admin_private_info WHERE admin_no=?";
			this.pstmt = this.con.prepareStatement(rstPrivateInfo);
			this.pstmt.setString(1, strAdminNo);
			this.rs = this.pstmt.executeQuery();
			if (!this.rs.next()) {
				throw new Exception("아이디 또는 비밀번호를 확인하세요");
			}
			String strAdminPassword = this.rs.getString("admin_password");
			String strPasswordSalt = this.rs.getString("password_salt");

			String strLoginPassFlag = "N";
			encoding encode = new encoding("SHA-256");
			String strEncodePassword = encode.toEncode(dto.getAdminPassword() + strPasswordSalt);
			if (strEncodePassword.equals(strAdminPassword)) {
				strLoginPassFlag = "Y";
			}

			String strLoginLog = "INSERT INTO admin_login_log ";
			strLoginLog = strLoginLog + " (admin_no, login_pass_flag, login_ip) ";
			strLoginLog = strLoginLog + " VALUES ";
			strLoginLog = strLoginLog + " (?, ?, ?) ";
			this.pstmt = this.con.prepareStatement(strLoginLog);
			this.pstmt.setString(1, strAdminNo);
			this.pstmt.setString(2, strLoginPassFlag);
			this.pstmt.setString(3, dto.getUserIp());
			if (this.pstmt.executeUpdate() < 1) {
				throw new Exception("로그인 처리에 실패했습니다");
			}

			if (strLoginPassFlag.equals("Y")) {
				loginDto.setAdminID(strAdminId);
				loginDto.setAdminNo(strAdminNo);
				loginDto.setAdminName(strAdminName);
			}

			this.con.commit();
		} catch (Exception e) {
			this.strMessage = e.getMessage();
			try {
				this.con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return loginDto;
	}

	public String getMessage() {
		return this.strMessage;
	}
}