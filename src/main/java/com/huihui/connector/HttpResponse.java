package com.huihui.connector;


import com.huihui.core.context.Context;
import com.huihui.util.CookieTools;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;


/**
 * A <b>Response</b> is the Catalina-internal facade for a
 * <code>ServletResponse</code> that is to be produced,
 * based on the processing of a corresponding <code>Request</code>.
 *
 * @author Craig R. McClanahan
 * @version $Revision: 1.7 $ $Date: 2002/03/15 19:12:48 $
 */

public class HttpResponse implements HttpServletResponse {
    private Connector connector;
    private OutputStream outputStream;
    private HttpServletResponseFacade facade;
    private HttpRequest request;
    private Socket socket;

    /**
     * The ServletOutputStream that has been returned by
     * <code>getOutputStream()</code>, if any.
     */
    protected ServletOutputStream stream = null;
    private boolean committed;
    private int  status =200;
    private String message = "ok";
    private int contentLength = -1;
    private String contentType;

    private List<Cookie> cookies = new ArrayList<>();
    private Context context;


    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    private PrintWriter writer;

    public HttpResponse(Connector connector) {
        this.connector = connector;
    }

    /**
     * Perform whatever actions are required to flush and close the output
     * stream or writer, in a single operation.
     *
     * @exception IOException if an input/output error occurs
     */
    public void finishResponse() throws IOException{

        sendHeaders();
        // Flush and close the appropriate output mechanism

        writer.flush();
        writer.close();

    }


    private void sendHeaders() {
        // Prepare a suitable output writer
        OutputStreamWriter osr = null;
        osr = new OutputStreamWriter(outputStream);
        final PrintWriter outputWriter = new PrintWriter(osr);
        // Send the "Status:" header
        outputWriter.print(getProtocol());
        outputWriter.print(" ");
        outputWriter.print(status);
        if (message != null) {
            outputWriter.print(" ");
            outputWriter.print(message);
        }
        outputWriter.print("\r\n");
        // Send the content-length and content-type headers (if any)
        if (getContentType() != null) {
            outputWriter.print("Content-Type: " + getContentType() + "\r\n");
        }
//        if (getContentLength() >= 0) {
//            outputWriter.print("Content-Length: " + 9511 +
//                    "\r\n");
//        }


        // Add the session ID cookie if necessary
        HttpSession session = request.getRequest().getSession(false);

        if ((session != null) && session.isNew() && (getContext() != null)
                && getContext().getCookies()) {
            Cookie cookie = new Cookie(Globals.COOKIE_SESSION,
                    session.getId());

            cookie.setMaxAge(-1);
            String contextPath = null;
            if (context != null)
                contextPath = context.getPath();
            if ((contextPath != null) && (contextPath.length() > 0))
                cookie.setPath(contextPath);
            else
                cookie.setPath("/");
            addCookie(cookie);
        }

        // Send all specified cookies (if any)
        synchronized (cookies) {
            Iterator items = cookies.iterator();
            while (items.hasNext()) {
                Cookie cookie = (Cookie) items.next();
                outputWriter.print(CookieTools.getCookieHeaderName(cookie));
                outputWriter.print(": ");
                outputWriter.print(CookieTools.getCookieHeaderValue(cookie));
                outputWriter.print("\r\n");
            }
        }
        outputWriter.print("\r\n");
        outputWriter.flush();
    }

    public void refresh(Socket socket) throws IOException {
        this.outputStream = socket.getOutputStream();
        this.socket = socket;
    }

    /**
     * Release all object references, and initialize instance variables, in
     * preparation for reuse of this object.
     */
    public void recycle(){

    }

    public Connector getConnector() {
        return connector;
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    public HttpRequest getRequest() {
        return request;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        if(facade==null)
            facade= new HttpServletResponseFacade(this);
        return facade;
    }



    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }


    public void setWriter(PrintWriter writer) {
        this.writer = writer;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (writer != null)
            return writer;

        ResponseStream newStream = (ResponseStream) createOutputStream();
        newStream.setCommit(false);
        OutputStreamWriter osr =
                new OutputStreamWriter(newStream);
//        writer = new ResponseWriter(osr, newStream);
        writer=new PrintWriter(outputStream);
        stream = newStream;
        return writer;
    }

    public OutputStream getStream() {
        return outputStream;
    }

