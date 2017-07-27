/**
 * (c) 2003-2017 MMT, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
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
