package servlet.admin.category_manage;

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
import model.item.categoryDAO;
import model.item.categoryDTO;
import util.dateFormat;

@WebServlet({ "/adminMall/category_manage/category_reg/*" })
public class categoryManageController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private HttpServletRequest req;

	private HttpServletResponse res;

	private String msg;

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String strGoUrl = getServletContext().getContextPath() + "/adminMall/category_manage/list.jsp";
		try {
			this.req = req;
			this.res = res;
			String[] rgUri = req.getRequestURI().split("/");
			String strLastUri = rgUri[rgUri.length - 1];
			switch (strLastUri) {
			case "delete":
				doDelete();
				break;
			case "modify":
				doModify();
				break;
			case "reg":
				doReg();
				break;
			default:
				throw new FileException("잘못된 경로로 접근했습니다", strGoUrl);
			}

		} catch (FileException e) {
			res.setContentType("text/html; charset=UTF-8");
			PrintWriter out = res.getWriter();
			out.println(e.toString());
			out.flush();
			out.close();
		}
	}

	// 분류 등록
	protected void doReg() throws IOException {
		int nCategorySeq = 0;
		try {
			// 유효성 검사
			HttpSession session = this.req.getSession();
			if (session.getAttribute("admin_no") == null || session.getAttribute("admin_no").equals("")) {
				throw new FileException("로그인 후 이용하시기 바랍니다");
			}
			validator valid = new validator(this.req);
			if (valid.isEmpty("category_name")) {
				throw new FileException("카테고리명을 입력하시기 바랍니다");
			}

			// 변수 담기
			categoryDTO rgCategoryInfo = new categoryDTO();
			rgCategoryInfo.setCategoryCode("0");
			rgCategoryInfo.setCategoryName(this.req.getParameter("category_name"));
			rgCategoryInfo.setRegAdminNo(session.getAttribute("admin_no").toString());

			// DB 저장
			categoryDAO dao = new categoryDAO(getServletContext());
			nCategorySeq = dao.insertCategory(rgCategoryInfo);

			if (nCategorySeq < 1) {
				throw new FileException(dao.getMsg());
			}
		} catch (FileException e) {
			e.printStackTrace();
			HttpSession globalSession = this.req.getSession();
			globalSession.setAttribute("globalMsg", e.getMessage());
		}
		this.res.sendRedirect(getServletContext().getContextPath() + "/adminMall/category_manage/list");
	}

	// 분류 수정
	protected void doModify() throws IOException {
		int nCategorySeq = 0;
		try {
			// 유효성 검사
			HttpSession session = this.req.getSession();
			if (session.getAttribute("admin_no") == null || session.getAttribute("admin_no").equals("")) {
				throw new Exception("로그인 후 이용하시기 바랍니다");
			}
			validator valid = new validator(this.req);
			if (valid.isEmpty("category_name") || valid.isEmpty("category_seq")) {
				throw new Exception("카테고리명을 입력하시기 바랍니다");
			}

			categoryDTO dto = new categoryDTO();
			dto.setCategorySeq(req.getParameter("category_seq"));

			categoryDAO dao = new categoryDAO(getServletContext());

			// 조건 검색
			dao.setSelectCategoryWhere(dto);
			List<categoryDTO> categoryInfo = dao.categoryList();
			if (categoryInfo.isEmpty()) {
				throw new Exception("해당 카테고리가 존재하지 않습니다");
			}

			// 변수 담기
			categoryDTO rgCategoryInfo = new categoryDTO();
			rgCategoryInfo.setCategoryName(this.req.getParameter("category_name"));
			rgCategoryInfo.setModAdminNo(session.getAttribute("admin_no").toString());
			rgCategoryInfo.setModDate(dateFormat.getNow());
			rgCategoryInfo.setCategorySeq(this.req.getParameter("category_seq"));

			// DB 저장
			nCategorySeq = dao.updateCategory(rgCategoryInfo);

			if (nCategorySeq < 1) {
				throw new Exception(dao.getMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			HttpSession globalSession = this.req.getSession();
			globalSession.setAttribute("globalMsg", e.getMessage());
		}
		this.res.sendRedirect(getServletContext().getContextPath() + "/adminMall/category_manage/list");
	}

	// 분류 삭제
	protected void doDelete() throws IOException, ServletException {
		try {
			// 유효성 검사
			HttpSession session = this.req.getSession();
			if (session.getAttribute("admin_no") == null || session.getAttribute("admin_no").equals("")) {
				throw new Exception("로그인 후 이용하시기 바랍니다");
			}
			validator valid = new validator(this.req);
			if (valid.isEmpty("category_seq")) {
				throw new Exception("카테고리명를 선택하시기 바랍니다");
			}

			// 카테고리 존재 여부 조회
			categoryDTO dto = new categoryDTO();
			dto.setCategorySeq(req.getParameter("category_seq"));

			categoryDAO dao = new categoryDAO(getServletContext());

			dao.setSelectCategoryWhere(dto);
			List<categoryDTO> categoryInfo = dao.categoryList();
			if (categoryInfo.isEmpty()) {
				throw new Exception("해당 카테고리가 존재하지 않습니다");
			}

			// 카테고리 삭제
			boolean bDelete = dao.deleteCategory(req.getParameter("category_seq"));
			if (!bDelete) {
				throw new Exception(dao.getMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();

			HttpSession globalSession = this.req.getSession();
			globalSession.setAttribute("globalMsg", e.getMessage());
		}

		this.res.sendRedirect(getServletContext().getContextPath() + "/adminMall/category_manage/list");
	}

	public String getMsg() {
		return this.msg;
	}

}
