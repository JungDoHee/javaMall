package model.item;

public class PaymentDTO {
	private String paymentSeq;
	private String itemSeq;
	private String orderSeq;
	private String userNo;
	private String paymentMethod;
	private String paymentPrice;
	private String paymentStatus;
	private String startDate;
	private String endDate;

	public String getPaymentSeq() {
		return paymentSeq;
	}

	public void setPaymentSeq(String paymentSeq) {
		this.paymentSeq = paymentSeq;
	}

	public String getItemSeq() {
		return itemSeq;
	}

	public void setItemSeq(String itemSeq) {
		this.itemSeq = itemSeq;
	}

	public String getOrderSeq() {
		return orderSeq;
	}

	public void setOrderSeq(String orderSeq) {
		this.orderSeq = orderSeq;
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getPaymentPrice() {
		return paymentPrice;
	}

	public void setPaymentPrice(String paymentPrice) {
		this.paymentPrice = paymentPrice;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
