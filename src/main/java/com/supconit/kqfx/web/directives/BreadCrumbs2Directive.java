package com.supconit.kqfx.web.directives;

import com.supconit.honeycomb.base.context.SpringContextHolder;
import com.supconit.honeycomb.business.authorization.entities.Menu;
import com.supconit.honeycomb.business.authorization.services.MenuService;
import hc.safety.manager.SafetyManager;
import java.io.IOException;
import java.io.Writer;
import jodd.util.StringUtil;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.springframework.web.servlet.support.RequestContext;

public class BreadCrumbs2Directive extends Directive {
	 private SafetyManager safetyManager;
	  private MenuService menuService;

	  public MenuService getMenuService()
	  {
	    if (null == this.menuService) {
	      this.menuService = ((MenuService)SpringContextHolder.getBean(MenuService.class));
	    }
	    return this.menuService;
	  }

	  public SafetyManager getSafetyManager()
	  {
	    if (null == this.safetyManager) {
	      this.safetyManager = ((SafetyManager)SpringContextHolder.getBean(SafetyManager.class));
	    }
	    return this.safetyManager;
	  }

	  public String getName()
	  {
	    return "bread_crumbs2";
	  }

	  public int getType()
	  {
	    return 2;
	  }

	  public boolean render(InternalContextAdapter context, Writer writer, Node node)
	    throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException
	  {
	    SimpleNode codeNode = (SimpleNode)node.jjtGetChild(0);
	    if (null == codeNode) return true;
	    String code = (String)codeNode.value(context);
	    if (StringUtil.isBlank(code)) return true;
	    Menu menu = getMenuService().getByCode(code);
	    if (null == menu) return true;
	    RequestContext rc = (RequestContext)context.get("rc");
	    String contextPath = (null == rc) ? "" : rc.getContextPath();
	    StringBuilder builder = new StringBuilder();

	    Menu pMenu = menu;

	    while (null != pMenu.getPid()) {
	      pMenu = (Menu)getMenuService().getById(pMenu.getPid());
	      if (null == pMenu) break;
	      if ("ROOT_FUNCTION".equals(pMenu.getCode())) break; if ("ROOT_SYSTEM".equals(pMenu.getCode())) break;
	      StringBuilder menuBuilder = new StringBuilder();
	      menuBuilder.append(new StringBuilder().append("<a code=\"").append(pMenu.getCode()).append("\" href=\"javascript:void(0)\"").toString());
	      if (StringUtil.isNotBlank(pMenu.getLinkUrl())) {
	        menuBuilder.append(new StringBuilder().append(" onclick=\"loadMain('").append(contextPath).append(pMenu.getLinkUrl()).append("')\"").toString());
	      }
	      menuBuilder.append(">");
	      menuBuilder.append(pMenu.getName());

	      menuBuilder.append("</a>&nbsp;&nbsp;&nbsp;>&nbsp;&nbsp;");
	      builder.insert(0, menuBuilder.toString());
	    }
	    builder.insert(0, "<div class='breadthumb'><div class='ui-top-nav'><i class='nav-icon'><a  href=\"/\" ></a></i>");

	    builder.append(new StringBuilder()
	    .append("<a code=\"")
	    .append(menu.getCode())
	    .append("\" href=\"javascript:void(0)\"")
	    .append("onclick=\"loadMain('")
	    .append(contextPath)
	    .append(menu.getLinkUrl())
	    .append("')\"")
	    .append(">")
	    .append(menu.getName())
	    .append("</a>")
	    .append("</div><div class='line'></div></div>").toString()
	    );

	    writer.write(builder.toString());
	    return true;
	  }
}
