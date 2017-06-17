package org.mule.modules.wechat.automation.functional;

import static org.junit.Assert.*;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.MimetypesFileTypeMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.wechat.WechatConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class GroupBasedBroadcastVoiceTestCases extends AbstractTestCase<WechatConnector> {

	public GroupBasedBroadcastVoiceTestCases() {
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

		//Upload a voice to get the mediaId
		java.lang.String accessToken = null;
		java.lang.String title = "UploadVoice";
		org.w3c.dom.Document payload = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		MimetypesFileTypeMap mfm = new MimetypesFileTypeMap();
		mfm.addMimeTypes("audio/mp3 mp3");
		FileDataSource fds = new FileDataSource(new java.io.File(System.getProperty("user.dir") + "/src/test/java/file/Voice.mp3"));
		fds.setFileTypeMap(mfm);
		java.util.Map<java.lang.String, DataHandler> attachment = new java.util.HashMap<java.lang.String, DataHandler>();
		attachment.put("file", new DataHandler(fds));
		
		java.lang.String groupId = "110";
		java.lang.String mediaId = String.valueOf(getConnector().uploadPermanentVoiceFile(accessToken, title, payload, attachment).get("media_id"));
		//Wait for the voice to be process
		Thread.sleep(3000);
		assertEquals(getConnector().groupBasedBroadcastVoice(accessToken, groupId, mediaId).get("errcode"), expected.get("errcode"));
	}

}