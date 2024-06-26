package model.item;

import java.util.HashMap;
import java.util.Map;

public class ItemDTO {
	private String itemSeq;
	private String itemSubject;
	private String itemPrice;
	private String itemStatus;
	private String itemInfo;
	private String thumbPath;
	private String thumbHashName;
	private String thumbName;
	private String regDate;
	private String modifyDate;
	private String adminNo;
	private String adminName;
	private String imageSeq;
	private String imagePath;
	private String imageHashName;
	private String imageName;
	private String imageRegDate;
	private String imageModifyDate;
	private String categorySeq;
	private String initStock;
	private String cartAmount;

	public String getCartAmount() {
		return cartAmount;
	}

	public void setCartAmount(String cartAmount) {
		this.cartAmount = cartAmount;
	}

	public String getCategorySeq() {
		return categorySeq;
	}

	public void setCategorySeq(String categorySeq) {
		this.categorySeq = categorySeq;
	}

	public String getInitStock() {
		return initStock;
	}

	public void setInitStock(String initStock) {
		this.initStock = initStock;
	}

	public String getAdminName() {
		return this.adminName;
	}

	public String getImageSeq() {
		return this.imageSeq;
	}

	public void setImageSeq(String imageSeq) {
		this.imageSeq = imageSeq;
	}

	public String getImagePath() {
		return this.imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getImageHashName() {
		return this.imageHashName;
	}

	public void setImageHashName(String imageHashName) {
		this.imageHashName = imageHashName;
	}

	public String getImageName() {
		return this.imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getImageRegDate() {
		return this.imageRegDate;
	}

	public void setImageRegDate(String imageRegDate) {
		this.imageRegDate = imageRegDate;
	}

	public String getImageModifyDate() {
		return this.imageModifyDate;
	}

	public void setImageModifyDate(String imageModifyDate) {
		this.imageModifyDate = imageModifyDate;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public String getAdminNo() {
		return this.adminNo;
	}

	public void setAdminNo(String adminNo) {
		this.adminNo = adminNo;
	}

	public String getItemSeq() {
		return this.itemSeq;
	}

	public void setItemSeq(String itemSeq) {
		this.itemSeq = itemSeq;
	}

	public String getItemSubject() {
		return this.itemSubject;
	}

	public void setItemSubject(String itemSubject) {
		this.itemSubject = itemSubject;
	}

	public String getItemPrice() {
		return this.itemPrice;
	}

	public void setItemPrice(String itemPrice) {
		this.itemPrice = itemPrice;
	}

	public String getItemStatus() {
		return this.itemStatus;
	}

	public void setItemStatus(String itemStatus) {
		this.itemStatus = itemStatus;
	}

	public String getItemInfo() {
		return this.itemInfo;
	}

	public void setItemInfo(String itemInfo) {
		this.itemInfo = itemInfo;
	}

	public String getThumbPath() {
		return this.thumbPath;
	}

	public void setThumbPath(String thumbPath) {
		this.thumbPath = thumbPath;
	}

	public String getThumbHashName() {
		return this.thumbHashName;
	}

	public void setThumbHashName(String thumbHashName) {
		this.thumbHashName = thumbHashName;
	}

	public String getThumbName() {
		return this.thumbName;
	}

	public void setThumbName(String thumbName) {
		this.thumbName = thumbName;
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

	public Map<String, String> getItemStatusKor() {
		Map<String, String> rgItemStatus = new HashMap<String, String>();
		rgItemStatus.put("a", "정상");
		rgItemStatus.put("b", "품절");
		rgItemStatus.put("c", "삭제");
		return rgItemStatus;
	}
}