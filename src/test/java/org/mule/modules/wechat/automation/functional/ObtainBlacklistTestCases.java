package org.mule.modules.wechat.automation.functional;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.wechat.WechatConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class ObtainBlacklistTestCases extends AbstractTestCase<WechatConnector> {

	public ObtainBlacklistTestCases() {
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
		expected.put("total", 1);
		java.lang.String accessToken = null;
		java.lang.String beginOpenId = "";
		assertEquals(getConnector().obtainBlacklist(accessToken, beginOpenId).containsKey("total"), expected.containsKey("total"));
	}

}