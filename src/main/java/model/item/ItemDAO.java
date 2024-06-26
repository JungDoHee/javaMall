package model.item;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import common.dbConnection;

public class ItemDAO extends dbConnection {
	public String strMessage = "";

	public ItemDAO(ServletContext application) {
		super(application);
	}

	// 물품 재고 조회
	public StockDTO getItemStock(String strItemSeq) {
		StockDTO dto = new StockDTO();
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
			dto.setItemSeq(this.rs.getString("item_seq"));
			dto.setRemainStock(this.rs.getString("remain_stock"));
			dto.setInitStock(this.rs.getString("init_stock"));
		} catch (Exception e) {
			e.printStackTrace();
			this.strMessage = e.getMessage();
		}
		return dto;
	}

	// 물품 등록 처리
	public int itemRegProc(ItemDTO rgItemInfo, Map<Integer, ImageDTO> rgImageList) {
		int itemSeq = 0;
		try {
			this.con.setAutoCommit(false);
			// 물품 등록
			String rstInsertItemList = "INSERT INTO item_list ";
			rstInsertItemList += " (item_subject, item_price, item_status, item_info, thumb_path, thumb_hash_name, thumb_name, admin_no, category_seq, init_stock) ";
			rstInsertItemList += " VALUES ";
			rstInsertItemList += " (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			this.pstmt = this.con.prepareStatement(rstInsertItemList, 1);
			this.pstmt.setString(1, rgItemInfo.getItemSubject());
			this.pstmt.setString(2, rgItemInfo.getItemPrice());
			this.pstmt.setString(3, rgItemInfo.getItemStatus());
			this.pstmt.setString(4, rgItemInfo.getItemInfo());
			this.pstmt.setString(5, rgItemInfo.getThumbPath());
			this.pstmt.setString(6, rgItemInfo.getThumbHashName());
			this.pstmt.setString(7, rgItemInfo.getThumbName());
			this.pstmt.setString(8, rgItemInfo.getAdminNo());
			this.pstmt.setString(9, rgItemInfo.getCategorySeq());
			this.pstmt.setString(10, rgItemInfo.getInitStock());

			int nResult = this.pstmt.executeUpdate();
			if (nResult < 1) {
				throw new Exception("물품 등록 실패했습니다");
			}

			this.rs = this.pstmt.getGeneratedKeys();
			if (!this.rs.next()) {
				throw new Exception("관리자 정보 등록에 실패했습니다 (2)");
			}
			itemSeq = this.rs.getInt(1);

			// 이미지 등록
			if (rgImageList.size() > 0) {
				String rstInsertImageList = "INSERT INTO image_list ";
				rstInsertImageList = rstInsertImageList + " (item_seq, image_path, image_hash_name, image_name) ";
				rstInsertImageList = rstInsertImageList + " VALUES ";
				rstInsertImageList = rstInsertImageList + " (?, ?, ?, ?) ";
				this.pstmt = this.con.prepareStatement(rstInsertImageList);
				for (Iterator<Integer> iterator = rgImageList.keySet().iterator(); iterator.hasNext();) {
					int imageKey = ((Integer) iterator.next()).intValue();
					String strHashName = rgItemInfo.getItemSubject() + rgItemInfo.getItemSubject();
					this.pstmt.setInt(1, itemSeq);
					this.pstmt.setString(2,
							((ImageDTO) rgImageList.get(Integer.valueOf(imageKey))).getImageServerPath());
					this.pstmt.setString(3, strHashName);
					this.pstmt.setString(4, ((ImageDTO) rgImageList.get(Integer.valueOf(imageKey))).getOriginalName());
					this.pstmt.addBatch();
					this.pstmt.clearParameters();
				}

				int[] nImageResult = this.pstmt.executeBatch();

				for (int nImageResultIndex = 0; nImageResultIndex < nImageResult.length; nImageResultIndex++) {
					if (nImageResult[nImageResultIndex] < 0) {
						throw new Exception("이미지 등록 실패했습니다. 실패 코드 [" + nImageResult[nImageResultIndex] + "]");
					}
				}
				this.pstmt.clearBatch();
			}
			this.pstmt.executeBatch();
			this.pstmt.clearBatch();
			this.con.commit();
		} catch (Exception e) {
			this.strMessage = e.getMessage();
			try {
				this.con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return itemSeq;
	}

	public int modifyItemProc(ItemDTO dto) {
		int nResult = 0;
		try {
			this.con.setAutoCommit(false);
			String rstUpdateItemList = "UPDATE item_list ";
			rstUpdateItemList += " SET ";
			rstUpdateItemList += " item_subject = ?, ";
			rstUpdateItemList += " item_price = ?, ";
			rstUpdateItemList += " item_status = ?, ";
			rstUpdateItemList += " item_info = ?, ";
			rstUpdateItemList += " modify_date = ?, ";
			rstUpdateItemList += " category_seq = ?, ";
			rstUpdateItemList += " init_stock = ? ";
			if (dto.getThumbName() != null && !dto.getThumbName().equals("")) {
				rstUpdateItemList += " ,thumb_path = ?, ";
				rstUpdateItemList += " thumb_name = ? ";
			}
			rstUpdateItemList = rstUpdateItemList + " WHERE seq = " + dto.getItemSeq();
			this.pstmt = this.con.prepareStatement(rstUpdateItemList);
			this.pstmt.setString(1, dto.getItemSubject());
			this.pstmt.setString(2, dto.getItemPrice());
			this.pstmt.setString(3, dto.getItemStatus());
			this.pstmt.setString(4, dto.getItemInfo());
			this.pstmt.setString(5, dto.getModifyDate());
			this.pstmt.setString(6, dto.getCategorySeq());
			this.pstmt.setString(7, dto.getInitStock());
			if (dto.getThumbName() != null && !dto.getThumbName().equals("")) {
				this.pstmt.setString(8, dto.getThumbPath());
				this.pstmt.setString(9, dto.getThumbName());
			}
			nResult = this.pstmt.executeUpdate();

			this.con.commit();
		} catch (Exception e) {
			this.strMessage = e.getMessage();
			e.printStackTrace();
		}
		return nResult;
	}

	public int deleteItemProc(ItemDTO dto) {
		int nResult = 0;
		try {
			this.con.setAutoCommit(false);
			String rstDeleteItemList = "UPDATE item_list SET item_status = 'c' WHERE seq = ?";
			this.pstmt = this.con.prepareStatement(rstDeleteItemList);
			this.pstmt.setString(1, dto.getItemSeq());
			nResult = this.pstmt.executeUpdate();

			this.con.commit();
		} catch (Exception e) {
			this.strMessage = e.getMessage();
			e.printStackTrace();
		}
		return nResult;
	}

	public List<ItemDTO> getItemDetail(String item_seq) {
		List<ItemDTO> itemInfo = new ArrayList<>();
		try {
			String rstItemList = "SELECT ";
			rstItemList += " item.seq AS item_seq, item.item_subject, item.item_price, item.item_status, item.item_info, item.thumb_path, item.thumb_name, item.admin_no, item.reg_date, ";
			rstItemList += " item.category_seq, item.init_stock, ";
			rstItemList += " image.seq as image_seq, image.image_path, image.image_name ";
			rstItemList += " FROM item_list AS item ";
			rstItemList += " LEFT JOIN image_list AS image ";
			rstItemList += " ON item.seq = image.item_seq ";
			rstItemList += " WHERE item.seq=? ";
			this.pstmt = this.con.prepareStatement(rstItemList);
			this.pstmt.setString(1, item_seq);
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
				dto.setAdminNo(this.rs.getString("admin_no"));
				dto.setRegDate(this.rs.getString("reg_date"));

				dto.setCategorySeq(this.rs.getString("category_seq"));
				dto.setInitStock(this.rs.getString("init_stock"));

				dto.setImageSeq(this.rs.getString("image_seq"));
				dto.setImagePath(this.rs.getString("image_path"));
				dto.setImageName(this.rs.getString("image_name"));
				itemInfo.add(dto);
			}
		} catch (Exception e) {
			this.strMessage = e.getMessage();
			e.printStackTrace();
		}
		return itemInfo;
	}

	public List<ItemDTO> getItemList() {
		List<ItemDTO> dto = new ArrayList<>();
		try {
			String rstItemList = "SELECT seq, item_subject, item_price, item_status, thumb_path, thumb_name, admin_no, reg_date, category_seq, init_stock ";
			rstItemList = rstItemList + " FROM item_list ";
			rstItemList = rstItemList + " WHERE item_status in ('a','b') ";
			this.pstmt = this.con.prepareStatement(rstItemList);
			this.rs = this.pstmt.executeQuery();
			while (this.rs.next()) {
				ItemDTO list = new ItemDTO();
				list.setItemSeq(this.rs.getString("seq"));
				list.setAdminNo(this.rs.getString("admin_no"));
				list.setItemSubject(this.rs.getString("item_subject"));
				list.setItemPrice(this.rs.getString("item_price"));
				list.setItemStatus(this.rs.getString("item_status"));
				list.setThumbPath(this.rs.getString("thumb_path"));
				list.setThumbName(this.rs.getString("thumb_name"));
				list.setAdminNo(this.rs.getString("admin_no"));
				list.setRegDate(this.rs.getString("reg_date"));
				list.setCategorySeq(this.rs.getString("category_seq"));
				list.setInitStock(this.rs.getString("init_stock"));
				dto.add(list);
			}
		} catch (Exception e) {
			this.strMessage = e.getMessage();
			e.printStackTrace();
		}
		return dto;
	}

	public String getMsg() {
		return this.strMessage;
	}
}