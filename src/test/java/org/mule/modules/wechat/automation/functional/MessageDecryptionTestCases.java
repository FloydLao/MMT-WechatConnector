package org.mule.modules.wechat.automation.functional;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.wechat.WechatConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class MessageDecryptionTestCases extends AbstractTestCase<WechatConnector> {

	public MessageDecryptionTestCases() {
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
		java.lang.String expected = "<xml><ToUserName><![CDATA[gh_a62a2f2821be]]></ToUserName>\n<FromUserName><![CDATA[oRwGLwz_vjiN-rogxjjUoSzfPzWs]]></FromUserName>\n<CreateTime>1494659229</CreateTime>\n<MsgType><![CDATA[text]]></MsgType>\n<Content><![CDATA[Hihi]]></Content>\n<MsgId>6419512507651604245</MsgId>\n</xml>";
		java.lang.String encrytedXml = "<xml><ToUserName><![CDATA[gh_a62a2f2821be]]></ToUserName><Encrypt><![CDATA[c37rivjPyHpnangjCJt7P+TbULf36cqkhIYfz1b+oBgDeOkwIJQ/eQXQzneu/dmd62+jbEgeG5Y9n83MJCjxtOXKbXxPSU3S/IdcuTv8M3RSVx6ZD6a8oVOiBMZUE4rXa7oJPQqyxp09E5H4dX1cjL47VWJtld0AhTPzWr7rKAbjxl4dvDPsl15yNQPIGbf5UeBJ13Kgkf9/LPKYZONGP7VRwMLhvmxT35Mk1dCjVI1CfYl8yUp6J10HMVEw2XFNVCkVqHNpy5MJvxHXKpPjeqeSuv4n6wlw11J6H0Z9VYDa9aZPpt0XmAUBtqep4e0X6wlBPwt4hFjKYMb8w2zn9tHaz72rc49KVvPW6cuB1zkOsgxRx7hvFntd7hrnaUFUYrWMIv9SdfDHQZhZ4zEr0nJeIJSJVGpVTlAZSeQ+P+Q=]]></Encrypt></xml>";
		java.lang.String encodingAesKey = "ig7gFVnrXDaK5koZooBGCeysMSGI8H34PKeybRQ96pe";
		java.lang.String uri = "/msg?signature=cd6f1a7cee05e49b153d2de38018eacb8a3408c1&timestamp=1494659230&nonce=307897310&openid=oRwGLwz_vjiN-rogxjjUoSzfPzWs&encrypt_type=aes&msg_signature=11cc79dccf25155e69277019cbe667a4ab8c1e82";
		assertEquals(getConnector().messageDecryption(encrytedXml, encodingAesKey, uri).trim(), expected);
	}

}