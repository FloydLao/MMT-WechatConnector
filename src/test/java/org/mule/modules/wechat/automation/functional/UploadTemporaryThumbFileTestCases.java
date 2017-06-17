package org.mule.modules.wechat.automation.functional;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.wechat.WechatConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class UploadTemporaryThumbFileTestCases extends AbstractTestCase<WechatConnector> {

	public UploadTemporaryThumbFileTestCases() {
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
		expected.put("thumb_media_id", "");
		java.lang.String accessToken = null;
		org.w3c.dom.Document payload = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		javax.activation.DataSource fds = new javax.activation.FileDataSource(System.getProperty("user.dir") + "/src/test/java/file/Thumb.jpg");
		java.util.Map<java.lang.String, javax.activation.DataHandler> attachment = new java.util.HashMap<java.lang.String, javax.activation.DataHandler>();
		attachment.put("file", new javax.activation.DataHandler(fds));
		assertEquals(getConnector().uploadTemporaryThumbFile(accessToken, payload, attachment).containsKey("thumb_media_id"), expected.containsKey("thumb_media_id"));
	}

}