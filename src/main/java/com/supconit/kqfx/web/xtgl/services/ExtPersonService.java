package com.supconit.kqfx.web.xtgl.services;

import hc.base.domains.Pageable;
import hc.base.domains.Pagination;
import hc.base.services.EntityBaiscOperator;

import java.util.List;
import java.util.Set;

import com.supconit.kqfx.web.xtgl.entities.ExtPerson;

/**
 * 人员管理Service接口
 * 
 * @author shenxinfeng
 *
 */
public interface ExtPersonService extends EntityBaiscOperator<ExtPerson> {

	String	EXTEND_MODEL_PERSON_CODE					= "PLAT_ORG_EX_PERSON";
	String	EXTEND_MODEL_PERSON_DEPARTMENT_INFO_CODE	= "PLAT_ORG_EX_PERSON_DEPARTMENT";
	String	EXTEND_MODEL_PERSON_POSITION_INFO_CODE		= "PLAT_ORG_EX_PERSON_POSITION";
	
	/**
	 * 根据ID获取人员信息。
	 * 
	 * @param id
	 * @return
	 */
	<X extends ExtPerson> X getById(long id);
	
	/**
	 * 根据CODE获取人员信息。
	 * 
	 * @param code
	 * @return
	 */
	<X extends ExtPerson> X getByCode(String code);
	
	
	/**
	 * 根据一批ID获取。
	 * 
	 * @param ids
	 * @return
	 */
	<X extends ExtPerson> List<X> findByIds(Set<Long> ids);
	
	/**
	 * 有条件的分页查询。
	 * 
	 * @param pager
	 * @param condition
	 * @return
	 */
	<X extends ExtPerson> Pageable<X> find(Pageable<X> pager, X condition);
	
	/**
	 * 保存人员信息
	 * 
	 * @param person
	 */
	<X extends ExtPerson> void save(X person);
	
	/**
	 * 根据ID批量删除。
	 * 
	 * @param id
	 */
	void delete(Long[] ids);
	
	/**
	 * 给传入的分页人员列表设置机构信息
	 * 
	 * @param pager
	 * @return
	 */
	<X extends ExtPerson> Pagination<X> personSetJgxx(Pagination<X> pager);
	
	
}
