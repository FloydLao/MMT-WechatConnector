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
		java.lang.String expected = "<xml><ToUserName><![CDATA[gh_a62a2f2821be]]></ToUserName>\n<FromUserName><![CDATA[oRwGLwz_vjiN-rogxjjUoSzfPzWs]]></FromUserName>\n<CreateTime>1497706119</CreateTime>\n<MsgType><![CDATA[text]]></MsgType>\n<Content><![CDATA[Ihoh]]></Content>\n<MsgId>6432598800556566732</MsgId>\n</xml>";
		java.lang.String encrytedXml = "<xml>\n    <ToUserName><![CDATA[gh_a62a2f2821be]]></ToUserName>\n    <Encrypt><![CDATA[uy4SSsU5Xkhth0h61OUAnrSi1N3S6X6UF4uu9YXDIQYWBw99NhHPrmvpgjZMpgV3PwdraND9axTg4GqNBucGpuC9YmBRr+nKslTmjYPrsM+BGLRVI5JSUNrj3C+yDunyuKw22zJBgjHO6mxIbY8cukMAN8lE8RnLrz0YZsoo5jrP55lJ8nr1YxtBxfuJku5j+hgaXBCnGXbqVW2+6PhjZTlNzxFR95dRcbOPdAVWH828QdgpcbuAzp5BKRuxhaEUUCv69z+fqRXgjNrkLyv+PFf+6+22nSN3uHwRwH5SNjw80FuGBOqI6N1KKFr5proaGsMCBlabvONQUjznVlA3ifdFsF6CAjicZFZBXXsSPDJp4nwTtFmsZddHp98kFg7OzyaKL0ze7YmF+Ckwh9bmD6YHzrTjfHB+Cy8ONol7jaA=]]></Encrypt>\n</xml>";
		java.lang.String encodingAesKey = "ig7gFVnrXDaK5koZooBGCeysMSGI8H34PKeybRQ96pe";
		java.lang.String uri = "/msg?signature=dd7a05727f6f5a9ff743f284f84cdfb924272c29&timestamp=1497706120&nonce=287773347&openid=oRwGLwz_vjiN-rogxjjUoSzfPzWs&encrypt_type=aes&msg_signature=9aae1f2a297d4a5f29636fa2bce70de6ca074065";
		assertEquals(getConnector().messageDecryption(encrytedXml, encodingAesKey, uri).trim(), expected);
	}

}