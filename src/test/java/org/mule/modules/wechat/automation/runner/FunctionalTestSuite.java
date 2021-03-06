/**
 * (c) 2003-2017 MMT, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.wechat.automation.runner;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.mule.modules.wechat.WechatConnector;
import org.mule.tools.devkit.ctf.mockup.ConnectorTestContext;
import org.mule.modules.wechat.automation.functional.NameRemarkTestCases;
import org.mule.modules.wechat.automation.functional.BatchTagFollowersTestCases;
import org.mule.modules.wechat.automation.functional.BatchUntagFollowersTestCases;
import org.mule.modules.wechat.automation.functional.BlacklistFollowersTestCases;
import org.mule.modules.wechat.automation.functional.CreateTagFailTestCases;
import org.mule.modules.wechat.automation.functional.CreateTagTestCases;
import org.mule.modules.wechat.automation.functional.CustomerAudioMessageTestCases;
import org.mule.modules.wechat.automation.functional.CustomerImageMessageTestCases;
import org.mule.modules.wechat.automation.functional.CustomerMusicMessageTestCases;
import org.mule.modules.wechat.automation.functional.CustomerTextMessageTestCases;
import org.mule.modules.wechat.automation.functional.CustomerVideoMessageTestCases;
import org.mule.modules.wechat.automation.functional.EditingTagFailTestCases;
import org.mule.modules.wechat.automation.functional.EditingTagTestCases;
import org.mule.modules.wechat.automation.functional.DeletingTagTestCases;
import org.mule.modules.wechat.automation.functional.GetFollowerListTestCases;
import org.mule.modules.wechat.automation.functional.GetUserProfileTestCases;
import org.mule.modules.wechat.automation.functional.ObtainBlacklistTestCases;
import org.mule.modules.wechat.automation.functional.ObtainFollowerTagsTestCases;
import org.mule.modules.wechat.automation.functional.ObtainFollowersWithTagTestCases;
import org.mule.modules.wechat.automation.functional.PreviewBroadcastArticleTestCases;
import org.mule.modules.wechat.automation.functional.PreviewBroadcastImageTestCases;
import org.mule.modules.wechat.automation.functional.PreviewBroadcastTextTestCases;
import org.mule.modules.wechat.automation.functional.PreviewBroadcastVideoTestCases;
import org.mule.modules.wechat.automation.functional.PreviewBroadcastVoiceTestCases;
import org.mule.modules.wechat.automation.functional.QueryTagsTestCases;
import org.mule.modules.wechat.automation.functional.UnblacklistFollowersTestCases;
import org.mule.modules.wechat.automation.functional.VerifyUrlTestCases;
import org.mule.modules.wechat.automation.functional.GroupBasedBroadcastArticleTestCases;
import org.mule.modules.wechat.automation.functional.GroupBasedBroadcastImageTestCases;
import org.mule.modules.wechat.automation.functional.GroupBasedBroadcastTextTestCases;
import org.mule.modules.wechat.automation.functional.GroupBasedBroadcastVideoTestCases;
import org.mule.modules.wechat.automation.functional.GroupBasedBroadcastVoiceTestCases;
import org.mule.modules.wechat.automation.functional.OpenIdListBroadcastArticleTestCases;
import org.mule.modules.wechat.automation.functional.OpenIdListBroadcastImageTestCases;
import org.mule.modules.wechat.automation.functional.OpenIdListBroadcastTextTestCases;
import org.mule.modules.wechat.automation.functional.OpenIdListBroadcastVideoTestCases;
import org.mule.modules.wechat.automation.functional.OpenIdListBroadcastVoiceTestCases;
import org.mule.modules.wechat.automation.functional.QueryBroadcastStatusTestCases;
import org.mule.modules.wechat.automation.functional.UploadArticleMessageDataTestCases;
import org.mule.modules.wechat.automation.functional.UploadPermanentImageFileTestCases;
import org.mule.modules.wechat.automation.functional.UploadPermanentThumbFileTestCases;
import org.mule.modules.wechat.automation.functional.UploadPermanentVideoFileTestCases;
import org.mule.modules.wechat.automation.functional.UploadPermanentVoiceFileTestCases;
import org.mule.modules.wechat.automation.functional.UploadTemporaryImageFileTestCases;
import org.mule.modules.wechat.automation.functional.UploadTemporaryThumbFileTestCases;
import org.mule.modules.wechat.automation.functional.UploadTemporaryVideoFileTestCases;
import org.mule.modules.wechat.automation.functional.UploadTemporaryVoiceFileTestCases;

@RunWith(Suite.class)
@SuiteClasses({ BatchTagFollowersTestCases.class, BatchUntagFollowersTestCases.class, BlacklistFollowersTestCases.class, 
	CreateTagTestCases.class, CustomerAudioMessageTestCases.class, CustomerImageMessageTestCases.class, 
	CustomerMusicMessageTestCases.class, CustomerTextMessageTestCases.class, CustomerVideoMessageTestCases.class, 
	DeletingTagTestCases.class, EditingTagTestCases.class, GetFollowerListTestCases.class, 
	GetUserProfileTestCases.class, GroupBasedBroadcastArticleTestCases.class, GroupBasedBroadcastImageTestCases.class, 
	GroupBasedBroadcastTextTestCases.class, GroupBasedBroadcastVideoTestCases.class, GroupBasedBroadcastVoiceTestCases.class, 
	NameRemarkTestCases.class, ObtainBlacklistTestCases.class, ObtainFollowersWithTagTestCases.class, 
	ObtainFollowerTagsTestCases.class, OpenIdListBroadcastArticleTestCases.class, OpenIdListBroadcastImageTestCases.class, 
	OpenIdListBroadcastTextTestCases.class, OpenIdListBroadcastVideoTestCases.class, OpenIdListBroadcastVoiceTestCases.class, 
	PreviewBroadcastArticleTestCases.class, PreviewBroadcastImageTestCases.class, PreviewBroadcastTextTestCases.class, 
	PreviewBroadcastVideoTestCases.class, PreviewBroadcastVoiceTestCases.class, QueryBroadcastStatusTestCases.class, 
	QueryTagsTestCases.class, UnblacklistFollowersTestCases.class, UploadArticleMessageDataTestCases.class, 
	UploadPermanentImageFileTestCases.class, UploadPermanentThumbFileTestCases.class, UploadPermanentVideoFileTestCases.class, 
	UploadPermanentVoiceFileTestCases.class, UploadTemporaryImageFileTestCases.class, UploadTemporaryThumbFileTestCases.class, 
	UploadTemporaryVideoFileTestCases.class, UploadTemporaryVoiceFileTestCases.class, VerifyUrlTestCases.class,
	CreateTagFailTestCases.class, EditingTagFailTestCases.class })


public class FunctionalTestSuite {

	@BeforeClass
	public static void initialiseSuite() {
		ConnectorTestContext.initialize(WechatConnector.class);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void shutdownSuite() {
		ConnectorTestContext.shutDown();
	}

}