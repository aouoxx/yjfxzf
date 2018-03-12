package com.supconit.kqfx.web.xtgl.controllers;

import hc.mvc.controllers.AbstractXssFilterController;

import org.springframework.util.CollectionUtils;

import com.supconit.kqfx.web.xtgl.entities.Jgxx;

public abstract class AbstractJgxxTreeController extends
		AbstractXssFilterController {

    private static final String style = "white-space:nowrap;";
    
	public AbstractJgxxTreeController() {
		super();
	}

	protected void buildTree(Jgxx node, StringBuilder builder, String url, String onclick, String rel) {
		builder.append(
				"<li><a data-id=\"" + node.getId() + "\" onclick=\"" + onclick + "(" + node.getId() + ")\" href=\"" + url + node.getId() + "\" target=\"ajax\" rel=\"" + rel
						+ "\" style=\"" + style + "\">").append(node.getJgmc()).append("</a>");
		if (!CollectionUtils.isEmpty(node.getChildren())) {
			builder.append("<ul>");
			for (Jgxx d : node.getChildren()) {
				buildTree(d, builder, url, onclick, rel);
			}
			builder.append("</ul>");
		}
		builder.append("</li>");
	}
	
}
