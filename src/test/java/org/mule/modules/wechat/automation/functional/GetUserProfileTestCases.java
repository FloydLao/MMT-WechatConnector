package org.mule.modules.wechat.automation.functional;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.wechat.WechatConnector;
import org.mule.modules.wechat.WechatConnector.Lang;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class GetUserProfileTestCases extends AbstractTestCase<WechatConnector> {

	public GetUserProfileTestCases() {
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
		expected.put("openid", "oRwGLwz_vjiN-rogxjjUoSzfPzWs");
		java.lang.String accessToken = null;
		java.lang.String openId = "oRwGLwz_vjiN-rogxjjUoSzfPzWs";
		org.mule.modules.wechat.WechatConnector.Lang lang = Lang.zh_CN;
		assertEquals(getConnector().getUserProfile(accessToken, openId, lang).get("openid"), expected.get("openid"));
	}

}