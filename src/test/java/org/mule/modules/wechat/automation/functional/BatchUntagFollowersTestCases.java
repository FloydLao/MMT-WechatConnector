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

public class BatchUntagFollowersTestCases extends AbstractTestCase<WechatConnector> {

	public BatchUntagFollowersTestCases() {
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
		expected.put("errcode", 0);
		expected.put("errmsg", "ok");
		java.lang.String accessToken = null;
		java.lang.Integer tagId = 110;
		java.lang.String ApiName = "UnblacklistFollowers";
		java.util.Map<java.lang.String, java.lang.Object> openidList = new java.util.HashMap<String, Object>();
		openidList.put("openid_list", java.util.Arrays.asList("oRwGLwz_vjiN-rogxjjUoSzfPzWs", "oRwGLw2OnKPY731KwiEqkEYv-l9o", "oRwGLw06gGrCdz_eXqkgCTF8wNw0"));
		assertEquals(getConnector().batchUntagFollowers(accessToken, tagId, ApiName, openidList), expected);
	}

}