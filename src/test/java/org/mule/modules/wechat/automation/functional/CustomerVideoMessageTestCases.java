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

public class CustomerVideoMessageTestCases extends AbstractTestCase<WechatConnector> {

	public CustomerVideoMessageTestCases() {
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

		//Upload a thumb to get the mediaId
		java.lang.String accessToken = null;
		java.lang.String _title = "UploadThumb";
		org.w3c.dom.Document payload = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		javax.activation.DataSource fds = new javax.activation.FileDataSource(System.getProperty("user.dir") + "/src/test/java/file/Thumb.jpg");
		java.util.Map<java.lang.String, javax.activation.DataHandler> attachment = new java.util.HashMap<java.lang.String, javax.activation.DataHandler>();
		attachment.put("file", new javax.activation.DataHandler(fds));
		
		//Upload a video to get the mediaId
		java.lang.String __title = "UploadVideo";
		java.lang.String introduction = "introduction";
		org.w3c.dom.Document _payload = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		MimetypesFileTypeMap mfm = new MimetypesFileTypeMap();
		mfm.addMimeTypes("video/mp4 mp4");
		FileDataSource _fds = new FileDataSource(System.getProperty("user.dir") + "/src/test/java/file/Video.mp4");
		_fds.setFileTypeMap(mfm);
		java.util.Map<java.lang.String, DataHandler> _attachment = new java.util.HashMap<java.lang.String, DataHandler>();
		_attachment.put("file", new DataHandler(_fds));
		
		java.lang.String openId = "oRwGLwz_vjiN-rogxjjUoSzfPzWs";
		java.lang.String mediaId = String.valueOf(getConnector().uploadPermanentVideoFile(accessToken, __title, introduction, _payload, _attachment).get("media_id"));
		java.lang.String thumbMediaId = String.valueOf(getConnector().uploadPermanentThumbFile(accessToken, _title, payload, attachment).get("media_id"));
		//Wait for the thumb to be process
		Thread.sleep(3000);
		assertEquals(getConnector().customerVideoMessage(accessToken, openId, mediaId, thumbMediaId), expected);
	}

}