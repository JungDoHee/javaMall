package servlet.web.member;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet({ "/shoppingMall/member/logout" })
public class logoutController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession();
		session.removeAttribute("user_no");
		session.removeAttribute("user_id");
		session.removeAttribute("user_name");
		res.sendRedirect(req.getContextPath() + "/index.jsp");
	}
}
