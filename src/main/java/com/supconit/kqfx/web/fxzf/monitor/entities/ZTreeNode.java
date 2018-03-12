package com.supconit.kqfx.web.fxzf.monitor.entities;

import java.util.List;

import org.springframework.util.CollectionUtils;

/**
 * ztree实体
 * @author caijianming@supcon.com
 * @create 2014-08-06
 * @since 1.0
 */
public class ZTreeNode {
	/**rootid**/
	public static final String root_id = "-1"; 
	/**节点ID**/
	private String id;
	/**父节点**/
	private String pId;
	/**节点名称**/
	private String name;
	/**虚拟字段**/
	private String t;
	/**节点是否打开**/
	private boolean open;
	/**节点是否最后一个节点**/
	private String ischildren;
	/**最后一个节点**/
	private String last;
	/**ZTreeNode父对象**/
	private ZTreeNode parent;
	/**ZTreeNode子列表**/
	private List<ZTreeNode> children;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getT() {
		return t;
	}
	public void setT(String t) {
		this.t = t;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
	public List<ZTreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<ZTreeNode> children) {
		this.children = children;
	}
	public ZTreeNode getParent() {
		return parent;
	}
	public void setParent(ZTreeNode parent) {
		this.parent = parent;
	}
	public String getIschildren() {
		return ischildren;
	}
	public void setIschildren(String ischildren) {
		this.ischildren = ischildren;
	}
	public String getLast() {
		if(CollectionUtils.isEmpty(children)&& null==last)
			return "O";
		return "M";
	}
	public void setLast(String last) {
		this.last = last;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	
	
}
