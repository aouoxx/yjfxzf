/**
 * 
 */
package com.supconit.kqfx.web.xtgl.services;

import hc.base.domains.Pageable;

import java.util.List;
import java.util.Map;

import com.supconit.kqfx.web.xtgl.entities.Xzqh;

/**
 * @author 
 * @create 2014-03-17 16:03:40 
 * @since 
 * 
 */
public interface XzqhService extends hc.orm.BasicOrmService<Xzqh, String> {

	/**
	 * 分页按条件查询。
	 * @param pager
	 * @param condition
	 * @return
	 */
	Pageable<Xzqh> find(Pageable<Xzqh> pager, Xzqh condition);
	
	List<Xzqh> findList(Xzqh condition);

	/**
	 * 根据当前登录用户返回当前用户所属行政区划以及下属行政区划
	 * 
	 * key 有两个值 xzqhdm ,xzqhs
	 * 
	 * @return
	 */
	Map<String, Object> findXzqhByCurrentUser();
	
	Xzqh getByXzqhdm(String xzqhdm);
	
	/**
	 * 根据地区名称模糊查询行政区划代码（比如，参数为‘青田县’ ，结果是 ‘%青田县%’的行政区划代码组成的字符串“1，2，3” ）
	 * @author hebingting
	 * @param condition
	 * @return
	 */
	String findXzqhDmsByDqmc(String dqmc);
}
