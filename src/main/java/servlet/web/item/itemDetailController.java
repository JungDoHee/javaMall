package servlet.web.item;

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
import model.item.CartDTO;
import model.item.ItemDTO;
import model.item.itemUserDAO;

@WebServlet({ "/shoppingMall/item/detail_view" })
public class itemDetailController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String strGoUrl = req.getContextPath() + "/item/list";
		try {
			validator valid = new validator(req);
			if (valid.isEmpty("item_seq")) {
				throw new FileException("잘못된 경로로 접근하였습니다", strGoUrl);
			}

			itemUserDAO dao = new itemUserDAO(getServletContext());
			List<ItemDTO> itemDetailInfo = dao.getItemDetail(req.getParameter("item_seq"));
			if (itemDetailInfo.isEmpty()) {
				throw new FileException("존재하지 않는 물품입니다", strGoUrl);
			}

			// 로그인한 경우 장바구니에 담긴 상품인지 조회
			CartDTO rgCartInfo = new CartDTO();
			HttpSession session = req.getSession();
			if (session.getAttribute("user_no") != null && !session.getAttribute("user_no").equals("")) {
				CartDTO cart = new CartDTO();
				cart.setItemSeq(req.getParameter("item_seq"));
				cart.setUserNo(session.getAttribute("user_no").toString());
				rgCartInfo = dao.getCartInfo(cart);
			}

			String imageServer = req.getServletContext().getInitParameter("imageServer");

			req.setAttribute("strImageUrl", imageServer);
			req.setAttribute("itemInfo", itemDetailInfo);
			req.setAttribute("cartInfo", rgCartInfo);
			req.getRequestDispatcher(req.getServletPath() + ".jsp").forward(req, res);
		} catch (FileException e) {
			res.setContentType("text/html; charset=UTF-8");
			PrintWriter out = res.getWriter();
			out.println(e.toString());
			out.flush();
			out.close();
		}
	}

}
