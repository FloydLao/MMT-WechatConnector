/**
 * (c) 2003-2017 MMT, Inc. The software in this package is published under the terms of the Commercial Free Software license V.1, a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.wechat.common;

public enum ContentTypeEnum {
	Type1("audio/mp3",".mp3"),
	Type2("audio/mpeg3",".mp3"),
	Type3("audio/x-mpeg-3",".mp3"),
	Type4("audio/mpeg",".mp3"),
	Type5("video/x-mpeg",".mp3");
	
	private String contentType;
	private String extension;
	
	private ContentTypeEnum(String contentType, String extension){
		this.contentType=contentType;
		this.extension=extension;
	}
	
	public String getContentType() {
		return contentType;
	}

	public String getExtension() {
		return extension;
	}

	public static ContentTypeEnum getContentTypeEnumByContentType(String contentType){
		ContentTypeEnum[] values = ContentTypeEnum.values();
		if(values != null){
			for(ContentTypeEnum value : values){
				if(value.contentType.contains(contentType)){
					return value;
				}
			}
		}
		return null;
	}
}
