package model.item;

import java.util.HashMap;
import java.util.Map;

public class OrderDTO {
	private String orderSeq;
	private String itemSeq;
	private String userNo;
	private String orderStatus;
	private String originPrice;
	private String unitPrice;
	private String discountedPrice;
	private String paymentPrice;
	private String amount;
	private String orderDate;
	private String deliveryDate;
	private String cancelDate;
	private String successDate;

	private String imageName;
	private String categorySeq;
	private String itemSubject;

	public String getItemSubject() {
		return itemSubject;
	}

	public void setItemSubject(String itemSubject) {
		this.itemSubject = itemSubject;
	}

	public String getCategorySeq() {
		return categorySeq;
	}

	public void setCategorySeq(String categorySeq) {
		this.categorySeq = categorySeq;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getOrderSeq() {
		return orderSeq;
	}

	public void setOrderSeq(String orderSeq) {
		this.orderSeq = orderSeq;
	}

	public String getItemSeq() {
		return itemSeq;
	}

	public void setItemSeq(String itemSeq) {
		this.itemSeq = itemSeq;
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOriginPrice() {
		return originPrice;
	}

	public void setOriginPrice(String originPrice) {
		this.originPrice = originPrice;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getDiscountedPrice() {
		return discountedPrice;
	}

	public void setDiscountedPrice(String discountedPrice) {
		this.discountedPrice = discountedPrice;
	}

	public String getPaymentPrice() {
		return paymentPrice;
	}

	public void setPaymentPrice(String paymentPrice) {
		this.paymentPrice = paymentPrice;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(String cancelDate) {
		this.cancelDate = cancelDate;
	}

	public String getSuccessDate() {
		return successDate;
	}

	public void setSuccessDate(String successDate) {
		this.successDate = successDate;
	}

	public Map<String, String> getOrderStatusKor() {
		Map<String, String> rgStatus = new HashMap<String, String>();
		rgStatus.put("a", "접수");
		rgStatus.put("d", "배송");
		rgStatus.put("c", "반품");
		rgStatus.put("s", "완료");
		rgStatus.put("g", "취소");
		return rgStatus;
	}
}