    protected int bufferCount = 0;
    protected int contentCount = 0;
    protected byte[] buffer = new byte[1024];
    /**
     * Write the specified byte to our output stream, flushing if necessary.
     *
     * @param b The byte to be written
     *
     * @exception IOException if an input/output error occurs
     */
    public void write(int b) throws IOException {

        if (bufferCount >= buffer.length)
            flushBuffer();
        buffer[bufferCount++] = (byte) b;
        contentCount++;
    }


    /**
     * Write <code>b.length</code> bytes from the specified byte array
     * to our output stream.  Flush the output stream as necessary.
     *
     * @param b The byte array to be written
     *
     * @exception IOException if an input/output error occurs
     */
    public void write(byte b[]) throws IOException {
        write(b, 0, b.length);
    }


    /**
     * Write <code>len</code> bytes from the specified byte array, starting
     * at the specified offset, to our output stream.  Flush the output
     * stream as necessary.
     *
     * @param b The byte array containing the bytes to be written
     * @param off Zero-relative starting offset of the bytes to be written
     * @param len The number of bytes to be written
     *
     * @exception IOException if an input/output error occurs
     */
    public void write(byte b[], int off, int len) throws IOException {
        // If the whole thing fits in the buffer, just put it there
        if (len == 0)
            return;
        if (len <= (buffer.length - bufferCount)) {
            System.arraycopy(b, off, buffer, bufferCount, len);
            bufferCount += len;
            contentCount += len;
            return;
        }

        // Flush the buffer and start writing full-buffer-size chunks
        flushBuffer();
        int iterations = len / buffer.length;
        int leftoverStart = iterations * buffer.length;
        int leftoverLen = len - leftoverStart;
        for (int i = 0; i < iterations; i++)
            write(b, off + (i * buffer.length), buffer.length);

        // Write the remainder (guaranteed to fit in the buffer)
        if (leftoverLen > 0)
            write(b, off + leftoverStart, leftoverLen);

    }


    /**
     * Create and return a ServletOutputStream to write the content
     * associated with this Response.
     *
     * @exception IOException if an input/output error occurs
     */
    public ServletOutputStream createOutputStream() throws IOException {

        return new ResponseStream(this);

    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return stream;
    }


    @Override
    public void addCookie(Cookie cookie) {
        cookies.add(cookie);
    }

    @Override
    public boolean containsHeader(String name) {
        return false;
    }

    @Override
    public String encodeURL(String url) {
        return null;
    }

    @Override
    public String encodeRedirectURL(String url) {
        return null;
    }

    @Override
    public String encodeUrl(String url) {
        return null;
    }

    @Override
    public String encodeRedirectUrl(String url) {
        return null;
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {

    }

    @Override
    public void sendError(int sc) throws IOException {

    }

    @Override
    public void sendRedirect(String location) throws IOException {

    }

    @Override
    public void setDateHeader(String name, long date) {

    }


    @Override
    public void addDateHeader(String name, long date) {

    }

    @Override
    public void setHeader(String name, String value) {

    }

    @Override
    public void addHeader(String name, String value) {

    }

    @Override
    public void setIntHeader(String name, int value) {

    }

    @Override
    public void addIntHeader(String name, int value) {

    }

    @Override
    public void setStatus(int sc) {

    }

    @Override
    public void setStatus(int sc, String sm) {

    }

    @Override
    public int getStatus() {
        return 0;
    }

    @Override
    public String getHeader(String name) {
        return null;
    }

    @Override
    public Collection<String> getHeaders(String name) {
        return null;
    }

    @Override
    public Collection<String> getHeaderNames() {
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public String getContentType() {
        return contentType;
    }





    @Override
    public void setCharacterEncoding(String charset) {

    }

    @Override
    public void setContentLength(int len) {

    }

    @Override
    public void setContentLengthLong(long len) {

    }

    @Override
    public void setContentType(String type) {
        this.contentType = type;
    }

    @Override
    public void setBufferSize(int size) {

    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public void flushBuffer() throws IOException {
        committed = true;
        if (bufferCount > 0) {
            try {
                outputStream.write(buffer, 0, bufferCount);
            } finally {
                bufferCount = 0;
            }
        }
    }

    @Override
    public void resetBuffer() {

    }

    @Override
    public boolean isCommitted() {
        return false;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setLocale(Locale loc) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }


    public int getContentLength() {

        return contentLength;

    }

    public String getProtocol() {
        return request.getProtocol();
    }

    public Context getContext() {
        if(context==null)
            context = request.getContext();
        return context;
    }

}
