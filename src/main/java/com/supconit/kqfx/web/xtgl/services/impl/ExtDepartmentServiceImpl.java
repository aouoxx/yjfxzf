package com.supconit.kqfx.web.xtgl.services.impl;

import hc.base.domains.Pageable;
import hc.base.domains.Pagination;
import hc.jdbc.JdbcProcessor;
import hc.modelextend.ExtendedModelProvider;
import hc.orm.search.Term;
import hc.orm.triggers.AbstractBusinessTriggerSupport;
import hc.orm.triggers.BusinessTriggerExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.supconit.honeycomb.business.organization.entities.Department;
import com.supconit.kqfx.web.xtgl.entities.ExtDepartment;
import com.supconit.kqfx.web.xtgl.services.ExtDepartmentService;

@Service
public class ExtDepartmentServiceImpl extends AbstractBusinessTriggerSupport<ExtDepartment> implements ExtDepartmentService {

	private static final String		SELECT_PRE_SQL				= "SELECT * FROM " + ExtDepartment.TABLE_NAME + " D ";
//	private static final String		CACHE_DEPARTMENT_BY_ID		= "DEPARTMENT_BY_ID";
//	private static final String		CACHE_DEPARTMENT_ID_BY_CODE	= "DEPARTMENT_ID_BY_CODE";
	
	@Autowired
	private ExtendedModelProvider	extendedModelProvider;
	@Autowired(required = false)
	private BusinessTriggerExecutor	businessTriggerExecutor;
	@Autowired
	private JdbcProcessor			jdbcProcessor;

	private String					defaultOrderBy				= "ORDER BY D.ID DESC";

//	@Autowired
//	private Cache					cache;
	
	/**
	 * @param defaultOrderBy
	 *            the defaultOrderBy to set
	 */
	public void setDefaultOrderBy(String defaultOrderBy) {
		this.defaultOrderBy = defaultOrderBy;
	}

	
	@Override
	@Transactional(readOnly = true)
	public <X extends ExtDepartment> X getById(long id) {
//		String key = String.valueOf(id);
//		X department = this.cache.get(CACHE_DEPARTMENT_BY_ID, key);
//		if (null == department) {
//			department = this.extendedModelProvider.get(EXTEND_MODEL_DEPARTMENT_CODE, ExtDepartment.class, id);
//			if (null != department) this.cache.put(CACHE_DEPARTMENT_BY_ID, key, department);
//		}
		X department = this.extendedModelProvider.get(EXTEND_MODEL_DEPARTMENT_CODE, ExtDepartment.class, id);
		return department;
	}
	
	@Override
	@Transactional(readOnly = true)
	public <X extends ExtDepartment> X getByCode(String code) {
//		Long id = this.cache.get(CACHE_DEPARTMENT_ID_BY_CODE, code);
//		if (null == id) {
//			X department = this.extendedModelProvider.get(EXTEND_MODEL_DEPARTMENT_CODE, ExtDepartment.class, SELECT_PRE_SQL + " WHERE D.CODE = ?", code);
//			if (null != department) {
//				this.cache.put(CACHE_DEPARTMENT_ID_BY_CODE, code, department.getId());
//			}
//			return department;
//		}
		X department = this.extendedModelProvider.get(EXTEND_MODEL_DEPARTMENT_CODE, ExtDepartment.class, SELECT_PRE_SQL + " WHERE D.CODE = ?", code);
		return department;
//		return getById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public <X extends ExtDepartment> Pageable<X> find(Pageable<X> pager,
			X condition) {
		Assert.notNull(pager);
		return this.extendedModelProvider.find(pager, EXTEND_MODEL_DEPARTMENT_CODE, ExtDepartment.class, condition);
	}
	
