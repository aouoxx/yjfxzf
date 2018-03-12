package com.supconit.kqfx.web.util;

import hc.business.dic.domains.FlatData;
import hc.business.dic.entities.Data;
import hc.business.dic.services.DataDictionaryService;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 *数据字典工具包
 * @author hebingting
 *
 */
public class DictionaryUtil {
	
	/**
	 *  根据字典 code获取该字典下字典项的映射关系
	 * @author hebingting
	 * @param code
	 * @param dataDictionaryService
	 * @return
	 */
	public static HashMap<String, String> dictionary(String code,DataDictionaryService dataDictionaryService){
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		FlatData flatData = (FlatData) dataDictionaryService.getByCode(code);
		if(null != flatData){
			for (Data data : flatData) {
				map.put(new String(data.getCode()), new String(data.getName()));
			}
		}
		return map;
	}
}
