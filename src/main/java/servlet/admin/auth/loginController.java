package servlet.admin.auth;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Exception.FileException;
import common.regexpPattern;
import common.userClient;
import common.validator;
import model.admin.auth.AdminDAO;
import model.admin.auth.AdminDTO;

@WebServlet({ "/adminMall/auth/login" })
public class loginController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String strGoUrl = req.getServletPath() + ".jsp";
		try {
			validator valid = new validator(req);
			if (valid.isEmpty("admin_id") || valid.isEmpty("admin_password")) {
				throw new FileException("필수값을 모두 입력하시기 바랍니다", strGoUrl);
			}
			String strAdminId = req.getParameter("admin_id");
			String strAdminPassword = req.getParameter("admin_password");
			if (!strAdminId.matches(regexpPattern.REGEXP_ID)
					|| !strAdminPassword.matches(regexpPattern.REGEXP_PASSWORD)) {
				throw new FileException("입력 형식을 다시 확인하시기 바랍니다", strGoUrl);
			}

			AdminDTO dto = new AdminDTO();
			dto.setAdminID(strAdminId);
			dto.setAdminPassword(strAdminPassword);
			dto.setUserIp(userClient.getClientIp(req));

			AdminDAO dao = new AdminDAO(getServletContext());
			AdminDTO loginDto = dao.loginProc(dto);

			if (loginDto.getAdminNo() == null || loginDto.getAdminNo().equals("")) {
				throw new FileException(dao.getMessage(), strGoUrl);
			}

			HttpSession session = req.getSession();
			session.setAttribute("admin_no", loginDto.getAdminNo());
			session.setAttribute("admin_name", loginDto.getAdminName());
			session.setAttribute("admin_id", loginDto.getAdminID());

			res.sendRedirect(getServletContext().getContextPath() + "/adminMall/");
		} catch (FileException e) {
			res.setContentType("text/html; charset=UTF-8");
			PrintWriter out = res.getWriter();
			out.println(e.toString());
			out.flush();
			out.close();
		}
	}
}