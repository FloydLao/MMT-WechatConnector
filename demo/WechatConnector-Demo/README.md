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


To be able to successfully run this application, this config values must be set in <wechat:config-wechat>:

Usage:
Once mule is up and running you must call http://localhost/followerList for the app to return list of followers.
