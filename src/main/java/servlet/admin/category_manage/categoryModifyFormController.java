package servlet.admin.category_manage;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Exception.FileException;
import common.validator;
import model.item.categoryDAO;
import model.item.categoryDTO;

@WebServlet({ "/adminMall/category_manage/category_modify_form" })
public class categoryModifyFormController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String strGoUrl = getServletContext().getContextPath() + "/adminMall/category_manage/list";
		try {
			validator valid = new validator(req);
			if (valid.isEmpty("category_seq")) {
				throw new FileException("잘못된 경로로 접근했습니다", strGoUrl);
			}

			categoryDTO dto = new categoryDTO();
			dto.setCategorySeq(req.getParameter("category_seq"));

			categoryDAO dao = new categoryDAO(getServletContext());

			// 조건 검색
			dao.setSelectCategoryWhere(dto);
			List<categoryDTO> categoryInfo = dao.categoryList();
			if (categoryInfo.isEmpty()) {
				throw new FileException("해당 카테고리가 존재하지 않습니다", strGoUrl);
			}

			req.setAttribute("rgCategoryInfo", categoryInfo.get(0));
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
