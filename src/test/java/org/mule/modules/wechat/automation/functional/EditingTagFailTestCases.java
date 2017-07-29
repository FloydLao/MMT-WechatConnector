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

public class EditingTagFailTestCases extends AbstractTestCase<WechatConnector> {

	public EditingTagFailTestCases() {
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
		java.util.Map<java.lang.String, java.lang.Object> expected = new java.util.HashMap<String, Object>();
		expected.put("errcode", 45157);
		expected.put("errmsg", "invalid tag name");
		java.lang.String accessToken = null;
		java.lang.Integer tagId = 110;
		java.lang.String tagName = "MMT";
		assertEquals(getConnector().editingTag(accessToken, tagId, tagName).get("errcode"), expected.get("errcode"));
	}

}