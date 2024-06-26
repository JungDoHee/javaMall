package servlet.admin.category_manage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.item.categoryDAO;
import model.item.categoryDTO;
import util.Pagination;

@WebServlet({ "/adminMall/category_manage/list" })
public class categoryListController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private int PAGE_NO = 1;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		categoryDAO dao = new categoryDAO(getServletContext());

		// ▼ 페이징 시작
		// 전체 대상 건수 조회
		int nTotalCategory = dao.categoryCount();
		// 현재 페이지 설정
		if (req.getParameter("page_no") != null && !req.getParameter("page_no").equals("")
				&& Integer.parseInt(req.getParameter("page_no")) > 0) {
			this.PAGE_NO = Integer.parseInt(req.getParameter("page_no"));
		}
		int nStartPage = (this.PAGE_NO - 1) * Pagination.DEFAULT_PER_PAGE;
		Map<String, Integer> pageInfo = new HashMap<>();
		pageInfo.put("page_no", this.PAGE_NO);
		pageInfo.put("per_page", Pagination.DEFAULT_PER_PAGE);
		pageInfo.put("total_page", nTotalCategory);
		// 페이징 (디자인 포함) 처리
		Pagination page = new Pagination(pageInfo);
		page.setUrl(req.getRequestURL().toString());
		if (page.getLastPageNo() < nStartPage) {
			page.setPageNo(page.getLastPageNo());
		}
		// ▲ 페이징 종료

		List<categoryDTO> categoryList = dao.categoryList(nStartPage, Pagination.DEFAULT_PER_PAGE);
		req.setAttribute("rgList", categoryList);
		req.setAttribute("pagination", page.getPagination());
		req.getRequestDispatcher(req.getServletPath() + ".jsp").forward(req, res);
	}
}
