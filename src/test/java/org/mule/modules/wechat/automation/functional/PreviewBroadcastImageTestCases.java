package org.mule.modules.wechat.automation.functional;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.wechat.WechatConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class PreviewBroadcastImageTestCases extends AbstractTestCase<WechatConnector> {

	public PreviewBroadcastImageTestCases() {
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
		expected.put("errmsg", "preview success");
		java.lang.String accessToken = null;
		java.lang.String toUser = "oRwGLwz_vjiN-rogxjjUoSzfPzWs";
		java.lang.String mediaId = "t6Spy95et4P_TBbsrjDXItelK7mP9sLc1XuiFWIDzEs";
		assertEquals(getConnector().previewBroadcastImage(accessToken, toUser, mediaId), expected);
	}

}