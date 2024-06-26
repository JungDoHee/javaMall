package servlet.admin.member_info;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.encoding;
import model.member.MemberDAO;
import model.member.MemberDTO;
import util.Pagination;

@WebServlet({ "/adminMall/member_info/list" })
public class listController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private int PAGE_NO = 1;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		MemberDAO dao = new MemberDAO(getServletContext());

		// ▼ 페이징 시작
		// 전체 대상 건수 조회
		int nTotalItem = dao.memberCount();
		// 현재 페이지 설정
		if (req.getParameter("page_no") != null && !req.getParameter("page_no").equals("")
				&& Integer.parseInt(req.getParameter("page_no")) > 0) {
			this.PAGE_NO = Integer.parseInt(req.getParameter("page_no"));
		}
		int nStartPage = (this.PAGE_NO - 1) * Pagination.DEFAULT_PER_PAGE;
		Map<String, Integer> pageInfo = new HashMap<>();
		pageInfo.put("page_no", this.PAGE_NO);
		pageInfo.put("per_page", Pagination.DEFAULT_PER_PAGE);
		pageInfo.put("total_page", nTotalItem);
		// 페이징 (디자인 포함) 처리
		Pagination page = new Pagination(pageInfo);
		page.setUrl(req.getRequestURL().toString());
		if (page.getLastPageNo() < nStartPage) {
			page.setPageNo(page.getLastPageNo());
		}
		// ▲ 페이징 종료

		List<MemberDTO> memberList = dao.memberList(nStartPage, Pagination.DEFAULT_PER_PAGE);
		// ▼ 암호화 컬럼 복호화
		for (MemberDTO memberInfo : memberList) {
			String strUserAddress = memberInfo.getUserAddress();
			String strUserZipCode = memberInfo.getUserZipCode();
			String strUserPhone = memberInfo.getUserPhone();

			encoding encodeAes = new encoding("AES");
			try {
				strUserPhone = encodeAes.toBothDecode(strUserPhone);
				strUserZipCode = encodeAes.toBothDecode(strUserZipCode);
				strUserAddress = encodeAes.toBothDecode(strUserAddress);
			} catch (Exception e) {
				e.printStackTrace();
			}
			memberInfo.setUserAddress(strUserAddress);
			memberInfo.setUserZipCode(strUserZipCode);
			memberInfo.setUserPhone(strUserPhone);
		}
		// ▲ 암호화 컬럼 복호화
		req.setAttribute("rgList", memberList);
		req.setAttribute("pagination", page.getPagination());
		req.getRequestDispatcher(req.getServletPath() + ".jsp").forward(req, res);
	}
}
