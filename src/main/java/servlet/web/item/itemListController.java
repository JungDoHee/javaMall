package servlet.web.item;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.item.ItemDTO;
import model.item.itemUserDAO;
import util.Pagination;

@WebServlet({ "/shoppingMall/item/list" })
public class itemListController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private int PAGE_NO = 1;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		itemUserDAO dao = new itemUserDAO(getServletContext());
		int nTotalItem = dao.getItemListCount();
		if (req.getParameter("page_no") != null && !req.getParameter("page_no").equals("")
				&& Integer.parseInt(req.getParameter("page_no")) > 0) {
			this.PAGE_NO = Integer.parseInt(req.getParameter("page_no"));
		}
		int nStartPage = (this.PAGE_NO - 1) * Pagination.DEFAULT_PER_PAGE;
		Map<String, Integer> pageInfo = new HashMap<>();
		pageInfo.put("page_no", this.PAGE_NO);
		pageInfo.put("per_page", Pagination.DEFAULT_PER_PAGE);
		pageInfo.put("total_page", nTotalItem);

		Pagination page = new Pagination(pageInfo);

		page.setUrl(req.getRequestURL().toString());

		if (page.getLastPageNo() < nStartPage) {
			page.setPageNo(page.getLastPageNo());
		}

		List<ItemDTO> itemList = dao.getItemList(nStartPage, Pagination.DEFAULT_PER_PAGE);

		String imageServer = req.getServletContext().getInitParameter("imageServer");

		req.setAttribute("strImageUrl", imageServer);
		req.setAttribute("rgItemList", itemList);
		req.setAttribute("pagination", page.getPagination());
		req.getRequestDispatcher(req.getServletPath() + ".jsp").forward(req, res);
	}
}
