package com.supconit.kqfx.web.directives;

import hc.business.dic.domains.FlatData;
import hc.business.dic.entities.Data;
import hc.business.dic.services.DataDictionaryService;

import java.io.IOException;
import java.io.Writer;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.supconit.honeycomb.base.context.SpringContextHolder;

public class HcDicRadioDirective extends Directive{
	private static final transient Logger logger = LoggerFactory
			.getLogger(HcDicRadioDirective.class);
	
	private DataDictionaryService dataDictionaryService;

	private DataDictionaryService getDataDictionaryService() {
		if (null == this.dataDictionaryService) {
			this.dataDictionaryService = ((DataDictionaryService) SpringContextHolder
					.getApplicationContext().getBean("DataDictionaryService",
							DataDictionaryService.class));
		}

		return this.dataDictionaryService;
	}
	
	@Override
	public String getName() {
		return "hc_dic_radio";
	}

	@Override
	public int getType() {
		return 2;
	}

	@Override
	public boolean render(InternalContextAdapter context, Writer writer, Node node)
			throws IOException, ResourceNotFoundException, ParseErrorException,
			MethodInvocationException {
		String dicCode = (String) node.jjtGetChild(0).value(context);
		String formName = (String) node.jjtGetChild(1).value(context);
		String value = (String) node.jjtGetChild(2).value(context);
		String styleClass = (String) node.jjtGetChild(3).value(context);
		
		String isChecked = null;
		if (node.jjtGetNumChildren() > 4) {
			isChecked = (String) node.jjtGetChild(4).value(context);
		}
		
		
		FlatData datas = (FlatData) getDataDictionaryService().getByCode(
				dicCode);
		StringBuilder builder = new StringBuilder();
		int i = 0;
		for (Data data : datas) {
			builder.append("<input type='radio' name='"+formName+"' value='"+data.getCode()+"'");
			if("".equals(value) || null == value){
				if("true".equals(isChecked) && i == 0){
					builder.append(" checked='checked'");
				}
			}else{
				if(value.equals(data.getCode())){
					builder.append(" checked='checked'");
				}
			}
			if(styleClass != null){
				builder.append(" class='"+styleClass+"'");
			}
			builder.append(">");
			builder.append("<label>"+data.getName()+"</label>");
			i++;
		}
		logger.debug(builder.toString());
		writer.write(builder.toString());
		return true;
	}

}
