package org.mule.modules.wechat.common;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.activation.DataHandler;
import javax.net.ssl.HttpsURLConnection;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.mime.MimeTypes;
import org.json.JSONObject;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HttpsConnection {
	public Map<String, Object> Get(String httpsURL) throws Exception {
		
		String result = "";
		URL url = new URL(httpsURL);
	    HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
	    con.setRequestMethod("GET");
	    con.setRequestProperty("Content-Type", "application/json; encoding=utf-8");
	    InputStream ins = con.getInputStream();
	    InputStreamReader isr = new InputStreamReader(ins, "UTF-8");
	    BufferedReader in = new BufferedReader(isr);
	    
	    String inputLine;
	    while ((inputLine = in.readLine()) != null)
	    {
	    	result += (new JSONObject(inputLine)).toString();
	    }
	    in.close();
	    
	    ObjectMapper mapper = new ObjectMapper();
	    Map<String, Object> map = new HashMap<String, Object>();
	    // convert JSON string to Map
	    map = mapper.readValue(result, new TypeReference<Map<String, Object>>() {});
	    
	    return map;
	}
	
	public Map<String, Object> Post(String httpsURL, String json) throws Exception {
		
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
		
	    InputStream ins = con.getInputStream();
	    InputStreamReader isr = new InputStreamReader(ins, "UTF-8");
	    BufferedReader in = new BufferedReader(isr);
	    
	    String inputLine;
	    while ((inputLine = in.readLine()) != null)
	    {
	    	result += (new JSONObject(inputLine)).toString();
	    }
	    in.close();
	    
	    ObjectMapper mapper = new ObjectMapper();
	    Map<String, Object> map = new HashMap<String, Object>();
	    // convert JSON string to Map
	    map = mapper.readValue(result, new TypeReference<Map<String, Object>>() {});
	    
	    return map;
	}
	
	public Map<String, Object> PostFile(String httpsURL, DataHandler attachment) throws Exception {
		String result = "";
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = UUID.randomUUID().toString();
		TikaConfig config = TikaConfig.getDefaultConfig();
		MimeTypes allTypes = config.getMimeRepository();
		String ext = allTypes.forName(attachment.getContentType()).getExtension();
		if (ext == "") {
			ContentTypeEnum contentTypeEnum = ContentTypeEnum.getContentTypeEnumByContentType(attachment.getContentType().toLowerCase());
			ext = java.util.Optional.ofNullable(contentTypeEnum.getExtension()).orElse("");
		}
		
		URL url = new URL(httpsURL);
	    HttpURLConnection con = (HttpURLConnection)url.openConnection();
	    con.setDoInput(true);
	    con.setDoOutput(true);
	    con.setUseCaches(false);
	    con.setRequestMethod("POST");
	    con.setRequestProperty("Connection", "Keep-Alive");
	    con.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
	    DataOutputStream dos = new DataOutputStream(con.getOutputStream());
	    dos.writeBytes(twoHyphens + boundary + lineEnd);
	    dos.writeBytes("Content-Disposition: form-data; name=\"file\";"
	    		+ " filename=\"file" + ext + "\"" + lineEnd);
	    dos.writeBytes("Content-Type: " + attachment.getContentType() + lineEnd);
	    dos.writeBytes(lineEnd);

	    byte[] buffer = new byte[1024];
        int bytesRead = -1;
        InputStream fis = attachment.getInputStream();
        while ((bytesRead = fis.read(buffer)) != -1) {
        	dos.write(buffer, 0, bytesRead);
        }
	    
	    dos.writeBytes(lineEnd);
	    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
	    
	    attachment.getInputStream().close();
	    dos.flush();
	    dos.close();
		
	    // Read Response
	    InputStream ins = con.getInputStream();
	    InputStreamReader isr = new InputStreamReader(ins, "UTF-8");
	    BufferedReader in = new BufferedReader(isr);
	    
	    String inputLine;
	    while ((inputLine = in.readLine()) != null)
	    {
	    	result += (new JSONObject(inputLine)).toString();
	    }
	    in.close();
	    
	    ObjectMapper mapper = new ObjectMapper();
	    Map<String, Object> map = new HashMap<String, Object>();
	    // convert JSON string to Map
	    map = mapper.readValue(result, new TypeReference<Map<String, Object>>() {});
	    
	    return map;
	}

}
