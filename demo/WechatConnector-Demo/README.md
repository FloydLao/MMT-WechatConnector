Wechat Connector Demo Application
===================================

Description:
Based on the user URL input this demo application performs the following -

* /msg - URL verify and follower send message.
* /followerList - Get list of followers.
* /broadcast - Send Wechat openIdListBroadcastText to list of follower.
* /file - Upload image.
* /profile - Get follower profile.
* /remark - Change follower remark.


To be able to successfully run this application, this config values must be set in <wechat:config-wechat>, <wechat:message-decryption> and <wechat:message-encrytion>:
    -AppID: The unique certificate of wechat official account
    -AppSecret: The key of wechat official account's certificate
    -Token: Token set by the developer on the WeChat Official Account Admin Platform
    -EncodingAesKey: EncodingAesKey set by the developer on the WeChat Official Account Admin Platform

Usage:
Once mule is up and running you must call http://localhost/followerList for the app to return list of followers.
