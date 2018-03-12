package com.supconit.kqfx.web.xtgl.services.impl;

import hc.base.domains.Pageable;
import hc.base.domains.Pagination;
import hc.jdbc.JdbcProcessor;
import hc.modelextend.ExtendedModelProvider;
import hc.orm.triggers.AbstractBusinessTriggerSupport;
import hc.orm.triggers.BusinessTriggerExecutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.supconit.honeycomb.util.StringPool;
import com.supconit.kqfx.web.xtgl.entities.ExtPerson;
import com.supconit.kqfx.web.xtgl.entities.Jgxx;
import com.supconit.kqfx.web.xtgl.services.ExtDepartmentService;
import com.supconit.kqfx.web.xtgl.services.ExtPersonService;
import com.supconit.kqfx.web.xtgl.services.JgxxService;

/**
 * 人员管理Service实现类
 * 
 * @author shenxinfeng
 *
 */
@Service
public class ExtPersonServiceImpl extends AbstractBusinessTriggerSupport<ExtPerson> implements ExtPersonService {

	private static final String		SELECT_PRE_SQL			= "SELECT P.* FROM HO_PERSON P";

//	private static final String		CACHE_PERSON_BY_ID		= "PERSON_BY_ID";
//	private static final String		CACHE_PERSON_ID_BY_CODE	= "PERSON_ID_BY_CODE";

	@Autowired
	private ExtDepartmentService		extDepartmentService;
	@Autowired
	private ExtendedModelProvider	extendedModelProvider;
	@Autowired
	private JdbcProcessor			jdbcProcessor;
	@Autowired(required = false)
	private BusinessTriggerExecutor	businessTriggerExecutor;
	@Autowired
	private JgxxService	jgxxService;


//	@Autowired
//	private Cache					cache;
	
