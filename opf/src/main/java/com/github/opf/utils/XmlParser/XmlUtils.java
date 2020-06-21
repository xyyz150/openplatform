package com.github.opf.utils.XmlParser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by xyyz150
 */
public class XmlUtils {
    private static final Log log = LogFactory.getLog(XmlUtils.class);
    private static final String XMLNS_XSI = "xmlns:xsi";
    private static final String XSI_SCHEMA_LOCATION = "xsi:schemaLocation";
    private static final String LOGIC_YES = "yes";
    private static final String DEFAULT_CHARSET = "UTF-8";

    public XmlUtils() {
    }

    public static Document newDocument() throws Exception {
        Document doc = null;

        try {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            return doc;
        } catch (ParserConfigurationException var2) {
            throw new Exception(var2);
        }
    }

    public static Document getDocument(File file) throws Exception {
        InputStream in = getInputStream(file);
        return getDocument(new InputSource(in), (InputStream)null);
    }

    public static Document getDocument(InputSource xml, InputStream xsd) throws Exception {
        Document doc = null;

        try {
            DocumentBuilderFactory e = DocumentBuilderFactory.newInstance();
            if(xsd != null) {
                e.setNamespaceAware(false);
            }

            DocumentBuilder builder = e.newDocumentBuilder();
            doc = builder.parse(xml);
            if(xsd != null) {
                validateXml((Node)doc, xsd);
            }
        } catch (ParserConfigurationException var11) {
            throw new Exception(var11);
        } catch (SAXException var12) {
            throw new Exception("XML_PARSE_ERROR", var12);
        } catch (IOException var13) {
            throw new Exception("XML_READ_ERROR", var13);
        } finally {
            closeStream(xml.getByteStream());
        }

        return doc;
    }

    public static Element createRootElement(String tagName) throws Exception {
        Document doc = newDocument();
        Element root = doc.createElement(tagName);
        doc.appendChild(root);
        return root;
    }

    public static Element getRootElementFromStream(InputStream xml) throws Exception {
        return getDocument(new InputSource(xml), (InputStream)null).getDocumentElement();
    }

    public static Element getRootElementFromStream(InputStream xml, InputStream xsd) throws Exception {
        return getDocument(new InputSource(xml), xsd).getDocumentElement();
    }

    public static Element getRootElementFromFile(File xml) throws Exception {
        return getDocument(xml).getDocumentElement();
    }

    public static Element getRootElementFromString(String payload) throws Exception {
        if(payload != null && payload.length() >= 1) {
            StringReader sr = new StringReader(escapeXml(payload));
            InputSource source = new InputSource(sr);
            return getDocument(source, (InputStream)null).getDocumentElement();
        } else {
            throw new Exception("XML_PAYLOAD_EMPTY");
        }
    }

    public static List<Element> getElements(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        ArrayList elements = new ArrayList();

        for(int i = 0; i < nodes.getLength(); ++i) {
            Node node = nodes.item(i);
            if(node instanceof Element) {
                elements.add((Element)node);
            }
        }

        return elements;
    }

    public static Element getElement(Element parent, String tagName) {
        List children = getElements(parent, tagName);
        return children.isEmpty()?null:(Element)children.get(0);
    }

    public static List<Element> getChildElements(Element parent, String tagName) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        ArrayList elements = new ArrayList();

        for(int i = 0; i < nodes.getLength(); ++i) {
            Node node = nodes.item(i);
            if(node instanceof Element && node.getParentNode() == parent) {
                elements.add((Element)node);
            }
        }

