package model.admin.auth;

public class AdminDTO {
	private String adminNo;
	private String adminName;
	private String adminID;
	private String adminPassword;
	private String passwordSalt;
	private String regDate;
	private String userIp;

	public String getUserIp() {
		return this.userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getAdminNo() {
		return this.adminNo;
	}

	public void setAdminNo(String adminNo) {
		this.adminNo = adminNo;
	}

	public String getAdminName() {
		return this.adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getAdminID() {
		return this.adminID;
	}

	public void setAdminID(String adminID) {
		this.adminID = adminID;
	}

	public String getAdminPassword() {
		return this.adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public String getPasswordSalt() {
		return this.passwordSalt;
	}

	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

	public String getRegDate() {
		return this.regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
}