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

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;


/**
 * A <b>Request</b> is the Catalina-internal facade for a
 * <code>ServletRequest</code> that is to be processed, in order to
 * produce the corresponding <code>Response</code>.
 *
 * @author Craig R. McClanahan
 * @version $Revision: 1.5 $ $Date: 2001/08/01 03:04:04 $
 */

public class RequestWrapper {
    private Connector connector;
    private HttpServletRequest request;
    private ResponseWrapper responseWrapper;
    private Socket socket;
    private InputStream stream;
    private Context context;

    public RequestWrapper(Connector connector) {
        this.connector = connector;
    }

    // --------------------------------------------------------- Public Methods

    public void refresh(Socket socket) throws IOException {
        this.stream = socket.getInputStream();
        this.socket = socket;
        request = new Request(this);
    }

    /**
     * Release all object references, and initialize instance variables, in
     * preparation for reuse of this object.
     */
    public void recycle(){
        this.request = null;
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
        return request;
    }



    public ResponseWrapper getResponseWrapper() {
        return responseWrapper;
    }

    public void setResponseWrapper(ResponseWrapper responseWrapper) {
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



    public String getScheme() {
        return connector.getScheme();
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
        return RequestWrapper.class.getName();
    }




//    /**
//     * Return the Wrapper within which this Request is being processed.
//     */
//    public Wrapper getWrapper();
//
//
//    /**
//     * Set the Wrapper within which this Request is being processed.  This
//     * must be called as soon as the appropriate Wrapper is identified, and
//     * before the Request is ultimately passed to an application servlet.
//     *
//     * @param wrapper The newly associated Wrapper
//     */
//    public void setWrapper(Wrapper wrapper);





//    /**
//     * Create and return a ServletInputStream to read the content
//     * associated with this Request.
//     *
//     * @exception IOException if an input/output error occurs
//     */
//    public ServletInputStream createInputStream() throws IOException;













}
