package servlet.web.item;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Exception.FileException;
import common.validator;
import model.item.CartDTO;
import model.item.itemUserDAO;
import util.scriptAlert;

@WebServlet("/shoppingMall/item/cart/*")
public class cartModifyController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HttpServletRequest req;
	private HttpServletResponse res;
	private HttpSession session;
	private String msg;

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String strGoUrl = getServletContext().getContextPath() + "/item/list.jsp";
		this.req = req;
		this.res = res;
		try {
			this.session = this.req.getSession();
			if (this.session.getAttribute("user_no") == null || this.session.getAttribute("user_no").equals("")) {
				throw new FileException("로그인 후 이용하시기 바랍니다", strGoUrl);
			}

			validator valid = new validator(this.req);
			if (valid.isEmpty("item_seq") || valid.isEmpty("add_cart_amount")) {
				throw new FileException("잘못된 경로로 접근했습니다");
			}

			String[] rgUri = req.getRequestURI().split("/");
			String strLastUri = rgUri[rgUri.length - 1];
			switch (strLastUri) {
			case "add":
				addCart();
				break;
			case "modify":
				modCart();
				break;
			default:
				throw new FileException("잘못된 경로로 접근했습니다");
			}
		} catch (FileException e) {
			res.setContentType("text/html; charset=UTF-8");
			PrintWriter out = res.getWriter();
			out.println(e.toString());
			out.flush();
			out.close();
		}
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		this.req = req;
		this.res = res;
		try {
			this.session = this.req.getSession();
			if (this.session.getAttribute("user_no") == null || this.session.getAttribute("user_no").equals("")) {
				throw new Exception("로그인 후 이용하시기 바랍니다");
			}

			validator valid = new validator(this.req);
			if (valid.isEmpty("item_seq")) {
				throw new Exception("잘못된 경로로 접근했습니다");
			}

			String[] rgUri = req.getRequestURI().split("/");
			String strLastUri = rgUri[rgUri.length - 1];
			switch (strLastUri) {
			case "delete":
				delCart();
				break;
			default:
				throw new Exception("잘못된 경로로 접근했습니다");
			}
		} catch (Exception e) {
			e.printStackTrace();
			HttpSession globalSession = req.getSession();
			globalSession.setAttribute("globalMsg", getMsg());
			res.sendRedirect(getServletContext().getContextPath() + "/shoppingMall/item/list");
		}
	}

	// 장바구니 넣기
	protected void addCart() throws IOException {
		String strGoUrl = getServletContext().getContextPath() + "/shoppingMall/item/detail_view?item_seq="
				+ this.req.getParameter("item_seq");
		try {
			// 장바구니 변수 할당
			CartDTO cart = new CartDTO();
			cart.setItemSeq(this.req.getParameter("item_seq"));
			cart.setUserNo(this.session.getAttribute("user_no").toString());
			cart.setAmount(this.req.getParameter("add_cart_amount"));

			// 장바구니 넣기 처리
			itemUserDAO cartDao = new itemUserDAO(getServletContext());
			boolean bAddCart = cartDao.cartAdd(cart);
			if (!bAddCart) {
				throw new Exception(cartDao.getMsg());
			}
			this.res.sendRedirect(strGoUrl);
		} catch (Exception e) {
			this.msg = e.getMessage();
			e.printStackTrace();

			scriptAlert alert = new scriptAlert(this.msg, strGoUrl);
			res.setContentType("text/html; charset=UTF-8");
			PrintWriter out = res.getWriter();
			out.println(alert.toString());
			out.flush();
			out.close();
		}

	}

	// 장바구니 수량 수정
	protected void modCart() throws IOException {
		String strGoUrl = getServletContext().getContextPath() + "/shoppingMall/item/detail_view?item_seq="
				+ this.req.getParameter("item_seq");
		try {
			// 장바구니 변수 할당
			CartDTO cart = new CartDTO();
			cart.setItemSeq(this.req.getParameter("item_seq"));
			cart.setUserNo(this.session.getAttribute("user_no").toString());
			cart.setAmount(this.req.getParameter("add_cart_amount"));

			itemUserDAO cartDao = new itemUserDAO(getServletContext());

			// 장바구니에 있는지 확인
			CartDTO rgCartInfo = cartDao.getCartInfo(cart);
			if (rgCartInfo.getItemSeq() == null || rgCartInfo.getUserNo() == null) {
				throw new FileException("장바구니에 들어있는 상품이 아닙니다", strGoUrl);
			}

			// 장바구니 수정 처리
			boolean bAddCart = cartDao.cartAdd(cart);
			if (!bAddCart) {
				throw new FileException(cartDao.getMsg(), strGoUrl);
			}
			this.res.sendRedirect(strGoUrl);
		} catch (FileException e) {
			this.msg = e.getMessage();

			res.setContentType("text/html; charset=UTF-8");
			PrintWriter out = res.getWriter();
			out.println(e.toString());
			out.flush();
			out.close();
		}
	}

	// 장바구니 삭제
	protected void delCart() throws IOException {
		String strGoUrl = getServletContext().getContextPath() + "/shoppingMall/item/detail_view?item_seq="
				+ this.req.getParameter("item_seq");
		try {
			// 장바구니 변수 할당
			CartDTO cart = new CartDTO();
			cart.setItemSeq(this.req.getParameter("item_seq"));
			cart.setUserNo(this.session.getAttribute("user_no").toString());

			itemUserDAO cartDao = new itemUserDAO(getServletContext());

			// 장바구니에 있는지 확인
			CartDTO rgCartInfo = cartDao.getCartInfo(cart);
			if (rgCartInfo.getItemSeq() == null || rgCartInfo.getUserNo() == null) {
				throw new FileException("장바구니에서 삭제할 수 있는 상품이 아닙니다", strGoUrl);
			}

			// 장바구니 삭제 처리
			boolean bAddCart = cartDao.cartDel(cart);
			if (!bAddCart) {
				throw new FileException(cartDao.getMsg(), strGoUrl);
			}
			this.res.sendRedirect(strGoUrl);
		} catch (FileException e) {
			this.msg = e.getMessage();
			res.setContentType("text/html; charset=UTF-8");
			PrintWriter out = res.getWriter();
			out.println(e.toString());
			out.flush();
			out.close();
		}
	}

	private String getMsg() {
		return this.msg;
	}
}
