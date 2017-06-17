package org.mule.modules.wechat.automation.functional;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.wechat.WechatConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class QueryBroadcastStatusTestCases extends AbstractTestCase<WechatConnector> {

	public QueryBroadcastStatusTestCases() {
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
		expected.put("msg_status", "SEND_SUCCESS");

		//Send a text broadcast, so that this test can have msgId to query it
		java.lang.String accessToken = null;
		java.lang.String groupId = "110";
		java.lang.String content = "Group Broadcast" + System.currentTimeMillis();
		java.lang.String msgId = String.valueOf(getConnector().groupBasedBroadcastText(accessToken, groupId, content).get("msg_id"));
		assertEquals(getConnector().queryBroadcastStatus(accessToken, msgId).containsKey("msg_status"), expected.containsKey("msg_status"));
	}

}