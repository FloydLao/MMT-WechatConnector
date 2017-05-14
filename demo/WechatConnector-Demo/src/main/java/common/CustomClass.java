package common;

public class CustomClass {
	static private String token;

	public static String getToken() {
		return token;
	}

	public static void setToken(String token) {
		CustomClass.token = token;
	}
}
