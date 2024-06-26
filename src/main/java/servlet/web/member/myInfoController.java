package servlet.web.member;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.encoding;
import model.member.MemberDAO;
import model.member.MemberDTO;

@WebServlet({ "/shoppingMall/member/my_info" })
public class myInfoController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
			HttpSession session = req.getSession();
			if (session.getAttribute("user_no") == null || session.getAttribute("user_no").equals("")) {
				throw new Exception("로그인 후 이용하시기 바랍니다");
			}

			MemberDAO dao = new MemberDAO(getServletContext());

			// 회원 정보 조회
			MemberDTO dto = dao.getMemberInfo(session.getAttribute("user_no").toString());

			// 암호화 컬럼 복호화
			encoding encodeAes = new encoding("AES");
			String strUserPhone = encodeAes.toBothDecode(dto.getUserPhone());
			String strUserZipCode = encodeAes.toBothDecode(dto.getUserZipCode());
			String strUserAddress = encodeAes.toBothDecode(dto.getUserAddress());

			dto.setUserPhone(strUserPhone);
			dto.setUserZipCode(strUserZipCode);
			dto.setUserAddress(strUserAddress);

			req.setAttribute("rgList", dto);
			req.getRequestDispatcher(req.getServletPath() + ".jsp").forward(req, res);
		} catch (Exception e) {
			e.printStackTrace();
			HttpSession globalSession = req.getSession();
			globalSession.setAttribute("globalMsg", e.getMessage());
			res.sendRedirect(getServletContext().getContextPath() + "/");
		}
	}
}
