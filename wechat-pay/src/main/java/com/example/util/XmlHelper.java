package com.example.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @Author: Killah
 * @Version: 1.0
 * @Description: XML 工具类
 */
public class XmlHelper {
    private final XPath path;
    private final Document doc;

    private XmlHelper(InputSource inputSource) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = getDocumentBuilderFactory();
        dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
        dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        dbf.setXIncludeAware(false);
        dbf.setExpandEntityReferences(false);
        DocumentBuilder db = dbf.newDocumentBuilder();
        this.doc = db.parse(inputSource);
        this.path = getXpathFactory().newXPath();
    }

    private static XmlHelper create(InputSource inputSource) {
        try {
            return new XmlHelper(inputSource);
        } catch (SAXException | IOException | ParserConfigurationException var2) {
            throw new RuntimeException(var2);
        }
    }

    public static XmlHelper of(InputStream is) {
        InputSource inputSource = new InputSource(is);
        return create(inputSource);
    }

    public static XmlHelper of(File file) {
        InputSource inputSource = new InputSource(file.toURI().toASCIIString());
        return create(inputSource);
    }

    public static XmlHelper of(String xmlStr) {
        StringReader sr = new StringReader(xmlStr.trim());
        InputSource inputSource = new InputSource(sr);
        XmlHelper xmlHelper = create(inputSource);
        sr.close();
        return xmlHelper;
    }

    private Object evalXpath(String expression, Object item, QName returnType) {
        item = null == item ? this.doc : item;

        try {
            return this.path.evaluate(expression, item, returnType);
        } catch (XPathExpressionException var5) {
            throw new RuntimeException(var5);
        }
    }

    public String getString(String expression) {
        return (String)this.evalXpath(expression, (Object)null, XPathConstants.STRING);
    }

    public Boolean getBoolean(String expression) {
        return (Boolean)this.evalXpath(expression, (Object)null, XPathConstants.BOOLEAN);
    }

    public Number getNumber(String expression) {
        return (Number)this.evalXpath(expression, (Object)null, XPathConstants.NUMBER);
    }

    public Node getNode(String expression) {
        return (Node)this.evalXpath(expression, (Object)null, XPathConstants.NODE);
    }

    public NodeList getNodeList(String expression) {
        return (NodeList)this.evalXpath(expression, (Object)null, XPathConstants.NODESET);
    }

    public String getString(Object node, String expression) {
        return (String)this.evalXpath(expression, node, XPathConstants.STRING);
    }

    public Boolean getBoolean(Object node, String expression) {
        return (Boolean)this.evalXpath(expression, node, XPathConstants.BOOLEAN);
    }

    public Number getNumber(Object node, String expression) {
        return (Number)this.evalXpath(expression, node, XPathConstants.NUMBER);
    }

    public Node getNode(Object node, String expression) {
        return (Node)this.evalXpath(expression, node, XPathConstants.NODE);
    }

    public NodeList getNodeList(Object node, String expression) {
        return (NodeList)this.evalXpath(expression, node, XPathConstants.NODESET);
    }

    public Map<String, String> toMap() {
        Element root = this.doc.getDocumentElement();
        NodeList list = root.getChildNodes();
        Map<String, String> params = new HashMap(list.getLength());

        for(int i = 0; i < list.getLength(); ++i) {
            Node node = list.item(i);
            params.put(node.getNodeName(), node.getTextContent());
        }

        params.remove("#text");
        return params;
    }

    private static DocumentBuilderFactory getDocumentBuilderFactory() {
        return XmlHelper.XmlHelperHolder.documentBuilderFactory;
    }

    private static XPathFactory getXpathFactory() {
        return XmlHelper.XmlHelperHolder.xPathFactory;
    }

    private static class XmlHelperHolder {
        private static DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        private static XPathFactory xPathFactory = XPathFactory.newInstance();

        private XmlHelperHolder() {
        }
    }
}