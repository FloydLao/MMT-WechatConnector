package org.mule.modules.wechat.automation.functional;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.wechat.WechatConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class PreviewBroadcastArticleTestCases extends AbstractTestCase<WechatConnector> {

	public PreviewBroadcastArticleTestCases() {
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
		java.lang.String toUser = "oU1fjv8UyBAxWqlGAExjW_8HHPlU";
		java.lang.String mediaId = "pjvRfkQdW9p998FHu--quYBS94plUt_hcySfamHZsto";
		assertEquals(getConnector().previewBroadcastArticle(accessToken, toUser, mediaId), expected);
	}

}