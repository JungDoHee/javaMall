package util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class dateFormat {
	private static Date nowDate = new Date();

	public static String getToday() {
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dtFormat.format(nowDate).toString();
	}

	public static String getNow() {
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dtFormat.format(nowDate).toString();
	}

	public static String getTime() {
		SimpleDateFormat dtFormat = new SimpleDateFormat("HH:mm:ss");
		return dtFormat.format(nowDate).toString();
	}

	public String getDate(String datetime) {
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dtFormat.format(datetime).toString();
	}
}
