package model.item;

public class categoryDTO {
	private String categorySeq;
	private String categoryCode;
	private String categoryName;
	private String regAdminNo;
	private String regDate;
	private String modAdminNo;
	private String modDate;
	private String categoryCount;

	public String getCategoryCount() {
		return categoryCount;
	}

	public void setCategoryCount(String categoryCount) {
		this.categoryCount = categoryCount;
	}

	public String getCategorySeq() {
		return categorySeq;
	}

	public void setCategorySeq(String categorySeq) {
		this.categorySeq = categorySeq;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getRegAdminNo() {
		return regAdminNo;
	}

	public void setRegAdminNo(String regAdminNo) {
		this.regAdminNo = regAdminNo;
	}

	public String getRegDate() {
		return regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public String getModAdminNo() {
		return modAdminNo;
	}

	public void setModAdminNo(String modAdminNo) {
		this.modAdminNo = modAdminNo;
	}

	public String getModDate() {
		return modDate;
	}

	public void setModDate(String modDate) {
		this.modDate = modDate;
	}

}
