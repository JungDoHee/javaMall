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
import common.validator;
import model.item.ItemDAO;
import model.item.ItemDTO;
import model.item.categoryDAO;
import model.item.categoryDTO;

@WebServlet({ "/adminMall/item_manage/item_modify_form" })
public class itemModifyController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String strGoUrl = getServletContext().getContextPath() + "/adminMall/item_manage/list";
		try {
			HttpSession session = req.getSession();
			if (session.getAttribute("admin_no") == null || session.getAttribute("admin_no").equals("")) {
				throw new FileException("로그인 후 이용하시기 바랍니다", strGoUrl);
			}

			validator valid = new validator(req);
			if (valid.isEmpty("seq")) {
				throw new FileException("잘못된 경로로 접근했습니다", strGoUrl);
			}

			String strItemSeq = req.getParameter("seq");
			ItemDAO dao = new ItemDAO(getServletContext());
			List<ItemDTO> rgItemInfo = dao.getItemDetail(strItemSeq);

			categoryDAO categoryDao = new categoryDAO(getServletContext());
			List<categoryDTO> rgCategory = categoryDao.categoryList();

			req.setAttribute("rgItemInfo", rgItemInfo);
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
