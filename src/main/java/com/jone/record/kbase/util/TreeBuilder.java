package com.jone.record.kbase.util;
import com.alibaba.fastjson.JSONObject;
import com.jone.record.kbase.entity.Catalog;
import java.util.ArrayList;
import java.util.List;

public class TreeBuilder {

    List<Catalog> nodes = new ArrayList<Catalog> ();

    public TreeBuilder(List<Catalog> nodes) {
        super ();
        this.nodes = nodes;
    }

    /**
     *  构建JSON树形结构
     *  @return
     */
    public String buildJSONTree() {
        List<Catalog> nodeTree = buildTree ();
        JSONObject jsonObject = (JSONObject) JSONObject.toJSON(nodeTree);
        return jsonObject.toString ();
    }

    /**
     * 构建树形结构
     *  @return        
     */
    public List<Catalog> buildTree() {
        List<Catalog> treeNodes = new ArrayList<Catalog> ();
        List<Catalog> rootNodes = getRootNodes ();
        for (Catalog rootNode : rootNodes) {
            buildChildNodes (rootNode);
            treeNodes.add (rootNode);
        }
        return treeNodes;
    }

    /**
     * 递归子节点
     *
     * @param node  
     */

    public void buildChildNodes(Catalog node) {
        List<Catalog> children = getChildNodes (node);
        if (!children.isEmpty ()) {
            for (Catalog child : children) {
                buildChildNodes (child);
            }
            node.setChildren (children);
        }
    }

    /**
     *  获取父节点下所有的子节点
     *  @param nodes
     *  @param pnode
     *  @return
     */
    public List<Catalog> getChildNodes(Catalog pNode) {
        List<Catalog> childNodes = new ArrayList<Catalog> ();
        for (Catalog node : nodes) {
            if (pNode.getGuid ().equals (node.getParentGuid ())) {
                childNodes.add (node);
            }
        }
        return childNodes;
    }

    /**
     * 判断是否为根节点
     *
     * @param nodes
     * @param inNode  
     * @return         
     */
    public boolean rootNode(Catalog node) {
        boolean isRootNode = true;
        for (Catalog catalogNode : nodes) {
            if (node.getParentGuid ().equals (catalogNode.getGuid ())) {
                isRootNode = false;
                break;
            }
        }
        return isRootNode;
    }

    /**
     * 获取集合中所有的根节点
     *
     * @param nodes
     * @return          
     */
    public List<Catalog> getRootNodes() {
        List<Catalog> rootNodes = new ArrayList<Catalog> ();
        for (Catalog catalogNode : nodes) {
            if (rootNode (catalogNode)) {
                rootNodes.add (catalogNode);
            }
        }
        return rootNodes;
    }
}
