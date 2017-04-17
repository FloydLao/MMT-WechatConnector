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
		java.lang.String expected = "<xml><ToUserName><![CDATA[gh_8aec0d8afbd1]]></ToUserName>\n<FromUserName><![CDATA[oU1fjv8UyBAxWqlGAExjW_8HHPlU]]></FromUserName>\n<CreateTime>1491543326</CreateTime>\n<MsgType><![CDATA[text]]></MsgType>\n<Content><![CDATA[Fff]]></Content>\n<MsgId>6406129806160182226</MsgId>\n</xml>";
		java.lang.String encrytedXml = "<xml><ToUserName><![CDATA[gh_8aec0d8afbd1]]></ToUserName><Encrypt><![CDATA[1JJ0FWx2QcKETIGmOzzIzagOKEk6SlCDmKfw27lwcWXi7BVcuKc+BXLxB4TbsmXYvqQpyTs9abQzAzAnP8dXaMuHDtRrHvsvTqU61kmK7Db/5HpEAeAvYpJEyQX2nr+fsPz7U80KQ8zXISA7I9vnG/7NuP4Fzgkk4r8CxJG4fyDotFV7m0L0ldmlyshy9KIYzHkHQw7ry6PWwvKeE3OD4wdYn10X6cgKGTjr/9DgJugI4pyjEQ5S5Q/cqmjZEAIC0eAIbXqQVnK6Ij/WRLZc5KCxPX1qwo+/rfn59JolULd+cwzjGXiGTNDJyF0pIkNLx+QDwhZkwogDfKciVtO6hT9ZFfqKP0XfBsimjQbSbSOHsSZqVZVm2jh75HpBgj+x/lZGff3BymiSLmdb3qdJ0CZaoe5PjnX/UwSTDpQtQUg=]]></Encrypt></xml>";
		java.lang.String encodingAesKey = "6n6F6pYqpgLwvsn6CIvgfuR1SESzwEjzFWSX2BzXtAS";
		java.lang.String uri = "/msg?signature=4808f28c03ca9db6d84c05c3b81ecd59dc869349&timestamp=1491543326&nonce=301374033&openid=oU1fjv8UyBAxWqlGAExjW_8HHPlU&encrypt_type=aes&msg_signature=d49b10890375855fa8f1e2f6699aeba34cd433d2";
		assertEquals(getConnector().messageDecryption(encrytedXml, encodingAesKey, uri).trim(), expected);
	}

}