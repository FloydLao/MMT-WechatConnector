package org.mule.modules.wechat.automation.functional;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.wechat.WechatConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class GroupBasedBroadcastImageTestCases extends AbstractTestCase<WechatConnector> {

	public GroupBasedBroadcastImageTestCases() {
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
		expected.put("errmsg", "send job submission success");
		expected.put("msg_id", "3147483650");
		java.lang.String accessToken = null;
		java.lang.String groupId = "110";
		java.lang.String mediaId = "t6Spy95et4P_TBbsrjDXItelK7mP9sLc1XuiFWIDzEs";
		assertEquals(getConnector().groupBasedBroadcastImage(accessToken, groupId, mediaId).get("errcode"), expected.get("errcode"));
	}

}