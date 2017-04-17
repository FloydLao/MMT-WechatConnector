package org.mule.modules.wechat.config;

import org.mule.api.annotations.components.Configuration;
import org.mule.api.annotations.display.Placement;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.param.Default;

@Configuration(friendlyName = "Configuration", configElementName = "config-wechat")
public class ConnectorConfig {

    /**
     * Host of WeChat Official Account Admin System
     */
    @Configurable
    @Default("https://api.weixin.qq.com")
    private String wechatHost;
    
    /**
     * Token set by the developer on the WeChat Official Account Admin Platform
     */
    @Configurable
    private String token;
    
    /**
     * The unique certificate of a official account
     */
    @Configurable
    private String appId;
    
    /**
     * The key of a official account's certificate
     */
    @Configurable
    private String appSecret;
    
    /**
     * Self-Manage Access Token
     */
    @Configurable
    @Default("false")
    @Placement(tab="Advanced", group = "Advanced")
    private Boolean selfManageAccessToken;

    /**
     * Get Wechat Host name
     */
	public String getWechatHost() {
		return wechatHost;
	}

    /**
     * Set Wechat Host name
     *
     * @param wechatHost Wechat Host name
     */
	public void setWechatHost(String wechatHost) {
		this.wechatHost = wechatHost;
	}

	/**
     * Get Verify Url token
     */
	public String getToken() {
		return token;
	}

	/**
	 * Set Verify Url token
	 * 
	 * @param token Verify Url token
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
     * Get Wechat account appId
     */
	public String getAppId() {
		return appId;
	}

	/**
	 * Set Wechat account appId
	 * 
	 * @param appId Wechat account appId
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}

	/**
     * Get Wechat account appSecret
     */
	public String getAppSecret() {
		return appSecret;
	}

	/**
	 * Set Wechat account appSecret
	 * 
	 * @param appSecret Wechat account appSecret
	 */
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public Boolean getSelfManageAccessToken() {
		return selfManageAccessToken;
	}

	public void setSelfManageAccessToken(Boolean selfManageAccessToken) {
		this.selfManageAccessToken = selfManageAccessToken;
	}

}