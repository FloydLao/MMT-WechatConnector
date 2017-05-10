package org.mule.modules.wechat.automation.functional;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.wechat.WechatConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class BatchTagFollowersTestCases extends AbstractTestCase<WechatConnector> {

	public BatchTagFollowersTestCases() {
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
		expected.put("errmsg", "ok");
		java.lang.String accessToken = null;
		java.lang.Integer tagId = 105;
		java.lang.String ApiName = "BatchTagFollowers";
		java.util.Map<java.lang.String, java.lang.Object> openidList = new java.util.HashMap<String, Object>();
		openidList.put("openid_list", java.util.Arrays.asList("oRwGLwz_vjiN-rogxjjUoSzfPzWs", "oRwGLw2OnKPY731KwiEqkEYv-l9o", "oRwGLw06gGrCdz_eXqkgCTF8wNw0"));
		assertEquals(getConnector().batchTagFollowers(accessToken, tagId, ApiName, openidList), expected);
	}

}