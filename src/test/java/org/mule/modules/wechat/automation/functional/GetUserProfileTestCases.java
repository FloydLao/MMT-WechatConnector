/**
 * (c) 2003-2017 MMT, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
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
	}

	@After
	public void tearDown() {
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