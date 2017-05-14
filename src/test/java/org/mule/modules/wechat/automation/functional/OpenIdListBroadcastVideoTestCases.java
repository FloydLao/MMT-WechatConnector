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
		expected.put("errcode", 45065);
		expected.put("errmsg", "clientmsgid exist");
		expected.put("msg_id", "3147483650");
		java.lang.String accessToken = null;
		java.lang.String mediaId = "guSo6hFCKfxMUsjyZKLbn_nEK6Vl4CxzVvFvhpPd_W6L4AHTyoR4Xs6_7_RV1Bz2";
		java.lang.String title = "OpenIdList Broadcast Video Title";
		java.lang.String description = "OpenIdList Broadcast Video Description";
		java.lang.String ApiName = "OpenIDListBroadcastVideo";
		java.util.Map<java.lang.String, java.lang.Object> toUser = new java.util.HashMap<String, Object>();
		toUser.put("touser", java.util.Arrays.asList("oRwGLwz_vjiN-rogxjjUoSzfPzWs","oRwGLwzaDc9z6IRhMyfBtjWs_mao"));
		assertEquals(getConnector().openIdListBroadcastVideo(accessToken, mediaId, title, description, ApiName, toUser).get("errcode"), expected.get("errcode"));
	}

}