package org.mule.modules.wechat.automation.functional;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.wechat.WechatConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class OpenIdListBroadcastVideoTestCases extends AbstractTestCase<WechatConnector> {

	public OpenIdListBroadcastVideoTestCases() {
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
		java.lang.String mediaId = "pjvRfkQdW9p998FHu--quas9au3iLgXphA2brAMz2Bk";
		java.lang.String title = "OpenIdList Broadcast Video Title";
		java.lang.String description = "OpenIdList Broadcast Video Description";
		java.lang.String ApiName = "OpenIDListBroadcastVideo";
		java.util.Map<java.lang.String, java.lang.Object> toUser = new java.util.HashMap<String, Object>();
		toUser.put("touser", java.util.Arrays.asList("oU1fjv8UyBAxWqlGAExjW_8HHPlU","oU1fjv9f3ddlyF3V1OpYgQGcQgyI"));
		assertEquals(getConnector().openIdListBroadcastVideo(accessToken, mediaId, title, description, ApiName, toUser).get("errcode"), expected.get("errcode"));
	}

}