	@Override
	@Transactional(readOnly = true)
	public <X extends ExtPerson> X getById(long id) {
//		String key = String.valueOf(id);
//		X person = this.cache.get(CACHE_PERSON_BY_ID, key);
//		if (null == person) {
			X person = this.extendedModelProvider.get(EXTEND_MODEL_PERSON_CODE, ExtPerson.class, id);
//			if (null != person) 
//				this.cache.put(CACHE_PERSON_BY_ID, key, person);
//		}
		return person;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public <X extends ExtPerson> X getByCode(String code) {
//		Long id = this.cache.get(CACHE_PERSON_ID_BY_CODE, code);
//		if (null == id) {
			X person = (X) this.extendedModelProvider.get(EXTEND_MODEL_PERSON_CODE, ExtPerson.class, SELECT_PRE_SQL + " WHERE P.CODE = ?", code);
//			if (null != person) this.cache.put(CACHE_PERSON_ID_BY_CODE, code, person.getId());
			return person;
//		}
//		return getById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public <X extends ExtPerson> List<X> findByIds(Set<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) return Collections.emptyList();
		List<X> result = new ArrayList<X>();

		for (Iterator<Long> it = ids.iterator(); it.hasNext();) {
			Long id = it.next();
//			X person = this.cache.get(CACHE_PERSON_BY_ID, String.valueOf(id));
			X person = getById(id);
			if (null != person) {
				result.add(person);
				it.remove();
			}
		}
		// 超过1000个时要分段查询 SQL限制
		if (!ids.isEmpty()) {
			StringBuilder builder = new StringBuilder(SELECT_PRE_SQL).append(" WHERE P.ID IN (");
			for (Long id : ids) {
				builder.append(id).append(",");
			}
			builder.deleteCharAt(builder.length() - 1);
			builder.append(")");
			List<X> list = this.extendedModelProvider.find(EXTEND_MODEL_PERSON_CODE, ExtPerson.class, builder.toString());
			if (!CollectionUtils.isEmpty(list)) {
				result.addAll(list);
//				for (X p : list) {
//					this.cache.put(CACHE_PERSON_BY_ID, String.valueOf(p.getId()), p);
//				}
			}
		}

		return result;
	}
	
	@Override
	@Transactional(readOnly = true)
	public <X extends ExtPerson> Pageable<X> find(Pageable<X> pager, X condition) {
		return this.extendedModelProvider.find(pager, EXTEND_MODEL_PERSON_CODE, ExtPerson.class, condition);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@Transactional
	public <X extends ExtPerson> void save(X person) {
		Assert.notNull(person);
		if (null == person.getId()) {
			// INSERT
			insert(person);
		} else {
			// UPDATE
			update(person);

			// 删除关联信息
			//deleteDepartmentPersonInfo(person.getId());
			//deletePositionPersonInfo(person.getId());

			// evict cache
//			evictCache(person);
		}
//		// 保存关联表
//		if (!CollectionUtils.isEmpty(person.getDepartmentPersonInfos())) {
//			for (DepartmentPersonInfo dpi : person.getDepartmentPersonInfos()) {
//				dpi.setPersonId(person.getId());
//			}
//			saveDepartmentInfos(person.getDepartmentPersonInfos());
//		}
//		if (!CollectionUtils.isEmpty(person.getPositionPersonInfos())) {
//			for (PositionPersonInfo ppi : person.getPositionPersonInfos()) {
//				ppi.setPersonId(person.getId());
//			}
//			savePositionPersonInfos(person.getPositionPersonInfos());
//		}
	}
	
//	private <X extends ExtPerson> void evictCache(X person) {
		// evict cache
//		if (null != person.getId()) 
//			this.cache.evict(CACHE_PERSON_BY_ID, String.valueOf(person.getId()));
//		if (StringUtil.isNotBlank(person.getCode())) 
//			this.cache.evict(CACHE_PERSON_ID_BY_CODE, person.getCode());
//	}
	
	@Override
	@Transactional
	public void delete(Long[] ids) {
		if (null == ids || ids.length == 0) return;
		// 先过滤重复
		Set<Long> set = new HashSet<Long>();
		for (Long id : ids) {
			set.add(id);
		}
		if (set.isEmpty()) return;
		StringBuilder builder = new StringBuilder();
		for (Long id : set) {
			builder.append(id).append(StringPool.COMMA);
		}
		builder.deleteCharAt(builder.length() - 1);
		StringBuilder deleteSQL = new StringBuilder("DELETE FROM ").append(ExtPerson.TABLE_NAME).append(" WHERE ID IN (").append(builder).append(")");
//		StringBuilder deleteDepartmentPersonInfoSQL = new StringBuilder("DELETE FROM ").append(DepartmentPersonInfo.TABLE_NAME).append(" WHERE PERSON_ID IN (").append(builder)
//				.append(")");
//		StringBuilder deletePositonPersonInfoSQL = new StringBuilder("DELETE FROM ").append(PositionPersonInfo.TABLE_NAME).append(" WHERE PERSON_ID IN (").append(builder)
//				.append(")");

		this.jdbcProcessor.update(deleteSQL.toString());
//		this.jdbcProcessor.update(deleteDepartmentPersonInfoSQL.toString());
//		this.jdbcProcessor.update(deletePositonPersonInfoSQL.toString());

//		for (Long id : set) {
//			this.cache.evict(CACHE_PERSON_BY_ID, String.valueOf(id));
//		}
	}
	
	@Override
	protected void doDelete(ExtPerson entity) {
		this.extendedModelProvider.deleteById(EXTEND_MODEL_PERSON_CODE, ExtPerson.class, entity.getId());
//		deleteDepartmentPersonInfo(entity.getId());
//		deletePositionPersonInfo(entity.getId());
		// evict cache
//		evictCache(entity);
	}

	@Override
	protected void doInsert(ExtPerson entity) {
		this.extendedModelProvider.insert(EXTEND_MODEL_PERSON_CODE, ExtPerson.class, entity);
		// evict cache
//		evictCache(entity);// 本来这里是不需要的，但是为了修改CODE的不产生冲突，还是evict下。
	}

	@Override
	protected void doUpdate(ExtPerson entity) {
		this.extendedModelProvider.update(EXTEND_MODEL_PERSON_CODE, ExtPerson.class, entity);
		// evict cache
//		evictCache(entity);
	}

	@Override
	protected Class<?> getDefaultEntityClass() {
		return ExtPerson.class;
	}

	@Override
	public <X extends ExtPerson> Pagination<X> personSetJgxx(Pagination<X> pager) {
		Set<Long> set = new HashSet<Long>();
		for (X person : pager) {
			set.add(person.getJgbh());
		}

		List<Jgxx> jgxxs = this.jgxxService.findByIds(set);
		if (!CollectionUtils.isEmpty(jgxxs)) {
			Map<Long, Jgxx> jgxxMap = new HashMap<Long, Jgxx>();
			for (Jgxx j : jgxxs) {
				jgxxMap.put(j.getId(), j);
			}
			for (X d : pager) {
				if (null != d.getJgbh()) 
					d.setJgxx(jgxxMap.get(d.getJgbh()));
			}
		}
		return pager;
	}
	
}
