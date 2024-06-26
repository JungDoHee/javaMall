package servlet.admin.item_manage;

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

import model.item.ItemDAO;
import model.item.ItemDTO;
import model.item.categoryDAO;
import model.item.categoryDTO;
import util.scriptAlert;

@WebServlet({ "/adminMall/item_manage/list" })
public class ItemListController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
			ItemDAO dao = new ItemDAO(getServletContext());
			List<ItemDTO> itemList = dao.getItemList();

			categoryDAO categoryDao = new categoryDAO(getServletContext());
			List<categoryDTO> rgCategoryList = categoryDao.categoryList();
			Map<String, String> rgCategory = new HashMap<String, String>();
			for (categoryDTO category : rgCategoryList) {
				rgCategory.put(category.getCategorySeq(), category.getCategoryName());
			}

			String imageServer = req.getServletContext().getInitParameter("imageServer");

			req.setAttribute("rgList", itemList);
			req.setAttribute("strImageUrl", imageServer);
			req.setAttribute("rgCategory", rgCategory);
			req.getRequestDispatcher(req.getServletPath() + ".jsp").forward(req, res);
		} catch (Exception e) {
			String strGoUrl = getServletContext().getContextPath() + "/adminMall/";
			scriptAlert alert = new scriptAlert("서비스가 원활하지 않습니다", strGoUrl);
			res.setContentType("text/html; charset=UTF-8");
			PrintWriter out = res.getWriter();
			out.println(alert.toString());
			out.flush();
			out.close();
		}
	}
}
