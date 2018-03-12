package com.supconit.kqfx.web.xtgl.services;

import hc.base.domains.Pageable;
import hc.base.services.EntityBaiscOperator;

import java.util.List;
import java.util.Set;

import com.supconit.kqfx.web.xtgl.entities.ExtDepartment;

/**
 * 
 * 
 * @author shenxinfeng
 *
 */
public interface ExtDepartmentService extends EntityBaiscOperator<ExtDepartment> {
	
	String	EXTEND_MODEL_DEPARTMENT_CODE	= "PLAT_ORG_EX_DEPARTMENT";
	
	/**
	 * 根据ID获取部门信息。
	 * 
	 * @param id
	 * @return
	 */
	<X extends ExtDepartment> X getById(long id);
	

	/**
	 * 根据CODE获取部门信息。
	 * 
	 * @param code
	 * @return
	 */
	<X extends ExtDepartment> X getByCode(String code);
	
	<X extends ExtDepartment> Pageable<X> find(Pageable<X> pager, X condition);

	<X extends ExtDepartment> Pageable<X> find(Pageable<X> pager, X condition, boolean includeSub);
	
	/**
	 * 保存.
	 * 
	 * @param department
	 */
	<X extends ExtDepartment> void save(X department);
	
	/**
	 * 物理删除。<br/>
	 * 
	 * @param id
	 */
	@Deprecated
	void deleteById(Long id);
	
	/**
	 * @param set
	 */
	<X extends ExtDepartment> List<X> findByIds(Set<Long> set);
	
	/**
	 * 根据机构Id获取部门列表
	 * 
	 * @param jgbh
	 * @return
	 */
	<X extends ExtDepartment> List<X> findByJgbh(Long jgbh);
}
