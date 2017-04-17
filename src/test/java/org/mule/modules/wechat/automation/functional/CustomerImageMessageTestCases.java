package org.mule.modules.wechat.automation.functional;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.wechat.WechatConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class CustomerImageMessageTestCases extends AbstractTestCase<WechatConnector> {

	public CustomerImageMessageTestCases() {
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
		java.lang.String mediaId = "pjvRfkQdW9p998FHu--quYihqJqJ8hv9buhFh_h83O8";
		assertEquals(getConnector().customerImageMessage(accessToken, openId, mediaId), expected);
	}

}