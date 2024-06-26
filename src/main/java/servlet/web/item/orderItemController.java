package servlet.web.item;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Exception.FileException;
import common.validator;
import model.item.ItemDTO;
import model.item.OrderDAO;
import model.item.OrderDTO;
import model.item.PaymentDTO;
import model.item.StockDTO;
import model.item.itemUserDAO;

@WebServlet({ "/shoppingMall/item/order" })
public class orderItemController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// 물품 주문
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String strGoUrl = req.getContextPath() + "/shoppingMall/item/list";
		try {
			// ▼ 유효성 검사
			validator valid = new validator(req);
			String[] rgRequiredNum = { "item_seq", "order_amount", "origin_price", "payment_price" };
			for (String valid_check : rgRequiredNum) {
				if (valid.isEmpty(valid_check) || !valid.isInt(valid_check)) {
					throw new FileException("필수값이 누락되었거나 입력 형식이 잘못되었습니다", strGoUrl);
				}
			}

			HttpSession session = req.getSession();
			if (session.getAttribute("user_no") == null || session.getAttribute("user_no").equals("")) {
				throw new FileException("로그인 후 이용하시기 바랍니다", strGoUrl);
			}
			// ▲ 유효성 검사

			// 형변환
			String strUserNo = session.getAttribute("user_no").toString();
			Map<String, String> rgParam = new HashMap<>();
			for (String strParamKey : rgRequiredNum) {
				rgParam.put(strParamKey, req.getParameter(strParamKey));
			}

			itemUserDAO itemDao = new itemUserDAO(getServletContext());

			// 물품 정보 조회
			List<ItemDTO> rgItemInfo = itemDao.getItemDetail(rgParam.get("item_seq"));
			rgItemInfo.get(0).getItemSeq();
			if (rgItemInfo.get(0).getItemSeq() == null || rgItemInfo.get(0).getItemSeq().equals("")) {
				throw new FileException("해당 물품은 주문할 수 없습니다", strGoUrl);
			}

			// 재고 조회
			StockDTO rgStock = itemDao.getStock(rgParam.get("item_seq"));
			if (rgStock.getItemSeq() == null || rgStock.getItemSeq().equals("")) {
				throw new FileException(itemDao.getMsg(), strGoUrl);
			}
			if (Integer.parseInt(rgStock.getRemainStock()) < 1) {
				throw new FileException("재고가 없습니다", strGoUrl);
			}

			// 총 금액 비교
			int nDiscountedPrice = 0;
			int nItemPrice = Integer.parseInt(rgItemInfo.get(0).getItemPrice());
			int nPaymentPrice = Integer.parseInt(rgParam.get("payment_price"));
			int nOrderAmount = Integer.parseInt(rgParam.get("order_amount"));
			int nFinalPaymentPrice = (nItemPrice * nOrderAmount) - nDiscountedPrice;
			if (nPaymentPrice != nFinalPaymentPrice) {
				throw new FileException("총 결제 금액을 다시 확인해주시기 바랍니다", strGoUrl);
			}

			// 주문내역 정보 생성
			OrderDTO orderInfo = new OrderDTO();
			orderInfo.setItemSeq(rgParam.get("item_seq"));
			orderInfo.setUserNo(strUserNo);
			orderInfo.setOriginPrice(rgItemInfo.get(0).getItemPrice());
			orderInfo.setUnitPrice(rgItemInfo.get(0).getItemPrice());
			orderInfo.setDiscountedPrice("0");
			orderInfo.setPaymentPrice(String.valueOf(nFinalPaymentPrice));
			orderInfo.setAmount(rgParam.get("order_amount"));

			// 결제 내역 정보 생성
			PaymentDTO paymentInfo = new PaymentDTO();
			paymentInfo.setItemSeq(rgParam.get("item_seq"));
			paymentInfo.setUserNo(strUserNo);
			paymentInfo.setPaymentPrice(String.valueOf(nFinalPaymentPrice));

			// 주문 처리
			OrderDAO orderDao = new OrderDAO(getServletContext());
			Map<String, Integer> orderProcResult = orderDao.itemOrder(orderInfo, paymentInfo);
			if (orderProcResult.isEmpty()) {
				throw new FileException(orderDao.getMsg(), strGoUrl);
			}

			res.sendRedirect(req.getContextPath() + "/shoppingMall/member/my_order");
		} catch (FileException e) {
			res.setContentType("text/html; charset=UTF-8");
			PrintWriter out = res.getWriter();
			out.println(e.toString());
			out.flush();
			out.close();
		}
	}
}
