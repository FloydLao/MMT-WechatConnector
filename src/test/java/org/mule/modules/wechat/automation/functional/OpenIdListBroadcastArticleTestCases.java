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

public class OpenIdListBroadcastArticleTestCases extends AbstractTestCase<WechatConnector> {

	public OpenIdListBroadcastArticleTestCases() {
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
		expected.put("errmsg", "send job submission success");
		expected.put("msg_id", "3147483650");

		//Upload a thumb image to get the thumbMediaId
		java.lang.String accessToken = null;
		org.w3c.dom.Document payload = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		javax.activation.DataSource fds = new javax.activation.FileDataSource(System.getProperty("user.dir") + "/src/test/java/file/Thumb.jpg");
		java.util.Map<java.lang.String, javax.activation.DataHandler> attachment = new java.util.HashMap<java.lang.String, javax.activation.DataHandler>();
		attachment.put("file", new javax.activation.DataHandler(fds));
		java.lang.String thumbMediaId  = String.valueOf(getConnector().uploadTemporaryThumbFile(accessToken, payload, attachment).get("thumb_media_id"));
				
		//Upload a article message to get the mediaId
		java.lang.String _ApiName = "UploadArticleMessageData";
		java.util.List<java.util.Map<java.lang.String, java.lang.Object>> articles =  null;
		java.util.Map<java.lang.String, java.lang.Object> _articles = new java.util.HashMap<String, Object>();
		_articles.put("thumb_media_id", thumbMediaId);
		_articles.put("author", "Test Person");
		_articles.put("title", "Article Message Title");
		_articles.put("content_source_url", "www.qq.com");
		_articles.put("content", "Article Message Content");
		_articles.put("digest", "Article Message Digest");
		_articles.put("show_cover_pic", 0);
		articles = java.util.Arrays.asList(_articles);
		
		java.lang.String mediaId = String.valueOf(getConnector().uploadArticleMessageData(accessToken, _ApiName, articles).get("media_id"));
		java.lang.String ApiName = "OpenIDListBroadcastArticle";
		java.util.Map<java.lang.String, java.lang.Object> toUser = new java.util.HashMap<String, Object>();
		toUser.put("touser", java.util.Arrays.asList("oRwGLwz_vjiN-rogxjjUoSzfPzWs","oRwGLwzaDc9z6IRhMyfBtjWs_mao"));
		assertEquals(getConnector().openIdListBroadcastArticle(accessToken, mediaId, ApiName, toUser).get("errcode"), expected.get("errcode"));
	}

}