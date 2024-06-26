package util;

public class scriptAlert {
	private String strScript;

	public scriptAlert(String strMsg, String strUrl) {
		this.strScript = "<script>";
		if (strMsg != null && !strMsg.equals("")) {
			this.strScript += "alert(\'" + strMsg + "\');";
		}
		this.strScript += "document.location.href=\'" + strUrl + "\';";
		this.strScript += "</script>";
	}

	public String toString() {
		return this.strScript;
	}
}
