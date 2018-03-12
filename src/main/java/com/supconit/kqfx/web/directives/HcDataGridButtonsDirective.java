package com.supconit.kqfx.web.directives;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Set;

import jodd.util.StringUtil;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.support.RequestContext;

import com.supconit.honeycomb.business.authorization.entities.Operate;
import com.supconit.honeycomb.business.platform.directives.OperateButtonsDirective;

public class HcDataGridButtonsDirective extends OperateButtonsDirective{
	
	@Override
	public String getName() {
		return "hc_grid_buttons";
	}
	
	@Override
	public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
		Set<Operate> operates = getOperates(context, node);
		if (CollectionUtils.isEmpty(operates)) return true;
		String display = "list";
		if (node.jjtGetNumChildren() > 2) {
			SimpleNode displayNode = (SimpleNode) node.jjtGetChild(1);
			if (null != displayNode) {
				String value = (String) displayNode.value(context);
				if (StringUtil.isNotBlank(value)) display = value;
			}
		}
		RequestContext rc = (RequestContext) context.get("rc");
		StringBuilder builder = new StringBuilder();
		int counter = 0;
		for (Operate operate : operates) {
			if (("list".equals(display) && null != operate.getListButton() && operate.getListButton())
					|| ("column".equals(display) && null != operate.getColumnButton() && operate.getColumnButton())) {
				if (counter++ > 0) builder.append(",");
				builder.append("{name:\"").append(operate.getName()).append("\"");
				if (StringUtil.isNotBlank(operate.getIcon()) && !"column".equals(display)) {
					builder.append(",icon:\"").append(operate.getIcon()).append("\"");
				}
				String onclick = operate.getOnclick();
				if (null != rc) {
					onclick = onclick.replaceAll("\\$\\!?\\{rc.contextPath\\}", rc.getContextPath());
				}
				if (StringUtil.isNotBlank(onclick)) {
					if ("list".equals(display)) builder.append(",onclick:");
					else builder.append(",onclick:");
					builder.append(onclick);
				}
				builder.append("}");
			}
		}

		Node body = node.jjtGetChild(node.jjtGetNumChildren() - 1);
		if (null != body) {

			StringWriter sw = new StringWriter();
			body.render(context, sw);

			String more = sw.toString();
			if (StringUtil.isNotBlank(more)) {
				builder.append(",").append(more);
			}
		}

		writer.write(builder.toString());
		return true;
	}
}
