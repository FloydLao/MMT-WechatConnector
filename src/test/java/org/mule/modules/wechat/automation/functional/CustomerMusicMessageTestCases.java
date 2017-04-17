package org.mule.modules.wechat.automation.functional;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.wechat.WechatConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class CustomerMusicMessageTestCases extends AbstractTestCase<WechatConnector> {

	public CustomerMusicMessageTestCases() {
		super(WechatConnector.class);
	}

	@Before
	public void setup() {
		// TODO
	}

	@After
	public void tearDown() {
		// TODO
	}

	@Test
	public void verify() throws Exception {
		java.util.Map<java.lang.String, java.lang.Object> expected = new java.util.HashMap<String, Object>();
		expected.put("errcode", 0);
		expected.put("errmsg", "ok");
		java.lang.String accessToken = null;
		java.lang.String openId = "oU1fjv8UyBAxWqlGAExjW_8HHPlU";
		java.lang.String title = "Music Title";
		java.lang.String description = "Music Description";
		java.lang.String musicUrl = "https://www.youtube.com/watch?v=PT2_F-1esPk";
		java.lang.String hqMusicUrl = "https://www.youtube.com/watch?v=PT2_F-1esPk";
		java.lang.String thumbMediaId = "1A4fOnYgIEnu2sXaRo6m635cbmhbXQBz6r-dwNm2SBMiAAUXAWHoJdbSJ3Rz0y2G";
		assertEquals(getConnector().customerMusicMessage(accessToken, openId, title, description, musicUrl, hqMusicUrl,
				thumbMediaId), expected);
	}

}