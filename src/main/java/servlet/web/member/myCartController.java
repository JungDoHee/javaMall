package servlet.web.member;

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
import model.item.ItemDTO;
import model.item.itemUserDAO;
import util.Pagination;

@WebServlet({ "/shoppingMall/member/my_cart" })
public class myCartController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private int PAGE_NO = 1;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String strGoUrl = getServletContext().getContextPath() + "/shoppingMall";
		try {
			HttpSession session = req.getSession();
			if (session.getAttribute("user_no") == null || session.getAttribute("user_no").equals("")) {
				throw new FileException("로그인 후 이용하시기 바랍니다", strGoUrl);
			}

			String strUserNo = String.valueOf(session.getAttribute("user_no"));

			itemUserDAO dao = new itemUserDAO(getServletContext());

			// ▼ 페이징
			int nTotalCnt = dao.getCartListCount(strUserNo);
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
			List<ItemDTO> cartItemList = dao.getCartList(nStartPage, Pagination.DEFAULT_PER_PAGE, strUserNo);

			// 이미지 경로 호출
			String imageServer = req.getServletContext().getInitParameter("imageServer");

			req.setAttribute("rgList", cartItemList);
			req.setAttribute("strImageUrl", imageServer);
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
}
