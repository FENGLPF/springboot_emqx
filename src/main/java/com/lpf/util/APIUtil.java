package com.lpf.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class APIUtil {
	

	public static String get(String url) {  
	    BufferedReader in = null;  
	    try {  
	        URL realUrl = new URL(url);
	        URLConnection connection = realUrl.openConnection();
	        connection.setRequestProperty("accept", "*/*");  
	        connection.setRequestProperty("connection", "Keep-Alive");  
	        connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");  
	        connection.setConnectTimeout(5000);  
	        connection.setReadTimeout(5000);
	        connection.connect();
	        in = new BufferedReader(new InputStreamReader(connection.getInputStream()));  
	        StringBuffer sb = new StringBuffer();  
	        String line;  
	        while ((line = in.readLine()) != null) {  
	            sb.append(line);  
	        }  
	        return sb.toString();  
	    } catch (Exception e) {  
	         
	    }
	    finally {  
	        try {  
	            if (in != null) {  
	                in.close();  
	            }  
	        } catch (Exception e2) {  
	            e2.printStackTrace();  
	        }  
	    }  
	    return null;  
	}

	public static String jsonPost(String strURL, String jsonStr) {  
	    try {  
	        URL url = new URL(strURL);// ��������  
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();  
	        connection.setDoOutput(true);  
	        connection.setDoInput(true);  
	        connection.setUseCaches(false);  
	        connection.setInstanceFollowRedirects(true);  
	        connection.setRequestMethod("POST");
	        connection.setRequestProperty("Accept", "application/json");
	        connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
	        connection.connect();  
	        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "utf-8");
	        out.append(jsonStr);  
	        out.flush();  
	        out.close();  

	        int code = connection.getResponseCode();  
	        InputStream is = null;  
	        if (code == 200) {  
	            is = connection.getInputStream();  
	        } else {  
	            is = connection.getErrorStream();  
	        }  

	        int length = (int) connection.getContentLength();
	        if (length != -1) {  
	            byte[] data = new byte[length];  
	            byte[] temp = new byte[512];  
	            int readLen = 0;  
	            int destPos = 0;  
	            while ((readLen = is.read(temp)) > 0) {  
	                System.arraycopy(temp, 0, data, destPos, readLen);  
	                destPos += readLen;  
	            }  
	            String result = new String(data, "UTF-8"); // utf-8����  
	            return result;  
	        }  

	    } catch (IOException e) {  
	        e.printStackTrace();
	    }  
	    return "error";
	}
	
	public static void webHookPost(String strURL,String jsonStr)  {
		try {
		    URL url = new URL(strURL);
		    HttpURLConnection con = (HttpURLConnection)url.openConnection();
		    con.setRequestMethod("POST");
		    con.setUseCaches(false);
		    con.setDoInput(true);
		    con.setDoOutput(true);

		    con.setRequestProperty("Content-Type", "application/json");
		    con.setRequestProperty("Authorization", "Basic cm9vdDp0YW9zZGF0YQ==");

		    con.connect();

		    OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream(),"UTF-8");
		    writer.write(jsonStr);
		    writer.flush();

		    InputStream is = con.getInputStream();

		    con.disconnect();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
