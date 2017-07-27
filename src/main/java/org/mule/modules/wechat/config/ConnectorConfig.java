/**
 * (c) 2003-2017 MMT, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.wechat.config;

import org.mule.api.annotations.components.Configuration;
import org.mule.api.annotations.display.Placement;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.param.Default;

@Configuration(friendlyName = "Configuration", configElementName = "config-wechat")
public class ConnectorConfig {

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
     * Get Verify Url token
	 * @return Token set by the developer on the WeChat Official Account Admin Platform
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
     * 
     * @return The unique certificate of a official account
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
     * 
     * @return The key of a official account's certificate
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

	/**
	 * Get If they want to self manage access token
	 * 
	 * @return Boolean
	 */
	public Boolean getSelfManageAccessToken() {
		return selfManageAccessToken;
	}

	/**
	 * Set If they want to self manage access token
	 * 
	 * @param selfManageAccessToken Boolean
	 */
	public void setSelfManageAccessToken(Boolean selfManageAccessToken) {
		this.selfManageAccessToken = selfManageAccessToken;
	}

}