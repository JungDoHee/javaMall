package servlet.web.member;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.encoding;
import common.regexpPattern;
import common.validator;
import model.member.MemberDAO;
import model.member.MemberDTO;
import util.random;

@WebServlet({ "/shoppingMall/member/join" })
public class joinController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
			// 필수 입력값 확인
			validator valid = new validator(req);
			String[] rgRequired = { "user_name", "user_email", "user_password", "join_agree", "user_password_confirm",
					"user_zip_code", "user_address", "user_phone" };
			for (String valid_check : rgRequired) {
				if (valid.isEmpty(valid_check)) {
					throw new Exception("필수 항목을 입력하시기 바랍니다");
				}
			}
			if (!req.getParameter("join_agree").equals("on")) {
				throw new Exception("약관 동의는 필수 사항입니다");
			}
			if (!req.getParameter("user_name").matches(regexpPattern.REGEXP_NAME)) {
				throw new Exception("이름은 한글, 영문 사용, 최소 2자, 최대 20자 형식으로 작성하시기 바랍니다");
			}
			if (!req.getParameter("user_email").matches(regexpPattern.REGEXP_EMAIL)) {
				throw new Exception("아이디는 이메일 형식으로 작성하시기 바랍니다");
			}
			if (!req.getParameter("user_password").matches(regexpPattern.REGEXP_PASSWORD)) {
				throw new Exception("비밀번호는 최소 8자, 최대 30자, 영문 대소문자, 숫자 포함하여 작성하시기 바랍니다");
			}
			if (!req.getParameter("user_password").equals(req.getParameter("user_password_confirm"))) {
				throw new Exception("비밀번호가 일치하지 않습니다");
			}
			String[] rgNumeric = { "user_phone", "user_zip_code" };
			for (String pattern_numeric : rgNumeric) {
				if (!req.getParameter(pattern_numeric).matches(regexpPattern.REGEXP_NUMERIC)) {
					throw new Exception(pattern_numeric + "을(를) 숫자 형식으로 입력하시기 바랍니다");
				}
			}

			MemberDTO dto = new MemberDTO();
			MemberDAO dao = new MemberDAO(getServletContext());
			encoding encodeSha = new encoding("SHA-256");
			String strRandomSalt = random.randomString(20);
			String strEncodePass = encodeSha.toEncode(req.getParameter("user_password") + strRandomSalt);

			encoding encodeAes = new encoding("AES");
			String strUserPhone = encodeAes.toBothEncode(req.getParameter("user_phone"));
			String strUserZipCode = encodeAes.toBothEncode(req.getParameter("user_zip_code"));
			String strUserAddress = encodeAes.toBothEncode(req.getParameter("user_address"));

			dto.setUserId(req.getParameter("user_email"));
			dto.setUserName(req.getParameter("user_name"));
			dto.setPasswordSalt(strRandomSalt);
			dto.setUserAddress(strUserAddress);
			dto.setUserPassword(strEncodePass);
			dto.setUserZipCode(strUserZipCode);
			dto.setUserPhone(strUserPhone);

			int nUserNo = dao.memberJoinProc(dto);

			if (nUserNo < 1) {
				String strMsg = dao.getMsg().isEmpty() ? "회원가입에 실패했습니다" : dao.getMsg();
				throw new Exception(strMsg);
			}

			res.sendRedirect(req.getContextPath() + "/shoppingMall/member/join_done.jsp");
		} catch (Exception e) {
			e.printStackTrace();
			HttpSession globalSession = req.getSession();
			globalSession.setAttribute("globalMsg", e.getMessage());
			req.getRequestDispatcher("/member/join.jsp").forward((ServletRequest) req, (ServletResponse) res);
		}
	}
}
