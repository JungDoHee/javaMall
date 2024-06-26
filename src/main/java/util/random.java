package util;

import java.util.Random;

public class random {
	public static String randomString(int stringLength) {
		Random rand = new Random();
		int leftLimit = 48;
		int rightLimit = 122;
		return ((StringBuilder) rand.ints(leftLimit, rightLimit + 1)
				.filter(i -> ((i <= 57 || i >= 65) && (i <= 90 || i >= 97))).limit(stringLength)
				.<StringBuilder>collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append))
				.toString();
	}
}
