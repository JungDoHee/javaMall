package servlet.web.item;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.item.ItemDTO;
import model.item.itemUserDAO;
import util.Pagination;

@WebServlet("/shoppingMall/main")
public class mainController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		itemUserDAO dao = new itemUserDAO(getServletContext());
		List<ItemDTO> itemList = dao.getItemList(0, Pagination.DEFAULT_PER_PAGE);

		String imageServer = req.getServletContext().getInitParameter("imageServer");

		req.setAttribute("strImageUrl", imageServer);
		req.setAttribute("rgItemList", itemList);
		req.getRequestDispatcher(req.getServletPath() + ".jsp").forward(req, res);
	}
}
