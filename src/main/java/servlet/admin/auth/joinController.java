package servlet.admin.auth;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Exception.FileException;
import common.encoding;
import common.regexpPattern;
import common.validator;
import model.admin.auth.AdminDAO;
import model.admin.auth.AdminDTO;
import util.scriptAlert;

@WebServlet({ "/adminMall/auth/join" })
public class joinController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String strGoUrl = getServletContext().getContextPath() + "/adminMall";
		try {
			validator valid = new validator(req);
			if (valid.isEmpty("admin_name") || valid.isEmpty("admin_id") || valid.isEmpty("admin_password")) {
				throw new FileException("필수값을 모두 입력하시기 바랍니다", strGoUrl);
			}

			String strAdminName = req.getParameter("admin_name");
			String strAdminId = req.getParameter("admin_id");
			String strAdminPassword = req.getParameter("admin_password");
			if (!strAdminName.matches(regexpPattern.REGEXP_NAME) || !strAdminId.matches(regexpPattern.REGEXP_ID)
					|| !strAdminPassword.matches(regexpPattern.REGEXP_PASSWORD)) {
				throw new FileException("입력 형식을 다시 확인하시기 바랍니다", strGoUrl);
			}

			encoding encode = new encoding("SHA-256");
			String strRandomSalt = randomString(20);
			String strEncodePass = encode.toEncode(req.getParameter("admin_password") + strRandomSalt);

			AdminDAO dao = new AdminDAO(getServletContext());
			AdminDTO dto = new AdminDTO();
			dto.setAdminID(strAdminId);
			dto.setAdminName(strAdminName);
			dto.setPasswordSalt(strRandomSalt);
			dto.setAdminPassword(strEncodePass);

			int nAdminNo = dao.adminJoinProc(dto);
			if (nAdminNo < 1) {
				throw new FileException(dao.getMessage(), strGoUrl);
			}
			res.sendRedirect(getServletContext().getContextPath() + "/adminMall/auth/join_done.jsp");
		} catch (FileException e) {
			res.setContentType("text/html; charset=UTF-8");
			PrintWriter out = res.getWriter();
			out.println(e.toString());
			out.flush();
			out.close();
		} catch (NoSuchAlgorithmException e) {
			scriptAlert alert = new scriptAlert("정보 암호화 실패했습니다", strGoUrl);
			res.setContentType("text/html; charset=UTF-8");
			PrintWriter out = res.getWriter();
			out.println(alert.toString());
			out.flush();
			out.close();
		}
	}

	private String randomString(int stringLength) {
		Random rand = new Random();
		int leftLimit = 48;
		int rightLimit = 122;
		return ((StringBuilder) rand.ints(leftLimit, rightLimit + 1)
				.filter(i -> ((i <= 57 || i >= 65) && (i <= 90 || i >= 97))).limit(stringLength)
				.<StringBuilder>collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append))
				.toString();
	}
}
