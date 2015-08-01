package com.huihui.connector;


import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.Socket;


/**
 * A <b>Response</b> is the Catalina-internal facade for a
 * <code>ServletResponse</code> that is to be produced,
 * based on the processing of a corresponding <code>Request</code>.
 *
 * @author Craig R. McClanahan
 * @version $Revision: 1.7 $ $Date: 2002/03/15 19:12:48 $
 */

public class ResponseWrapper {
    private Connector connector;
    private OutputStream outputStream;
    private HttpServletResponse response;
    private RequestWrapper requestWrapper;
    private Socket socket;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    private PrintWriter writer;

    public ResponseWrapper(Connector connector) {
        this.connector = connector;
    }

    /**
     * Perform whatever actions are required to flush and close the output
     * stream or writer, in a single operation.
     *
     * @exception IOException if an input/output error occurs
     */
    public void finishResponse() throws IOException{
         //sendHeaders();

//        String response =
//                "HTTP/1.1 200 \r\n" +
//                "Content-Length: 22\r\n" +
//                "Content-Type: text/html\r\n\r\n";
//        String body ="<h1>404 Not Foundaaaaaaaaa</h1>";
//        PrintWriter pw1 = new PrintWriter(outputStream);
//        PrintWriter pw2 = new PrintWriter(outputStream);
//        pw1.write(response);
//        pw2.write(body);
//        pw1.flush();
//        pw2.flush();
        writer.flush();
        // Flush and close the appropriate output mechanism


          //  writer.close();

    }

    private void sendHeaders() {
        // Prepare a suitable output writer
        OutputStreamWriter osr = null;

        try {
            osr = new OutputStreamWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        final PrintWriter outputWriter = new PrintWriter(osr);

        // Send the "Status:" header
        outputWriter.print("123");
        outputWriter.print(" ");
        outputWriter.print(200);

        outputWriter.print("\r\n");
        outputWriter.print("Content-Type: " + "text/html" + "\r\n");
        outputWriter.print("\r\n");

        outputWriter.flush();
    }

    public void refresh(Socket socket) throws IOException {
        this.outputStream = socket.getOutputStream();
        writer = new PrintWriter(outputStream);
        response = new Response(this);
        this.socket = socket;
    }

    /**
     * Release all object references, and initialize instance variables, in
     * preparation for reuse of this object.
     */
    public void recycle(){
        this.response = null;
    }

    public Connector getConnector() {
        return connector;
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    public RequestWrapper getRequestWrapper() {
        return requestWrapper;
    }

    public void setRequestWrapper(RequestWrapper requestWrapper) {
        this.requestWrapper = requestWrapper;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public PrintWriter getWriter() {
        return writer;
    }

    public void setWriter(PrintWriter writer) {
        this.writer = writer;
    }

//    /**
//     * Return the number of bytes actually written to the output stream.
//     */
//    public int getContentCount();


//    /**
//     * Return the Context with which this Response is associated.
//     */
//    public Context getContext();
//
//
//    /**
//     * Set the Context with which this Response is associated.  This should
//     * be called as soon as the appropriate Context is identified.
//     *
//     * @param context The associated Context
//     */
//    public void setContext(Context context);


//    /**
//     * Set the application commit flag.
//     *
//     * @param appCommitted The new application committed flag value
//     */
//    public void setAppCommitted(boolean appCommitted);
//
//
//    /**
//     * Application commit flag accessor.
//     */
//    public boolean isAppCommitted();
//
//
//    /**
//     * Return the "processing inside an include" flag.
//     */
//    public boolean getIncluded();
//
//
//    /**
//     * Set the "processing inside an include" flag.
//     *
//     * @param included <code>true</code> if we are currently inside a
//     *  RequestDispatcher.include(), else <code>false</code>
//     */
//    public void setIncluded(boolean included);
//
//
//    /**
//     * Return descriptive information about this Response implementation and
//     * the corresponding version number, in the format
//     * <code>&lt;description&gt;/&lt;version&gt;</code>.
//     */
//    public String getInfo();





//    /**
//     * Set the suspended flag.
//     *
//     * @param suspended The new suspended flag value
//     */
//    public void setSuspended(boolean suspended);
//
//
//    /**
//     * Suspended flag accessor.
//     */
//    public boolean isSuspended();
//
//
//    /**
//     * Set the error flag.
//     */
//    public void setError();
//
//
//    /**
//     * Error flag accessor.
//     */
//    public boolean isError();


    // --------------------------------------------------------- Public Methods


//    /**
//     * Create and return a ServletOutputStream to write the content
//     * associated with this Response.
//     *
//     * @exception IOException if an input/output error occurs
//     */
//    public ServletOutputStream createOutputStream() throws IOException;





//    /**
//     * Return the content length that was set or calculated for this Response.
//     */
//    public int getContentLength();
//
//
//    /**
//     * Return the content type that was set or calculated for this response,
//     * or <code>null</code> if no content type was set.
//     */
//    public String getContentType();
//
//
//    /**
//     * Return a PrintWriter that can be used to render error messages,
//     * regardless of whether a stream or writer has already been acquired.
//     *
//     * @return Writer which can be used for error reports. If the response is
//     * not an error report returned using sendError or triggered by an
//     * unexpected exception thrown during the servlet processing
//     * (and only in that case), null will be returned if the response stream
//     * has already been used.
//     */
//    public PrintWriter getReporter();




//    /**
//     * Reset the data buffer but not any status or header information.
//     */
//    public void resetBuffer();
//
//
//    /**
//     * Send an acknowledgment of a request.
//     *
//     * @exception IOException if an input/output error occurs
//     */
//    public void sendAcknowledgement()
//        throws IOException;


}