        return elements;
    }

    public static List<Element> getChildElements(Element parent) {
        NodeList nodes = parent.getChildNodes();
        ArrayList elements = new ArrayList();

        for(int i = 0; i < nodes.getLength(); ++i) {
            Node node = nodes.item(i);
            if(node instanceof Element && node.getParentNode() == parent) {
                elements.add((Element)node);
            }
        }

        return elements;
    }

    public static Element getChildElement(Element parent, String tagName) {
        List children = getChildElements(parent, tagName);
        return children.isEmpty()?null:(Element)children.get(0);
    }

    public static String getElementValue(Element parent, String tagName) {
        Element element = getElement(parent, tagName);
        return element != null?element.getTextContent():null;
    }

    public static String getChildElementValue(Element parent, String tagName) {
        Element element = getChildElement(parent, tagName);
        return element != null?element.getTextContent():null;
    }

    public static String getElementValue(Element element) {
        if(element != null) {
            NodeList nodes = element.getChildNodes();
            if(nodes != null && nodes.getLength() > 0) {
                for(int i = 0; i < nodes.getLength(); ++i) {
                    Node node = nodes.item(i);
                    if(node instanceof Text) {
                        return ((Text)node).getData();
                    }
                }
            }
        }

        return null;
    }

    public static String getAttributeValue(Element current, String attrName) {
        return current.hasAttribute(attrName)?current.getAttribute(attrName):null;
    }

    public static Element appendElement(Element parent, String tagName) {
        Element child = parent.getOwnerDocument().createElement(tagName);
        parent.appendChild(child);
        return child;
    }

    public static Element appendElement(Element parent, String tagName, String value) {
        Element child = appendElement(parent, (String)tagName);
        child.setTextContent(value);
        return child;
    }

    public static void appendElement(Element parent, Element child) {
        Node tmp = parent.getOwnerDocument().importNode(child, true);
        parent.appendChild(tmp);
    }

    public static Element appendCDATAElement(Element parent, String tagName, String value) {
        Element child = appendElement(parent, (String)tagName);
        if(value == null) {
            value = "";
        }

        CDATASection cdata = child.getOwnerDocument().createCDATASection(value);
        child.appendChild(cdata);
        return child;
    }

    public static String childNodeToString(Node node) throws Exception {
        String payload = null;

        try {
            Transformer e = TransformerFactory.newInstance().newTransformer();
            Properties props = e.getOutputProperties();
            props.setProperty("omit-xml-declaration", "yes");
            props.setProperty("encoding", "UTF-8");
            e.setOutputProperties(props);
            StringWriter writer = new StringWriter();
            e.transform(new DOMSource(node), new StreamResult(writer));
            payload = escapeXml(writer.toString());
            return payload;
        } catch (TransformerException var5) {
            throw new Exception("XML_TRANSFORM_ERROR", var5);
        }
    }

    public static String nodeToString(Node node) throws Exception {
        String payload = null;

        try {
            Transformer e = TransformerFactory.newInstance().newTransformer();
            Properties props = e.getOutputProperties();
            props.setProperty("encoding", "UTF-8");
            props.setProperty("indent", "yes");
            e.setOutputProperties(props);
            StringWriter writer = new StringWriter();
            e.transform(new DOMSource(node), new StreamResult(writer));
            payload = escapeXml(writer.toString());
            return payload;
        } catch (TransformerException var5) {
            throw new Exception("XML_TRANSFORM_ERROR", var5);
        }
    }

    public static String escapeXml(String payload) {
        StringBuilder out = new StringBuilder();

        for(int i = 0; i < payload.length(); ++i) {
            char c = payload.charAt(i);
            if(c == 9 || c == 10 || c == 13 || c >= 32 && c <= '\ud7ff' || c >= '\ue000' && c <= '�' || c >= 65536 && c <= 1114111) {
                out.append(c);
            }
        }

        return out.toString();
    }

    public static String xmlToString(File file) throws Exception {
        Element root = getRootElementFromFile(file);
        return nodeToString(root);
    }

    public static String xmlToString(InputStream in) throws Exception {
        Element root = getRootElementFromStream(in);
        return nodeToString(root);
    }

    public static void saveToXml(Node doc, File file) throws Exception {
        saveToXml(doc, file, "UTF-8");
    }

    public static void saveToXml(Node doc, File file, String charset) throws Exception {
        OutputStreamWriter writer = null;

        try {
            Transformer e = TransformerFactory.newInstance().newTransformer();
            Properties props = e.getOutputProperties();
            props.setProperty("method", "xml");
            props.setProperty("indent", "yes");
            e.setOutputProperties(props);
            DOMSource dom = new DOMSource(doc);
            writer = new OutputStreamWriter(getOutputStream(file), charset);
            StreamResult result = new StreamResult(writer);
            e.transform(dom, result);
        } catch (Exception var12) {
            throw new Exception("XML_WRITE_FILE_ERROR", var12);
        } finally {
            closeStream(writer);
        }

    }

    public static void validateXml(InputStream xml, InputStream xsd) throws Exception {
        try {
            DocumentBuilderFactory e = DocumentBuilderFactory.newInstance();
            e.setNamespaceAware(true);
            Document doc = e.newDocumentBuilder().parse(xml);
            validateXml((Node)doc, xsd);
        } catch (SAXException var9) {
            throw new Exception("XML_VALIDATE_ERROR", var9);
        } catch (Exception var10) {
            throw new Exception("XML_READ_ERROR", var10);
        } finally {
            closeStream(xml);
            closeStream(xsd);
        }

    }

    public static void validateXml(Node root, InputStream xsd) throws Exception {
        try {
            StreamSource e = new StreamSource(xsd);
            Schema schema = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema").newSchema(e);
            Validator validator = schema.newValidator();
            validator.validate(new DOMSource(root));
        } catch (SAXException var10) {
            if(log.isErrorEnabled()) {
                log.error("验证XML文件出错：\n" + nodeToString(root));
            }

            throw new Exception("XML_VALIDATE_ERROR", var10);
        } catch (Exception var11) {
            throw new Exception("XML_READ_ERROR", var11);
        } finally {
            closeStream(xsd);
        }

    }

    public static String xmlToHtml(String payload, File xsltFile) throws Exception {
        String result = null;

        try {
            StreamSource e = new StreamSource(xsltFile);
            Transformer transformer = TransformerFactory.newInstance().newTransformer(e);
            Properties props = transformer.getOutputProperties();
            props.setProperty("omit-xml-declaration", "yes");
            transformer.setOutputProperties(props);
            StreamSource source = new StreamSource(new StringReader(payload));
            StreamResult sr = new StreamResult(new StringWriter());
            transformer.transform(source, sr);
            result = sr.getWriter().toString();
            return result;
        } catch (TransformerException var8) {
            throw new Exception("XML_TRANSFORM_ERROR", var8);
        }
    }

    public static void setNamespace(Element element, String namespace, String schemaLocation) {
        element.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", namespace);
        element.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        element.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:schemaLocation", schemaLocation);
    }

    public static String encodeXml(String payload) throws Exception {
        Element root = createRootElement("xml");
        root.setTextContent(payload);
        return childNodeToString(root.getFirstChild());
    }

    private static void closeStream(Closeable stream) {
        if(stream != null) {
            try {
                stream.close();
            } catch (IOException var2) {
                ;
            }
        }

    }

    private static InputStream getInputStream(File file) throws Exception {
        FileInputStream in = null;

        try {
            in = new FileInputStream(file);
            return in;
        } catch (FileNotFoundException var3) {
            throw new Exception("XML_FILE_NOT_FOUND", var3);
        }
    }

    private static OutputStream getOutputStream(File file) throws Exception {
        FileOutputStream in = null;

        try {
            in = new FileOutputStream(file);
            return in;
        } catch (FileNotFoundException var3) {
            throw new Exception("XML_FILE_NOT_FOUND", var3);
        }
    }
}
