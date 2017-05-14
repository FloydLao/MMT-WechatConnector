package org.mule.modules.wechat.automation.functional;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.wechat.WechatConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class DeleteBroadcastMessageTestCases extends AbstractTestCase<WechatConnector> {

	public DeleteBroadcastMessageTestCases() {
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
		expected.put("errcode", 45009);
		expected.put("errmsg", "reach max api daily quota limit");
		java.lang.String accessToken = null;
		java.lang.String msgId = "3147483650";
		assertEquals(getConnector().deleteBroadcastMessage(accessToken, msgId).get("errcode"), expected.get("errcode"));
	}

}