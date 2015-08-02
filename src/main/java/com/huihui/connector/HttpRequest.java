/*
 * $Header: /home/cvs/jakarta-tomcat-4.0/catalina/src/share/org/apache/catalina/Request.java,v 1.5 2001/08/01 03:04:04 craigmcc Exp $
 * $Revision: 1.5 $
 * $Date: 2001/08/01 03:04:04 $
 *
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Tomcat", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 * [Additional notices, if required by prior licensing conditions]
 *
 */


package com.huihui.connector;


import com.huihui.core.Context;
import com.huihui.core.Host;
import com.huihui.util.Enumerator;
import com.huihui.util.RequestUtil;
import com.huihui.util.URIUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URI;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * A <b>Request</b> is the Catalina-internal facade for a
 * <code>ServletRequest</code> that is to be processed, in order to
 * produce the corresponding <code>Response</code>.
 *
 * @author Craig R. McClanahan
 * @version $Revision: 1.5 $ $Date: 2001/08/01 03:04:04 $
 */

public class HttpRequest implements HttpServletRequest{
    private Connector connector;
    private HttpServletRequestFacade facade;
    private HttpResponse responseWrapper;
    private Socket socket;
    private InputStream stream;
    private Context context;

    private URI uri;
    private String method;
    private String protocol;
    private Map<String,String[]> parameterMap = new ConcurrentHashMap<>();
    private boolean parsed;
    private Map<String,String> headers= new ConcurrentHashMap<>();
    private String postContentType ;
    private String queryString;
    private String contentType;
    private int contentLength;
    private Map<String,Object> attributes = new ConcurrentHashMap<>();

    public HttpRequest(Connector connector) {
        this.connector = connector;
    }


    // --------------------------------------------------------- Public Methods

    public void refresh(Socket socket) throws IOException {
        this.stream = socket.getInputStream();
        this.socket = socket;
        init();
    }

    /**
     * Release all object references, and initialize instance variables, in
     * preparation for reuse of this object.
     */
    public void recycle(){

    }

    /**
     * Perform whatever actions are required to flush and close the input
     * stream or reader, in a single operation.
     *
     * @exception IOException if an input/output error occurs
     */
    public void finishRequest() throws IOException{

    }

    public Connector getConnector() {
        return connector;
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    public HttpServletRequest getRequest() {
        if(facade==null)
            facade = new HttpServletRequestFacade(this);
        return facade;
    }



    public HttpResponse getResponseWrapper() {
        return responseWrapper;
    }

    public void setResponseWrapper(HttpResponse responseWrapper) {
        this.responseWrapper = responseWrapper;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public InputStream getStream() {
        return stream;
    }

    public void setStream(InputStream stream) {
        this.stream = stream;
    }






    /**
     * Return the Context within which this Request is being processed.
     */
    public Context getContext(){
        return context;
    }



    /**
     * Set the Context within which this Request is being processed.  This
     * must be called as soon as the appropriate Context is identified, because
     * it identifies the value to be returned by <code>getContextPath()</code>,
     * and thus enables parsing of the request URI.
     *
     * @param context The newly associated Context
     */
    public void setContext(Context context){
        this.context = context;
    }

    public void setContext(String contextPattern) {
        Host host = (Host) connector.getContainer();
        this.context = host.getContext(contextPattern);
    }


    /**
     * Return descriptive information about this Request implementation and
     * the corresponding version number, in the format
     * <code>&lt;description&gt;/&lt;version&gt;</code>.
     */
    public String getInfo(){
        return HttpRequest.class.getName();
    }



    public void init(){
        // Read a set of characters from the socket
        StringBuffer request = new StringBuffer(2048);
        byte[] buffer = new byte[2048];
        int i=-1;
        try {
            i = stream.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int j=0; j<i; j++) {
            request.append((char) buffer[j]);
        }
        parse(request.toString());
    }

    /**
     * 解析头信息，和内容体
     * @param request
     */
    public void parse(String request) {
        String[] headerArr = request.split("\r\n");
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
        int parameterIndex = uri.toString().indexOf("?");
        if(parameterIndex!=-1){
            uri = URIUtil.getUri(url.substring(0, parameterIndex));
            queryString = url.substring(parameterIndex+1);
        }

    }
    private void parseFirstLine(String str){
        String[] arr = str.split(" ");
        method = arr[0];//初始化method
        dealUri(arr[1]);
        protocol = arr[2];
    }

    private void dealUri(String url){
        int position = url.indexOf("/", 1);
        String contextPattern = url.substring(0, position);
        setContext(contextPattern);
        url = url.substring(position);
        uri = URIUtil.getUri(url);
    }

    public void parseHeader(String headerStr){
        int index=  headerStr.indexOf(":");
        String name = headerStr.substring(0, index);
        String value = headerStr.substring(index + 1).trim();
        headers.put(name, value);
        if(name.toLowerCase().equals("contentType")){
            contentType = value;
        }else if(name.toLowerCase().equals("Content-Length")){
            contentLength = Integer.valueOf(value);
        }

    }
    //--------------------------------未实现的
    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {

    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }
    @Override
    public String getRequestedSessionId() {
        return null;
    }
    @Override
    public HttpSession getSession(boolean create) {
        return null;
    }

    @Override
    public HttpSession getSession() {
        return null;
    }

    @Override
    public String changeSessionId() {
        return null;
    }
    @Override
    public Cookie[] getCookies() {
        return new Cookie[0];
    }
    //----------------------------------headers,attributes,and parameters

    @Override
    public String getHeader(String name) {
        return headers.get(name);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        throw new UnsupportedOperationException("i don't kown");
    }

    @Override
    public Enumeration<String> getHeaderNames() {

        return new Enumerator(headers.keySet());
    }

    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }
    @Override
    public void setAttribute(String name, Object o) {
        attributes.put(name, o);
    }

    @Override
    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return new Enumerator(attributes.entrySet());
    }

