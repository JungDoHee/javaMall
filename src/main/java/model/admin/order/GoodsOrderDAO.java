package model.admin.order;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import common.dbConnection;
import model.item.OrderDTO;

public class GoodsOrderDAO extends dbConnection {
	private String errMsg;

	public GoodsOrderDAO(ServletContext application) {
		super(application);
	}

	// 총 주문 수량 조회
	public int getOrderListCount() {
		int nTotal = 0;
		try {
			String rstOrderList = "SELECT count(seq) AS total_count FROM item_order_list ";
			this.stmt = this.con.createStatement();
			this.rs = this.stmt.executeQuery(rstOrderList);
			if (this.rs.next()) {
				nTotal = this.rs.getInt("total_count");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nTotal;
	}

	// 주문한 상품 목록 조회 (페이징 포함)
	public List<OrderDTO> getOrderList(int nStartPage, int nPerPage) {
		List<OrderDTO> rgOrderList = new ArrayList<>();
		try {
			String rstOrderList = "SELECT ";
			rstOrderList += " order_item.seq AS order_seq, order_item.order_status, order_item.origin_price, order_item.user_no, ";
			rstOrderList += " order_item.unit_price, order_item.discounted_price, order_item.payment_price, order_item.amount, ";
			rstOrderList += " order_item.order_date, order_item.delivery_date, order_item.cancel_date, order_item.success_date, ";
			rstOrderList += " item.thumb_name, item.category_seq, item.item_subject ";
			rstOrderList += " FROM item_order_list AS order_item ";
			rstOrderList += " LEFT JOIN item_list AS item ";
			rstOrderList += " ON order_item.item_seq = item.seq ";
			rstOrderList += " ORDER BY order_item.seq DESC ";
			rstOrderList += " OFFSET ? ROWS ";
			rstOrderList += " FEtCH NEXT ? ROWS ONLY ";

			this.pstmt = this.con.prepareStatement(rstOrderList);
			this.pstmt.setInt(1, nStartPage);
			this.pstmt.setInt(2, nPerPage);
			this.rs = this.pstmt.executeQuery();
			while (this.rs.next()) {
				OrderDTO dto = new OrderDTO();
				dto.setOrderSeq(this.rs.getString("order_seq"));
				dto.setOrderStatus(this.rs.getString("order_status"));
				dto.setOriginPrice(this.rs.getString("origin_price"));
				dto.setUserNo(this.rs.getString("user_no"));
				dto.setUnitPrice(this.rs.getString("unit_price"));
				dto.setDiscountedPrice(this.rs.getString("discounted_price"));
				dto.setPaymentPrice(this.rs.getString("payment_price"));
				dto.setAmount(this.rs.getString("amount"));
				dto.setOrderDate(this.rs.getString("order_date"));
				dto.setDeliveryDate(this.rs.getString("delivery_date"));
				dto.setCancelDate(this.rs.getString("cancel_date"));
				dto.setSuccessDate(this.rs.getString("success_date"));
				dto.setImageName(this.rs.getString("thumb_name"));
				dto.setCategorySeq(this.rs.getString("category_seq"));
				dto.setItemSubject(this.rs.getString("item_subject"));
				rgOrderList.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.errMsg = e.getMessage();
		}
		return rgOrderList;
	}

	// 특정 주문 상품 정보 조회
	public OrderDTO getOrderInfo(String strOrderSeq) {
		OrderDTO orderDto = new OrderDTO();
		try {
			String rstOrderInfo = "SELECT * FROM item_order_list WHERE seq = ?";
			this.pstmt = this.con.prepareStatement(rstOrderInfo);
			this.pstmt.setString(1, strOrderSeq);
			this.rs = this.pstmt.executeQuery();
			if (this.rs.next()) {
				orderDto.setOrderSeq(this.rs.getString("seq"));
				orderDto.setItemSeq(this.rs.getString("item_seq"));
				orderDto.setUserNo(this.rs.getString("user_no"));
				orderDto.setOrderStatus(this.rs.getString("order_status"));
				orderDto.setOriginPrice(this.rs.getString("origin_price"));
				orderDto.setUnitPrice(this.rs.getString("unit_price"));
				orderDto.setDiscountedPrice(this.rs.getString("discounted_price"));
				orderDto.setPaymentPrice(this.rs.getString("payment_price"));
				orderDto.setAmount(this.rs.getString("amount"));
				orderDto.setOrderDate(this.rs.getString("order_date"));
				orderDto.setDeliveryDate(this.rs.getString("delivery_date"));
				orderDto.setCancelDate(this.rs.getString("cancel_date"));
				orderDto.setSuccessDate(this.rs.getString("success_date"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.errMsg = e.getMessage();
		}
		return orderDto;
	}

	// 메세지 리턴
	public String getMsg() {
		return this.errMsg;
	}
}
