/**
 * (c) 2003-2017 MMT, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.wechat.automation.functional;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mule.modules.wechat.WechatConnector;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

public class VerifyUrlTestCases extends AbstractTestCase<WechatConnector> {

	public VerifyUrlTestCases() {
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
		java.lang.Object expected = "3638184141321108956";
		java.lang.String uri = "/msg?signature=67743c85f9a7c7390b9231575fee6ead2e5ae1f2&echostr=3638184141321108956&timestamp=1491560924&nonce=1293642198";
		assertEquals(getConnector().verifyUrl(uri), expected);
	}

}