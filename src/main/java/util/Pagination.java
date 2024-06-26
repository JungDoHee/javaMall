package util;

import java.util.Map;

public class Pagination {
	public static final int DEFAULT_PER_PAGE = 20;

	public static final int DEFAULT_PER_BLOCK = 10;

	public static final int DEFAULT_PAGE_NO = 1;

	private int pageNo;

	private int totalPage;

	private int perPage;

	private String url = "";

	public Pagination(int totalPage) {
		setPageNo(1);
		setPerPage(5);
		setTotalPage(totalPage);
	}

	public Pagination(Map<String, Integer> pageInfo) {
		setPageNo(((Integer) pageInfo.get("page_no")).intValue());
		setPerPage(((Integer) pageInfo.get("per_page")).intValue());
		setTotalPage(((Integer) pageInfo.get("total_page")).intValue());
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public void setPerPage(int perPage) {
		this.perPage = perPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getLastPageNo() {
		return (int) Math.ceil((this.totalPage / this.perPage));
	}

	public int getBlockNo() {
		return (this.pageNo - 1) / this.perPage + 1;
	}

	public int getBlockStartPageNo() {
		int nBlockNo = getBlockNo();
		return (nBlockNo - 1) * this.perPage + 1;
	}

	public int getBlockEndPageNo() {
		int nLastPageNo = getLastPageNo();
		int nBlockEndPageNo = getBlockNo() * this.perPage;
		if (nBlockEndPageNo > nLastPageNo)
			nBlockEndPageNo = nLastPageNo;
		return nBlockEndPageNo;
	}

	public int getPrevBlockPageNo() {
		int nPrevBlockNo = 0;
		if (getBlockNo() == 1) {
			nPrevBlockNo = 1;
		} else {
			nPrevBlockNo = getBlockNo() - 1;
		}
		return (nPrevBlockNo - 1) * this.perPage + 1;
	}

	public int getNextBlockPageNo() {
		int nNextBlockNo = getBlockNo() / this.perPage + 1;
		if (nNextBlockNo >= getBlockNo())
			nNextBlockNo = getBlockNo();
		return (nNextBlockNo - 1) * this.perPage + 1;
	}

	public void setUrl(String paging_url) {
		this.url = paging_url;
	}

	public String getPagination() {
		String strActiveClass = "";
		String strHtml = "<div class=\"my-2 container d-flex justify-content-center\">";
		strHtml = strHtml + "<nav>";
		strHtml = strHtml + "<ul class=\"pagination\">";
		if (getBlockNo() > 1) {
			strHtml = strHtml + "<li class=\"page-item\">";
			strHtml = strHtml + "<a class=\"page-link\" href=\"" + this.url + "?page_no=" + getBlockStartPageNo()
					+ "\" aria-label=\"Previous\">";
			strHtml = strHtml + "<span aria-hidden=\"true\">";
			strHtml = strHtml + "</a>";
			strHtml = strHtml + "</li>";
		}
		for (int pagingIndex = getBlockStartPageNo(); pagingIndex <= getBlockEndPageNo(); pagingIndex++) {
			if (pagingIndex == this.pageNo) {
				strActiveClass = " active";
			} else {
				strActiveClass = "";
			}
			strHtml = strHtml + "<li class=\"page-item\"><a class=\"page-link" + strActiveClass + "\" href=\""
					+ this.url + "?page_no=" + pagingIndex + "\">" + pagingIndex + "</a></li>";
		}
		if (getNextBlockPageNo() > getBlockEndPageNo() && getNextBlockPageNo() > getBlockNo()) {
			strHtml = strHtml + "<li class=\"page-item\">";
			strHtml = strHtml + "<a class=\"page-link\" href=\"" + strHtml + "?page_no=" + getLastPageNo()
					+ "\" aria-label=\"Next\">";
			strHtml = strHtml + "<span aria-hidden=\"true\">";
			strHtml = strHtml + "</a>";
			strHtml = strHtml + "</li>";
		}
		strHtml = strHtml + "</ul>";
		strHtml = strHtml + "</nav>";
		strHtml = strHtml + "</div>";
		return strHtml;
	}
}
