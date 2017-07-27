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

public class OpenIdListBroadcastVideoTestCases extends AbstractTestCase<WechatConnector> {

	public OpenIdListBroadcastVideoTestCases() {
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
		
		java.lang.String mediaId = String.valueOf(getConnector().uploadPermanentVideoFile(accessToken, _title, introduction, payload, attachment).get("media_id"));
		java.lang.String title = "OpenIdList Broadcast Video Title";
		java.lang.String description = "OpenIdList Broadcast Video Description";
		java.lang.String ApiName = "OpenIDListBroadcastVideo";
		java.util.Map<java.lang.String, java.lang.Object> toUser = new java.util.HashMap<String, Object>();
		toUser.put("touser", java.util.Arrays.asList("oRwGLwz_vjiN-rogxjjUoSzfPzWs","oRwGLwzaDc9z6IRhMyfBtjWs_mao"));
		//Wait for the video to be process
		Thread.sleep(3000);
		assertEquals(getConnector().openIdListBroadcastVideo(accessToken, mediaId, title, description, ApiName, toUser).get("errcode"), expected.get("errcode"));
	}

}