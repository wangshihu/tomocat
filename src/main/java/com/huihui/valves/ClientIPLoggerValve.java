package com.huihui.valves;

import com.huihui.connector.RequestWrapper;
import com.huihui.connector.ResponseWrapper;
import com.huihui.core.*;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import java.io.IOException;


public class ClientIPLoggerValve implements Valve, Contained {

  protected Container container;

  public void invoke(RequestWrapper request, ResponseWrapper response)
    throws IOException, ServletException {


    System.out.println("Client IP Logger Valve");
    ServletRequest sreq = request.getRequest();
    System.out.println(sreq.getRemoteAddr());
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