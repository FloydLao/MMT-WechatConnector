package org.mule.modules.wechat.automation.functional;

import static org.junit.Assert.*;

import javax.activation.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.wechat.WechatConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class UploadVoiceFileTestCases extends AbstractTestCase<WechatConnector> {

	public UploadVoiceFileTestCases() {
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
		org.w3c.dom.Document payload = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		MimetypesFileTypeMap mfm = new MimetypesFileTypeMap();
		mfm.addMimeTypes("audio/mp3 mp3");
		FileDataSource fds = new FileDataSource(new java.io.File(System.getProperty("user.dir") + "/src/test/java/file/Voice.mp3"));
		fds.setFileTypeMap(mfm);
		java.util.Map<java.lang.String, DataHandler> attachment = new java.util.HashMap<java.lang.String, DataHandler>();
		attachment.put("file", new DataHandler(fds));
		assertEquals(getConnector().uploadVoiceFile(accessToken, payload, attachment).containsKey("media_id"), expected.containsKey("media_id"));
	}

}