    @Override
    public String getParameter(String name) {
        if(!parsed)
            parseParmeter();
        return parameterMap.get(name)[0];
    }

    private void parseParmeter() {
        String encoding = getCharacterEncoding();
        if (encoding == null)
            encoding = "ISO-8859-1";
        try {
            RequestUtil.parseParameters(parameterMap, postContentType, encoding);
            RequestUtil.parseParameters(parameterMap,queryString,encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        parsed=true;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        if(!parsed)
            parseParmeter();
        return new Enumerator(parameterMap.keySet());
    }

    @Override
    public String[] getParameterValues(String name) {
        if(!parsed)
            parseParmeter();
        return parameterMap.get(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        if(!parsed)
            parseParmeter();
        return parameterMap;
    }

    @Override
    public String getQueryString() {
        return queryString;
    }



    //---------------------------------------------------------------------------一些属性Get
    @Override
    public ServletInputStream getInputStream() throws IOException {
        return (ServletInputStream) this.stream;
    }
    @Override
    public int getContentLength() {
        return contentLength;
    }

    @Override
    public long getContentLengthLong() {
        return contentLength;
    }

    @Override
    public String getContentType() {
        return contentType;
    }
    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public String getMethod() {
        return method;
    }
    @Override
    public String getRequestURI() {
        return uri.toString();
    }

    @Override
    public StringBuffer getRequestURL() {
        StringBuffer result = null;
        try {
            result = new StringBuffer(uri.toURL().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return result;
    }
    //-----------------------------------------------------------------------------网络项
    @Override
    public String getScheme() {
       return connector.getScheme();
    }

    @Override
    public int getRemotePort() {
        return socket.getPort();
    }

    @Override
    public String getLocalName() {
        return socket.getLocalAddress().toString();
    }

    @Override
    public String getLocalAddr() {
        return socket.getLocalAddress().toString();
    }

    @Override
    public int getLocalPort() {
        return socket.getLocalPort();
    }
    @Override
    public String getServerName() {
        return socket.getLocalAddress().toString();
    }

    @Override
    public int getServerPort() {
        return socket.getLocalPort();
    }
    @Override
    public String getRemoteAddr() {
        return socket.getRemoteSocketAddress().toString();
    }

    @Override
    public String getRemoteHost() {
        return socket.getInetAddress().toString();
    }

    //-----------------------------------------------------------------------------下面基本没用
    @Override
    public String getAuthType() {
        return null;
    }



    @Override
    public long getDateHeader(String name) {
        return 0;
    }



    @Override
    public int getIntHeader(String name) {
        return 0;
    }


    @Override
    public String getPathInfo() {
        return null;
    }

    @Override
    public String getPathTranslated() {
        return null;
    }

    @Override
    public String getContextPath() {
        return null;
    }



    @Override
    public String getRemoteUser() {
        return null;
    }

    @Override
    public boolean isUserInRole(String role) {
        return false;
    }

    @Override
    public Principal getUserPrincipal() {
        return null;
    }




    @Override
    public String getServletPath() {
        return null;
    }



    @Override
    public boolean isRequestedSessionIdValid() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        return false;
    }

    @Override
    public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
        return false;
    }

    @Override
    public void login(String username, String password) throws ServletException {

    }

    @Override
    public void logout() throws ServletException {

    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        return null;
    }

    @Override
    public Part getPart(String name) throws IOException, ServletException {
        return null;
    }

    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {
        return null;
    }





    @Override
    public BufferedReader getReader() throws IOException {
        return null;
    }



    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public Enumeration<Locale> getLocales() {
        return null;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return null;
    }

    @Override
    public String getRealPath(String path) {
        return null;
    }



    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return null;
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return null;
    }

    @Override
    public boolean isAsyncStarted() {
        return false;
    }

    @Override
    public boolean isAsyncSupported() {
        return false;
    }

    @Override
    public AsyncContext getAsyncContext() {
        return null;
    }

    @Override
    public DispatcherType getDispatcherType() {
        return null;
    }











}
