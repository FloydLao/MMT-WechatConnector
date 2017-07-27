/**
 * (c) 2003-2017 MMT, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.wechat.automation.system;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.callback.SourceCallback;
import org.mule.modules.wechat.WechatConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class WrongAppIDTestCases extends AbstractTestCase<WechatConnector> {

	public WrongAppIDTestCases() {
		super(WechatConnector.class);
	}
	
	private String appId;
	private String appSecret;
	
	@Before
	public void setup() throws Exception {
		appId = "WrongAppID";
		appSecret = "db03ad92747fa53d4ac27dff4b072211";
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
		assertTrue(events.get(0).toString().contains("40013"));
	}

}