	@Override
	@Transactional(readOnly = true)
	public <X extends ExtDepartment> Pageable<X> find(Pageable<X> pager,
			X condition, boolean includeSub) {
		if (!includeSub || null == condition || null == condition.getPid()) return find(pager, condition);
		Department parent = this.getById(condition.getPid());
		if (null == parent) return Pagination.empty();
		condition.setPid(null);
		List<Object> params = new ArrayList<Object>();
		StringBuilder builder = new StringBuilder("SELECT * FROM HO_DEPARTMENT D WHERE D.LFT >= ? and D.RGT <= ? ");
		params.add(parent.getLft());
		params.add(parent.getRgt());
		Term term = condition.getTerm("D");
		if (null != term && StringUtil.isNotBlank(term.getSql())) {
			builder.append(" AND ").append(term.getSql());
			if (null != term.getParams() && term.getParams().length != 0) {
				for (Object p : term.getParams()) {
					params.add(p);
				}
			}
		}
		String orderBySQL = this.extendedModelProvider.buildOrderBySQL(condition);
		if (StringUtil.isBlank(orderBySQL)) {
			orderBySQL = " ORDER BY ID DESC";
		}
		builder.append(orderBySQL);
		return this.extendedModelProvider.find(pager, EXTEND_MODEL_DEPARTMENT_CODE, Department.class, builder.toString(), params.toArray());
	}
	
	@Override
	@Transactional
	public <X extends ExtDepartment> void save(X department) {
		Assert.notNull(department);
		if (null == department.getId()) {
			// INSERT
			insert(department);
		} else {
			// UPDATE
			update(department);
		}
		
	}
	
	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public <X extends ExtDepartment> List<X> findByIds(Set<Long> set) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*
	 * @see hc.base.triggers.AbstractBusinessTriggerSupport#getDefaultEntityClass()
	 */
	@Override
	protected Class<?> getDefaultEntityClass() {
		return Department.class;
	}

	/*
	 * @see hc.base.triggers.AbstractBusinessTriggerSupport#doInsert(java.lang.Object)
	 */
	@Override
	protected void doInsert(ExtDepartment entity) {
		this.extendedModelProvider.insert(EXTEND_MODEL_DEPARTMENT_CODE, Department.class, entity);
		// evict cache
//		this.evictCache(entity);
	}

	/*
	 * @see hc.base.triggers.AbstractBusinessTriggerSupport#doUpdate(java.lang.Object)
	 */
	@Override
	protected void doUpdate(ExtDepartment entity) {
		this.extendedModelProvider.update(EXTEND_MODEL_DEPARTMENT_CODE, Department.class, entity);
		// evict cache
//		this.evictCache(entity);
	}

	/*
	 * @see hc.base.triggers.AbstractBusinessTriggerSupport#doDelete(java.lang.Object)
	 */
	@Override
	protected void doDelete(ExtDepartment entity) {
		this.extendedModelProvider.deleteById(EXTEND_MODEL_DEPARTMENT_CODE, Department.class, entity.getId());
		// evict cache
//		this.evictCache(entity);
	}
	
//	private <X extends ExtDepartment> void evictCache(X department) {
		// evict cache
//		if (null != department.getId()) this.cache.evict(CACHE_DEPARTMENT_BY_ID, String.valueOf(department.getId()));
//		if (StringUtil.isNotBlank(department.getCode())) this.cache.evict(CACHE_DEPARTMENT_ID_BY_CODE, department.getCode());
//	}


	@Override
	public <X extends ExtDepartment> List<X> findByJgbh(Long jgbh) {
		List<ExtDepartment> departments = this.extendedModelProvider.find(EXTEND_MODEL_DEPARTMENT_CODE, ExtDepartment.class, SELECT_PRE_SQL + " WHERE D.JGBH = ?", jgbh);
		if(!CollectionUtils.isEmpty(departments)){
			for (ExtDepartment extDepartment : departments) {
				if(!StringUtil.isEmpty(extDepartment.getName()) && extDepartment.getName().length()>=16)
				{
					extDepartment.setName(extDepartment.getName().substring(0, 16)+"...");
				}
			}
		}
		
		return (List<X>) departments;
	}
	
	
}
