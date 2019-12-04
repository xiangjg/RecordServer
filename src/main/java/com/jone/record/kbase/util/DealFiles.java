package com.jone.record.kbase.util;

import com.alibaba.fastjson.JSONObject;
import com.jone.record.kbase.tool.EResourceType;
import org.apache.commons.collections.map.LinkedMap;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.boot.CommandLineRunner;

import java.util.LinkedList;
import java.util.List;

public class DealFiles {

    private LoadConfig tool = null;

    private String strServerFilePath = "";
    private int type = 0;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public DealFiles() {
        super();
        tool = new LoadConfig();
    }

    public String getStrServerFilePath() {
        return strServerFilePath;
    }

    public void setStrServerFilePath(String strServerFilePath) {
        this.strServerFilePath = strServerFilePath;
    }

    public JSONObject AnalysisXmlFileInfo(String strXmlInfo) {
        List<String> list = new LinkedList<String>();
        Document doc = null;
        Element root = null;
        JSONObject jsonObject = new JSONObject(new LinkedMap());
        try {
            doc = DocumentHelper.parseText(strXmlInfo);
            root = doc.getRootElement();// 指向根节点  <root>
            getNodes(root, list);

            String strContent = "";
            String strTitle = "";
            for (int i = 0; i < list.size(); i++) {
                if (0 == i) {
                    strTitle = list.get(i);
                } else {
                    String strList = list.get(i);
                    strContent += strList;
                }
            }
            jsonObject.put("title", strTitle);
            jsonObject.put("content", strContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * 从指定节点开始,递归遍历所有子节点
     */
    public void getNodes(Element node, List<String> list) {
        //当前节点的名称、文本内容和属性
        String strContent = null;
        String nodeName = node.getName();
        String nodeText = node.getTextTrim();
        if (nodeName.equals("title")) {
            Element parentNode = node.getParent();
            if (parentNode.getName().equals("section") || parentNode.getName().equals("article")
                    || parentNode.getName().equals("part") || parentNode.getName().equals("chapter")) {
                list.add(nodeText);
            } else {
                Attribute attrImage = parentNode.attribute("fileref");
                String strFileName = EResourceType.GetFieldsByCode(type);
                String strImageFile = String.format("%s/%s/%s/%s", Common.GetFilePath(), strFileName, strServerFilePath, attrImage.getValue());
                strContent = String.format("<p><span>%s</span><img src=\"%s\"/></p>", nodeText, strImageFile);
                list.add(strContent);
            }
        } else if (nodeName.equals("para")) {
            if (!nodeText.isEmpty()) {
                strContent = String.format("<p>%s</p>", nodeText);
                list.add(strContent);
            }
        }

        // 递归遍历当前节点所有的子节点
        List<Element> listElement = node.elements();         // 所有一级子节点的list
        for (Element element : listElement) {                      // 遍历所有一级子节点
            getNodes(element, list);                    // 递归
        }
    }
}
