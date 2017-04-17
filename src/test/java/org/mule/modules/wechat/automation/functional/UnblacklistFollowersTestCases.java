package org.mule.modules.wechat.automation.functional;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.wechat.WechatConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class UnblacklistFollowersTestCases extends AbstractTestCase<WechatConnector> {

	public UnblacklistFollowersTestCases() {
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
		java.lang.String ApiName = "UnblacklistFollowers";
		java.util.Map<java.lang.String, java.lang.Object> openidList = new java.util.HashMap<String, Object>();
		openidList.put("openid_list", java.util.Arrays.asList("oU1fjv6xniBQRFYJN-v32bXyaAoU"));
		assertEquals(getConnector().unblacklistFollowers(accessToken, ApiName, openidList), expected);
	}

}