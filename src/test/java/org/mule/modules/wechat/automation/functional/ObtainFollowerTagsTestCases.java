package org.mule.modules.wechat.automation.functional;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.wechat.WechatConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class ObtainFollowerTagsTestCases extends AbstractTestCase<WechatConnector> {

	public ObtainFollowerTagsTestCases() {
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
		expected.put("tagid_list", 101);
		java.lang.String accessToken = null;
		java.lang.String openId = "oRwGLw7-FmWPRt7DKYqU7rIGWKvE";
		assertEquals(getConnector().obtainFollowerTags(accessToken, openId).containsKey("tagid_list"), expected.containsKey("tagid_list"));
	}

}