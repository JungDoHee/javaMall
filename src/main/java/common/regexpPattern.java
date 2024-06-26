package common;

public class regexpPattern {
	public static final String REGEXP_NAME = "[a-zA-Z가-힣]{2,20}";

	public static final String REGEXP_ID = "[a-zA-Z0-9]{5,20}";

	public static final String REGEXP_EMAIL = "^[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\\.]?[0-9a-zA-Z])*\\.[a-zA-Z]+$";

	public static final String REGEXP_PASSWORD = "^(?=.*[a-zA-Z])((?=.*\\d)|(?=.*\\W)).{8,30}+$";

	public static final String REGEXP_NUMERIC = "^[\\d]*$";
}