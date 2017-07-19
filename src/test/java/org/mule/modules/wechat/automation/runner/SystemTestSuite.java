/**
 *
 */
package org.mule.modules.wechat.automation.runner;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.mule.modules.wechat.WechatConnector;
import org.mule.modules.wechat.automation.system.WrongAppIDTestCases;
import org.mule.modules.wechat.automation.system.WrongAppSecretTestCases;
import org.mule.tools.devkit.ctf.mockup.ConnectorTestContext;

@RunWith(Suite.class)
@SuiteClasses({ WrongAppIDTestCases.class, WrongAppSecretTestCases.class })


public class SystemTestSuite {

	@BeforeClass
	public static void initialiseSuite() {
		ConnectorTestContext.initialize(WechatConnector.class);
	}

	@AfterClass
	public static void shutdownSuite() {
		ConnectorTestContext.shutDown();
	}

}