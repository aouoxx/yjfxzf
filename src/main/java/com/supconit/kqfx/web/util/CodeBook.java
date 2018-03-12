/**
 * 自动生成代码
 * 版权所属  YYYY/MM/DD
 */

package com.supconit.kqfx.web.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 自动生成代码，请不要手动修改
 * 
 * @author zongkai
 * @version 1.0
 */
public class CodeBook {

    private List<String> tempList = null;

    private Map<String, Map<String, String>> codeBookMap = 
      new HashMap<String, Map<String,String>>();

    private Map<String, Map<String, String>> codeNameBookMap = 
      new HashMap<String, Map<String,String>>();

    /** 取得Code名称(中文) */
    public String getCodeName(String code, String value) {
        /* 是否为空 */
        if (code == null || value == null) {
            return "";
        } 

        return codeNameBookMap.get(code).get(value);
    }

    /** 取得大分类Code所有的子code对应名*/
    public List<String> getCodeNameList(String code){
        Map<String, String> codeMap = codeNameBookMap.get(code);
        tempList = new ArrayList<String>();
        Iterator<String> ite = codeMap.keySet().iterator();
        while(ite.hasNext()){
            tempList.add(getCodeName(code, ite.next()));
        }
        return tempList;
    }

    /** 取得Code国际化标签名称 */
    public String geti18nCodeName(String code, String value) {
        /* 是否为空 */
        if (code == null || value == null) {
            return "";
        } 

        StringBuilder sb = new StringBuilder();
        sb.append(code);
        sb.append("_");
        sb.append(codeBookMap.get(code).get(value));
        sb.append("_LABLE");
        return sb.toString();
    }

    /** 取得大分类Code所有的子code值 list*/
    public List<String> getCodeValueList(String code){
        return new ArrayList<String>(codeBookMap.get(code).keySet());
    }

    /** 取得大分类Code所有的子code对应的国际化标签名*/
    public List<String> geti18nCodeNameList(String code){
        Map<String, String> codeMap = codeBookMap.get(code);
        tempList = new ArrayList<String>();
        Iterator<String> ite = codeMap.keySet().iterator();
        while(ite.hasNext()){
            tempList.add(getCodeName(code, ite.next()));
        }
        return tempList;
    }


    /**
     * 项目自定义码<BR>
     */
    private CodeBook() {
    }
    private static CodeBook codeBook = null;

    public static synchronized CodeBook getInstance(){
        if (codeBook == null){
            codeBook = new CodeBook();
        }
        return codeBook;
    }    
}

