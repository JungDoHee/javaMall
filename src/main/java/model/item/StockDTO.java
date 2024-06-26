package model.item;

public class StockDTO {
	private String itemSeq;
	private String remainStock;
	private String initStock;

	public String getItemSeq() {
		return itemSeq;
	}

	public void setItemSeq(String itemSeq) {
		this.itemSeq = itemSeq;
	}

	public String getRemainStock() {
		return remainStock;
	}

	public void setRemainStock(String remainStock) {
		this.remainStock = remainStock;
	}

	public String getInitStock() {
		return initStock;
	}

	public void setInitStock(String initStock) {
		this.initStock = initStock;
	}
}
