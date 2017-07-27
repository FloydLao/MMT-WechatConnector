/**
 * (c) 2003-2017 MMT, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
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
	}

	@After
	public void tearDown() {
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