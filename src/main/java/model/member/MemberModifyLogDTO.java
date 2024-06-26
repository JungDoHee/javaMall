package model.member;

public class MemberModifyLogDTO {
	public String userNo;

	public String privateFlag;

	public String modifyKey;

	public String modifyValue;

	public String regDate;

	public String getUserNo() {
		return this.userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getPrivateFlag() {
		return this.privateFlag;
	}

	public void setPrivateFlag(String privateFlag) {
		this.privateFlag = privateFlag;
	}

	public String getModifyKey() {
		return this.modifyKey;
	}

	public void setModifyKey(String modifyKey) {
		this.modifyKey = modifyKey;
	}

	public String getModifyValue() {
		return this.modifyValue;
	}

	public void setModifyValue(String modifyValue) {
		this.modifyValue = modifyValue;
	}

	public String getRegDate() {
		return this.regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
}
