/**
 * 
 */
package com.supconit.kqfx.web.xtgl.services;

import hc.base.domains.Pageable;

import com.supconit.kqfx.web.xtgl.entities.SystemLog;

/**
 * @author 
 * @create 2014-04-09 15:46:42 
 * @since 
 * 
 */
public interface SystemLogService extends hc.orm.BasicOrmService<SystemLog, String> {

	/**
	 * 分页按条件查询。
	 * @param pager
	 * @param condition
	 * @return
	 */
	Pageable<SystemLog> find(Pageable<SystemLog> pager, SystemLog condition);
	
	/**
	 * 记录操作日志
	 * 
	 * @param moduleCode	模块代码
	 * @param operateType	操作类型
	 * @param operateContent	操作内容
	 * @param operateIp		访问Ip
	 */
	void log(String moduleCode, String operateType, String operateContent, String operateIp);

}
