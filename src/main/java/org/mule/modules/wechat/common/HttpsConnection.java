package org.mule.modules.wechat.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;

import javax.activation.DataHandler;
import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.mime.MimeTypes;
import org.json.JSONObject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class HttpsConnection {
	public Map<String, Object> get(String httpsURL) throws Exception {
		// Setup connection
		String result = "";
		URL url = new URL(httpsURL);
	    HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
	    con.setRequestMethod("GET");
	    con.setRequestProperty("Content-Type", "application/json; encoding=utf-8");
	    
	    // Call wechat
	    InputStream ins = con.getInputStream();
	    InputStreamReader isr = new InputStreamReader(ins, "UTF-8");
	    BufferedReader in = new BufferedReader(isr);
	    
	    // Read result
	    String inputLine;
	    StringBuilder sb = new StringBuilder();
	    while ((inputLine = in.readLine()) != null)
	    {
	    	sb.append((new JSONObject(inputLine)).toString());
	    }
	    result = sb.toString();
	    in.close();
	    
	    // Convert JSON string to Map
	    ObjectMapper mapper = new ObjectMapper();
	    Map<String, Object> map = mapper.readValue(result, new TypeReference<Map<String, Object>>() {});
	    
	    return map;
	}
	
	public Map<String, Object> post(String httpsURL, String json) throws Exception {
		// Setup connection
		String result = "";
		URL url = new URL(httpsURL);
	    HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
	    con.setRequestMethod("POST");
	    con.setRequestProperty("Content-Type", "application/json; encoding=utf-8");
	    con.setDoOutput(true);
	    OutputStream ops = con.getOutputStream();
	    ops.write(json.getBytes("UTF-8"));
	    ops.flush();
	    ops.close();
		
	    // Call wechat
	    InputStream ins = con.getInputStream();
	    InputStreamReader isr = new InputStreamReader(ins, "UTF-8");
	    BufferedReader in = new BufferedReader(isr);
	    
	    // Read result
	    String inputLine;
	    StringBuilder sb = new StringBuilder();
	    while ((inputLine = in.readLine()) != null)
	    {
	    	sb.append((new JSONObject(inputLine)).toString());
	    }
	    result = sb.toString();
	    in.close();
	    
	    // Convert JSON string to Map
	    ObjectMapper mapper = new ObjectMapper();
	    Map<String, Object> map = mapper.readValue(result, new TypeReference<Map<String, Object>>() {});
	    
	    return map;
	}
	
	public Map<String, Object> postFile(String httpsURL, DataHandler attachment) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(httpsURL);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		// Get extension of attachment
        TikaConfig config = TikaConfig.getDefaultConfig();
		MimeTypes allTypes = config.getMimeRepository();
		String ext = allTypes.forName(attachment.getContentType()).getExtension();
		if (ext.equals("")) {
			ContentTypeEnum contentTypeEnum = ContentTypeEnum.getContentTypeEnumByContentType(attachment.getContentType().toLowerCase());
			ext = java.util.Optional.ofNullable(contentTypeEnum.getExtension()).orElse("");
		}
		
		// Create file
		InputStream fis = attachment.getInputStream();
        byte[] bytes = IOUtils.toByteArray(fis);
        File f = new File(System.getProperty("user.dir") + "/fileTemp/" + attachment.getName().replace(ext, "") + ext);
        FileUtils.writeByteArrayToFile(f, bytes);
		builder.addBinaryBody("media", f);
		
		// Post to wechat
		HttpEntity entity = builder.build();
		post.setEntity(entity);
		CloseableHttpResponse httpResponse = httpClient.execute(post);
		String content = "";
		try {
			HttpEntity _entity = httpResponse.getEntity();
			content = EntityUtils.toString(_entity);
			EntityUtils.consume(_entity);
		} finally {
			httpResponse.close();
		}
		f.delete();
	    
        // Convert JSON string to Map
	    ObjectMapper mapper = new ObjectMapper();
	    Map<String, Object> map = mapper.readValue(content, new TypeReference<Map<String, Object>>() {});
	    
	    return map;
	}
	
	public Map<String, Object> postFile(String httpsURL, String title, DataHandler attachment) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(httpsURL);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		// Get extension of attachment
        TikaConfig config = TikaConfig.getDefaultConfig();
		MimeTypes allTypes = config.getMimeRepository();
		String ext = allTypes.forName(attachment.getContentType()).getExtension();
		if (ext.equals("")) {
			ContentTypeEnum contentTypeEnum = ContentTypeEnum.getContentTypeEnumByContentType(attachment.getContentType().toLowerCase());
			ext = java.util.Optional.ofNullable(contentTypeEnum.getExtension()).orElse("");
		}
		
		// Create file
		InputStream fis = attachment.getInputStream();
        byte[] bytes = IOUtils.toByteArray(fis);
        File f = new File(System.getProperty("user.dir") + "/fileTemp/" + title + ext);
        FileUtils.writeByteArrayToFile(f, bytes);
		builder.addBinaryBody("media", f);
		
		// Post to wechat
		HttpEntity entity = builder.build();
		post.setEntity(entity);
		CloseableHttpResponse httpResponse = httpClient.execute(post);
		String content = "";
		try {
			HttpEntity _entity = httpResponse.getEntity();
			content = EntityUtils.toString(_entity);
			EntityUtils.consume(_entity);
		} finally {
			httpResponse.close();
		}
		f.delete();
	    
        // Convert JSON string to Map
	    ObjectMapper mapper = new ObjectMapper();
	    Map<String, Object> map = mapper.readValue(content, new TypeReference<Map<String, Object>>() {});
	    
	    return map;
	}

	public Map<String, Object> postFile(String httpsURL, String title, String introduction, DataHandler attachment) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost(httpsURL);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		// Get extension of attachment
        TikaConfig config = TikaConfig.getDefaultConfig();
		MimeTypes allTypes = config.getMimeRepository();
		String ext = allTypes.forName(attachment.getContentType()).getExtension();
		if (ext.equals("")) {
			ContentTypeEnum contentTypeEnum = ContentTypeEnum.getContentTypeEnumByContentType(attachment.getContentType().toLowerCase());
			ext = java.util.Optional.ofNullable(contentTypeEnum.getExtension()).orElse("");
		}
		
		// Create file
		InputStream fis = attachment.getInputStream();
        byte[] bytes = IOUtils.toByteArray(fis);
        File f = new File(System.getProperty("user.dir") + "/fileTemp/" + title + ext);
        FileUtils.writeByteArrayToFile(f, bytes);
        
        // Create JSON
        JSONObject obj = new JSONObject();
		obj.put("title", title);
		obj.put("introduction", introduction);
        ContentType contentType = ContentType.create("text/plain", Charset.forName("UTF-8"));
		builder.addBinaryBody("media", f);
		builder.addTextBody("description", obj.toString(), contentType);
		
		// Post to wechat
		HttpEntity entity = builder.build();
		post.setEntity(entity);
		CloseableHttpResponse httpResponse = httpClient.execute(post);
		String content = "";
		try {
			HttpEntity _entity = httpResponse.getEntity();
			content = EntityUtils.toString(_entity);
			EntityUtils.consume(_entity);
		} finally {
			httpResponse.close();
		}
		f.delete();
	    
        // Convert JSON string to Map
	    ObjectMapper mapper = new ObjectMapper();
	    Map<String, Object> map = mapper.readValue(content, new TypeReference<Map<String, Object>>() {});
	    
	    return map;
	}
}
