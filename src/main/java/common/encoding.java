package common;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class encoding {
	private String encoding_algorithm;
	private String sKey = "2024_jungdohee_java_project_BYTE";

	public encoding() {
	}

	public encoding(String algorithm) {
		this.encoding_algorithm = algorithm;
	}

	// 단방향 암호화
	public String toEncode(String plainText) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance(this.encoding_algorithm);
		md.update(plainText.getBytes());

		StringBuffer sBuffer = new StringBuffer();
		byte b;
		int i;
		byte[] arrayOfByte;
		for (i = (arrayOfByte = md.digest()).length, b = 0; b < i;) {
			byte b1 = arrayOfByte[b];
			sBuffer.append(String.format("%02x", new Object[] { Byte.valueOf(b1) }));
			b++;
		}

		return sBuffer.toString();
	}

	// 양방향 암호화
	public String toBothEncode(String plainText) throws Exception {
		if (plainText == null || plainText.isEmpty()) {
			return plainText;
		}

		Cipher cipher = Cipher.getInstance(this.encoding_algorithm);
		SecretKeySpec secretKey = new SecretKeySpec(this.sKey.getBytes(StandardCharsets.UTF_8),
				this.encoding_algorithm);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] rgDecrypt = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(rgDecrypt);
	}

	// 양방향 복호화
	public String toBothDecode(String encryptText) throws Exception {
		if (encryptText == null || encryptText.isEmpty()) {
			return encryptText;
		}

		Cipher cipher = Cipher.getInstance(this.encoding_algorithm);
		SecretKeySpec secretKey = new SecretKeySpec(this.sKey.getBytes(StandardCharsets.UTF_8),
				this.encoding_algorithm);
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] rgDecode = Base64.getDecoder().decode(encryptText);
		byte[] rgDecrypt = cipher.doFinal(rgDecode);
		return new String(rgDecrypt, StandardCharsets.UTF_8);
	}
}