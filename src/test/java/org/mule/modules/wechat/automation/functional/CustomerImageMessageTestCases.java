package org.mule.modules.wechat.automation.functional;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.wechat.WechatConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class CustomerImageMessageTestCases extends AbstractTestCase<WechatConnector> {

	public CustomerImageMessageTestCases() {
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

		//Upload a image to get the mediaId
		java.lang.String accessToken = null;
		java.lang.String title = "UploadImage";
		org.w3c.dom.Document payload = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		javax.activation.DataSource fds = new javax.activation.FileDataSource(System.getProperty("user.dir") + "/src/test/java/file/Image.jpg");
		java.util.Map<java.lang.String, javax.activation.DataHandler> attachment = new java.util.HashMap<java.lang.String, javax.activation.DataHandler>();
		attachment.put("file", new javax.activation.DataHandler(fds));
		
		java.lang.String openId = "oRwGLwz_vjiN-rogxjjUoSzfPzWs";
		java.lang.String mediaId = String.valueOf(getConnector().uploadPermanentImageFile(accessToken, title, payload, attachment).get("media_id"));
		//Wait for the image to be process
		Thread.sleep(3000);
		assertEquals(getConnector().customerImageMessage(accessToken, openId, mediaId), expected);
	}

}