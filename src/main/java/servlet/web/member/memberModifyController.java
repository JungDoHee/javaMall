package servlet.web.member;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
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

@WebServlet({ "/shoppingMall/member/modify/*" })
public class memberModifyController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private HttpServletRequest req;

	private HttpServletResponse res;

	private String msg;

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try {
			this.req = req;
			this.res = res;
			String[] rgUri = req.getRequestURI().split("/");
			String strLastUri = rgUri[rgUri.length - 1];

			switch (strLastUri) {
			case "mod_my_info":
				modMyInfo();
				break;
			case "mod_my_password":
				modMyPassword();
				break;
			default:
				throw new Exception("잘못된 경로로 접근했습니다");
			}

		} catch (Exception e) {
			e.printStackTrace();
			HttpSession globalSession = req.getSession();
			globalSession.setAttribute("globalMsg", getMsg());
			res.sendRedirect(getServletContext().getContextPath() + "/shoppingMall/");
		}
	}

	protected void modMyInfo() throws IOException, ServletException {
		try {
			// 로그인 확인
			HttpSession session = this.req.getSession();
			if (session.getAttribute("user_no") == null || session.getAttribute("user_no").equals("")) {
				throw new Exception("로그인 후 이용하시기 바랍니다");
			}

			validator valid = new validator(this.req);
			String[] rgRequired = { "user_name", "user_email", "user_phone", "user_zip_code", "user_address" };
			for (String valid_check : rgRequired) {
				if (valid.isEmpty(valid_check)) {
					throw new Exception("필수값을 모두 입력하시기 바랍니다");
				}
			}

			String[] rgNumeric = { "user_phone", "user_zip_code" };
			for (String pattern_numeric : rgNumeric) {
				if (!req.getParameter(pattern_numeric).matches(regexpPattern.REGEXP_NUMERIC)) {
					throw new Exception(pattern_numeric + "을(를) 숫자 형식으로 입력하시기 바랍니다");
				}
			}

			MemberDAO dao = new MemberDAO(getServletContext());

			// 현재 회원 정보 조회
			MemberDTO originDto = dao.getMemberInfo(session.getAttribute("user_no").toString());

			// 비교 대상 암호화 컬럼 복호화
			encoding encodeAes = new encoding("AES");
			originDto.setUserPhone(encodeAes.toBothDecode(this.req.getParameter("user_phone")));
			originDto.setUserZipCode(encodeAes.toBothDecode(this.req.getParameter("user_zip_code")));
			originDto.setUserAddress(encodeAes.toBothDecode(this.req.getParameter("user_address")));

			Map<String, String> rgOriginCompare = new HashMap<String, String>();
			rgOriginCompare.put("user_name", originDto.getUserName());
			rgOriginCompare.put("user_email", originDto.getUserId());
			rgOriginCompare.put("user_phone", originDto.getUserPhone());
			rgOriginCompare.put("user_zip_code", originDto.getUserZipCode());
			rgOriginCompare.put("user_address", originDto.getUserAddress());

			boolean bUpdate = false;
			for (String strKey : rgOriginCompare.keySet()) {
				if (!this.req.getParameter(strKey).equals(rgOriginCompare.get(strKey))) {
					bUpdate = true;
					break;
				}
			}

			if (!bUpdate) {
				throw new Exception("변경 정보가 기존 정보와 일치합니다");
			}

			// 회원 정보 변경 처리를 위한 DTO 할당
			MemberDTO changeDto = new MemberDTO();
			changeDto.setUserNo(session.getAttribute("user_no").toString());
			changeDto.setUserPhone(encodeAes.toBothEncode(this.req.getParameter("user_phone")));
			changeDto.setUserZipCode(encodeAes.toBothEncode(this.req.getParameter("user_zip_code")));
			changeDto.setUserAddress(encodeAes.toBothEncode(this.req.getParameter("user_address")));
			changeDto.setUserName(this.req.getParameter("user_name"));

			// 회원 정보 변경 처리
			if (!this.req.getParameter("user_name").equals(originDto.getUserName())) {
				boolean bGeneralUpdate = dao.modifyGeneralMemberInfo(changeDto);
				if (!bGeneralUpdate) {
					throw new Exception(dao.getMsg());
				}
			}

			// 회원 암호화 정보 변경 처리
			boolean bPrivateUpdate = dao.modifyPrivateMemberInfo(changeDto);
			if (!bPrivateUpdate) {
				throw new Exception(dao.getMsg());
			}

		} catch (Exception e) {
			this.msg = e.getMessage();
			e.printStackTrace();
			res.sendRedirect(getServletContext().getContextPath() + "/shoppingMall/");
		}
		res.sendRedirect(getServletContext().getContextPath() + "/shoppingMall/");
	}

	protected void modMyPassword() throws IOException, ServletException {
		try {
			// 로그인 확인
			HttpSession session = this.req.getSession();
			if (session.getAttribute("user_no") == null || session.getAttribute("user_no").equals("")) {
				throw new Exception("로그인 후 이용하시기 바랍니다");
			}

			validator valid = new validator(this.req);
			String[] rgRequired = { "user_password_origin", "user_password", "user_password_confirm" };
			for (String valid_check : rgRequired) {
				if (valid.isEmpty(valid_check)) {
					throw new Exception("필수값을 모두 입력하시기 바랍니다");
				}
			}
			if (!req.getParameter("user_password").matches(regexpPattern.REGEXP_PASSWORD)) {
				throw new Exception("비밀번호는 최소 8자, 최대 30자, 영문 대소문자, 숫자 포함하여 작성하시기 바랍니다");
			}
			if (!req.getParameter("user_password").equals(req.getParameter("user_password_confirm"))) {
				throw new Exception("비밀번호가 일치하지 않습니다");
			}

			MemberDAO dao = new MemberDAO(getServletContext());

			// 비밀번호 대조
			String cLoginPassFlag = dao.fnPasswordConfirm(session.getAttribute("user_no").toString(),
					this.req.getParameter("user_password_origin"));
			if (!cLoginPassFlag.equals("Y")) {
				throw new Exception("비밀번호가 일치하지 않습니다");
			}

			// 비밀번호 암호화
			encoding encodeSha = new encoding("SHA-256");
			String strRandomSalt = random.randomString(20);
			String strEncodePass = encodeSha.toEncode(this.req.getParameter("user_password") + strRandomSalt);

			// 회원 정보 변경 처리를 위한 DTO 할당
			MemberDTO changeDto = new MemberDTO();
			changeDto.setUserNo(session.getAttribute("user_no").toString());
			changeDto.setUserPassword(strEncodePass);
			changeDto.setPasswordSalt(strRandomSalt);

			// 비밀번호 변경 처리
			boolean bPrivateUpdate = dao.modifyPrivateMemberInfo(changeDto);
			if (!bPrivateUpdate) {
				throw new Exception(dao.getMsg());
			}
		} catch (Exception e) {
			this.msg = e.getMessage();
			e.printStackTrace();
			res.sendRedirect(getServletContext().getContextPath() + "/shoppingMall/");
		}
		res.sendRedirect(getServletContext().getContextPath() + "/shoppingMall/");
	}

	private String getMsg() {
		return this.msg;
	}
}
