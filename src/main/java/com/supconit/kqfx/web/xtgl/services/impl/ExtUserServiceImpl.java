package com.supconit.kqfx.web.xtgl.services.impl;

import hc.base.domains.Pageable;
import hc.orm.triggers.AbstractBusinessTriggerSupport;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.supconit.kqfx.web.xtgl.daos.ExtUserDao;
import com.supconit.kqfx.web.xtgl.entities.ExtUser;
import com.supconit.kqfx.web.xtgl.entities.ExtUserCondition;
import com.supconit.kqfx.web.xtgl.services.ExtUserService;

@Service("extUserServiceImpl")
public class ExtUserServiceImpl extends AbstractBusinessTriggerSupport<ExtUser> implements ExtUserService {

//	private static final String	CACHE_GROUP_USER		= "PLAT_USER";
//	private static final String	CACHE_GROUP_USERNAME_ID	= "PLAT_USERNAME_ID";
	@Autowired
	private ExtUserDao			extUserDao;
//	@Autowired
//	private Cache				cache;


	/*
	 * @see hc.orm.BasicOrmService#getById(java.io.Serializable)
	 */
	@Override
	@Transactional(readOnly = true)
	public ExtUser getById(Long id) {
		if (null == id || id < 1) return null;
//		String key = String.valueOf(id);
//		ExtUser user = this.cache.get(CACHE_GROUP_USER, key);
//		if (null == user) {
		ExtUser	user = this.extUserDao.getById(id);
//			if (null != user) {
//				this.cache.put(CACHE_GROUP_USER, key, user);
//			}
//		}
		return user;
	}

	/*
	 * @see hc.orm.BasicOrmService#save(hc.base.domains.PK)
	 */
	@Override
	public void save(ExtUser user) {
		Assert.notNull(user);
		if (null == user.getId()) {
			insert(user);
		} else {
			update(user);
		}

		// 失效缓存
		evictCache(user);
	}
	
	private void evictCache(ExtUser entity) {
//		this.cache.evict(CACHE_GROUP_USER, String.valueOf(entity.getId()));
//		this.cache.evict(CACHE_GROUP_USERNAME_ID, entity.getUsername());
	}

	/*
	 * @see hc.orm.triggers.AbstractBusinessTriggerSupport#getDefaultEntityClass()
	 */
	@Override
	protected Class<?> getDefaultEntityClass() {
		return ExtUser.class;
	}

	/*
	 * @see hc.orm.triggers.AbstractBusinessTriggerSupport#doInsert(java.lang.Object)
	 */
	@Override
	protected void doInsert(ExtUser entity) {
		this.extUserDao.insert(entity);
	}

	/*
	 * @see hc.orm.triggers.AbstractBusinessTriggerSupport#doUpdate(java.lang.Object)
	 */
	@Override
	protected void doUpdate(ExtUser entity) {
		this.extUserDao.update(entity);
	}

	/*
	 * @see hc.orm.triggers.AbstractBusinessTriggerSupport#doDelete(java.lang.Object)
	 */
	@Override
	protected void doDelete(ExtUser entity) {
		this.extUserDao.delete(entity);
	}

	/*
	 * @see com.supconit.honeycomb.business.authorization.services.UserService#find(hc.base.domains.Pageable, com.supconit.honeycomb.business.authorization.entities.User)
	 */
	@Override
	@Transactional(readOnly = true)
	public Pageable<ExtUser> find(Pageable<ExtUser> pager, ExtUserCondition condition) {
		Assert.notNull(pager);
		return this.extUserDao.findByPager(pager, condition);
	}

	@Override
	public List<ExtUser> findList(ExtUserCondition condition) {
		return this.extUserDao.findList(condition);
	}

	@Override
	public List<ExtUser> findRoleByUserId(ExtUser condition) {
		
		return this.extUserDao.findRoleByUserId(condition);
	}

	@Override
	public List<ExtUser> getUserIdByJgbh(long jgbh) {
		// TODO Auto-generated method stub
		return extUserDao.getUserIdByJgbh(jgbh);
	}

	@Override
	public List<ExtUser> getUserIdByRoleId(long roleid) {
		// TODO Auto-generated method stub
		return extUserDao.getUserIdByRoleId(roleid);
	}
	
}
