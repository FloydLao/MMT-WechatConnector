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

public class PreviewBroadcastVideoTestCases extends AbstractTestCase<WechatConnector> {

	public PreviewBroadcastVideoTestCases() {
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

		//Upload a video to get the mediaId
		java.lang.String accessToken = null;
		java.lang.String _title = "UploadVideo";
		java.lang.String introduction = "introduction";
		org.w3c.dom.Document payload = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		MimetypesFileTypeMap mfm = new MimetypesFileTypeMap();
		mfm.addMimeTypes("video/mp4 mp4");
		FileDataSource fds = new FileDataSource(System.getProperty("user.dir") + "/src/test/java/file/Video.mp4");
		fds.setFileTypeMap(mfm);
		java.util.Map<java.lang.String, DataHandler> attachment = new java.util.HashMap<java.lang.String, DataHandler>();
		attachment.put("file", new DataHandler(fds));
		
		java.lang.String toUser = "oRwGLwz_vjiN-rogxjjUoSzfPzWs";
		java.lang.String mediaId = String.valueOf(getConnector().uploadPermanentVideoFile(accessToken, _title, introduction, payload, attachment).get("media_id"));
		//Wait for the image to be process
		Thread.sleep(3000);
		assertEquals(getConnector().previewBroadcastVideo(accessToken, toUser, mediaId), expected);
	}

}