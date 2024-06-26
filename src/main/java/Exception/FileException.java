package Exception;

public class FileException extends Throwable {
	private static final long serialVersionUID = 1L;
	private String strScript;

	public FileException() {
	}

	public FileException(String errMsg) {
		super(errMsg);
	}

	public FileException(String strMsg, String strUrl) {
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
