package org.mule.modules.wechat.automation.functional;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.wechat.WechatConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class GroupBasedBroadcastArticleTestCases extends AbstractTestCase<WechatConnector> {

	public GroupBasedBroadcastArticleTestCases() {
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
		java.lang.String mediaId = "t6Spy95et4P_TBbsrjDXIs9NJG_saO9m0yrn1cOc3Po";
		assertEquals(getConnector().groupBasedBroadcastArticle(accessToken, groupId, mediaId).get("errcode"), expected.get("errcode"));
	}

}