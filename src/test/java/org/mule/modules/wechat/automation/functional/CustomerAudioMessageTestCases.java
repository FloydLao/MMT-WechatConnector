/**
 * (c) 2003-2017 MMT, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
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

public class CustomerAudioMessageTestCases extends AbstractTestCase<WechatConnector> {

	public CustomerAudioMessageTestCases() {
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
		expected.put("errmsg", "ok");
		
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
		
		java.lang.String openId = "oRwGLwz_vjiN-rogxjjUoSzfPzWs";
		java.lang.String mediaId = String.valueOf(getConnector().uploadPermanentVoiceFile(accessToken, title, payload, attachment).get("media_id"));
		//Wait for the voice to be process
		Thread.sleep(3000);
		assertEquals(getConnector().customerAudioMessage(accessToken, openId, mediaId), expected);
	}

}