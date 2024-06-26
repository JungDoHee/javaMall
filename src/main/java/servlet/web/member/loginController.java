package servlet.web.member;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.regexpPattern;
import common.userClient;
import common.validator;
import model.member.MemberDAO;
import model.member.MemberDTO;

@WebServlet({ "/shoppingMall/member/login" })
public class loginController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		System.out.println("loginController doGet");
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
			validator valid = new validator(req);
			if (valid.isEmpty("user_email") || valid.isEmpty("user_password")) {
				throw new Exception("필수 항목을 입력하시기 바랍니다");
			}
			if (!req.getParameter("user_email").matches(regexpPattern.REGEXP_EMAIL)) {
				throw new Exception("아이디는 이메일 형식으로 작성하시기 바랍니다");
			}
			if (!req.getParameter("user_password").matches(regexpPattern.REGEXP_PASSWORD)) {
				throw new Exception("비밀번호는 최소 8자, 최대 30자, 영문 대소문자, 숫자 포함하여 작성하시기 바랍니다");
			}

			MemberDTO dto = new MemberDTO();
			MemberDAO dao = new MemberDAO(getServletContext());
			dto.setUserId(req.getParameter("user_email"));
			dto.setUserPassword(req.getParameter("user_password"));
			dto.setUserIp(userClient.getClientIp(req));
			MemberDTO loginDto = dao.loginProc(dto);

			if (loginDto.getUserNo() == null || loginDto.getUserNo().equals("")) {
				throw new Exception("아이디 또는 비밀번호를 확인하세요");
			}

			HttpSession session = req.getSession();
			session.setAttribute("user_no", loginDto.getUserNo());
			session.setAttribute("user_name", loginDto.getUserName());
			session.setAttribute("user_id", loginDto.getUserId());

			res.sendRedirect(req.getContextPath() + "/index.jsp");
		} catch (Exception e) {
			e.printStackTrace();

			HttpSession globalSession = req.getSession();
			globalSession.setAttribute("globalMsg", e.getMessage());

			req.getRequestDispatcher(req.getServletPath() + ".jsp").forward(req, res);
		}
	}
}
