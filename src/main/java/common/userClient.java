package common;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class userClient {
	public static String getClientIp(HttpServletRequest req) throws UnknownHostException {
		String ip = null;
		boolean bIpHeader = false;

		List<String> rgHeaderList = new ArrayList<>();
		rgHeaderList.add("X-Forwarded-For");
		rgHeaderList.add("HTTP_CLIENT_IP");
		rgHeaderList.add("HTTP_X_FORWARDED_FOR");
		rgHeaderList.add("HTTP_X_FORWARDED");
		rgHeaderList.add("HTTP_FORWARDED_FOR");
		rgHeaderList.add("HTTP_FORWARDED");
		rgHeaderList.add("Proxy-Client-IP");
		rgHeaderList.add("WL-Proxy-Client-IP");
		rgHeaderList.add("HTTP_VIA");
		rgHeaderList.add("IPV6_ADR");

		for (String header : rgHeaderList) {
			ip = req.getHeader(header);
			if (ip != null && !ip.equals("unknown")) {
				bIpHeader = true;

				break;
			}
		}
		if (!bIpHeader) {
			ip = req.getRemoteAddr();
		}

		if (ip.equals("0:0:0:0:0:0:0:1") || ip.equals("127.0.0.1")) {
			InetAddress address = InetAddress.getLocalHost();
			ip = address.getHostAddress();
		}

		return ip;
	}
}