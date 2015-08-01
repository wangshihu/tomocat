package com.huihui.valves;

import com.huihui.connector.RequestWrapper;
import com.huihui.connector.ResponseWrapper;
import com.huihui.core.*;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;


public class HeaderLoggerValve implements Valve, Contained {

  protected Container container;

  public void invoke(RequestWrapper request, ResponseWrapper response)
    throws IOException, ServletException {


    System.out.println("Header Logger Valve");
    ServletRequest sreq = request.getRequest();
    if (sreq instanceof HttpServletRequest) {
      HttpServletRequest hreq = (HttpServletRequest) sreq;
      Enumeration headerNames = hreq.getHeaderNames();
      while (headerNames.hasMoreElements()) {
        String headerName = headerNames.nextElement().toString();
        String headerValue = hreq.getHeader(headerName);
        System.out.println(headerName + ":" + headerValue);
      }

    }
    else
      System.out.println("Not an HTTP Request");

    System.out.println("------------------------------------");
  }

  public String getInfo() {
    return null;
  }

  public Container getContainer() {
    return container;
  }

  public void setContainer(Container container) {
    this.container = container;
  }
}