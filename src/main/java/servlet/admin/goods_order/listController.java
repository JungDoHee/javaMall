package servlet.admin.goods_order;

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
import model.admin.order.GoodsOrderDAO;
import model.item.OrderDAO;
import model.item.OrderDTO;
import model.item.categoryDAO;
import model.item.categoryDTO;
import util.Pagination;
import util.dateFormat;

@WebServlet({ "/adminMall/goods_order/list" })
public class listController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private int PAGE_NO = 1;

	// 주문 내역
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String strGoUrl = getServletContext().getContextPath() + "/adminMall";
		try {
			HttpSession session = req.getSession();
			if (session.getAttribute("admin_no") == null || session.getAttribute("admin_no").equals("")) {
				throw new FileException("로그인 후 이용하시기 바랍니다", strGoUrl);
			}

			GoodsOrderDAO dao = new GoodsOrderDAO(getServletContext());

			// ▼ 페이징
			int nTotalCnt = dao.getOrderListCount();
			if (req.getParameter("page_no") != null && !req.getParameter("page_no").equals("")
					&& Integer.parseInt(req.getParameter("page_no")) > 0) {
				this.PAGE_NO = Integer.parseInt(req.getParameter("page_no"));
			}

			// 페이징 변수 세팅
			int nStartPage = (this.PAGE_NO - 1) * Pagination.DEFAULT_PER_PAGE;
			Map<String, Integer> pageInfo = new HashMap<>();
			pageInfo.put("page_no", this.PAGE_NO);
			pageInfo.put("per_page", Pagination.DEFAULT_PER_PAGE);
			pageInfo.put("total_page", nTotalCnt);

			Pagination page = new Pagination(pageInfo);

			page.setUrl(req.getRequestURL().toString());

			if (page.getLastPageNo() < nStartPage) {
				page.setPageNo(page.getLastPageNo());
			}
			// ▲ 페이징

			// 페이징 포함 데이터 조회
			List<OrderDTO> orderItemList = dao.getOrderList(nStartPage, Pagination.DEFAULT_PER_PAGE);

			// 이미지 경로 호출
			String imageServer = req.getServletContext().getInitParameter("imageServer");

			// 카테고리 조회
			categoryDAO categoryDao = new categoryDAO(getServletContext());
			List<categoryDTO> rgCategoryList = categoryDao.categoryList();
			Map<String, String> rgCategory = new HashMap<String, String>();
			for (categoryDTO category : rgCategoryList) {
				rgCategory.put(category.getCategorySeq(), category.getCategoryName());
			}

			// 주문 상태명 조회를 위한 호출
			OrderDTO orderDto = new OrderDTO();

			req.setAttribute("rgList", orderItemList);
			req.setAttribute("strImageUrl", imageServer);
			req.setAttribute("rgCategory", rgCategory);
			req.setAttribute("rgOrderStatusInfo", orderDto.getOrderStatusKor());
			req.setAttribute("pagination", page.getPagination());
			req.getRequestDispatcher(req.getServletPath() + ".jsp").forward(req, res);
		} catch (FileException e) {
			res.setContentType("text/html; charset=UTF-8");
			PrintWriter out = res.getWriter();
			out.println(e.toString());
			out.flush();
			out.close();
		}
	}

	// 주문 상태 변경 (ajax)
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
			req.setCharacterEncoding("utf-8");
			res.setContentType("application/x-json; charset=utf-8");

			// 로그인 확인
			HttpSession session = req.getSession();
			if (session.getAttribute("admin_no") == null || session.getAttribute("admin_no").equals("")) {
				throw new Exception("로그인 후 이용하시기 바랍니다");
			}

			// ▼ 필수값 확인
			validator valid = new validator(req);
			if (valid.isEmpty("order_seq") || !valid.isInt("order_seq") || valid.isEmpty("order_status")) {
				throw new Exception("잘못된 경로로 접근하였습니다");
			}
			// ▲ 필수값 확인

			// 변수 선언
			String strOrderSeq = req.getParameter("order_seq");
			String strOrderStatus = req.getParameter("order_status");

			OrderDTO orderDto = new OrderDTO();

			// 상태값 확인
			orderDto.getOrderStatusKor();
			if (!orderDto.getOrderStatusKor().containsKey(strOrderStatus)) {
				throw new Exception("상태값이 잘못되었습니다");
			}

			// 해당 주문의 상태값 확인
			OrderDAO orderDao = new OrderDAO(getServletContext());
			OrderDTO rgOderInfo = orderDao.getOrderInfo(strOrderSeq);
			if (rgOderInfo.getOrderSeq() == null) {
				throw new Exception("해당 주문건은 존재하지 않습니다");
			}
			if (rgOderInfo.getOrderStatus().equals(strOrderStatus)) {
				throw new Exception("동일한 상태로 변경할 수 없습니다");
			}

			// 주문 상태 변경
			orderDto.setOrderSeq(strOrderSeq);
			orderDto.setOrderStatus(strOrderStatus);
			switch (strOrderStatus) {
			case "s":
				orderDto.setSuccessDate(dateFormat.getNow());
				break;
			case "d":
				orderDto.setDeliveryDate(dateFormat.getNow());
				break;
			case "g":
				orderDto.setCancelDate(dateFormat.getNow());
				break;
			}
			Map<String, Integer> rgUpdateStatus = orderDao.updateOrderStatus(orderDto);
			if (rgUpdateStatus.isEmpty() || !rgUpdateStatus.containsKey("order_seq")) {
				throw new Exception("상태 변경에 실패했습니다");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
