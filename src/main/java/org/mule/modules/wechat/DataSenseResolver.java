package org.mule.modules.wechat;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.mule.api.annotations.MetaDataKeyRetriever;
import org.mule.api.annotations.MetaDataRetriever;
import org.mule.api.annotations.components.MetaDataCategory;
import org.mule.common.metadata.DefaultMetaData;
import org.mule.common.metadata.DefaultMetaDataKey;
import org.mule.common.metadata.MetaData;
import org.mule.common.metadata.MetaDataKey;
import org.mule.common.metadata.MetaDataModel;
import org.mule.common.metadata.builder.DefaultMetaDataBuilder;
import org.mule.common.metadata.builder.DynamicObjectBuilder;
import org.mule.common.metadata.datatype.DataType;

/**
 * Category which can differentiate between input or output MetaDataRetriever
 */
@MetaDataCategory
public class DataSenseResolver {

    /**
     * If you have a service that describes the entities, you may want to use
     * that through the connector. Devkit will inject the connector, after
     * initializing it.
     */
    @Inject
    private WechatConnector connector;

    /**
     * Retrieves the list of keys
     */
    @MetaDataKeyRetriever
    public List<MetaDataKey> getMetaDataKeys() throws Exception {
        List<MetaDataKey> keys = new ArrayList<MetaDataKey>();

        //Generate the keys
        keys.add(new DefaultMetaDataKey("CustomerRichMediaMessage", "Customer Rich Media Message"));
        keys.add(new DefaultMetaDataKey("BatchTagFollowers", "Batch Tag Followers"));
        keys.add(new DefaultMetaDataKey("BatchUntagFollowers", "Batch Untag Followers"));
        keys.add(new DefaultMetaDataKey("BlacklistFollowers", "Blacklist Followers"));
        keys.add(new DefaultMetaDataKey("UnblacklistFollowers", "Unblacklist Followers"));
        keys.add(new DefaultMetaDataKey("UploadArticleMessageData", "Upload Article Message Data"));
        keys.add(new DefaultMetaDataKey("OpenIDListBroadcastArticle", "OpenID List Broadcast Article"));
        keys.add(new DefaultMetaDataKey("OpenIDListBroadcastText", "OpenID List Broadcast Text"));
        keys.add(new DefaultMetaDataKey("OpenIDListBroadcastVoice", "OpenID List Broadcast Voice"));
        keys.add(new DefaultMetaDataKey("OpenIDListBroadcastImage", "OpenID List Broadcast Image"));
        keys.add(new DefaultMetaDataKey("OpenIDListBroadcastVideo", "OpenID List Broadcast Video"));

        return keys;
    }

    /**
     * Get MetaData given the Key the user selects
     * 
     * @param key The key selected from the list of valid keys
     * @return The MetaData model of that corresponds to the key
     * @throws Exception If anything fails
     */
    @MetaDataRetriever
    public MetaData getMetaData(MetaDataKey key) throws Exception {
        DefaultMetaDataBuilder builder = new DefaultMetaDataBuilder();
        //If you have a Pojo class
        //PojoMetaDataBuilder<?>  pojoObject=builder.createPojo(Pojo.class);

        //If you use maps as input of your processors that work with DataSense
        DynamicObjectBuilder<?> dynamicObject = builder.createDynamicObject(key
                .getId());

        if (key.getId().equals("CustomerRichMediaMessage")) {
            dynamicObject.addSimpleField("title", DataType.STRING);
            dynamicObject.addSimpleField("description", DataType.STRING);
            dynamicObject.addSimpleField("url", DataType.STRING);
            dynamicObject.addSimpleField("picurl", DataType.STRING);
        } else if (key.getId().equals("BatchTagFollowers")) {
            dynamicObject.addList("openid_list").ofSimpleField(DataType.STRING);
        } else if (key.getId().equals("BatchUntagFollowers")) {
            dynamicObject.addList("openid_list").ofSimpleField(DataType.STRING);
        } else if (key.getId().equals("BlacklistFollowers")) {
            dynamicObject.addList("openid_list").ofSimpleField(DataType.STRING);
        } else if (key.getId().equals("UnblacklistFollowers")) {
            dynamicObject.addList("openid_list").ofSimpleField(DataType.STRING);
        } else if (key.getId().equals("UploadArticleMessageData")) {
        	dynamicObject.addSimpleField("thumb_media_id", DataType.STRING);
            dynamicObject.addSimpleField("author", DataType.STRING);
            dynamicObject.addSimpleField("title", DataType.STRING);
            dynamicObject.addSimpleField("content_source_url", DataType.STRING);
            dynamicObject.addSimpleField("content", DataType.STRING);
            dynamicObject.addSimpleField("digest", DataType.STRING);
            dynamicObject.addSimpleField("show_cover_pic", DataType.INTEGER);
        } else if (key.getId().equals("OpenIDListBroadcastArticle")) {
        	dynamicObject.addList("touser").ofSimpleField(DataType.STRING);
        } else if (key.getId().equals("OpenIDListBroadcastText")) {
        	dynamicObject.addList("touser").ofSimpleField(DataType.STRING);
        } else if (key.getId().equals("OpenIDListBroadcastVoice")) {
        	dynamicObject.addList("touser").ofSimpleField(DataType.STRING);
        } else if (key.getId().equals("OpenIDListBroadcastImage")) {
        	dynamicObject.addList("touser").ofSimpleField(DataType.STRING);
        } else if (key.getId().equals("OpenIDListBroadcastVideo")) {
        	dynamicObject.addList("touser").ofSimpleField(DataType.STRING);
        }
        MetaDataModel model = builder.build();
        MetaData metaData = new DefaultMetaData(model);

        return metaData;
    }

    public WechatConnector getConnector() {
        return connector;
    }

    public void setConnector(WechatConnector connector) {
        this.connector = connector;
    }
}
