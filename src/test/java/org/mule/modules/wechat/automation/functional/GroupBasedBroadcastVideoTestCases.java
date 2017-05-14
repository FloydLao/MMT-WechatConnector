package org.mule.modules.wechat.automation.functional;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.wechat.WechatConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class GroupBasedBroadcastVideoTestCases extends AbstractTestCase<WechatConnector> {

	public GroupBasedBroadcastVideoTestCases() {
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
		expected.put("errcode", 45065);
		expected.put("errmsg", "clientmsgid exist");
		expected.put("msg_id", "3147483650");
		java.lang.String accessToken = null;
		java.lang.String groupId = "110";
		java.lang.String mediaId = "t6Spy95et4P_TBbsrjDXIsljhW8xrXU9CwT0rm86CpY";
		assertEquals(getConnector().groupBasedBroadcastVideo(accessToken, groupId, mediaId).get("errcode"), expected.get("errcode"));
	}

}