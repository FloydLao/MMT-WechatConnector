package org.mule.modules.wechat.automation.functional;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.wechat.WechatConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class GroupBasedBroadcastImageTestCases extends AbstractTestCase<WechatConnector> {

	public GroupBasedBroadcastImageTestCases() {
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

		//Upload a image to get the mediaId
		java.lang.String accessToken = null;
		java.lang.String title = "UploadImage";
		org.w3c.dom.Document payload = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		javax.activation.DataSource fds = new javax.activation.FileDataSource(System.getProperty("user.dir") + "/src/test/java/file/Image.jpg");
		java.util.Map<java.lang.String, javax.activation.DataHandler> attachment = new java.util.HashMap<java.lang.String, javax.activation.DataHandler>();
		attachment.put("file", new javax.activation.DataHandler(fds));
		
		java.lang.String groupId = "110";
		java.lang.String mediaId = String.valueOf(getConnector().uploadPermanentImageFile(accessToken, title, payload, attachment).get("media_id"));
		//Wait for the image to be process
		Thread.sleep(3000);
		assertEquals(getConnector().groupBasedBroadcastImage(accessToken, groupId, mediaId).get("errcode"), expected.get("errcode"));
	}

}