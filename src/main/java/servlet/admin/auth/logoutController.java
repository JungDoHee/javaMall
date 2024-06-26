package servlet.admin.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet({ "/adminMall/auth/logout" })
public class logoutController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession();
		session.removeAttribute("admin_id");
		session.removeAttribute("admin_no");
		session.removeAttribute("admin_name");

		res.sendRedirect(getServletContext().getContextPath() + "/adminMall/");
	}
}