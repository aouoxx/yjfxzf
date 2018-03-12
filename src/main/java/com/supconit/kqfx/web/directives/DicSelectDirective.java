package com.supconit.kqfx.web.directives;
import com.supconit.honeycomb.base.context.SpringContextHolder;
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

/**
 * 兼容默认选中
 * @author caijianming
 *
 */
public class DicSelectDirective extends Directive
{
  private DataDictionaryService dataDictionaryService;

  private DataDictionaryService getDataDictionaryService()
  {
    if (null == this.dataDictionaryService) {
      this.dataDictionaryService = ((DataDictionaryService)SpringContextHolder.getApplicationContext().getBean("DataDictionaryService", DataDictionaryService.class));
    }

    return this.dataDictionaryService;
  }

  public String getName()
  {
    return "dic_select2";
  }

  public int getType()
  {
    return 2;
  }

  public boolean render(InternalContextAdapter paramInternalContextAdapter, Writer paramWriter, Node paramNode)
    throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException
  {
    String dicCode = paramNode.jjtGetChild(0).value(paramInternalContextAdapter).toString();
    String name = paramNode.jjtGetChild(1).value(paramInternalContextAdapter).toString();
    Object vNode = paramNode.jjtGetChild(2).value(paramInternalContextAdapter);
    String value = "";
    if (null != vNode)
      value = vNode.toString();
    String styleClass = null;
    if (paramNode.jjtGetNumChildren() > 3) {
      styleClass = paramNode.jjtGetChild(3).value(paramInternalContextAdapter).toString();
    }
    String id = null;
    if (paramNode.jjtGetNumChildren() > 4) {
      id = paramNode.jjtGetChild(4).value(paramInternalContextAdapter).toString();
    }
    String blank = null;
    if (paramNode.jjtGetNumChildren() > 5) {
      blank = paramNode.jjtGetChild(5).value(paramInternalContextAdapter).toString();
    }
    FlatData datas = (FlatData)getDataDictionaryService().getByCode(dicCode);
    StringBuffer wStringBuffer = new StringBuffer(500);
    wStringBuffer.append("<select ");
    if (id != null) {
      wStringBuffer.append(" id=\"" + id + "\" ");
    }
    if (styleClass != null) {
      wStringBuffer.append(" class=\"" + styleClass + "\" ");
    }
    wStringBuffer.append(" name=\"" + name + "\" value=\"" + value + "\">");
    if ((blank == null) || (!blank.equals("false"))) {
      wStringBuffer.append(" <option value=\"\"></option>");
    }
    for (Data data : datas) {
      wStringBuffer.append("<option value=\"" + data.getCode() + "\"");
      if (value.equals(data.getCode())) {
        wStringBuffer.append(" selected=\"selected\" ");
      }
      wStringBuffer.append(">" + data.getName() + "</option>");
    }
    wStringBuffer.append("</select>");
    paramWriter.write(wStringBuffer.toString());
    return true;
  }
}