package model.member;

public class MemberDTO {
	private String userNo;
	private String userId;
	private String userPhone;
	private String userZipCode;
	private String userAddress;
	private String userName;
	private String userJoinDate;
	private String userPassword;
	private String passwordSalt;
	private String userIp;

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserZipCode() {
		return userZipCode;
	}

	public void setUserZipCode(String userZipCode) {
		this.userZipCode = userZipCode;
	}

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserJoinDate() {
		return userJoinDate;
	}

	public void setUserJoinDate(String userJoinDate) {
		this.userJoinDate = userJoinDate;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getPasswordSalt() {
		return passwordSalt;
	}

	public void setPasswordSalt(String passwordSalt) {
		this.passwordSalt = passwordSalt;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}
}
