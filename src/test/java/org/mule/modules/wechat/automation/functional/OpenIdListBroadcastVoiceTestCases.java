package org.mule.modules.wechat.automation.functional;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.wechat.WechatConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class OpenIdListBroadcastVoiceTestCases extends AbstractTestCase<WechatConnector> {

	public OpenIdListBroadcastVoiceTestCases() {
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
		java.lang.String mediaId = "pjvRfkQdW9p998FHu--quTRYRg91ZO9cjEoiI3Y_1BU";
		java.lang.String ApiName = "OpenIDListBroadcastVoice";
		java.util.Map<java.lang.String, java.lang.Object> toUser = new java.util.HashMap<String, Object>();
		toUser.put("touser", java.util.Arrays.asList("oU1fjv8UyBAxWqlGAExjW_8HHPlU","oU1fjv9f3ddlyF3V1OpYgQGcQgyI"));
		assertEquals(getConnector().openIdListBroadcastVoice(accessToken, mediaId, ApiName, toUser).get("errcode"), expected.get("errcode"));
	}

}