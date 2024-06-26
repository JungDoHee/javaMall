package model.item;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import common.dbConnection;

public class OrderDAO extends dbConnection {
	private String errMsg;

	public OrderDAO(ServletContext application) {
		super(application);
	}

	// 주문 처리
	public Map<String, Integer> itemOrder(OrderDTO orderInfo, PaymentDTO paymentInfo) {
		Map<String, Integer> rgOrderResult = new HashMap<>();
		try {
			this.con.setAutoCommit(false);

			// 주문 내역 insert
			String rstInsertOrderList = "INSERT INTO item_order_list ";
			rstInsertOrderList += " (item_seq, user_no, origin_price, unit_price, discounted_price, payment_price, amount) ";
			rstInsertOrderList += " VALUES ";
			rstInsertOrderList += " (?, ?, ?, ?, ?, ?, ?) ";
			// 결과 PK 받기
			this.pstmt = this.con.prepareStatement(rstInsertOrderList, 1);
			this.pstmt.setString(1, orderInfo.getItemSeq());
			this.pstmt.setString(2, orderInfo.getUserNo());
			this.pstmt.setString(3, orderInfo.getOriginPrice());
			this.pstmt.setString(4, orderInfo.getUnitPrice());
			this.pstmt.setString(5, orderInfo.getDiscountedPrice());
			this.pstmt.setString(6, orderInfo.getPaymentPrice());
			this.pstmt.setString(7, orderInfo.getAmount());

			int nOrderResult = this.pstmt.executeUpdate();
			if (nOrderResult < 1) {
				throw new Exception("주문 처리에 실패했습니다");
			}

			// 주문 PK 가져오기
			this.rs = this.pstmt.getGeneratedKeys();
			if (!this.rs.next()) {
				throw new Exception("주문 처리에 실패했습니다(2)");
			}
			rgOrderResult.put("order_seq", this.rs.getInt(1));

			// 결제 내역 insert
			String rstPaymentList = "INSERT INTO payment_list ";
			rstPaymentList += " (item_seq, order_seq, user_no, payment_price) ";
			rstPaymentList += " VALUES ";
			rstPaymentList += " (?, ?, ?, ?) ";
			// 결과 PK 받기
			this.pstmt = this.con.prepareStatement(rstPaymentList, 1);
			this.pstmt.setString(1, paymentInfo.getItemSeq());
			this.pstmt.setString(2, String.valueOf(rgOrderResult.get("order_seq")));
			this.pstmt.setString(3, paymentInfo.getUserNo());
			this.pstmt.setString(4, paymentInfo.getPaymentPrice());

			int nPaymentResult = this.pstmt.executeUpdate();
			if (nPaymentResult < 1) {
				throw new Exception("결제 처리에 실패했습니다");
			}

			// 결제 PK 가져오기
			this.rs = this.pstmt.getGeneratedKeys();
			if (!this.rs.next()) {
				throw new Exception("결제 처리에 실패했습니다(2)");
			}
			rgOrderResult.put("payment_seq", this.rs.getInt(1));
			this.con.commit();
		} catch (Exception e) {
			e.printStackTrace();
			this.errMsg = e.getMessage();
			rgOrderResult.clear();

			try {
				this.con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return rgOrderResult;
	}

	// 주문한 상품 총 수량 조회
	public int getOrderListCount(String strUSerNo) {
		int nTotal = 0;
		try {
			String rstOrderList = "SELECT count(seq) AS total_count ";
			rstOrderList += " FROM item_order_list ";
			rstOrderList += " WHERE user_no = ? ";
			this.pstmt = this.con.prepareStatement(rstOrderList);
			this.pstmt.setString(1, strUSerNo);
			this.rs = this.pstmt.executeQuery();
			this.rs.next();
			nTotal = this.rs.getInt("total_count");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nTotal;
	}

	// 주문한 상품 목록 조회
	public List<OrderDTO> getOrderList(int nStartPage, int nPerPage, String strUserNo) {
		List<OrderDTO> rgOrderList = new ArrayList<>();
		try {
			String rstOrderList = "SELECT ";
			rstOrderList += " order_item.seq AS order_seq, order_item.order_status, order_item.origin_price, ";
			rstOrderList += " order_item.unit_price, order_item.discounted_price, order_item.payment_price, order_item.amount, ";
			rstOrderList += " order_item.order_date, order_item.delivery_date, order_item.cancel_date, order_item.success_date, ";
			rstOrderList += " item.thumb_name, item.category_seq, item.item_subject ";
			rstOrderList += " FROM item_order_list AS order_item ";
			rstOrderList += " LEFT JOIN item_list AS item ";
			rstOrderList += " ON order_item.item_seq = item.seq ";
			rstOrderList += " WHERE order_item.user_no = ? ";
			rstOrderList += " ORDER BY order_item.seq DESC ";
			rstOrderList += " OFFSET ? ROWS ";
			rstOrderList += " FEtCH NEXT ? ROWS ONLY ";

			this.pstmt = this.con.prepareStatement(rstOrderList);
			this.pstmt.setString(1, strUserNo);
			this.pstmt.setInt(2, nStartPage);
			this.pstmt.setInt(3, nPerPage);
			this.rs = this.pstmt.executeQuery();
			while (this.rs.next()) {
				OrderDTO dto = new OrderDTO();
				dto.setOrderSeq(this.rs.getString("order_seq"));
				dto.setOrderStatus(this.rs.getString("order_status"));
				dto.setOriginPrice(this.rs.getString("origin_price"));
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

	// 주문서 상태 변경
	public boolean setOrderStatus(OrderDTO updateOrderDto) {
		boolean bResult = false;
		try {
			// 주문 내역 취소
			String rstUpdateOrder = "UPDATE item_order_list SET ";
			rstUpdateOrder += " order_status = ? ";
			if (updateOrderDto.getDeliveryDate() != null) {
				rstUpdateOrder += " , delivery_date = '" + updateOrderDto.getDeliveryDate() + "' ";
			}
			if (updateOrderDto.getCancelDate() != null) {
				rstUpdateOrder += " , cancel_date = '" + updateOrderDto.getCancelDate() + "' ";
			}
			if (updateOrderDto.getSuccessDate() != null) {
				rstUpdateOrder += " , success_date = '" + updateOrderDto.getSuccessDate() + "' ";
			}
			rstUpdateOrder += " WHERE seq = " + updateOrderDto.getOrderSeq() + " ";
			this.pstmt = this.con.prepareStatement(rstUpdateOrder);
			this.pstmt.setString(1, updateOrderDto.getOrderStatus());
			if (this.pstmt.executeUpdate() < 1) {
				throw new Exception("주문서 상태 변경 실패");
			}

			bResult = true;
		} catch (Exception e) {
			e.printStackTrace();
			this.errMsg = e.getMessage();

			try {
				this.con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return bResult;
	}

	// 주문 취소 처리
	public boolean itemOrderCancel(String strOrderSeq) {
		boolean bCancelResult = false;
		try {
			String strCancelType = "g";

			this.con.setAutoCommit(false);

			// 주문 내역 취소
			OrderDTO rgSetOrderStatus = new OrderDTO();
			rgSetOrderStatus.setOrderStatus(strCancelType);
			rgSetOrderStatus.setOrderSeq(strOrderSeq);
			boolean bUpdateStatus = setOrderStatus(rgSetOrderStatus);
			if (!bUpdateStatus) {
				throw new Exception("주문 취소 실패했습니다");
			}

			// 결제 내역 취소
			String rstUpdatePaymentCancel = "UPDATE payment_list SET ";
			rstUpdatePaymentCancel += " payment_status = ? ";
			rstUpdatePaymentCancel += " WHERE order_seq = ? ";
			this.pstmt = this.con.prepareStatement(rstUpdatePaymentCancel);
			this.pstmt.setString(1, strCancelType);
			this.pstmt.setString(2, strOrderSeq);
			if (this.pstmt.executeUpdate() < 1) {
				throw new Exception("결제 취소 실패했습니다");
			}

			bCancelResult = true;
			this.con.commit();
		} catch (Exception e) {
			e.printStackTrace();
			this.errMsg = e.getMessage();

			try {
				this.con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return bCancelResult;
	}

	// 주문 상태 변경
	public Map<String, Integer> updateOrderStatus(OrderDTO updateOrderDto) {
		Map<String, Integer> rgResult = new HashMap<String, Integer>();
		try {
			this.con.setAutoCommit(false);

			// 주문 정보 조회
			OrderDTO rgOrderInfo = getOrderInfo(updateOrderDto.getOrderSeq());

			// 주문 상태 변경
			boolean bUpdateStatus = setOrderStatus(updateOrderDto);
			if (!bUpdateStatus) {
				throw new Exception(getMsg());
			}

			// 주문 상태 변경 성공 시 주문PK 세팅
			rgResult.put("order_seq", Integer.parseInt(updateOrderDto.getOrderSeq()));

			// 주문 완료 처리
			if (updateOrderDto.getOrderStatus().equals("s")) {
				String dtDelivery = updateOrderDto.getDeliveryDate().isEmpty() ? rgOrderInfo.getDeliveryDate()
						: updateOrderDto.getDeliveryDate();
				String dtCancel = updateOrderDto.getCancelDate().isEmpty() ? rgOrderInfo.getCancelDate()
						: updateOrderDto.getCancelDate();
				String dtSuccess = updateOrderDto.getSuccessDate().isEmpty() ? rgOrderInfo.getSuccessDate()
						: updateOrderDto.getSuccessDate();

				// 주문 완료인 경우 완료 테이블 입력
				String rstInsertSuccess = "INSERT INTO order_success_list ";
				rstInsertSuccess += " (order_seq, item_seq, user_no, order_status, origin_price, unit_price, discounted_price, payment_price, amount, order_date, delivery_date, cancel_date, success_date) ";
				rstInsertSuccess += " VALUES ";
				rstInsertSuccess += " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
				this.pstmt = this.con.prepareStatement(rstInsertSuccess, 1);
				this.pstmt.setString(1, updateOrderDto.getOrderSeq());
				this.pstmt.setString(2, rgOrderInfo.getItemSeq());
				this.pstmt.setString(3, rgOrderInfo.getUserNo());
				this.pstmt.setString(4, updateOrderDto.getOrderStatus());
				this.pstmt.setString(5, rgOrderInfo.getOriginPrice());
				this.pstmt.setString(6, rgOrderInfo.getUnitPrice());
				this.pstmt.setString(7, rgOrderInfo.getDiscountedPrice());
				this.pstmt.setString(8, rgOrderInfo.getPaymentPrice());
				this.pstmt.setString(9, rgOrderInfo.getAmount());
				this.pstmt.setString(10, rgOrderInfo.getOrderDate());
				this.pstmt.setString(11, dtDelivery);
				this.pstmt.setString(12, dtCancel);
				this.pstmt.setString(13, dtSuccess);
				if (this.pstmt.executeUpdate() < 1) {
					throw new Exception("주문 완료 처리 실패");
				}

				// 주문 완료 테이블의 PK 세팅
				this.rs = this.pstmt.getGeneratedKeys();
				if (!this.rs.next()) {
					throw new Exception("주문 완료 처리 실패했습니다(2)");
				}
				rgResult.put("success_seq", this.rs.getInt(1));
			}

			this.con.commit();
		} catch (Exception e) {
			e.printStackTrace();
			this.errMsg = e.getMessage();

			try {
				this.con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		return rgResult;
	}

	public String getMsg() {
		return this.errMsg;
	}
}
