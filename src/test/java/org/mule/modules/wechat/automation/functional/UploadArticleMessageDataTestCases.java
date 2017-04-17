package org.mule.modules.wechat.automation.functional;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.wechat.WechatConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class UploadArticleMessageDataTestCases extends AbstractTestCase<WechatConnector> {

	public UploadArticleMessageDataTestCases() {
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
		expected.put("media_id", "");
		java.lang.String accessToken = null;
		java.lang.String ApiName = "UploadArticleMessageData";
		java.util.List<java.util.Map<java.lang.String, java.lang.Object>> articles =  null;
		java.util.Map<java.lang.String, java.lang.Object> _articles = new java.util.HashMap<String, Object>();
		_articles.put("thumb_media_id", "1A4fOnYgIEnu2sXaRo6m635cbmhbXQBz6r-dwNm2SBMiAAUXAWHoJdbSJ3Rz0y2G");
		_articles.put("author", "Test Person");
		_articles.put("title", "Article Message Title");
		_articles.put("content_source_url", "www.qq.com");
		_articles.put("content", "Article Message Content");
		_articles.put("digest", "Article Message Digest");
		_articles.put("show_cover_pic", 0);
		articles = java.util.Arrays.asList(_articles);
		assertEquals(getConnector().uploadArticleMessageData(accessToken, ApiName, articles).containsKey("media_id"), expected.containsKey("media_id"));
	}

}