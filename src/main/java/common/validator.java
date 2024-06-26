package common;

import javax.servlet.http.HttpServletRequest;

public class validator {
	public HttpServletRequest request;

	public validator(HttpServletRequest req) {
		this.request = req;
	}

	public boolean isEmpty(String key) {
		boolean result = true;
		try {
			if (this.request.getParameter(key) != null && !this.request.getParameter(key).equals("")) {
				result = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public boolean isInt(String key) {
		boolean result = false;
		try {
			if (this.request.getParameter(key) != null && !this.request.getParameter(key).equals("")) {
				result = false;
			}
			int nNumeric = Integer.parseInt(this.request.getParameter(key));

			if (nNumeric >= 0) {
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}