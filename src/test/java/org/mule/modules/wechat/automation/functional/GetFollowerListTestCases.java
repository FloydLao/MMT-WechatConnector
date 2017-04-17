package org.mule.modules.wechat.automation.functional;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.wechat.WechatConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class GetFollowerListTestCases extends AbstractTestCase<WechatConnector> {

	public GetFollowerListTestCases() {
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
		expected.put("total", 3);
		expected.put("count", 3);
		java.lang.String accessToken = null;
		java.lang.String nextOpenId = null;
		assertEquals(getConnector().getFollowerList(accessToken, nextOpenId).containsKey("count"), expected.containsKey("count"));
	}

}