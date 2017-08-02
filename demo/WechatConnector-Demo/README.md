Wechat Connector Demo Application
===================================

Description:
Based on the user URL input this demo application performs the following -

* /msg - URL verify from wehcat and receive follower's message.
* /followerList - Get list of followers' openID.
* /broadcast - Send text broadcast to list of follower.
* /file - Upload image.
* /profile - Get follower's profile.
* /remark - Change follower remark.


To be able to successfully run this application, this config values must be set in ./src/main/app/mule-app.properties:
    -AppID: The unique certificate of wechat official account
    -AppSecret: The key of wechat official account's certificate
    -Token: Token set by the developer on the WeChat Official Account Admin Platform

Usage:
If AppID and AppSecret is valid. By default, once mule is up and running, Wecaht connector will use AppID and AppSecret to retrieve a access token from Wechat. And Wechat connector will automatically refresh the access token for you every 110 minutes.

* Example:org.mule.api.processor.LoggerMessageProcessor:Aq7uRMiulTly...

Call http://localhost:8081/followerList for the app to return list of followers' openID.

* Example:{"total"=994,"data":{"openid":[OpenID array]},"count":994,"next_openid":"oRwGLw0xokgCcTaSUTvfa_OdPKBY"}

Call http://localhost:8081/broadcast to send text broadcast to list of follower.

* Example:{"errcode":0,"errmsg":"send job submission success","msg_id":3147484619}

Go to folder ./sample and open UploadFile.html with browser, then select picture ./sample/thumb.jpg to submit.

* Example:{"media_id":"t6Spy...","url":"http://mmbiz.qpic.cn/mmbiz_jpg/.../0?wx_fmt=jpeg"}

Call http://localhost:8081/profile to get follower's profile.

* Example:{"country":"xx","subscribe":1,"city":"xx","openid":"oRwGLwz_vjiN-rogxjjUoSzfPzWs","tagid_list":[255]...}

Call http://localhost:8081/remark to change a follower's remark.

* Example:{"errcode":0,"errmsg":"ok"}