/**
 * (c) 2003-2017 MMT, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.wechat;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.activation.DataHandler;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
 
import java.io.StringReader;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mule.api.annotations.Config;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.param.MetaDataKeyParam;
import org.mule.api.annotations.param.Optional;
import org.mule.api.annotations.param.Payload;
import org.mule.api.annotations.MetaDataScope;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.Source;
import org.mule.api.annotations.SourceStrategy;
import org.mule.api.annotations.display.Placement;
import org.mule.api.annotations.licensing.RequiresEnterpriseLicense;
import org.mule.api.callback.SourceCallback;
import org.mule.api.annotations.lifecycle.OnException;
import org.mule.api.annotations.param.Default;
import org.mule.api.annotations.param.InboundAttachments;
import org.mule.modules.wechat.common.HttpsConnection;
import org.mule.modules.wechat.config.ConnectorConfig;
import org.mule.modules.wechat.encrytion.AesException;
import org.mule.modules.wechat.encrytion.WXBizMsgCrypt;
import org.mule.modules.wechat.error.ErrorHandler;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

@Connector(name="wechat", friendlyName="Wechat", minMuleVersion="3.8.0")
@MetaDataScope( DataSenseResolver.class )
@OnException(handler=ErrorHandler.class)
@RequiresEnterpriseLicense(allowEval = true)
public class WechatConnector {

    @Config
    ConnectorConfig config = null;
    private static String accessToken = "";
    private static final Logger logger = Logger.getLogger(WechatConnector.class);
    public enum Lang
    {
    	zh_CN, zh_TW, en
    };
    
    /**
     * Constructor
     */
    public WechatConnector()
    {
    	Timer t = new Timer();
    	t.schedule(new TimerTask() {
    	    @Override
    	    public void run() {
    	    	while (config == null) {
    	    		try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						logger.error(e);
					}
    	    	}
    	    	
    	    	if (!config.getSelfManageAccessToken()){
    	    		String httpsURL = "https://api.wechat.com/cgi-bin/token?grant_type=client_credential&appid=" + config.getAppId() + "&secret=" + config.getAppSecret();
        		    System.out.println(httpsURL);
        	    	HttpsConnection con = new HttpsConnection();
        		    Map<String, Object> map = null;
					try {
						map = con.get(httpsURL);
					} catch (Exception e) {
						logger.error(e);
					}
        		    
        		    if (map.containsKey("access_token")) {
    		    		WechatConnector.accessToken = map.get("access_token").toString();
    		    	}
    	    	}
    	    }
    	}, 0, 6600000);
    }
   
    /**
     * Verify validity of the URL
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Getting_Started#Step_2._Verify_validity_of_the_URL">http://admin.wechat.com/wiki/index.php?title=Getting_Started#Step_2._Verify_validity_of_the_URL</a>
     * 
     * @param uri URI sent by WeChat Official Account Admin System
     * @return The developer's backend system should return the echostr parameter value indicating that the request has been successfully received
     * @throws Exception If anything fails
     */
    @Processor
    public Object verifyUrl(@Default("#[message.inboundProperties.'http.request.uri']") String uri) throws Exception {
        String result = "";
        MultiValueMap<String, String> parameters = UriComponentsBuilder.fromUri(new URI(uri)).build().getQueryParams();
        
        if (parameters.containsKey("timestamp") && parameters.containsKey("nonce") && parameters.containsKey("signature")) {
            String[] arr = {config.getToken(), parameters.get("timestamp").get(0), parameters.get("nonce").get(0)};
            Arrays.sort(arr);
            String tmpStr = String.join("", arr);
            tmpStr = DigestUtils.sha1Hex(tmpStr);
            if (tmpStr.equals(parameters.get("signature").get(0))){
                if (parameters.containsKey("echostr")) {
                    result = parameters.get("echostr").get(0);
                }
            }
        }
        
        return result;
    }
    
    /**
     * Upload Temporary Image Material
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Transferring_Multimedia_Files">http://admin.wechat.com/wiki/index.php?title=Transferring_Multimedia_Files</a>
	 *
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param payload Document
     * @param attachment Attached file
     * @return HashMap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> uploadTemporaryImageFile(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, @Payload Document payload, @InboundAttachments("*") @Default("#[message.inboundAttachments]") Map<String, DataHandler> attachment) throws Exception {
    	String httpsURL = "";
    	Map<String, Object> map = null;
    	if (attachment.size() > 0) {
    		if (!config.getSelfManageAccessToken()){
        		httpsURL = "http://file.api.wechat.com/cgi-bin/media/upload?access_token=" + WechatConnector.accessToken + "&type=image";
        	} else {
        		httpsURL = "http://file.api.wechat.com/cgi-bin/media/upload?access_token=" + accessToken + "&type=image";
        	}
    		
            // Post to Wechat
    		HttpsConnection con = new HttpsConnection();
    	    map = con.postFile(httpsURL, java.util.Optional.ofNullable(attachment.values().iterator().next()).orElse(null));
    	}
	    
        return map;
    }
    
    /**
     * Upload Temporary Voice Material
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Transferring_Multimedia_Files">http://admin.wechat.com/wiki/index.php?title=Transferring_Multimedia_Files</a>
	 *
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param payload Document
     * @param attachment Attached file
     * @return HashMap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> uploadTemporaryVoiceFile(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, @Payload Document payload, @InboundAttachments("*") @Default("#[message.inboundAttachments]") Map<String, DataHandler> attachment) throws Exception {
    	String httpsURL = "";
    	Map<String, Object> map = null;
    	if (attachment.size() > 0) {
    		if (!config.getSelfManageAccessToken()){
        		httpsURL = "http://file.api.wechat.com/cgi-bin/media/upload?access_token=" + WechatConnector.accessToken + "&type=voice";
        	} else {
        		httpsURL = "http://file.api.wechat.com/cgi-bin/media/upload?access_token=" + accessToken + "&type=voice";
        	}
    		
            // Post to Wechat
    		HttpsConnection con = new HttpsConnection();
    	    map = con.postFile(httpsURL, java.util.Optional.ofNullable(attachment.values().iterator().next()).orElse(null));
    	}
	    
        return map;
    }
    
    /**
     * Upload Temporary Video Material
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Transferring_Multimedia_Files">http://admin.wechat.com/wiki/index.php?title=Transferring_Multimedia_Files</a>
	 *
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param payload Document
     * @param attachment Attached file
     * @return HashMap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> uploadTemporaryVideoFile(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, @Payload Document payload, @InboundAttachments("*") @Default("#[message.inboundAttachments]") Map<String, DataHandler> attachment) throws Exception {
    	String httpsURL = "";
    	Map<String, Object> map = null;
    	if (attachment.size() > 0) {
    		if (!config.getSelfManageAccessToken()){
				httpsURL = "http://file.api.wechat.com/cgi-bin/media/upload?access_token=" + WechatConnector.accessToken + "&type=video";
        	} else {
        		httpsURL = "http://file.api.wechat.com/cgi-bin/media/upload?access_token=" + accessToken + "&type=video";
        	}
    		
            // Post to Wechat
    		HttpsConnection con = new HttpsConnection();
    	    map = con.postFile(httpsURL, java.util.Optional.ofNullable(attachment.values().iterator().next()).orElse(null));
    	}
	    
        return map;
    }
    
    /**
     * Upload Temporary Thumb Material
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Transferring_Multimedia_Files">http://admin.wechat.com/wiki/index.php?title=Transferring_Multimedia_Files</a>
	 *
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param payload Document
     * @param attachment Attached file
     * @return HashMap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> uploadTemporaryThumbFile(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, @Payload Document payload, @InboundAttachments("*") @Default("#[message.inboundAttachments]") Map<String, DataHandler> attachment) throws Exception {
    	String httpsURL = "";
    	Map<String, Object> map = null;
    	if (attachment.size() > 0) {
    		if (!config.getSelfManageAccessToken()){
        		httpsURL = "http://file.api.wechat.com/cgi-bin/media/upload?access_token=" + WechatConnector.accessToken + "&type=thumb";
        	} else {
        		httpsURL = "http://file.api.wechat.com/cgi-bin/media/upload?access_token=" + accessToken + "&type=thumb";
        	}
    		
            // Post to Wechat
    		HttpsConnection con = new HttpsConnection();
    	    map = con.postFile(httpsURL, java.util.Optional.ofNullable(attachment.values().iterator().next()).orElse(null));
    	}
	    
        return map;
    }
    
    /**
     * Upload Permanent Image Material
     * <br><a href="http://open.wechat.com/cgi-bin/newreadtemplate?t=overseas_open/docs/oa/asset-management/upload-perm-asset#asset-management_upload-perm-asset">http://open.wechat.com/cgi-bin/newreadtemplate?t=overseas_open/docs/oa/asset-management/upload-perm-asset#asset-management_upload-perm-asset</a>
	 *
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param title The title of this image.
     * @param payload Document
     * @param attachment Attached file
     * @return HashMap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> uploadPermanentImageFile(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, String title, @Payload Document payload, @InboundAttachments("*") @Default("#[message.inboundAttachments]") Map<String, DataHandler> attachment) throws Exception {
    	String httpsURL = "";
    	Map<String, Object> map = null;
    	if (attachment.size() > 0) {
    		if (!config.getSelfManageAccessToken()){
        		httpsURL = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=" + WechatConnector.accessToken + "&type=image";
        	} else {
        		httpsURL = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=" + accessToken + "&type=image";
        	}
    		
            // Post to Wechat
    		HttpsConnection con = new HttpsConnection();
    	    map = con.postFile(httpsURL, title, java.util.Optional.ofNullable(attachment.values().iterator().next()).orElse(null));
    	}
	    
        return map;
    }
    
    /**
     * Upload Permanent Voice Material
     * <br><a href="http://open.wechat.com/cgi-bin/newreadtemplate?t=overseas_open/docs/oa/asset-management/upload-perm-asset#asset-management_upload-perm-asset">http://open.wechat.com/cgi-bin/newreadtemplate?t=overseas_open/docs/oa/asset-management/upload-perm-asset#asset-management_upload-perm-asset</a>
	 *
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param title The title of this voice.
     * @param payload Document
     * @param attachment Attached file
     * @return HashMap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> uploadPermanentVoiceFile(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, String title, @Payload Document payload, @InboundAttachments("*") @Default("#[message.inboundAttachments]") Map<String, DataHandler> attachment) throws Exception {
    	String httpsURL = "";
    	Map<String, Object> map = null;
    	if (attachment.size() > 0) {
    		if (!config.getSelfManageAccessToken()){
        		httpsURL = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=" + WechatConnector.accessToken + "&type=voice";
        	} else {
        		httpsURL = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=" + accessToken + "&type=voice";
        	}
    		
            // Post to Wechat
    		HttpsConnection con = new HttpsConnection();
    	    map = con.postFile(httpsURL, title, java.util.Optional.ofNullable(attachment.values().iterator().next()).orElse(null));
    	}
	    
        return map;
    }
    
    /**
     * Upload Permanent Video Material
     * <br><a href="http://open.wechat.com/cgi-bin/newreadtemplate?t=overseas_open/docs/oa/asset-management/upload-perm-asset#asset-management_upload-perm-asset">http://open.wechat.com/cgi-bin/newreadtemplate?t=overseas_open/docs/oa/asset-management/upload-perm-asset#asset-management_upload-perm-asset</a>
	 *
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param title The title of this video.
     * @param introduction A description for this video.
     * @param payload Document
     * @param attachment Attached file
     * @return HashMap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> uploadPermanentVideoFile(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, String title, String introduction, @Payload Document payload, @InboundAttachments("*") @Default("#[message.inboundAttachments]") Map<String, DataHandler> attachment) throws Exception {
    	String httpsURL = "";
    	Map<String, Object> map = null;
    	if (attachment.size() > 0) {
    		if (!config.getSelfManageAccessToken()){
				httpsURL = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=" + WechatConnector.accessToken + "&type=video";
        	} else {
        		httpsURL = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=" + accessToken + "&type=video";
        	}
    		
            // Post to Wechat
    		HttpsConnection con = new HttpsConnection();
    	    map = con.postFile(httpsURL, title, introduction, java.util.Optional.ofNullable(attachment.values().iterator().next()).orElse(null));
    	}
	    
        return map;
    }
    
    /**
     * Upload Permanent Thumb Material
     * <br><a href="http://open.wechat.com/cgi-bin/newreadtemplate?t=overseas_open/docs/oa/asset-management/upload-perm-asset#asset-management_upload-perm-asset">http://open.wechat.com/cgi-bin/newreadtemplate?t=overseas_open/docs/oa/asset-management/upload-perm-asset#asset-management_upload-perm-asset</a>
	 *
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param title The title of this thumb.
     * @param payload Document
     * @param attachment Attached file
     * @return HashMap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> uploadPermanentThumbFile(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, String title, @Payload Document payload, @InboundAttachments("*") @Default("#[message.inboundAttachments]") Map<String, DataHandler> attachment) throws Exception {
    	String httpsURL = "";
    	Map<String, Object> map = null;
    	if (attachment.size() > 0) {
    		if (!config.getSelfManageAccessToken()){
        		httpsURL = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=" + WechatConnector.accessToken + "&type=thumb";
        	} else {
        		httpsURL = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=" + accessToken + "&type=thumb";
        	}
    		
            // Post to Wechat
    		HttpsConnection con = new HttpsConnection();
    	    map = con.postFile(httpsURL, title, java.util.Optional.ofNullable(attachment.values().iterator().next()).orElse(null));
    	}
	    
        return map;
    }
    
    /**
     * Send Customer Text Message to OpenId
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Customer_Service_Messages#Text_Message">http://admin.wechat.com/wiki/index.php?title=Customer_Service_Messages#Text_Message</a>
     * 
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param openId Follower's openId
     * @param content Text Message
     * @return Hashmap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> customerTextMessage(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, String openId, String content) throws Exception {
    	String httpsURL = "";
    	if (!config.getSelfManageAccessToken()){
    		httpsURL = "https://api.wechat.com/cgi-bin/message/custom/send?access_token=" + WechatConnector.accessToken;
    	} else {
    		httpsURL = "https://api.wechat.com/cgi-bin/message/custom/send?access_token=" + accessToken;
    	}
    	
    	// Create Text Message JSON
    	JSONObject obj = new JSONObject();
        obj.put("touser", openId);
        obj.put("msgtype", "text");
        JSONObject subObj = new JSONObject();
        subObj.put("content", content);
        obj.put("text", subObj);
    	
        // Post to Wechat
    	HttpsConnection con = new HttpsConnection();
    	Map<String, Object> map = con.post(httpsURL, obj.toString());
	    
        return map;
    }
    
    /**
     * Send Customer Image Message to OpenId
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Customer_Service_Messages#Image_Message">http://admin.wechat.com/wiki/index.php?title=Customer_Service_Messages#Image_Message</a>
     * 
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param openId Follower's openId
     * @param mediaId Image Materials' mediaId
     * @return Hashmap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> customerImageMessage(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, String openId, String mediaId) throws Exception {
    	String httpsURL = "";
    	if (!config.getSelfManageAccessToken()){
    		httpsURL = "https://api.wechat.com/cgi-bin/message/custom/send?access_token=" + WechatConnector.accessToken;
    	} else {
    		httpsURL = "https://api.wechat.com/cgi-bin/message/custom/send?access_token=" + accessToken;
    	}
    	
    	// Create Text Message JSON
    	JSONObject obj = new JSONObject();
        obj.put("touser", openId);
        obj.put("msgtype", "image");
        JSONObject subObj = new JSONObject();
        subObj.put("media_id", mediaId);
        obj.put("image", subObj);
    	
        // Post to Wechat
    	HttpsConnection con = new HttpsConnection();
	    Map<String, Object> map = con.post(httpsURL, obj.toString());
	    
        return map;
    }
    
    /**
     * Send Customer Audio Message to OpenId
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Customer_Service_Messages#Audio_Message">http://admin.wechat.com/wiki/index.php?title=Customer_Service_Messages#Audio_Message</a>
     * 
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param openId Follower's openId
     * @param mediaId Audio Materials' mediaId
     * @return Hashmap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> customerAudioMessage(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, String openId, String mediaId) throws Exception {
    	String httpsURL = "";
    	if (!config.getSelfManageAccessToken()){
    		httpsURL = "https://api.wechat.com/cgi-bin/message/custom/send?access_token=" + WechatConnector.accessToken;
    	} else {
    		httpsURL = "https://api.wechat.com/cgi-bin/message/custom/send?access_token=" + accessToken;
    	}
    	
    	// Create Text Message JSON
    	JSONObject obj = new JSONObject();
        obj.put("touser", openId);
        obj.put("msgtype", "voice");
        JSONObject subObj = new JSONObject();
        subObj.put("media_id", mediaId);
        obj.put("voice", subObj);
    	
        // Post to Wechat
    	HttpsConnection con = new HttpsConnection();
	    Map<String, Object> map = con.post(httpsURL, obj.toString());
	    
        return map;
    }
    
    /**
     * Send Customer Video Message to OpenId
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Customer_Service_Messages#Video_Message">http://admin.wechat.com/wiki/index.php?title=Customer_Service_Messages#Video_Message</a>
     * 
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param openId Follower's openId
     * @param mediaId Video Materials' mediaId
     * @param thumbMediaId Video Materials' thumbMediaId
     * @return Hashmap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> customerVideoMessage(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, String openId, String mediaId, String thumbMediaId) throws Exception {
    	String httpsURL = "";
    	if (!config.getSelfManageAccessToken()){
    		httpsURL = "https://api.wechat.com/cgi-bin/message/custom/send?access_token=" + WechatConnector.accessToken;
    	} else {
    		httpsURL = "https://api.wechat.com/cgi-bin/message/custom/send?access_token=" + accessToken;
    	}
    	
    	// Create Text Message JSON
    	JSONObject obj = new JSONObject();
        obj.put("touser", openId);
        obj.put("msgtype", "video");
        JSONObject subObj = new JSONObject();
        subObj.put("media_id", mediaId);
        subObj.put("thumb_media_id", thumbMediaId);
        obj.put("video", subObj);
    	
        // Post to Wechat
    	HttpsConnection con = new HttpsConnection();
	    Map<String, Object> map = con.post(httpsURL, obj.toString());
	    
        return map;
    }
    
    /**
     * Send Customer Music Message to OpenId
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Customer_Service_Messages#Music_Message">http://admin.wechat.com/wiki/index.php?title=Customer_Service_Messages#Music_Message</a>
     * 
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param openId Follower's openId
     * @param title Music title
     * @param description Music description
     * @param musicUrl Music URL
     * @param hqMusicUrl High-quality URL that WeChat accesses on WiFi
     * @param thumbMediaId The media ID of the thumb
     * @return Hashmap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> customerMusicMessage(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, String openId, String title, String description, String musicUrl, String hqMusicUrl, String thumbMediaId) throws Exception {
    	String httpsURL = "";
    	if (!config.getSelfManageAccessToken()){
    		httpsURL = "https://api.wechat.com/cgi-bin/message/custom/send?access_token=" + WechatConnector.accessToken;
    	} else {
    		httpsURL = "https://api.wechat.com/cgi-bin/message/custom/send?access_token=" + accessToken;
    	}
    	
    	// Create Text Message JSON
    	JSONObject obj = new JSONObject();
        obj.put("touser", openId);
        obj.put("msgtype", "music");
        JSONObject subObj = new JSONObject();
        subObj.put("title", title);
        subObj.put("description", description);
        subObj.put("musicurl", musicUrl);
        subObj.put("hqmusicurl", hqMusicUrl);
        subObj.put("thumb_media_id", thumbMediaId);
        obj.put("music", subObj);
    	
        // Post to Wechat
    	HttpsConnection con = new HttpsConnection();
	    Map<String, Object> map = con.post(httpsURL, obj.toString());
	    
        return map;
    }
    
    /**
     * Upload Article Message Data
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Advanced_Broadcast_Interface#Upload_Article_Message_Data">http://admin.wechat.com/wiki/index.php?title=Advanced_Broadcast_Interface#Upload_Article_Message_Data</a>
     * 
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param ApiName Upload Article Message Data
     * @param articles Articles of Upload Article Message Data API
     * @return Hashmap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> uploadArticleMessageData(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, @MetaDataKeyParam @Default("UploadArticleMessageData") String ApiName, @Default("#[payload]") List<Map<String,Object>> articles) throws Exception {
    	String httpsURL = "";
    	if (!config.getSelfManageAccessToken()){
    		httpsURL = "https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token=" + WechatConnector.accessToken;
    	} else {
    		httpsURL = "https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token=" + accessToken;
    	}
    	
    	// Create Text Message JSON
    	JSONObject obj = new JSONObject();
        JSONArray arraySubObj = new JSONArray();
        for (Map<String,Object> article : articles) {
        	JSONObject _subObj = new JSONObject();
        	_subObj.put("thumb_media_id", article.getOrDefault("thumb_media_id", ""));
        	_subObj.put("author", article.getOrDefault("author", ""));
        	_subObj.put("title", article.getOrDefault("title", ""));
        	_subObj.put("content_source_url", article.getOrDefault("content_source_url", ""));
        	_subObj.put("content", article.getOrDefault("content", ""));
        	_subObj.put("digest", article.getOrDefault("digest", ""));
        	_subObj.put("show_cover_pic", article.getOrDefault("show_cover_pic", 0));
        	arraySubObj.put(_subObj);
        }
        obj.put("articles", arraySubObj);
    	
        // Post to Wechat
    	HttpsConnection con = new HttpsConnection();
	    Map<String, Object> map = con.post(httpsURL, obj.toString());
	    
        return map;
    }
    
    /**
     * Group-Based Broadcast Article
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Advanced_Broadcast_Interface#Group-Based_Broadcast">http://admin.wechat.com/wiki/index.php?title=Advanced_Broadcast_Interface#Group-Based_Broadcast</a>
     * 
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param groupId ID of any groups to be broadcast to
     * @param mediaId ID of the message to be broadcast
     * @return Hashmap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> groupBasedBroadcastArticle(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, String groupId, String mediaId) throws Exception {
    	String httpsURL = "";
    	if (!config.getSelfManageAccessToken()){
    		httpsURL = "https://api.wechat.com/cgi-bin/message/mass/sendall?access_token=" + WechatConnector.accessToken;
    	} else {
    		httpsURL = "https://api.wechat.com/cgi-bin/message/mass/sendall?access_token=" + accessToken;
    	}
    	
    	// Create Text Message JSON
    	JSONObject obj = new JSONObject();
        obj.put("msgtype", "mpnews");
        JSONObject subObj = new JSONObject();
        subObj.put("group_id", groupId);
        obj.put("filter", subObj);
        subObj = new JSONObject();
        subObj.put("media_id", mediaId);
        obj.put("mpnews", subObj);
    	
        // Post to Wechat
    	HttpsConnection con = new HttpsConnection();
	    Map<String, Object> map = con.post(httpsURL, obj.toString());
	    
        return map;
    }
    
    /**
     * Group-Based Broadcast Text
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Advanced_Broadcast_Interface#Group-Based_Broadcast">http://admin.wechat.com/wiki/index.php?title=Advanced_Broadcast_Interface#Group-Based_Broadcast</a>
     * 
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param groupId ID of any groups to be broadcast to
     * @param content Text content
     * @return Hashmap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> groupBasedBroadcastText(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, String groupId, String content) throws Exception {
    	String httpsURL = "";
    	if (!config.getSelfManageAccessToken()){
    		httpsURL = "https://api.wechat.com/cgi-bin/message/mass/sendall?access_token=" + WechatConnector.accessToken;
    	} else {
    		httpsURL = "https://api.wechat.com/cgi-bin/message/mass/sendall?access_token=" + accessToken;
    	}
    	
    	// Create Text Message JSON
    	JSONObject obj = new JSONObject();
        obj.put("msgtype", "text");
        JSONObject subObj = new JSONObject();
        subObj.put("group_id", groupId);
        obj.put("filter", subObj);
        subObj = new JSONObject();
        subObj.put("content", content);
        obj.put("text", subObj);
    	
        // Post to Wechat
    	HttpsConnection con = new HttpsConnection();
	    Map<String, Object> map = con.post(httpsURL, obj.toString());
	    
        return map;
    }
    
    /**
     * Group-Based Broadcast Voice
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Advanced_Broadcast_Interface#Group-Based_Broadcast">http://admin.wechat.com/wiki/index.php?title=Advanced_Broadcast_Interface#Group-Based_Broadcast</a>
     * 
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param groupId ID of any groups to be broadcast to
     * @param mediaId ID of the message to be broadcast
     * @return Hashmap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> groupBasedBroadcastVoice(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, String groupId, String mediaId) throws Exception {
    	String httpsURL = "";
    	if (!config.getSelfManageAccessToken()){
    		httpsURL = "https://api.wechat.com/cgi-bin/message/mass/sendall?access_token=" + WechatConnector.accessToken;
    	} else {
    		httpsURL = "https://api.wechat.com/cgi-bin/message/mass/sendall?access_token=" + accessToken;
    	}
    	
    	// Create Text Message JSON
    	JSONObject obj = new JSONObject();
        obj.put("msgtype", "voice");
        JSONObject subObj = new JSONObject();
        subObj.put("group_id", groupId);
        obj.put("filter", subObj);
        subObj = new JSONObject();
        subObj.put("media_id", mediaId);
        obj.put("voice", subObj);
    	
        // Post to Wechat
    	HttpsConnection con = new HttpsConnection();
	    Map<String, Object> map = con.post(httpsURL, obj.toString());
	    
        return map;
    }
    
    /**
     * Group-Based Broadcast Image
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Advanced_Broadcast_Interface#Group-Based_Broadcast">http://admin.wechat.com/wiki/index.php?title=Advanced_Broadcast_Interface#Group-Based_Broadcast</a>
     * 
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param groupId ID of any groups to be broadcast to
     * @param mediaId ID of the message to be broadcast
     * @return Hashmap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> groupBasedBroadcastImage(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, String groupId, String mediaId) throws Exception {
    	String httpsURL = "";
    	if (!config.getSelfManageAccessToken()){
    		httpsURL = "https://api.wechat.com/cgi-bin/message/mass/sendall?access_token=" + WechatConnector.accessToken;
    	} else {
    		httpsURL = "https://api.wechat.com/cgi-bin/message/mass/sendall?access_token=" + accessToken;
    	}
    	
    	// Create Text Message JSON
    	JSONObject obj = new JSONObject();
        obj.put("msgtype", "image");
        JSONObject subObj = new JSONObject();
        subObj.put("group_id", groupId);
        obj.put("filter", subObj);
        subObj = new JSONObject();
        subObj.put("media_id", mediaId);
        obj.put("image", subObj);
    	
        // Post to Wechat
    	HttpsConnection con = new HttpsConnection();
	    Map<String, Object> map = con.post(httpsURL, obj.toString());
	    
        return map;
    }
    
    /**
     * Group-Based Broadcast Video
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Advanced_Broadcast_Interface#Group-Based_Broadcast">http://admin.wechat.com/wiki/index.php?title=Advanced_Broadcast_Interface#Group-Based_Broadcast</a>
     * 
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param groupId ID of any groups to be broadcast to
     * @param mediaId ID of the message to be broadcast
     * @return Hashmap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> groupBasedBroadcastVideo(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, String groupId, String mediaId) throws Exception {
    	String httpsURL = "";
    	if (!config.getSelfManageAccessToken()){
    		httpsURL = "https://api.wechat.com/cgi-bin/message/mass/sendall?access_token=" + WechatConnector.accessToken;
    	} else {
    		httpsURL = "https://api.wechat.com/cgi-bin/message/mass/sendall?access_token=" + accessToken;
    	}
    	
    	// Create Text Message JSON
    	JSONObject obj = new JSONObject();
        obj.put("msgtype", "mpvideo");
        JSONObject subObj = new JSONObject();
        subObj.put("group_id", groupId);
        obj.put("filter", subObj);
        subObj = new JSONObject();
        subObj.put("media_id", mediaId);
        obj.put("mpvideo", subObj);
    	
        // Post to Wechat
    	HttpsConnection con = new HttpsConnection();
	    Map<String, Object> map = con.post(httpsURL, obj.toString());
	    
        return map;
    }
    
    /**
     * OpenID List-Based Broadcast Article
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Advanced_Broadcast_Interface#OpenID_List-Based_Broadcast">http://admin.wechat.com/wiki/index.php?title=Advanced_Broadcast_Interface#OpenID_List-Based_Broadcast</a>
     * 
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param mediaId ID of the message to be broadcast
     * @param ApiName OpenID List Broadcast Article
     * @param toUser List of Follower's openId
     * @return Hashmap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> openIdListBroadcastArticle(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, String mediaId, @MetaDataKeyParam @Default("OpenIDListBroadcastArticle") String ApiName, @Default("#[payload]") Map<String, Object> toUser) throws Exception {
    	String httpsURL = "";
    	if (!config.getSelfManageAccessToken()){
    		httpsURL = "https://api.wechat.com/cgi-bin/message/mass/send?access_token=" + WechatConnector.accessToken;
    	} else {
    		httpsURL = "https://api.wechat.com/cgi-bin/message/mass/send?access_token=" + accessToken;
    	}
    	
    	// Create Text Message JSON
    	JSONObject obj = new JSONObject();
    	obj.put("msgtype", "mpnews");
    	JSONObject subObj = new JSONObject();
        subObj.put("media_id", mediaId);
        obj.put("mpnews", subObj);
        if (toUser.containsKey("touser")) {
        	obj.put("touser", toUser.get("touser"));
        }
    	
        // Post to Wechat
    	HttpsConnection con = new HttpsConnection();
	    Map<String, Object> map = con.post(httpsURL, obj.toString());
	    
        return map;
    }
    
    /**
     * OpenID List-Based Broadcast Text
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Advanced_Broadcast_Interface#OpenID_List-Based_Broadcast">http://admin.wechat.com/wiki/index.php?title=Advanced_Broadcast_Interface#OpenID_List-Based_Broadcast</a>
     * 
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param content Text content
     * @param ApiName OpenID List Broadcast Text
     * @param toUser List of Follower's openId
     * @return Hashmap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> openIdListBroadcastText(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, String content, @MetaDataKeyParam @Default("OpenIDListBroadcastText") String ApiName, @Default("#[payload]") Map<String, Object> toUser) throws Exception {
    	String httpsURL = "";
    	if (!config.getSelfManageAccessToken()){
    		httpsURL = "https://api.wechat.com/cgi-bin/message/mass/send?access_token=" + WechatConnector.accessToken;
    	} else {
    		httpsURL = "https://api.wechat.com/cgi-bin/message/mass/send?access_token=" + accessToken;
    	}
    	
    	// Create Text Message JSON
    	JSONObject obj = new JSONObject();
    	obj.put("msgtype", "text");
    	JSONObject subObj = new JSONObject();
        subObj.put("content", content);
        obj.put("text", subObj);
        if (toUser.containsKey("touser")) {
        	obj.put("touser", toUser.get("touser"));
        }
    	
        // Post to Wechat
    	HttpsConnection con = new HttpsConnection();
	    Map<String, Object> map = con.post(httpsURL, obj.toString());
	    
        return map;
    }
    
    /**
     * OpenID List-Based Broadcast Voice
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Advanced_Broadcast_Interface#OpenID_List-Based_Broadcast">http://admin.wechat.com/wiki/index.php?title=Advanced_Broadcast_Interface#OpenID_List-Based_Broadcast</a>
     * 
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param mediaId ID of the message to be broadcast
     * @param ApiName OpenID List Broadcast Voice
     * @param toUser List of Follower's openId
     * @return Hashmap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> openIdListBroadcastVoice(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, String mediaId, @MetaDataKeyParam @Default("OpenIDListBroadcastVoice") String ApiName, @Default("#[payload]") Map<String, Object> toUser) throws Exception {
    	String httpsURL = "";
    	if (!config.getSelfManageAccessToken()){
    		httpsURL = "https://api.wechat.com/cgi-bin/message/mass/send?access_token=" + WechatConnector.accessToken;
    	} else {
    		httpsURL = "https://api.wechat.com/cgi-bin/message/mass/send?access_token=" + accessToken;
    	}
    	
    	// Create Text Message JSON
    	JSONObject obj = new JSONObject();
    	obj.put("msgtype", "voice");
    	JSONObject subObj = new JSONObject();
        subObj.put("media_id", mediaId);
        obj.put("voice", subObj);
        if (toUser.containsKey("touser")) {
        	obj.put("touser", toUser.get("touser"));
        }
    	
        // Post to Wechat
    	HttpsConnection con = new HttpsConnection();
	    Map<String, Object> map = con.post(httpsURL, obj.toString());
	    
        return map;
    }
    
    /**
     * OpenID List-Based Broadcast Image
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Advanced_Broadcast_Interface#OpenID_List-Based_Broadcast">http://admin.wechat.com/wiki/index.php?title=Advanced_Broadcast_Interface#OpenID_List-Based_Broadcast</a>
     * 
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param mediaId ID of the message to be broadcast
     * @param ApiName OpenID List Broadcast Image
     * @param toUser List of Follower's openId
     * @return Hashmap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> openIdListBroadcastImage(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, String mediaId, @MetaDataKeyParam @Default("OpenIDListBroadcastImage") String ApiName, @Default("#[payload]") Map<String, Object> toUser) throws Exception {
    	String httpsURL = "";
    	if (!config.getSelfManageAccessToken()){
    		httpsURL = "https://api.wechat.com/cgi-bin/message/mass/send?access_token=" + WechatConnector.accessToken;
    	} else {
    		httpsURL = "https://api.wechat.com/cgi-bin/message/mass/send?access_token=" + accessToken;
    	}
    	
    	// Create Text Message JSON
    	JSONObject obj = new JSONObject();
    	obj.put("msgtype", "image");
    	JSONObject subObj = new JSONObject();
        subObj.put("media_id", mediaId);
        obj.put("image", subObj);
        if (toUser.containsKey("touser")) {
        	obj.put("touser", toUser.get("touser"));
        }
    	
        // Post to Wechat
    	HttpsConnection con = new HttpsConnection();
	    Map<String, Object> map = con.post(httpsURL, obj.toString());
	    
        return map;
    }
    
    /**
     * OpenID List-Based Broadcast Video
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Advanced_Broadcast_Interface#OpenID_List-Based_Broadcast">http://admin.wechat.com/wiki/index.php?title=Advanced_Broadcast_Interface#OpenID_List-Based_Broadcast</a>
     * 
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param mediaId here is obtained on Basic Support and then Transferring Multimedia Files
     * @param title The title of the message
     * @param description The description of the message
     * @param ApiName OpenID List Broadcast Video
     * @param toUser List of Follower's openId
     * @return Hashmap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> openIdListBroadcastVideo(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, String mediaId, String title, String description, @MetaDataKeyParam @Default("OpenIDListBroadcastVideo") String ApiName, @Default("#[payload]") Map<String, Object> toUser) throws Exception {
    	String httpsURL = "";
    	if (!config.getSelfManageAccessToken()){
    		httpsURL = "https://api.wechat.com/cgi-bin/message/mass/send?access_token=" + WechatConnector.accessToken;
    	} else {
    		httpsURL = "https://api.wechat.com/cgi-bin/message/mass/send?access_token=" + accessToken;
    	}
    	
    	// Create Text Message JSON
    	JSONObject obj = new JSONObject();
    	obj.put("msgtype", "mpvideo");
    	JSONObject subObj = new JSONObject();
        subObj.put("media_id", mediaId);
        subObj.put("title", title);
        subObj.put("description", description);
        obj.put("mpvideo", subObj);
        if (toUser.containsKey("touser")) {
        	obj.put("touser", toUser.get("touser"));
        }
    	
        // Post to Wechat
        HttpsConnection con = new HttpsConnection();
	    Map<String, Object> map = con.post(httpsURL, obj.toString());
	    
        return map;
    }
    
    /**
     * Send Preview Broadcast Article to OpenId
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Advanced_Broadcast_Interface#Preview_API">http://admin.wechat.com/wiki/index.php?title=Advanced_Broadcast_Interface#Preview_API</a>
     * 
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param toUser OpenID of the message receiver visible by the official account
     * @param mediaId ID of the message to be broadcast
     * @return Hashmap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> previewBroadcastArticle(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, String toUser, String mediaId) throws Exception {
    	String httpsURL = "";
    	if (!config.getSelfManageAccessToken()){
    		httpsURL = "https://api.wechat.com/cgi-bin/message/mass/preview?access_token=" + WechatConnector.accessToken;
    	} else {
    		httpsURL = "https://api.wechat.com/cgi-bin/message/mass/preview?access_token=" + accessToken;
    	}
    	
    	// Create Text Message JSON
    	JSONObject obj = new JSONObject();
        obj.put("msgtype", "mpnews");
        obj.put("touser", toUser);
        JSONObject subObj = new JSONObject();
        subObj.put("media_id", mediaId);
        obj.put("mpnews", subObj);
    	
        // Post to Wechat
    	HttpsConnection con = new HttpsConnection();
	    Map<String, Object> map = con.post(httpsURL, obj.toString());
	    
        return map;
    }
    
    /**
     * Send Preview Broadcast Text to OpenId
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Advanced_Broadcast_Interface#Preview_API">http://admin.wechat.com/wiki/index.php?title=Advanced_Broadcast_Interface#Preview_API</a>
     * 
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param toUser OpenID of the message receiver visible by the official account
     * @param content Text content
     * @return Hashmap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> previewBroadcastText(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, String toUser, String content) throws Exception {
    	String httpsURL = "";
    	if (!config.getSelfManageAccessToken()){
    		httpsURL = "https://api.wechat.com/cgi-bin/message/mass/preview?access_token=" + WechatConnector.accessToken;
    	} else {
    		httpsURL = "https://api.wechat.com/cgi-bin/message/mass/preview?access_token=" + accessToken;
    	}
    	
    	// Create Text Message JSON
    	JSONObject obj = new JSONObject();
        obj.put("msgtype", "text");
        obj.put("touser", toUser);
        JSONObject subObj = new JSONObject();
        subObj.put("content", content);
        obj.put("text", subObj);
    	
        // Post to Wechat
    	HttpsConnection con = new HttpsConnection();
	    Map<String, Object> map = con.post(httpsURL, obj.toString());
	    
        return map;
    }
    
    /**
     * Send Preview Broadcast Voice to OpenId
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Advanced_Broadcast_Interface#Preview_API">http://admin.wechat.com/wiki/index.php?title=Advanced_Broadcast_Interface#Preview_API</a>
     * 
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param toUser OpenID of the message receiver visible by the official account
     * @param mediaId ID of the message to be broadcast
     * @return Hashmap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> previewBroadcastVoice(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, String toUser, String mediaId) throws Exception {
    	String httpsURL = "";
    	if (!config.getSelfManageAccessToken()){
    		httpsURL = "https://api.wechat.com/cgi-bin/message/mass/preview?access_token=" + WechatConnector.accessToken;
    	} else {
    		httpsURL = "https://api.wechat.com/cgi-bin/message/mass/preview?access_token=" + accessToken;
    	}
    	
    	// Create Text Message JSON
    	JSONObject obj = new JSONObject();
        obj.put("msgtype", "voice");
        obj.put("touser", toUser);
        JSONObject subObj = new JSONObject();
        subObj.put("media_id", mediaId);
        obj.put("voice", subObj);
    	
        // Post to Wechat
    	HttpsConnection con = new HttpsConnection();
	    Map<String, Object> map = con.post(httpsURL, obj.toString());
	    
        return map;
    }
    
    /**
     * Send Preview Broadcast Image to OpenId
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Advanced_Broadcast_Interface#Preview_API">http://admin.wechat.com/wiki/index.php?title=Advanced_Broadcast_Interface#Preview_API</a>
     * 
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param toUser OpenID of the message receiver visible by the official account
     * @param mediaId ID of the message to be broadcast
     * @return Hashmap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> previewBroadcastImage(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, String toUser, String mediaId) throws Exception {
    	String httpsURL = "";
    	if (!config.getSelfManageAccessToken()){
    		httpsURL = "https://api.wechat.com/cgi-bin/message/mass/preview?access_token=" + WechatConnector.accessToken;
    	} else {
    		httpsURL = "https://api.wechat.com/cgi-bin/message/mass/preview?access_token=" + accessToken;
    	}
    	
    	// Create Text Message JSON
    	JSONObject obj = new JSONObject();
        obj.put("msgtype", "image");
        obj.put("touser", toUser);
        JSONObject subObj = new JSONObject();
        subObj.put("media_id", mediaId);
        obj.put("image", subObj);
    	
        // Post to Wechat
    	HttpsConnection con = new HttpsConnection();
	    Map<String, Object> map = con.post(httpsURL, obj.toString());
	    
        return map;
    }
    
    /**
     * Send Preview Broadcast Video to OpenId
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Advanced_Broadcast_Interface#Preview_API">http://admin.wechat.com/wiki/index.php?title=Advanced_Broadcast_Interface#Preview_API</a>
     * 
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param toUser OpenID of the message receiver visible by the official account
     * @param mediaId ID of the message to be broadcast
     * @return Hashmap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> previewBroadcastVideo(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, String toUser, String mediaId) throws Exception {
    	String httpsURL = "";
    	if (!config.getSelfManageAccessToken()){
    		httpsURL = "https://api.wechat.com/cgi-bin/message/mass/preview?access_token=" + WechatConnector.accessToken;
    	} else {
    		httpsURL = "https://api.wechat.com/cgi-bin/message/mass/preview?access_token=" + accessToken;
    	}
    	
    	// Create Text Message JSON
    	JSONObject obj = new JSONObject();
        obj.put("msgtype", "mpvideo");
        obj.put("touser", toUser);
        JSONObject subObj = new JSONObject();
        subObj.put("media_id", mediaId);
        obj.put("mpvideo", subObj);
    	
        // Post to Wechat
    	HttpsConnection con = new HttpsConnection();
	    Map<String, Object> map = con.post(httpsURL, obj.toString());
	    
        return map;
    }
    
    /**
     * Query Message Sending Status
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Advanced_Broadcast_Interface#Query_Message_Sending_Status">http://admin.wechat.com/wiki/index.php?title=Advanced_Broadcast_Interface#Query_Message_Sending_Status</a>
     * 
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param msgId Message ID returned after a message is broadcast
     * @return Hashmap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> queryBroadcastStatus(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, String msgId) throws Exception {
    	String httpsURL = "";
    	if (!config.getSelfManageAccessToken()){
    		httpsURL = "https://api.wechat.com/cgi-bin/message/mass/get?access_token=" + WechatConnector.accessToken;
    	} else {
    		httpsURL = "https://api.wechat.com/cgi-bin/message/mass/get?access_token=" + accessToken;
    	}
    	
    	// Create Text Message JSON
    	JSONObject obj = new JSONObject();
        obj.put("msg_id", msgId);
    	
        // Post to Wechat
    	HttpsConnection con = new HttpsConnection();
	    Map<String, Object> map = con.post(httpsURL, obj.toString());
	    
        return map;
    }
    
    /**
     * Get Follower List
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Follower_List">http://admin.wechat.com/wiki/index.php?title=Follower_List</a>
     * 
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param nextOpenId The first OpenID to be loaded. Load from the first follower if it is empty
     * @return Hashmap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> getFollowerList(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, @Optional String nextOpenId) throws Exception {
    	String httpsURL = java.util.Optional.ofNullable(nextOpenId).orElse("");
    	if (!config.getSelfManageAccessToken()){
    		httpsURL = "https://api.wechat.com/cgi-bin/user/get?access_token=" + WechatConnector.accessToken + "&next_openid=" + httpsURL;
    	} else {
    		httpsURL = "https://api.wechat.com/cgi-bin/user/get?access_token=" + accessToken + "&next_openid=" + httpsURL;
    	}
    	
    	HttpsConnection con = new HttpsConnection();
	    Map<String, Object> map = con.get(httpsURL);
	    
        return map;
    }
    
    /**
     * Get User Profile
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=User_Profile">http://admin.wechat.com/wiki/index.php?title=User_Profile</a>
     * 
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param openId Unique user ID for the official account
     * @param lang zh_CN: Simplified Chinese, zh_TW: Traditional Chinese, en: English
     * @return Hashmap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> getUserProfile(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, String openId, @Default("en") Lang lang) throws Exception {
    	String httpsURL = "";
    	if (!config.getSelfManageAccessToken()){
    		httpsURL = "https://api.wechat.com/cgi-bin/user/info?access_token=" + WechatConnector.accessToken + "&openid=" + openId + "&lang=" + lang;
    	} else {
    		httpsURL = "https://api.wechat.com/cgi-bin/user/info?access_token=" + accessToken + "&openid=" + openId + "&lang=" + lang;
    	}
    	
    	HttpsConnection con = new HttpsConnection();
	    Map<String, Object> map = con.get(httpsURL);
	    
        return map;
    }
    
    /**
     * Create Tag
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Tag_Management_API#Create_Tags">http://admin.wechat.com/wiki/index.php?title=Tag_Management_API#Create_Tags</a>
     * 
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param tagName Tag name (up to 30 characters)
     * @return Hashmap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> createTag(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, String tagName) throws Exception {
    	String httpsURL = "";
    	if (!config.getSelfManageAccessToken()){
    		httpsURL = "https://api.wechat.com/cgi-bin/tags/create?access_token=" + WechatConnector.accessToken;
    	} else {
    		httpsURL = "https://api.wechat.com/cgi-bin/tags/create?access_token=" + accessToken;
    	}
    	
    	// Create Text Message JSON
    	JSONObject obj = new JSONObject();
        JSONObject subObj = new JSONObject();
        subObj.put("name", tagName);
        obj.put("tag", subObj);
    	
        // Post to Wechat
    	HttpsConnection con = new HttpsConnection();
	    Map<String, Object> map = con.post(httpsURL, obj.toString());
	    
        return map;
    }
    
    /**
     * Query Tags
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Tag_Management_API#Query_Tags">http://admin.wechat.com/wiki/index.php?title=Tag_Management_API#Query_Tags</a>
     * 
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @return Hashmap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> queryTags(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken) throws Exception {
    	String httpsURL = "";
    	if (!config.getSelfManageAccessToken()){
    		httpsURL = "https://api.wechat.com/cgi-bin/tags/get?access_token=" + WechatConnector.accessToken;
    	} else {
    		httpsURL = "https://api.wechat.com/cgi-bin/tags/get?access_token=" + accessToken;
    	}
    	
    	HttpsConnection con = new HttpsConnection();
	    Map<String, Object> map = con.get(httpsURL);
	    
        return map;
    }
    
    /**
     * Editing Tag
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Tag_Management_API#Editing_Tags">http://admin.wechat.com/wiki/index.php?title=Tag_Management_API#Editing_Tags</a>
     * 
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param tagId Tag ID
     * @param tagName Tag name (up to 30 characters)
     * @return Hashmap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> editingTag(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, Integer tagId, String tagName) throws Exception {
    	String httpsURL = "";
    	if (!config.getSelfManageAccessToken()){
    		httpsURL = "https://api.wechat.com/cgi-bin/tags/update?access_token=" + WechatConnector.accessToken;
    	} else {
    		httpsURL = "https://api.wechat.com/cgi-bin/tags/update?access_token=" + accessToken;
    	}
    	
    	// Create Text Message JSON
    	JSONObject obj = new JSONObject();
        JSONObject subObj = new JSONObject();
        subObj.put("id", tagId);
        subObj.put("name", tagName);
        obj.put("tag", subObj);
    	
        // Post to Wechat
    	HttpsConnection con = new HttpsConnection();
	    Map<String, Object> map = con.post(httpsURL, obj.toString());
	    
        return map;
    }
    
    /**
     * Deleting Tag
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Tag_Management_API#Deleting_Tags">http://admin.wechat.com/wiki/index.php?title=Tag_Management_API#Deleting_Tags</a>
     * 
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param tagId Tag ID
     * @return Hashmap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> deletingTag(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, Integer tagId) throws Exception {
    	String httpsURL = "";
    	if (!config.getSelfManageAccessToken()){
    		httpsURL = "https://api.wechat.com/cgi-bin/tags/delete?access_token=" + WechatConnector.accessToken;
    	} else {
    		httpsURL = "https://api.wechat.com/cgi-bin/tags/delete?access_token=" + accessToken;
    	}
    	
    	// Create Text Message JSON
    	JSONObject obj = new JSONObject();
        JSONObject subObj = new JSONObject();
        subObj.put("id", tagId);
        obj.put("tag", subObj);
    	
        // Post to Wechat
    	HttpsConnection con = new HttpsConnection();
	    Map<String, Object> map = con.post(httpsURL, obj.toString());
	    
        return map;
    }
    
    /**
     * Obtaining the List of Followers Configured with a Tag
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Tag_Management_API#Obtaining_the_List_of_Followers_Configured_with_a_Tag">http://admin.wechat.com/wiki/index.php?title=Tag_Management_API#Obtaining_the_List_of_Followers_Configured_with_a_Tag</a>
     * 
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param tagId Tag ID
     * @param nextOpenId Next Open ID
     * @return Hashmap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> obtainFollowersWithTag(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, Integer tagId, @Optional String nextOpenId) throws Exception {
    	String httpsURL = "";
    	if (!config.getSelfManageAccessToken()){
    		httpsURL = "https://api.wechat.com/cgi-bin/user/tag/get?access_token=" + WechatConnector.accessToken;
    	} else {
    		httpsURL = "https://api.wechat.com/cgi-bin/user/tag/get?access_token=" + accessToken;
    	}
    	
    	// Create Text Message JSON
    	JSONObject obj = new JSONObject();
        JSONObject subObj = new JSONObject();
        subObj.put("id", tagId);
        subObj.put("next_openid", java.util.Optional.ofNullable(nextOpenId).orElse(""));
        obj.put("tag", subObj);
    	
        // Post to Wechat
    	HttpsConnection con = new HttpsConnection();
	    Map<String, Object> map = con.post(httpsURL, obj.toString());
	    
        return map;
    }
    
    /**
     * Configuring Tags for Users in Batches
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Tag_Management_API#Configuring_Tags_for_Users_in_Batches">http://admin.wechat.com/wiki/index.php?title=Tag_Management_API#Configuring_Tags_for_Users_in_Batches</a>
     * 
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param tagId Tag Id
     * @param ApiName Batch Tag Followers
     * @param openidList List of Follower's openId
     * @return Hashmap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> batchTagFollowers(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, @Placement(order = 1) Integer tagId, @MetaDataKeyParam @Default("BatchTagFollowers") String ApiName, @Default("#[payload]") Map<String, Object> openidList) throws Exception {
    	String httpsURL = "";
    	if (!config.getSelfManageAccessToken()){
    		httpsURL = "https://api.wechat.com/cgi-bin/tags/members/batchtagging?access_token=" + WechatConnector.accessToken;
    	} else {
    		httpsURL = "https://api.wechat.com/cgi-bin/tags/members/batchtagging?access_token=" + accessToken;
    	}
    	
    	// Create Text Message JSON
    	JSONObject obj = new JSONObject();
        obj.put("tagid", tagId);
        if (openidList.containsKey("openid_list")) {
        	obj.put("openid_list", openidList.get("openid_list"));
        }
    	
        // Post to Wechat
    	HttpsConnection con = new HttpsConnection();
	    Map<String, Object> map = con.post(httpsURL, obj.toString());
	    
        return map;
    }
    
    /**
     * UnTags for Users in Batches
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Tag_Management_API#Canceling_Tags_for_Users_in_Batches">http://admin.wechat.com/wiki/index.php?title=Tag_Management_API#Canceling_Tags_for_Users_in_Batches</a>
     * 
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param tagId Tag Id
     * @param ApiName Batch Untag Followers
     * @param openidList List of Follower's openId
     * @return Hashmap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> batchUntagFollowers(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, @Placement(order = 1) Integer tagId, @MetaDataKeyParam @Default("BatchUntagFollowers") String ApiName, @Default("#[payload]") Map<String, Object> openidList) throws Exception {
    	String httpsURL = "";
    	if (!config.getSelfManageAccessToken()){
    		httpsURL = "https://api.wechat.com/cgi-bin/tags/members/batchuntagging?access_token=" + WechatConnector.accessToken;
    	} else {
    		httpsURL = "https://api.wechat.com/cgi-bin/tags/members/batchuntagging?access_token=" + accessToken;
    	}
    	
    	// Create Text Message JSON
    	JSONObject obj = new JSONObject();
        obj.put("tagid", tagId);
        if (openidList.containsKey("openid_list")) {
        	obj.put("openid_list", openidList.get("openid_list"));
        }
    	
        // Post to Wechat
    	HttpsConnection con = new HttpsConnection();
	    Map<String, Object> map = con.post(httpsURL, obj.toString());
	    
        return map;
    }
    
    /**
     * Obtaining the List of Tags Configured for a User
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Tag_Management_API#Obtaining_the_List_of_Tags_Configured_for_a_User">http://admin.wechat.com/wiki/index.php?title=Tag_Management_API#Obtaining_the_List_of_Tags_Configured_for_a_User</a>
     * 
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param openId Follower's openId
     * @return Hashmap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> obtainFollowerTags(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, String openId) throws Exception {
    	String httpsURL = "";
    	if (!config.getSelfManageAccessToken()){
    		httpsURL = "https://api.wechat.com/cgi-bin/tags/getidlist?access_token=" + WechatConnector.accessToken;
    	} else {
    		httpsURL = "https://api.wechat.com/cgi-bin/tags/getidlist?access_token=" + accessToken;
    	}
    	
    	// Create Text Message JSON
    	JSONObject obj = new JSONObject();
        obj.put("openid", openId);
    	
        // Post to Wechat
    	HttpsConnection con = new HttpsConnection();
	    Map<String, Object> map = con.post(httpsURL, obj.toString());
	    
        return map;
    }
    
    /**
     * Set name remarks for specified users
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Name_Remarks">http://admin.wechat.com/wiki/index.php?title=Name_Remarks</a>
     * 
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param openId Follower's openId
     * @param remark New name remark, less than 30 characters.
     * @return Hashmap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> nameRemark(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, String openId, String remark) throws Exception {
    	String httpsURL = "";
    	if (!config.getSelfManageAccessToken()){
    		httpsURL = "https://api.wechat.com/cgi-bin/user/info/updateremark?access_token=" + WechatConnector.accessToken;
    	} else {
    		httpsURL = "https://api.wechat.com/cgi-bin/user/info/updateremark?access_token=" + accessToken;
    	}
    	
    	// Create Text Message JSON
    	JSONObject obj = new JSONObject();
        obj.put("openid", openId);
        obj.put("remark", remark);
    	
        // Post to Wechat
    	HttpsConnection con = new HttpsConnection();
	    Map<String, Object> map = con.post(httpsURL, obj.toString());
	    
        return map;
    }
    
    /**
     * Obtaining the Blacklist of an Official Account
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Blacklist_Management_API">http://admin.wechat.com/wiki/index.php?title=Blacklist_Management_API</a>
     * 
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param beginOpenId The first OpenID to be loaded. Load from the first follower if it is empty
     * @return Hashmap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> obtainBlacklist(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, @Optional String beginOpenId) throws Exception {
    	String httpsURL = "";
    	if (!config.getSelfManageAccessToken()){
    		httpsURL = "https://api.wechat.com/cgi-bin/tags/members/getblacklist?access_token=" + WechatConnector.accessToken;
    	} else {
    		httpsURL = "https://api.wechat.com/cgi-bin/tags/members/getblacklist?access_token=" + accessToken;
    	}
    	
    	// Create Text Message JSON
    	JSONObject obj = new JSONObject();
        obj.put("begin_openid", java.util.Optional.ofNullable(beginOpenId).orElse(""));
    	
        // Post to Wechat
    	HttpsConnection con = new HttpsConnection();
	    Map<String, Object> map = con.post(httpsURL, obj.toString());
	    
        return map;
    }
    
    /**
     * Blacklist Followers
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Blacklist_Management_API">http://admin.wechat.com/wiki/index.php?title=Blacklist_Management_API</a>
     * 
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param ApiName Blacklist Followers
     * @param openidList List of Follower's openId
     * @return Hashmap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> blacklistFollowers(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, @MetaDataKeyParam @Default("BlacklistFollowers") String ApiName, @Default("#[payload]") Map<String, Object> openidList) throws Exception {
    	String httpsURL = "";
    	if (!config.getSelfManageAccessToken()){
    		httpsURL = "https://api.wechat.com/cgi-bin/tags/members/batchblacklist?access_token=" + WechatConnector.accessToken;
    	} else {
    		httpsURL = "https://api.wechat.com/cgi-bin/tags/members/batchblacklist?access_token=" + accessToken;
    	}
    	
    	// Create Text Message JSON
    	JSONObject obj = new JSONObject();
        if (openidList.containsKey("openid_list")) {
        	obj.put("openid_list", openidList.get("openid_list"));
        }
    	
        // Post to Wechat
    	HttpsConnection con = new HttpsConnection();
	    Map<String, Object> map = con.post(httpsURL, obj.toString());
	    
        return map;
    }
    
    /**
     * Unblacklist Followers
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Blacklist_Management_API">http://admin.wechat.com/wiki/index.php?title=Blacklist_Management_API</a>
     * 
     * @param accessToken The certificate for the calling API. Mandatory if "Self Manage Access Token" config is true
     * @param ApiName Unblacklist Followers
     * @param openidList List of Follower's OpenId
     * @return Hashmap
     * @throws Exception If anything fails
     */
    @Processor
    public Map<String, Object> unblacklistFollowers(@Placement(tab="Advanced", group = "Advanced") @Optional String accessToken, @MetaDataKeyParam @Default("UnblacklistFollowers") String ApiName, @Default("#[payload]") Map<String, Object> openidList) throws Exception {
    	String httpsURL = "";
    	if (!config.getSelfManageAccessToken()){
    		httpsURL = "https://api.wechat.com/cgi-bin/tags/members/batchunblacklist?access_token=" + WechatConnector.accessToken;
    	} else {
    		httpsURL = "https://api.wechat.com/cgi-bin/tags/members/batchunblacklist?access_token=" + accessToken;
    	}
    	
    	// Create Text Message JSON
    	JSONObject obj = new JSONObject();
        if (openidList.containsKey("openid_list")) {
        	obj.put("opened_list", openidList.get("openid_list"));
        }
    	
        // Post to Wechat
    	HttpsConnection con = new HttpsConnection();
	    Map<String, Object> map = con.post(httpsURL, obj.toString());
	    
        return map;
    }

    /**
     * Get Access Token
     * <br><a href="http://admin.wechat.com/wiki/index.php?title=Access_token">http://admin.wechat.com/wiki/index.php?title=Access_token</a>
     * 
     * @param callback Hashmap
     * @param otherAppId Other official account's AppID
     * @param otherAppSecret Other official account's AppSecret
     * @throws Exception error produced while processing the payload
     */
    @Source(sourceStrategy = SourceStrategy.POLLING,pollingPeriod=6600000)
    public void getAccessToken(SourceCallback callback, @Optional String otherAppId, @Optional String otherAppSecret) throws Exception {
    	String httpsURL = "https://api.wechat.com/cgi-bin/token?grant_type=client_credential&appid=" + otherAppId + "&secret=" + otherAppSecret;
    	if (otherAppId == null && otherAppSecret == null) {
    		httpsURL = "https://api.wechat.com/cgi-bin/token?grant_type=client_credential&appid=" + config.getAppId() + "&secret=" + config.getAppSecret();
    	}
    	HttpsConnection con = new HttpsConnection();
	    Map<String, Object> map = con.get(httpsURL);
	    
        callback.process(map);
    }

    public ConnectorConfig getConfig() {
        return config;
    }

    public void setConfig(ConnectorConfig config) {
        this.config = config;
    }

}