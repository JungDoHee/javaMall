package util;

public class activatePage {
	private String curPage;
	private String targetPage;
	private boolean compare;

	public String getCurPage() {
		return curPage;
	}

	public void setCurPage(String curPage) {
		this.curPage = curPage;
	}

	public String getTargetPage() {
		return targetPage;
	}

	public void setTargetPage(String targetPage) {
		this.targetPage = targetPage;
	}

	public boolean getCompare() {
		boolean compare = false;
		String strCurPage = this.curPage;
		String strTargetPage = this.targetPage;
		if (strCurPage.contains(strTargetPage)) {
			compare = true;
		}

		return compare;
	}

	public void setCompare(boolean isCompare) {
		this.compare = isCompare;
	}
}
