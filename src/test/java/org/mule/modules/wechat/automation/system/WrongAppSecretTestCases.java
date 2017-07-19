package org.mule.modules.wechat.automation.system;

import static org.junit.Assert.*;

import java.util.List;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.MimetypesFileTypeMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.wechat.WechatConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class WrongAppSecretTestCases extends AbstractTestCase<WechatConnector> {

	public WrongAppSecretTestCases() {
		super(WechatConnector.class);
	}

	private String appId;
	private String appSecret;
	
	@Before
	public void setup() throws Exception {
		appId = "wx47b6603c2007770b";
		appSecret = "WrongAppSecret";
        Object[] signature = {null, appId, appSecret};
		try {
			getDispatcher().initializeSource("getAccessToken", signature);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@After
	public void tearDown() throws Exception {
		try {
			getDispatcher().shutDownSource("getAccessToken");
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Test
	public void verify() throws Exception {
		List<Object> events = getDispatcher().getSourceMessages("getAccessToken");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			
		}
		assertTrue(events.get(0).toString().contains("40125"));
	}

}