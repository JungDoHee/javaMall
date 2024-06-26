package model.item;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import common.dbConnection;

public class itemUserDAO extends dbConnection {
	private String errMsg;

	public itemUserDAO(ServletContext application) {
		super(application);
	}

	// 물품 내역 조회 (페이징 없음)
	public List<ItemDTO> getItemList() {
		List<ItemDTO> itemList = new ArrayList<>();
		try {
			String rstItemList = "SELECT seq, item_subject, item_price, item_status, item_info, thumb_path, thumb_name, reg_date ";
			rstItemList = rstItemList + " FROM item_list ";
			rstItemList = rstItemList + " WHERE item_status in ('a', 'b') ";
			this.pstmt = this.con.prepareStatement(rstItemList);
			this.rs = this.pstmt.executeQuery();
			while (this.rs.next()) {
				ItemDTO dto = new ItemDTO();
				dto.setItemSeq(this.rs.getString("seq"));
				dto.setItemSubject(this.rs.getString("item_subject"));
				dto.setItemPrice(this.rs.getString("item_price"));
				dto.setItemStatus(this.rs.getString("item_status"));
				dto.setItemInfo(this.rs.getString("item_info"));
				dto.setThumbPath(this.rs.getString("thumb_path"));
				dto.setThumbName(this.rs.getString("thumb_name"));
				dto.setRegDate(this.rs.getString("reg_date"));
				itemList.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemList;
	}

	// 등록된 물품 수량 조회
	public int getItemListCount() {
		int nTotal = 0;
		try {
			String rstItemList = "SELECT count(seq) AS total_count ";
			rstItemList = rstItemList + " FROM item_list ";
			rstItemList = rstItemList + " WHERE item_status in ('a', 'b') ";
			this.pstmt = this.con.prepareStatement(rstItemList);
			this.rs = this.pstmt.executeQuery();
			this.rs.next();
			nTotal = this.rs.getInt("total_count");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nTotal;
	}

	// 물품 내역 조회 (페이징 포함)
	public List<ItemDTO> getItemList(int start_page, int per_page) {
		List<ItemDTO> itemList = new ArrayList<>();
		try {
			String rstItemList = "SELECT seq, item_subject, item_price, item_status, item_info, thumb_path, thumb_name, reg_date ";
			rstItemList = rstItemList + " FROM item_list ";
			rstItemList = rstItemList + " WITH(NOLOCK) ";
			rstItemList = rstItemList + " WHERE item_status in ('a', 'b') ";
			rstItemList = rstItemList + " ORDER BY seq DESC ";
			rstItemList = rstItemList + " OFFSET ? ROWS ";
			rstItemList = rstItemList + " FETCH NEXT ? ROWS ONLY ";
			this.pstmt = this.con.prepareStatement(rstItemList);
			this.pstmt.setInt(1, start_page);
			this.pstmt.setInt(2, per_page);
			this.rs = this.pstmt.executeQuery();
			while (this.rs.next()) {
				ItemDTO dto = new ItemDTO();
				dto.setItemSeq(this.rs.getString("seq"));
				dto.setItemSubject(this.rs.getString("item_subject"));
				dto.setItemPrice(this.rs.getString("item_price"));
				dto.setItemStatus(this.rs.getString("item_status"));
				dto.setItemInfo(this.rs.getString("item_info"));
				dto.setThumbPath(this.rs.getString("thumb_path"));
				dto.setThumbName(this.rs.getString("thumb_name"));
				dto.setRegDate(this.rs.getString("reg_date"));
				itemList.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemList;
	}

	// 물품 상세 정보 조회
	public List<ItemDTO> getItemDetail(String item_seq) {
		List<ItemDTO> itemDetailList = new ArrayList<>();
		try {
			String rstItemList = "SELECT ";
			rstItemList = rstItemList
					+ " item.seq AS item_seq, item.item_subject, item.item_price, item.item_status, item.item_info, item.thumb_path, item.thumb_name, item.init_stock, ";
			rstItemList = rstItemList + " image.seq AS image_seq, image.image_path, image.image_name ";
			rstItemList = rstItemList + " FROM item_list AS item ";
			rstItemList = rstItemList + " LEFT JOIN image_list AS image ";
			rstItemList = rstItemList + " ON item.seq = image.item_seq ";
			rstItemList = rstItemList + " WHERE ";
			rstItemList = rstItemList + " item.seq = ? AND ";
			rstItemList = rstItemList + " item.item_status in ('a','b') ";
			this.pstmt = this.con.prepareStatement(rstItemList);
			this.pstmt.setString(1, item_seq);
			this.rs = this.pstmt.executeQuery();
			while (this.rs.next()) {
				ItemDTO itemList = new ItemDTO();
				itemList.setItemSeq(this.rs.getString("item_seq"));
				itemList.setItemSubject(this.rs.getString("item_subject"));
				itemList.setItemPrice(this.rs.getString("item_price"));
				itemList.setItemStatus(this.rs.getString("item_status"));
				itemList.setItemInfo(this.rs.getString("item_info"));
				itemList.setThumbPath(this.rs.getString("thumb_path"));
				itemList.setThumbName(this.rs.getString("thumb_name"));
				itemList.setImageSeq(this.rs.getString("image_seq"));
				itemList.setImagePath(this.rs.getString("image_path"));
				itemList.setImageName(this.rs.getString("image_name"));
				itemList.setImageName(this.rs.getString("init_stock"));
				itemDetailList.add(itemList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.errMsg = e.getMessage();
		}
		return itemDetailList;
	}

	// 장바구니 수량 조회
	public int getCartListCount(String strUSerNo) {
		int nTotal = 0;
		try {
			String rstCartList = "SELECT count(user_no) AS total_count ";
			rstCartList += " FROM item_cart_list ";
			rstCartList += " WHERE user_no = ? ";
			this.pstmt = this.con.prepareStatement(rstCartList);
			this.pstmt.setString(1, strUSerNo);
			this.rs = this.pstmt.executeQuery();
			this.rs.next();
			nTotal = this.rs.getInt("total_count");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nTotal;
	}

	// 카트에 담긴 정보를 가져온다 (한 개만)
	public CartDTO getCartInfo(CartDTO rgCart) {
		CartDTO cart = new CartDTO();
		try {
			String rstSelectCartInfo = "SELECT * FROM item_cart_list WHERE item_seq = ? AND user_no = ?";
			this.pstmt = this.con.prepareStatement(rstSelectCartInfo);
			this.pstmt.setString(1, rgCart.getItemSeq());
			this.pstmt.setString(2, rgCart.getUserNo());
			this.rs = this.pstmt.executeQuery();
			if (this.rs.next()) {
				cart.setItemSeq(this.rs.getString("item_seq"));
				cart.setUserNo(this.rs.getString("user_no"));
				cart.setAmount(this.rs.getString("amount"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.errMsg = e.getMessage();
		}
		return cart;
	}

	// 카트에 담긴 정보를 가져온다 (페이징 포함)
	public List<ItemDTO> getCartList(int nStartPage, int nPerPage, String strUserNo) {
		List<ItemDTO> cartList = new ArrayList<>();
		try {
			String rstCartList = "SELECT ";
			rstCartList += " item.seq AS item_seq, item.item_subject, item.item_price, item.item_status, item.item_info, item.thumb_path, item.thumb_name, item.reg_date, ";
			rstCartList += " cart.amount ";
			rstCartList += " from item_cart_list AS cart left join item_list AS item ";
			rstCartList += " with(nolock) ";
			rstCartList += " on cart.item_seq = item.seq ";
			rstCartList += " where cart.user_no = ? and item.item_status in ('a', 'b') ";
			rstCartList += " order by item.seq DESC ";
			rstCartList += " offset ? rows ";
			rstCartList += " fetch next ? rows only ";
			this.pstmt = this.con.prepareStatement(rstCartList);
			this.pstmt.setString(1, strUserNo);
			this.pstmt.setInt(2, nStartPage);
			this.pstmt.setInt(3, nPerPage);
			this.rs = this.pstmt.executeQuery();
			while (this.rs.next()) {
				ItemDTO dto = new ItemDTO();
				dto.setItemSeq(this.rs.getString("item_seq"));
				dto.setItemSubject(this.rs.getString("item_subject"));
				dto.setItemPrice(this.rs.getString("item_price"));
				dto.setItemStatus(this.rs.getString("item_status"));
				dto.setItemInfo(this.rs.getString("item_info"));
				dto.setThumbPath(this.rs.getString("thumb_path"));
				dto.setThumbName(this.rs.getString("thumb_name"));
				dto.setRegDate(this.rs.getString("reg_date"));
				dto.setCartAmount(this.rs.getString("amount"));
				cartList.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cartList;
	}

	// 장바구니에 넣기
	public boolean cartAdd(CartDTO cart) {
		boolean bAdd = false;
		try {
			String rstSelectItemInfo = "SELECT seq FROM item_list WHERE seq = ?";
			this.pstmt = this.con.prepareStatement(rstSelectItemInfo);
			this.pstmt.setString(1, cart.getItemSeq());
			this.rs = this.pstmt.executeQuery();
			if (!this.rs.next()) {
				throw new Exception("존재하지 않는 물품입니다");
			}

			String strAddType = "UPDATE";

			CartDTO rgCart = this.getCartInfo(cart);
			if (rgCart.getItemSeq() == null || rgCart.getUserNo() == null) {
				strAddType = "INSERT";
			}

			if (strAddType.equals("INSERT")) {
				String rstInsertCart = "INSERT INTO item_cart_list ";
				rstInsertCart += " (item_seq, user_no, amount) ";
				rstInsertCart += " VALUES ";
				rstInsertCart += " (?, ?, ?) ";
				this.pstmt = this.con.prepareStatement(rstInsertCart);
				this.pstmt.setString(1, cart.getItemSeq());
				this.pstmt.setString(2, cart.getUserNo());
				this.pstmt.setString(3, cart.getAmount());
			} else {
				String rstUpdateCart = "UPDATE item_cart_list ";
				rstUpdateCart += " SET amount = ? ";
				rstUpdateCart += " WHERE item_seq = ? AND user_no = ? ";
				this.pstmt = this.con.prepareStatement(rstUpdateCart);
				this.pstmt.setString(1, cart.getAmount());
				this.pstmt.setString(2, cart.getItemSeq());
				this.pstmt.setString(3, cart.getUserNo());
			}

			if (this.pstmt.executeUpdate() > 0) {
				bAdd = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.errMsg = e.getMessage();
		}
		return bAdd;
	}

	// 장바구니에서 삭제
	public boolean cartDel(CartDTO cart) {
		boolean bDel = false;
		try {
			String rstSelectItemInfo = "SELECT seq FROM item_list WHERE seq = ?";
			this.pstmt = this.con.prepareStatement(rstSelectItemInfo);
			this.pstmt.setString(1, cart.getItemSeq());
			this.rs = this.pstmt.executeQuery();
			if (!this.rs.next()) {
				throw new Exception("존재하지 않는 물품입니다");
			}

			CartDTO rgCart = this.getCartInfo(cart);
			if (rgCart.getItemSeq() == null || rgCart.getUserNo() == null) {
				throw new Exception("장바구니에 등록된 상품이 아닙니다");
			}

			String rstDeleteCart = "DELETE FROM item_cart_list ";
			rstDeleteCart += " WHERE item_seq = ? AND user_no = ? ";
			this.pstmt = this.con.prepareStatement(rstDeleteCart);
			this.pstmt.setString(1, cart.getItemSeq());
			this.pstmt.setString(2, cart.getUserNo());

			if (this.pstmt.executeUpdate() > 0) {
				bDel = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.errMsg = e.getMessage();
		}
		return bDel;
	}

	// 해당 물품의 재고 조회
	public StockDTO getStock(String strItemSeq) {
		StockDTO rgStock = new StockDTO();
		try {
			String rstItemStock = "SELECT init_stock, SUM(item_order.amount) AS remain_stock ";
			rstItemStock += " FROM item_list AS item ";
			rstItemStock += " LEFT JOIN item_order_list AS item_order ";
			rstItemStock += " ON item.seq = item_order.item_seq ";
			rstItemStock += " WHERE item.seq = ? ";
			rstItemStock += " group by init_stock, item_order.amount ";
			this.pstmt = this.con.prepareStatement(rstItemStock);
			this.pstmt.setString(1, strItemSeq);
			this.rs = this.pstmt.executeQuery();
			if (!this.rs.next()) {
				throw new Exception("존재하지 않는 물품입니다");
			}
			rgStock.setItemSeq(this.rs.getString("item_seq"));
			rgStock.setRemainStock(this.rs.getString("remain_stock"));
			rgStock.setInitStock(this.rs.getString("init_stock"));
		} catch (Exception e) {
			e.printStackTrace();
			this.errMsg = e.getMessage();
		}
		return rgStock;
	}

	public String getMsg() {
		return this.errMsg;
	}
}
