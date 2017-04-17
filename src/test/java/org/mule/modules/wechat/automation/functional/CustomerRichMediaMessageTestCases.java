package org.mule.modules.wechat.automation.functional;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.wechat.WechatConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class CustomerRichMediaMessageTestCases extends AbstractTestCase<WechatConnector> {

	public CustomerRichMediaMessageTestCases() {
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
		expected.put("errmsg", "ok");
		java.lang.String accessToken = null;
		java.lang.String openId = "oU1fjv8UyBAxWqlGAExjW_8HHPlU";
		java.lang.String ApiName = "CustomerRichMediaMessage";
		java.util.List<java.util.Map<java.lang.String, java.lang.Object>> articles = null;
		java.util.Map<java.lang.String, java.lang.Object> _articles = new java.util.HashMap<String, Object>();
		_articles.put("title", "RichMedia Title");
		_articles.put("description", "RichMedia Description");
		_articles.put("url", "http://mp.weixin.qq.com/s?__biz=MzIxOTM0NzI4Mw==&mid=100000006&idx=1&sn=f44ba37828c235af33652504a2673489&chksm=17dde13a20aa682cbf78c409750f8415b111da21f14d15f9630696d2f38b50207197283f833e#rd");
		_articles.put("picurl", "http://mmbiz.qpic.cn/mmbiz_png/pVJSPTdJ4r1Siaz7KJxQaZMoyzHXVSf4Ih7hxVzRkPG91iapk6mCJBibWKggqdhkwJ363LcicwzWL6hicfDPB4cl0Rg/0?wx_fmt=png");
		articles = java.util.Arrays.asList(_articles);
		assertEquals(getConnector().customerRichMediaMessage(accessToken, openId, ApiName, articles), expected);
	}

}