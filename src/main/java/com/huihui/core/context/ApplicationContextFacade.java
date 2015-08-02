package com.huihui.core.context;

import javax.servlet.*;
import javax.servlet.descriptor.JspConfigDescriptor;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Map;
import java.util.Set;

/**
 * Created by hadoop on 2015/8/2 0002.
 */
public class ApplicationContextFacade implements ServletContext{
    private ApplicationContext applicationContext;
    public ApplicationContextFacade(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public URL getResource(String path) throws MalformedURLException {
        return applicationContext.getResource(path);
    }

    @Override
    public Set<String> getResourcePaths(String path) {
        return applicationContext.getResourcePaths(path);
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, Servlet servlet) {
        return applicationContext.addServlet(servletName, servlet);
    }

    @Override
    public int getEffectiveMajorVersion() {
        return applicationContext.getEffectiveMajorVersion();
    }

    @Override
    public String getMimeType(String file) {
        return applicationContext.getMimeType(file);
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, Class<? extends Filter> filterClass) {
        return applicationContext.addFilter(filterName, filterClass);
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return applicationContext.getRequestDispatcher(path);
    }

    @Override
    public String getRealPath(String path) {
        return applicationContext.getRealPath(path);
    }

    @Override
    public Servlet getServlet(String name) throws ServletException {
        return applicationContext.getServlet(name);
    }

    @Override
    public ClassLoader getClassLoader() {
        return applicationContext.getClassLoader();
    }

    @Override
    public RequestDispatcher getNamedDispatcher(String name) {
        return applicationContext.getNamedDispatcher(name);
    }

    @Override
    public Map<String, ? extends ServletRegistration> getServletRegistrations() {
        return applicationContext.getServletRegistrations();
    }

    @Override
    public ServletRegistration getServletRegistration(String servletName) {
        return applicationContext.getServletRegistration(servletName);
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, String className) {
        return applicationContext.addServlet(servletName, className);
    }

    @Override
    public int getEffectiveMinorVersion() {
        return applicationContext.getEffectiveMinorVersion();
    }

    @Override
    public Object getAttribute(String name) {
        return applicationContext.getAttribute(name);
    }

    @Override
    public FilterRegistration getFilterRegistration(String filterName) {
        return applicationContext.getFilterRegistration(filterName);
    }

    @Override
    public String getContextPath() {
        return applicationContext.getContextPath();
    }

    @Override
    public String getServletContextName() {
        return applicationContext.getServletContextName();
    }

    @Override
    public <T extends Filter> T createFilter(Class<T> clazz) throws ServletException {
        return applicationContext.createFilter(clazz);
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, Filter filter) {
        return applicationContext.addFilter(filterName, filter);
    }

    @Override
    public String getVirtualServerName() {
        return applicationContext.getVirtualServerName();
    }

    @Override
    public InputStream getResourceAsStream(String path) {
        return applicationContext.getResourceAsStream(path);
    }

    @Override
    public Enumeration<Servlet> getServlets() {
        return applicationContext.getServlets();
    }

    @Override
    public void setAttribute(String name, Object value) {
        applicationContext.setAttribute(name, value);
    }

    @Override
    public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes) {
        applicationContext.setSessionTrackingModes(sessionTrackingModes);
    }

    @Override
    public int getMinorVersion() {
        return applicationContext.getMinorVersion();
    }

    @Override
    public SessionCookieConfig getSessionCookieConfig() {
        return applicationContext.getSessionCookieConfig();
    }

    public ServletContext getServletContext() {
        return applicationContext.getServletContext();
    }

    @Override
    public ServletRegistration.Dynamic addServlet(String servletName, Class<? extends Servlet> servletClass) {
        return applicationContext.addServlet(servletName, servletClass);
    }

    @Override
    public Enumeration<String> getServletNames() {
        return applicationContext.getServletNames();
    }

    @Override
    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
        return applicationContext.getEffectiveSessionTrackingModes();
    }

    @Override
    public <T extends EventListener> T createListener(Class<T> clazz) throws ServletException {
        return applicationContext.createListener(clazz);
    }

    @Override
    public JspConfigDescriptor getJspConfigDescriptor() {
        return applicationContext.getJspConfigDescriptor();
    }

    @Override
    public void addListener(String className) {
        applicationContext.addListener(className);
    }

    @Override
    public String getServerInfo() {
        return applicationContext.getServerInfo();
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return applicationContext.getAttributeNames();
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return applicationContext.getInitParameterNames();
    }

    @Override
    public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
        return applicationContext.getFilterRegistrations();
    }

    @Override
    public int getMajorVersion() {
        return applicationContext.getMajorVersion();
    }

    @Override
    public <T extends Servlet> T createServlet(Class<T> clazz) throws ServletException {
        return applicationContext.createServlet(clazz);
    }

    @Override
    public void removeAttribute(String name) {
        applicationContext.removeAttribute(name);
    }

    @Override
    public <T extends EventListener> void addListener(T t) {
        applicationContext.addListener(t);
    }

    @Override
    public void addListener(Class<? extends EventListener> listenerClass) {
        applicationContext.addListener(listenerClass);
    }

    @Override
    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
        return applicationContext.getDefaultSessionTrackingModes();
    }

    @Override
    public ServletContext getContext(String uripath) {
        return applicationContext.getContext(uripath);
    }

    @Override
    public void log(Exception exception, String msg) {
        applicationContext.log(exception, msg);
    }

    @Override
    public boolean setInitParameter(String name, String value) {
        return applicationContext.setInitParameter(name, value);
    }

    @Override
    public void declareRoles(String... roleNames) {
        applicationContext.declareRoles(roleNames);
    }

    @Override
    public void log(String msg) {
        applicationContext.log(msg);
    }

    @Override
    public String getInitParameter(String name) {
        return applicationContext.getInitParameter(name);
    }

    @Override
    public FilterRegistration.Dynamic addFilter(String filterName, String className) {
        return applicationContext.addFilter(filterName, className);
    }

    @Override
    public void log(String message, Throwable throwable) {
        applicationContext.log(message, throwable);
    }
}
