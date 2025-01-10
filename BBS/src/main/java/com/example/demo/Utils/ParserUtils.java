package com.example.demo.Utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ParserUtils {
    public static Map<String, Object> parseStringToMap(String json){
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = null;

        try {
            map = mapper.readValue(json, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public Map<String, Object> parseXmlToMap(String xml) throws IOException, XMLStreamException, ParserConfigurationException, SAXException {
        Map<String, Object> map = new HashMap<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new ByteArrayInputStream(xml.getBytes()));

        Element root = document.getDocumentElement();
        parseElement(root, map);

        return map;
    }

    private static void parseElement(Element element, Map<String, Object> resultMap) {
        NodeList children = element.getChildNodes();

        if (children.getLength() == 1 && children.item(0).getNodeType() == Node.TEXT_NODE) {
            resultMap.put(element.getNodeName(), element.getTextContent());
        } else {
            Map<String, Object> childMap = new HashMap<>();
            for (int i = 0; i < children.getLength(); i++) {
                Node childNode = children.item(i);
                if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element childElement = (Element) childNode;
                    parseElement(childElement, childMap);
                }
            }
            resultMap.put(element.getNodeName(), childMap);
        }
    }
}
