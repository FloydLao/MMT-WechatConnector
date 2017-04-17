package org.mule.modules.wechat.automation.functional;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.wechat.WechatConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class NameRemarkTestCases extends AbstractTestCase<WechatConnector> {

	public NameRemarkTestCases() {
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
		java.lang.Object expected = "{errcode=0, errmsg=ok}";
		java.lang.String accessToken = null;
		java.lang.String openId = "oU1fjv6xniBQRFYJN-v32bXyaAoU";
		java.lang.String remark = "牛日asdf";
		assertEquals(getConnector().nameRemark(accessToken, openId, remark).toString(), expected);
	}

}