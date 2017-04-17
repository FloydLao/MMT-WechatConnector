  package org.mule.modules.wechat.automation.functional;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.wechat.WechatConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class MessageEncrytionTestCases extends AbstractTestCase<WechatConnector> {

	public MessageEncrytionTestCases() {
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
		java.lang.String expected = "<xml>\n<Encrypt><![CDATA[3mI5wVhqEhxjAl9OZo5rQOS9zr7wcG5wMxqpAIqSOstIO4wW4uuWku+RIRfQpBkAzfmhWnykNuinERvuStoARg+brlN+W1IjCTCO4Gdmi+4CFkGZz0NMfwQ590tNvqwSotx14lkot7eFypwB579mUxVggKJrfJ96MeU83Ck+zmPkkI6P6Yaq/uOE00xtDVfaHgDChWx088m6wM8AdPfvQfz63e985I5P/viy+zL9K3zo17mlnQGEvqco8BS9VSXxnwobVmBwg2SWjnhix1LAD8JMnWTN4r8gZcBI/mfOwmQ/MWzrMjD412T4noXkPAYEpEoMevo/LZoS+6NmBE1eA8ZfrCoxaSGvuHrnv3In5KO/mM3lgCdJ6kiyx2kiSTPFjt7yk8t8PNOHU/PlrwwlQMmTPMTNqutc2wXQDrrATZw=]]></Encrypt>\n<MsgSignature><![CDATA[8539714922d958ca53277321d46d3e790fd14478]]></MsgSignature>\n<TimeStamp>1491543326</TimeStamp>\n<Nonce><![CDATA[301374033]]></Nonce>\n</xml>";
		java.lang.String replyXml = "<xml><ToUserName><![CDATA[oU1fjv8UyBAxWqlGAExjW_8HHPlU]]></ToUserName><FromUserName><![CDATA[gh_8aec0d8afbd1]]></FromUserName><CreateTime>1489683323</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[Hello World!]]></Content><FuncFlag>0</FuncFlag></xml>";
		java.lang.String encodingAesKey = "6n6F6pYqpgLwvsn6CIvgfuR1SESzwEjzFWSX2BzXtAS";
		java.lang.String uri = "/msg?signature=4808f28c03ca9db6d84c05c3b81ecd59dc869349&timestamp=1491543326&nonce=301374033&openid=oU1fjv8UyBAxWqlGAExjW_8HHPlU&encrypt_type=aes&msg_signature=d49b10890375855fa8f1e2f6699aeba34cd433d2";
		assertEquals(getConnector().messageEncrytion(replyXml, encodingAesKey, uri).contains("<Encrypt>"), expected.contains("<Encrypt>"));
	}

}