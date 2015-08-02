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


import com.huihui.core.context.Context;
import com.huihui.core.Host;
import com.huihui.core.session.Manager;
import com.huihui.core.session.Session;
import com.huihui.util.Enumerator;

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
import java.util.*;
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


    private URI uri;
    private String method;
    private String protocol;
    private Map<String,String[]> parameterMap = new ConcurrentHashMap<>();
    private Map<String,Object> attributes = new ConcurrentHashMap<>();
    private Map<String,String> headers= new ConcurrentHashMap<>();
    private RequestParse requestParse;
    private String queryString;
    private String contentType;
    private int contentLength;

    private String requestedSessionId;
    private Session session;
    private Context context;
    private String contextPath;
    private String servletPath;
    private List<Cookie> cookies = new ArrayList<>();


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




    /**
     * Return descriptive information about this Request implementation and
     * the corresponding version number, in the format
     * <code>&lt;description&gt;/&lt;version&gt;</code>.
     */
    public String getInfo(){
        return HttpRequest.class.getName();
    }



    public void init(){
        if(requestParse==null)
            requestParse = new RequestParse(this);
        requestParse.parsing();
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
        return requestedSessionId;
    }

    public void setRequestedSessionId(String requestedSessionId) {
        this.requestedSessionId = requestedSessionId;
    }

    @Override
    public HttpSession getSession(boolean create){
        return doGetSession(create);
    }

    private HttpSession doGetSession(boolean create) {
        if(session!=null){
            if(session.isValid())
                return session.getSession();
            else
                session = null;
        }
        // Return the requested session if it exists and is valid
        Manager manager = null;
        if (context != null)
            manager = context.getManager();

        if (manager == null)
            return null;      // Sessions are not supported
        if (requestedSessionId != null) {
            session = manager.findSession(requestedSessionId);
            if(session!=null){
                if(session.isValid())
                    return session.getSession();
                else
                    session = null;
            }
        }
        // Create a new session if requested and the response is not committed
        if (!create)
            return (null);
        session = manager.createSession();
        if (session != null)
            return session.getSession();
        return null;
    }

    @Override
    public HttpSession getSession() {
        return getSession(true);
    }

    @Override
    public String changeSessionId() {
        return null;
    }
    @Override
    public Cookie[] getCookies() {
        synchronized (cookies){
            return cookies.toArray(new Cookie[0]);
        }
    }

    public void addCookie(Cookie cookie) {
        this.cookies.add(cookie);
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

    public Map<String,String> getHeaderMap(){
        return headers;
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
        if(!requestParse.isParsed())
            requestParse.parseParmeter();
        String[] values = parameterMap.get(name);
        if(values==null)
            return null;
        return parameterMap.get(name)[0];
    }



    @Override
    public Enumeration<String> getParameterNames() {
        if(!requestParse.isParsed())
            requestParse.parseParmeter();
        return new Enumerator(parameterMap.keySet());
    }

    @Override
    public String[] getParameterValues(String name) {
        if(!requestParse.isParsed())
            requestParse.parseParmeter();
        return parameterMap.get(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return parameterMap;
    }

    @Override
    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString){
        this.queryString = queryString;
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
    public void setContentLength(int contentLength){
        this.contentLength = contentLength;
    }

    @Override
    public long getContentLengthLong() {
        return contentLength;
    }

    @Override
    public String getContentType() {
        return contentType;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    @Override
    public String getProtocol() {
        return protocol;
    }
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
    @Override
    public String getMethod() {
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
    }
    @Override
    public String getRequestURI() {
        return uri.toString();
    }
    public void setRequestURI(URI uri) {
        this.uri = uri;
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

    @Override
    public String getContextPath() {
        return this.contextPath;
    }

    @Override
    public String getServletPath() {
        return this.servletPath;
    }

    public void setServletPath(String servletPath) {
        this.servletPath = servletPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
        if(context==null){
            Host host  = (Host) connector.getContainer();
            context = host.getContext(contextPath);
        }

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
