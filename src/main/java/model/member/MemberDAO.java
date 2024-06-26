package model.member;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import common.dbConnection;
import common.encoding;

public class MemberDAO extends dbConnection {
	public String strMessage = "";

	public MemberDAO(ServletContext application) {
		super(application);
	}

	public String fnPasswordConfirm(String strUserNo, String strUserPassword) {
		String cLoginPassFlag = "N";
		try {
			String qryPrivateInfo = "SELECT user_password, password_salt FROM user_private_info WHERE user_no = ?";
			this.pstmt = this.con.prepareStatement(qryPrivateInfo);
			this.pstmt.setString(1, strUserNo);
			this.rs = this.pstmt.executeQuery();
			cLoginPassFlag = "N";
			if (this.rs.next()) {
				encoding encode = new encoding("SHA-256");
				String strPasswordSalt = this.rs.getString("password_salt");
				String strPasswordEncode = encode.toEncode(strUserPassword + strPasswordSalt);
				String strPassword = this.rs.getString("user_password");
				if (strPasswordEncode.equals(strPassword))
					cLoginPassFlag = "Y";
			}
		} catch (Exception e) {
			this.strMessage = e.getMessage();
		}
		return cLoginPassFlag;
	}

	public MemberDTO loginProc(MemberDTO dto) {
		MemberDTO loginDto = new MemberDTO();
		try {
			this.con.setAutoCommit(false);
			String qryGeneralInfo = "SELECT user_no, user_name FROM user_general_info WHERE user_id = ?";
			this.pstmt = this.con.prepareStatement(qryGeneralInfo);
			this.pstmt.setString(1, dto.getUserId());
			this.rs = this.pstmt.executeQuery();
			if (!this.rs.next()) {
				throw new Exception("아이디 또는 비밀번호를 확인하세요");
			}

			String strUserNo = this.rs.getString("user_no");
			String strUserName = this.rs.getString("user_name");
			String cLoginPassFlag = this.fnPasswordConfirm(strUserNo, dto.getUserPassword());

			String qryLoginLog = "INSERT INTO user_login_log ";
			qryLoginLog = qryLoginLog + " (user_no, login_pass_flag, login_ip) ";
			qryLoginLog = qryLoginLog + " VALUES ";
			qryLoginLog = qryLoginLog + " (?, ?, ?) ";
			this.pstmt = this.con.prepareStatement(qryLoginLog);
			this.pstmt.setString(1, strUserNo);
			this.pstmt.setString(2, cLoginPassFlag);
			this.pstmt.setString(3, dto.getUserIp());
			int nLoginLogResult = this.pstmt.executeUpdate();

			if (nLoginLogResult < 1) {
				throw new Exception("로그인 처리에 실패했습니다");
			}

			this.con.commit();

			if (cLoginPassFlag == "Y") {
				loginDto.setUserNo(strUserNo);
				loginDto.setUserName(strUserName);
				loginDto.setUserId(dto.getUserId());
			}
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

	public int memberJoinProc(MemberDTO dto) {
		int nUserNo = 0;
		int nResult = 0;
		try {
			this.con.setAutoCommit(false);

			String qryUniqueUserId = "SELECT user_no FROM user_general_info WHERE user_id = ?";
			this.pstmt = this.con.prepareStatement(qryUniqueUserId);
			this.pstmt.setString(1, dto.getUserId());
			this.rs = this.pstmt.executeQuery();
			if (this.rs.next()) {
				throw new Exception("사용할 수 없는 아이디입니다");
			}

			String qryGenralInfo = "INSERT INTO user_general_info ";
			qryGenralInfo = qryGenralInfo + " (user_id, user_name) ";
			qryGenralInfo = qryGenralInfo + " VALUES ";
			qryGenralInfo = qryGenralInfo + " (?, ?) ";
			this.pstmt = this.con.prepareStatement(qryGenralInfo, 1);
			this.pstmt.setString(1, dto.getUserId());
			this.pstmt.setString(2, dto.getUserName());
			nResult = this.pstmt.executeUpdate();

			if (nResult < 1) {
				throw new Exception("회원 정보 저장에 실패했습니다 (1)");
			}

			this.rs = this.pstmt.getGeneratedKeys();
			this.rs.next();
			nUserNo = this.rs.getInt(1);
			String qryPrivateInfo = "INSERT INTO user_private_info ";
			qryPrivateInfo = qryPrivateInfo
					+ " (user_no, user_password, password_salt, user_phone, user_zip_code, user_address) ";
			qryPrivateInfo = qryPrivateInfo + " VALUES ";
			qryPrivateInfo = qryPrivateInfo + " (?, ?, ?, ?, ?, ?) ";
			this.pstmt = this.con.prepareStatement(qryPrivateInfo);
			this.pstmt.setInt(1, nUserNo);
			this.pstmt.setString(2, dto.getUserPassword());
			this.pstmt.setString(3, dto.getPasswordSalt());
			this.pstmt.setString(4, dto.getUserPhone());
			this.pstmt.setString(5, dto.getUserZipCode());
			this.pstmt.setString(6, dto.getUserAddress());
			nResult = this.pstmt.executeUpdate();

			if (nResult < 1) {
				throw new Exception("회원 정보 저장에 실패했습니다 (2)");
			}

			this.con.commit();
		} catch (Exception e) {
			nUserNo = 0;
			this.strMessage = e.getMessage();
			try {
				this.con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return nUserNo;
	}

	public int memberCount() {
		int nMemberCnt = 0;

		try {
			String qryMemberList = "SELECT count(general.user_no) AS member_cnt ";
			qryMemberList += " FROM user_general_info AS general ";
			qryMemberList += " LEFT JOIN user_private_info AS private ";
			qryMemberList += " ON general.user_no = private.user_no ";

			this.stmt = this.con.createStatement();
			this.rs = this.stmt.executeQuery(qryMemberList);
			if (this.rs.next()) {
				nMemberCnt = Integer.parseInt(this.rs.getString("member_cnt"));
			}
		} catch (SQLException e) {
			this.strMessage = e.getMessage();
			e.printStackTrace();
		}

		return nMemberCnt;
	}

	public List<MemberDTO> memberList(int start_page, int per_page) {
		List<MemberDTO> rgMemberList = new ArrayList<>();
		try {
			String qryMemberList = "SELECT ";
			qryMemberList += " general.user_no, general.user_id, general.user_name, general.reg_date, ";
			qryMemberList += " private.user_phone, private.user_zip_code, private.user_address ";
			qryMemberList += " FROM user_general_info AS general ";
			qryMemberList += " LEFT JOIN user_private_info AS private ";
			qryMemberList += " ON general.user_no = private.user_no ";
			qryMemberList += " ORDER BY general.user_no DESC ";
			qryMemberList += " OFFSET ? ROWS ";
			qryMemberList += " FETCH NEXT ? ROWS ONLY ";

			this.pstmt = this.con.prepareStatement(qryMemberList);
			this.pstmt.setInt(1, start_page);
			this.pstmt.setInt(2, per_page);
			this.rs = this.pstmt.executeQuery();

			while (this.rs.next()) {
				MemberDTO rgMemberInfo = new MemberDTO();
				rgMemberInfo.setUserNo(this.rs.getString("user_no"));
				rgMemberInfo.setUserId(this.rs.getString("user_id"));
				rgMemberInfo.setUserName(this.rs.getString("user_name"));
				rgMemberInfo.setUserJoinDate(this.rs.getString("reg_date"));
				rgMemberInfo.setUserPhone(this.rs.getString("user_phone"));
				rgMemberInfo.setUserZipCode(this.rs.getString("user_zip_code"));
				rgMemberInfo.setUserAddress(this.rs.getString("user_address"));
				rgMemberList.add(rgMemberInfo);
			}
		} catch (Exception e) {
			this.strMessage = e.getMessage();
			e.printStackTrace();
		}
		return rgMemberList;
	}

	public MemberDTO getMemberInfo(String userNo) {
		MemberDTO rgMemberInfo = new MemberDTO();
		try {
			String rstMemberInfo = "SELECT ";
			rstMemberInfo += " general.user_no, general.user_id, general.user_name, general.reg_date, ";
			rstMemberInfo += " private.user_zip_code, private.user_address, private.user_phone ";
			rstMemberInfo += " FROM user_general_info AS general ";
			rstMemberInfo += " LEFT JOIN user_private_info AS private ";
			rstMemberInfo += " ON general.user_no = private.user_no ";
			rstMemberInfo += " WHERE general.user_no = ? ";

			this.pstmt = this.con.prepareStatement(rstMemberInfo);
			this.pstmt.setString(1, userNo);
			this.rs = this.pstmt.executeQuery();

			if (!this.rs.next()) {
				throw new Exception("회원 정보가 존재하지 않습니다");
			}

			rgMemberInfo.setUserNo(this.rs.getString("user_no"));
			rgMemberInfo.setUserId(this.rs.getString("user_id"));
			rgMemberInfo.setUserName(this.rs.getString("user_name"));
			rgMemberInfo.setUserJoinDate(this.rs.getString("reg_date"));
			rgMemberInfo.setUserZipCode(this.rs.getString("user_zip_code"));
			rgMemberInfo.setUserAddress(this.rs.getString("user_address"));
			rgMemberInfo.setUserPhone(this.rs.getString("user_phone"));
		} catch (Exception e) {
			this.strMessage = e.getMessage();
			e.printStackTrace();
		}
		return rgMemberInfo;
	}

	// 회원 정보 변경 (일반 정보)
	public boolean modifyGeneralMemberInfo(MemberDTO dto) {
		boolean bModify = false;
		try {
			// 회원번호 유무 확인
			if (dto.getUserNo() == null) {
				throw new Exception("필수값이 누락되었습니다");
			}

			// 일반정보에서 변경 가능한 컬럼 확인
			if (dto.getUserName() == null) {
				throw new Exception("변경 가능한 값이 없습니다");
			}

			this.con.setAutoCommit(false);
			String rstUpdateGeneralInfo = "UPDATE user_general_info SET user_name = ? WHERE user_no = ?";
			this.pstmt = this.con.prepareStatement(rstUpdateGeneralInfo);
			this.pstmt.setString(1, dto.getUserName());
			this.pstmt.setString(2, dto.getUserNo());
			if (this.pstmt.executeUpdate() < 1) {
				throw new Exception("회원 정보 수정 실패");
			}

			// ▼ 로그 작성
			String rstModifyLog = "INSERT INTO user_modify_log ";
			rstModifyLog += " (user_no, private_flag, modify_key, modify_value) ";
			rstModifyLog += " VALUES ";
			rstModifyLog += " (?, ?, ?, ?) ";
			this.pstmt = this.con.prepareStatement(rstModifyLog);
			this.pstmt.setString(1, dto.getUserNo());
			this.pstmt.setString(2, "n");
			this.pstmt.setString(3, "user_name");
			this.pstmt.setString(4, dto.getUserName());
			if (this.pstmt.executeUpdate() < 1) {
				throw new Exception("로그 작성 실패");
			}
			// ▲ 로그 작성

			this.con.commit();
		} catch (Exception e) {
			this.strMessage = e.getMessage();
		}
		return bModify;
	}

	// 회원 정보 변경 (암호화 정보)
	public boolean modifyPrivateMemberInfo(MemberDTO dto) {
		boolean bModify = false;
		try {
			// 회원번호 유무 확인
			if (dto.getUserNo() == null) {
				throw new Exception("필수값이 누락되었습니다");
			}

			Map<String, String> rgChangeList = new HashMap<String, String>();
			if (dto.getUserPassword() != null) {
				rgChangeList.put("user_password", dto.getUserPassword());
			}
			if (dto.getUserPhone() != null) {
				rgChangeList.put("user_phone", dto.getUserPhone());
			}
			if (dto.getUserZipCode() != null) {
				rgChangeList.put("user_zip_code", dto.getUserZipCode());
			}
			if (dto.getUserAddress() != null) {
				rgChangeList.put("user_address", dto.getUserAddress());
			}

			this.con.setAutoCommit(false);

			// 암호화 정보 수정
			String rstUpdatePrivateInfo = "UPDATE user_private_info SET ";
			rstUpdatePrivateInfo += " user_phone = ?, ";
			rstUpdatePrivateInfo += " user_zip_code = ?, ";
			rstUpdatePrivateInfo += " user_address = ?, ";
			if (dto.getUserPassword() != null && dto.getPasswordSalt() != null) {
				rstUpdatePrivateInfo += " user_password = ?, ";
				rstUpdatePrivateInfo += " password_salt = ? ";
			}
			rstUpdatePrivateInfo += " WHERE user_no = ? ";
			this.pstmt = this.con.prepareStatement(rstUpdatePrivateInfo);
			this.pstmt.setString(1, dto.getUserPhone());
			this.pstmt.setString(2, dto.getUserZipCode());
			this.pstmt.setString(3, dto.getUserAddress());
			if (dto.getUserPassword() != null && dto.getPasswordSalt() != null) {
				this.pstmt.setString(4, dto.getUserPassword());
				this.pstmt.setString(5, dto.getPasswordSalt());
				this.pstmt.setString(6, dto.getUserNo());
			} else {
				this.pstmt.setString(4, dto.getUserNo());
			}
			if (this.pstmt.executeUpdate() < 1) {
				throw new Exception("회원 정보 수정 실패");
			}

			// ▼ 로그 작성
			String rstModifyLog = "INSERT INTO user_modify_log ";
			rstModifyLog += " (user_no, private_flag, modify_key, modify_value) ";
			rstModifyLog += " VALUES ";
			rstModifyLog += " (?, ?, ?, ?) ";
			this.pstmt = this.con.prepareStatement(rstModifyLog);
			for (String strKey : rgChangeList.keySet()) {
				this.pstmt.setString(1, dto.getUserNo());
				this.pstmt.setString(2, "y");
				this.pstmt.setString(3, strKey);
				this.pstmt.setString(4, rgChangeList.get(strKey));

				this.pstmt.addBatch();
				this.pstmt.clearParameters();
			}
			int[] nLogResult = this.pstmt.executeBatch();
			for (int nLogIndex = 0; nLogIndex < nLogResult.length; nLogIndex++) {
				if (nLogResult[nLogIndex] < 0) {
					throw new Exception("로그 작성 실패했습니다. 실패 코드 [" + nLogResult[nLogIndex] + "]");
				}
			}
			this.pstmt.clearBatch();
			// ▲ 로그 작성

			this.con.commit();
		} catch (Exception e) {
			this.strMessage = e.getMessage();
		}
		return bModify;
	}

	public String getMsg() {
		return this.strMessage;
	}
}
