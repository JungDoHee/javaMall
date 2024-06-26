package servlet.admin.item_manage;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Exception.FileException;
import model.item.categoryDAO;
import model.item.categoryDTO;

@WebServlet({ "/adminMall/item_manage/item_reg_form" })
public class itemRegController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String strGoUrl = getServletContext().getContextPath() + "/adminMall/item_manage/list";
		try {
			HttpSession session = req.getSession();
			if (session.getAttribute("admin_no") == null || session.getAttribute("admin_no").equals("")) {
				throw new FileException("로그인 후 이용하시기 바랍니다", strGoUrl);
			}

			categoryDAO categoryDao = new categoryDAO(getServletContext());
			List<categoryDTO> rgCategory = categoryDao.categoryList();

			if (rgCategory.get(0).getCategorySeq().isEmpty()) {
				throw new FileException("카테고리가 조회되지 않았습니다. 잠시 후 다시 이용하시기 바랍니다", strGoUrl);
			}

			req.setAttribute("rgCategory", rgCategory);
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
