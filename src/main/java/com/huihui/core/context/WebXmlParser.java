package com.huihui.core.context;

import com.huihui.core.context.Context;
import com.huihui.core.Wrapper;
import com.huihui.exceptions.ExceptionFactory;
import com.huihui.simple.ServletWrapper;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by hadoop on 2015/7/31 0031.
 */
public class WebXmlParser {
    Context context;
    private Queue<ServletMapping> servletMappingQueue = new LinkedList<>();
    private Map<String, Wrapper> servletCache = new HashMap<>();

    public WebXmlParser(Context context) {
        this.context = context;
    }

    public void parser() {
        String path = context.getDirFile().getSource().getAbsolutePath() + File.separator + "WEB-INF" + File.separator + "web.xml";
        try {
            InputStream resource = new FileInputStream(new File(path));
            SAXReader reader = new SAXReader();
            Document document = reader.read(resource);
            Element root = document.getRootElement();
            parsingRoot(root);
        } catch (DocumentException e) {
            ExceptionFactory.wrapException("parse WEB-XML error path:" + path, e);
        } catch (FileNotFoundException e) {
            ExceptionFactory.wrapException("connot found WEB-XML path:" + path, e);
        }
    }

    private void parsingRoot(Element root) {
        Iterator<Element> it = root.elementIterator();
        while (it.hasNext()) {
            Element child = it.next();
            parserRootChild(child);
        }
        mappingUrlAndWrapper();
    }

    private void parserRootChild(Element element) {
        String name = element.getName();
        if (name.equals("servlet")) {
            parserServlet(element);
        } else if (name.equals("servlet-mapping")) {
            parserServlet_mapping(element);
        }
    }

    private void mappingUrlAndWrapper() {
        while (!servletMappingQueue.isEmpty()) {
            ServletMapping mapping = servletMappingQueue.poll();
            Wrapper wrapper = servletCache.get(mapping.name);
            context.addServletMapping(mapping.url, wrapper);
        }
    }

    private void parserServlet(Element element) {
        Iterator<Element> it = element.elementIterator();
        ServletWrapper wrapper = new ServletWrapper();
        String servletName = null;
        while (it.hasNext()) {
            Element child = it.next();
            String childName  = child.getName();
            if (childName.equals("servlet-name")) {
                servletName = child.getTextTrim();
                wrapper.setName(servletName);
            } else if (childName.equals("servlet-class")) {
                wrapper.setServletClassName(child.getTextTrim());
            }
        }
        servletCache.put(servletName, wrapper);
    }

    /**
     * 解析ServletMapping,队列缓存
     *
     * @param element
     * @return
     */
    private void parserServlet_mapping(Element element) {
        ServletMapping servletMapping = new ServletMapping();
        Iterator<Element> it = element.elementIterator();
        while (it.hasNext()) {
            Element child = it.next();
            String childNmae = child.getName();
            if (childNmae.equals("servlet-name")) {
                servletMapping.name = child.getTextTrim();
            } else if (childNmae.equals("url-pattern")) {
                servletMapping.url = child.getTextTrim();
            }
        }
        servletMappingQueue.add(servletMapping);

    }

    class ServletMapping {
        String url;
        String name;
    }
}
