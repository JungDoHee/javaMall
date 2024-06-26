package servlet.web.member;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.validator;
import model.item.OrderDAO;
import model.item.OrderDTO;
import model.item.categoryDAO;
import model.item.categoryDTO;
import util.Pagination;

@WebServlet({ "/shoppingMall/member/my_order" })
public class myOrderController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private int PAGE_NO = 1;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
			HttpSession session = req.getSession();
			if (session.getAttribute("user_no") == null || session.getAttribute("user_no").equals("")) {
				throw new Exception("로그인 후 이용하시기 바랍니다");
			}

			String strUserNo = String.valueOf(session.getAttribute("user_no"));

			OrderDAO dao = new OrderDAO(getServletContext());

			// ▼ 페이징
			int nTotalCnt = dao.getOrderListCount(strUserNo);
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
			List<OrderDTO> orderItemList = dao.getOrderList(nStartPage, Pagination.DEFAULT_PER_PAGE, strUserNo);

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
		} catch (Exception e) {
			e.printStackTrace();
			HttpSession globalSession = req.getSession();
			globalSession.setAttribute("globalMsg", e.getMessage());
			res.sendRedirect(getServletContext().getContextPath() + "/shoppingMall");
		}
	}

	// 주문 취소
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
			// ▼ 필수값 확인
			validator valid = new validator(req);
			if (valid.isEmpty("order_seq") || !valid.isInt("order_seq") || valid.isEmpty("method_type")) {
				throw new Exception("잘못된 경로로 접근하였습니다");
			}
			if (!req.getParameter("method_type").equals("del")) {
				throw new Exception("잘못된 경로로 접근하였습니다");
			}
			// ▲ 필수값 확인

			// 로그인 확인
			HttpSession session = req.getSession();
			if (session.getAttribute("user_no") == null || session.getAttribute("user_no").equals("")) {
				throw new Exception("로그인 후 이용하시기 바랍니다");
			}
			String strUserNo = session.getAttribute("user_no").toString();
			String strOrderSeq = req.getParameter("order_seq");

			// 주문 취소 가능 상태인지 확인
			OrderDAO orderDao = new OrderDAO(getServletContext());
			OrderDTO rgOrderInfo = orderDao.getOrderInfo(strOrderSeq);
			if (rgOrderInfo.getOrderSeq() == null || !rgOrderInfo.getUserNo().equals(strUserNo)) {
				throw new Exception("주문 내역이 없습니다");
			}
			if (!rgOrderInfo.getOrderStatus().equals("a")) {
				throw new Exception("취소 가능한 상태가 아닙니다");
			}

			// 주문 취소 처리
			boolean bCancel = orderDao.itemOrderCancel(strOrderSeq);
			if (!bCancel) {
				throw new Exception(orderDao.getMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		res.sendRedirect(getServletContext().getContextPath() + "/shoppingMall/member/my_order");
	}
}
