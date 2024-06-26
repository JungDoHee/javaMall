package model.item;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import common.dbConnection;

public class categoryDAO extends dbConnection {

	private String msg;
	private ArrayList<String> rgWhere = new ArrayList<String>();

	public categoryDAO(ServletContext application) {
		super(application);
	}

	public int categoryCount() {
		int nCategoryCnt = 0;
		try {
			String qryCategoryList = "SELECT count(seq) AS category_cnt FROM category_info";
			this.stmt = this.con.createStatement();
			this.rs = this.stmt.executeQuery(qryCategoryList);
			if (this.rs.next()) {
				nCategoryCnt = Integer.parseInt(this.rs.getString("category_cnt"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nCategoryCnt;
	}

	// where 절 데이터 추가
	public void setSelectCategoryWhere(categoryDTO rgCategory) {
		try {
			if (rgCategory.getCategoryCode() != null) {
				this.rgWhere.add("category_code = " + rgCategory.getCategoryCode());
			}
			if (rgCategory.getCategoryName() != null) {
				this.rgWhere.add("category_name = " + rgCategory.getCategoryName());
			}
			if (rgCategory.getCategorySeq() != null) {
				this.rgWhere.add("seq = " + rgCategory.getCategorySeq());
			}
			if (rgCategory.getModAdminNo() != null) {
				this.rgWhere.add("mod_admin_no = " + rgCategory.getModAdminNo());
			}
			if (rgCategory.getModDate() != null) {
				this.rgWhere.add("mod_date = " + rgCategory.getModDate());
			}
			if (rgCategory.getRegAdminNo() != null) {
				this.rgWhere.add("reg_admin_no = " + rgCategory.getRegAdminNo());
			}
			if (rgCategory.getRegDate() != null) {
				this.rgWhere.add("reg_date = " + rgCategory.getRegDate());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 페이징 없이 출력
	public List<categoryDTO> categoryList() {
		List<categoryDTO> rgCategoryList = new ArrayList<>();
		try {
			String qryCategoryList = "SELECT seq, category_name, reg_admin_no, reg_date, mod_admin_no, mod_date ";
			qryCategoryList += " FROM category_info ";
			if (!this.rgWhere.isEmpty()) {
				String strWhere = String.join(" AND ", this.rgWhere);
				qryCategoryList += " WHERE " + strWhere;
			}
			qryCategoryList += " ORDER BY seq DESC";
			this.stmt = this.con.createStatement();
			this.rs = this.stmt.executeQuery(qryCategoryList);
			while (this.rs.next()) {
				categoryDTO categoryInfo = new categoryDTO();
				categoryInfo.setCategorySeq(this.rs.getString("seq"));
				categoryInfo.setCategoryName(this.rs.getString("category_name"));
				categoryInfo.setRegAdminNo(this.rs.getString("reg_admin_no"));
				categoryInfo.setRegDate(this.rs.getString("reg_date"));
				categoryInfo.setModAdminNo(this.rs.getString("mod_admin_no"));
				categoryInfo.setModDate(this.rs.getString("mod_date"));
				rgCategoryList.add(categoryInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rgCategoryList;
	}

	// 페이징 포함 출력
	public List<categoryDTO> categoryList(int start_page, int per_page) {
		List<categoryDTO> rgCategoryList = new ArrayList<>();
		try {
			String qryCategoryList = "SELECT category.seq, category.category_name, category.reg_admin_no, category.reg_date, category.mod_admin_no, category.mod_date, COUNT(item.seq) AS category_cnt  ";
			qryCategoryList += " FROM category_info AS category ";
			qryCategoryList += " LEFT JOIN item_list AS item ";
			qryCategoryList += " ON category.seq = item.category_seq ";
			qryCategoryList += " GROUP BY category.seq, category.category_name, category.reg_admin_no, category.reg_date, category.mod_admin_no, category.mod_date  ";
			qryCategoryList += " ORDER BY category.seq DESC ";
			qryCategoryList += " OFFSET ? ROWS ";
			qryCategoryList += " FETCH NEXT ? ROWS ONLY ";

			this.pstmt = this.con.prepareStatement(qryCategoryList);
			this.pstmt.setInt(1, start_page);
			this.pstmt.setInt(2, per_page);
			this.rs = this.pstmt.executeQuery();

			while (this.rs.next()) {
				categoryDTO categoryInfo = new categoryDTO();
				categoryInfo.setCategorySeq(this.rs.getString("seq"));
				categoryInfo.setCategoryName(this.rs.getString("category_name"));
				categoryInfo.setRegAdminNo(this.rs.getString("reg_admin_no"));
				categoryInfo.setRegDate(this.rs.getString("reg_date"));
				categoryInfo.setModAdminNo(this.rs.getString("mod_admin_no"));
				categoryInfo.setModDate(this.rs.getString("mod_date"));
				categoryInfo.setCategoryCount(this.rs.getString("category_cnt"));
				rgCategoryList.add(categoryInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rgCategoryList;
	}

	public int insertCategory(categoryDTO rgCategory) {
		int categorySeq = 0;
		try {
			String rstInsertCategory = "INSERT INTO category_info ";
			rstInsertCategory += " (category_code, category_name, reg_admin_no) ";
			rstInsertCategory += " VALUES ";
			rstInsertCategory += " (?, ?, ?) ";
			this.pstmt = this.con.prepareStatement(rstInsertCategory, 1);
			this.pstmt.setString(1, rgCategory.getCategoryCode());
			this.pstmt.setString(2, rgCategory.getCategoryName());
			this.pstmt.setString(3, rgCategory.getRegAdminNo());

			int nResult = this.pstmt.executeUpdate();
			if (nResult < 1) {
				throw new Exception("분류 등록 실패했습니다");
			}

			this.rs = this.pstmt.getGeneratedKeys();
			if (!this.rs.next()) {
				throw new Exception("정보 등록에 실패했습니다 (2)");
			}

			categorySeq = this.rs.getInt(1);
		} catch (Exception e) {
			this.msg = e.getMessage();
			e.printStackTrace();
		}
		return categorySeq;
	}

	public int updateCategory(categoryDTO rgCategory) {
		int nUpdateCnt = 0;
		try {
			this.con.setAutoCommit(false);
			String rstUpdateCategory = "UPDATE category_info ";
			rstUpdateCategory += " SET ";
			rstUpdateCategory += " category_name = ?, ";
			rstUpdateCategory += " mod_admin_no = ?, ";
			rstUpdateCategory += " mod_date = ? ";
			rstUpdateCategory += " WHERE seq = " + rgCategory.getCategorySeq();
			this.pstmt = this.con.prepareStatement(rstUpdateCategory);
			this.pstmt.setString(1, rgCategory.getCategoryName());
			this.pstmt.setString(2, rgCategory.getModAdminNo());
			this.pstmt.setString(3, rgCategory.getModDate());
			nUpdateCnt = this.pstmt.executeUpdate();
			this.con.commit();
		} catch (Exception e) {
			this.msg = e.getMessage();
			e.printStackTrace();
		}
		return nUpdateCnt;
	}

	public boolean deleteCategory(String strCategorySeq) {
		boolean bDelete = false;
		try {
			// 해당 카테고리로 등록된 물품 조회
			String rstSelectItem = "SELECT TOP 1 seq FROM item_list WHERE category_seq = ? AND item_status in ('a','b')";
			this.pstmt = this.con.prepareStatement(rstSelectItem);
			this.pstmt.setString(1, strCategorySeq);
			this.rs = this.pstmt.executeQuery();
			if (this.rs.next()) {
				throw new Exception("해당 카테고리로 등록된 상품이 있습니다");
			}

			this.con.setAutoCommit(false);

			// 카테고리 삭제
			String rstDeleteCategory = "DELETE FROM category_info WHERE seq = ?";
			this.pstmt = this.con.prepareStatement(rstDeleteCategory);
			this.pstmt.setString(1, strCategorySeq);
			this.pstmt.executeUpdate();
			if (this.pstmt.executeUpdate() > 0) {
				bDelete = true;
			}
			this.con.commit();
		} catch (Exception e) {
			this.msg = e.getMessage();
			e.printStackTrace();
		}
		return bDelete;
	}

	public String getMsg() {
		return this.msg;
	}
}
