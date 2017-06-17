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
		java.lang.String expected = "<xml>\n<Encrypt><![CDATA[1KNi7KG7PdVKSNdGrwUZgfHoVMNXAS+FQ+iYOMZ4+/I3DJUhN6NfT4KruZIS8ZcYwR11uhAmq5nxm7/Uh8yEwi2VoV2zwR9WHOhlZCOU2nbj6OrThoo4/xHs+jC49BiJ217acZZEImF25M4z6JV+nB7oV9G0MaNg8NVuZ4dkwYDUe4+kfOQirCz9sdmmlBaD8YUiiIZq6Kh6zVgHFYcQRcZ0xoYcM+cOshUXWDlFWEmH7MKhsc8of27/3/OFIOHlw3pAUEuZmZ52V08tVJQmbRd++NCbneWouWAcFyIrklbCNXL1jPu6oS/vvuaCccy1zNcf0/CtedUpj2sgdCj5t7ONUXWELHOBbG/YkXBxa/wc+PMHGQ7DGyibT0NNTd+Ff5C8PwsUbLatc736wsdBgL9674EAog0Ku3fUvJ9DXEN93pFIejSFjs2S2Q657BLe2wp5aV5WE78x7E0UVU7wgA==]]></Encrypt>\n<MsgSignature><![CDATA[b2194a59653a6d20afa4e32331a946633ea594f8]]></MsgSignature>\n<TimeStamp>1494659230</TimeStamp>\n<Nonce><![CDATA[307897310]]></Nonce>\n</xml>";
		java.lang.String replyXml = "<xml>\n  <ToUserName><![CDATA[oRwGLwz_vjiN-rogxjjUoSzfPzWs]]></ToUserName>\n  <FromUserName><![CDATA[gh_a62a2f2821be]]></FromUserName>\n  <CreateTime>1489683323</CreateTime>\n  <MsgType><![CDATA[text]]></MsgType>\n  <Content><![CDATA[Hello World!]]></Content>\n  <FuncFlag>0</FuncFlag>\n</xml>";
		java.lang.String encodingAesKey = "ig7gFVnrXDaK5koZooBGCeysMSGI8H34PKeybRQ96pe";
		java.lang.String uri = "/msg?signature=dd7a05727f6f5a9ff743f284f84cdfb924272c29&timestamp=1497706120&nonce=287773347&openid=oRwGLwz_vjiN-rogxjjUoSzfPzWs&encrypt_type=aes&msg_signature=9aae1f2a297d4a5f29636fa2bce70de6ca074065";
		assertEquals(getConnector().messageEncrytion(replyXml, encodingAesKey, uri).contains("<Encrypt>"), expected.contains("<Encrypt>"));
	}

}