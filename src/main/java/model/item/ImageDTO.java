package model.item;

public class ImageDTO {
	private String itemSeq;
	private String absoluteImagePath;
	private String imageServerPath;
	private String hashName;
	private String originalName;
	private String regDate;
	private String modifyDate;

	public String getItemSeq() {
		return this.itemSeq;
	}

	public void setItemSeq(String itemSeq) {
		this.itemSeq = itemSeq;
	}

	public String getAbsoluteImagePath() {
		return this.absoluteImagePath;
	}

	public void setAbsoluteImagePath(String absoluteImagePath) {
		this.absoluteImagePath = absoluteImagePath;
	}

	public String getImageServerPath() {
		return this.imageServerPath;
	}

	public void setImageServerPath(String imageServerPath) {
		this.imageServerPath = imageServerPath;
	}

	public String getHashName() {
		return this.hashName;
	}

	public void setHashName(String hashName) {
		this.hashName = hashName;
	}

	public String getOriginalName() {
		return this.originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public String getRegDate() {
		return this.regDate;
	}

	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}

	public String getModifyDate() {
		return this.modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
}