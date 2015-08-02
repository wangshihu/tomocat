package com.huihui.connector;

import com.huihui.util.RequestUtil;
import com.huihui.util.URIUtil;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;

/**
 * Created by hadoop on 2015/8/2 0002.
 */
public class RequestParse {
    HttpRequest request;
    private String postContentType;
    private boolean parsed =false;
    private URI uri;

    public RequestParse(HttpRequest httpRequest) {
        this.request = httpRequest;
    }

    public void parsing(){
        // Read a set of characters from the socket
        StringBuffer requestBuf = new StringBuffer(2048);
        byte[] buffer = new byte[2048];
        int i=-1;
        try {
            i = request.getStream().read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int j=0; j<i; j++) {
            requestBuf.append((char) buffer[j]);
        }
        parse(requestBuf.toString());
        request.setRequestURI(uri);
    }

    /**
     * 解析头信息，和内容体
     * @param requestStr
     */
    private void parse(String requestStr) {
        String[] headerArr = requestStr.split("\r\n");
        parseFirstLine(headerArr[0]);
        for(int i=1;i<headerArr.length;i++){
            if(!headerArr[i].equals("")){
                parseHeader(headerArr[i]);
            }else{//Post parameter储存。
                postContentType = headerArr[i+1];
                break;
            }
        }
        //uri处理parameter字符串。
        String url = uri.toString();
        int parameterIndex = url.indexOf("?");
        if(parameterIndex!=-1){
            request.setQueryString(url.substring(parameterIndex+1));
            url =url.substring(0, parameterIndex);
            uri = URIUtil.getUri(url);

        }
        //
        int position = url.indexOf("/", 1);
        String contextPattern = url.substring(0, position);
        request.setContextPath(contextPattern);
        String servletPath = url.substring(position);
        request.setServletPath(servletPath);

    }
    private void parseFirstLine(String str){
        String[] arr = str.split(" ");
        request.setMethod(arr[0]);//初始化method
        uri = URIUtil.getUri(arr[1]);
        request.setProtocol(arr[2]);

    }



    private void parseHeader(String headerStr){
        int index=  headerStr.indexOf(":");
        String name = headerStr.substring(0, index);
        String value = headerStr.substring(index + 1).trim();

        request.getHeaderMap().put(name, value);
        if(name.toLowerCase().equals("contentType")){
            request.setContentType(value);
        }else if(name.toLowerCase().equals("Content-Length")){
            request.setContentLength(Integer.valueOf(value));
        }else if(name.equals("Cookie")){
            parseCookie(value);
        }
    }

    private void parseCookie(String cookieStr){
        for(String cookie:cookieStr.split(";") ){
            cookie = cookie.trim();
            String[] arr = cookie.split("=");
            if(arr[0].equals(Globals.COOKIE_SESSION)){
                request.setRequestedSessionId(arr[1]);
            }
            request.addCookie(new Cookie(arr[0],arr[1]));
        }
    }

    public void parseParmeter() {
        String encoding = request.getCharacterEncoding();
        if (encoding == null)
            encoding = "ISO-8859-1";
        try {
            RequestUtil.parseParameters(request.getParameterMap(),postContentType, encoding);
            RequestUtil.parseParameters(request.getParameterMap(),request.getQueryString(),encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        parsed = true;
    }

    public boolean isParsed() {
        return parsed;
    }

}
