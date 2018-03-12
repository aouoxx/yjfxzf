package com.supconit.kqfx.web.xtgl.daos.impl;

import hc.base.domains.Pageable;
import hc.orm.AbstractBasicDaoImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.supconit.kqfx.web.xtgl.daos.ExtUserDao;
import com.supconit.kqfx.web.xtgl.entities.ExtUser;
import com.supconit.kqfx.web.xtgl.entities.ExtUserCondition;

@Repository
public class ExtUserDaoImpl extends AbstractBasicDaoImpl<ExtUser, Long> implements ExtUserDao {
	private static final String	NAMESPACE	= ExtUser.class.getName();

	@Override
	protected String getNamespace() {
		return NAMESPACE;
	}

	@Override
	public ExtUser getByCondition(ExtUser condition) {
		return selectOne("getByCondition", condition);
	}

	/*
	 * @see hc.orm.AbstractBasicDaoImpl#getById(java.io.Serializable)
	 */
	@Override
	public ExtUser getById(Long id) {
		return getByCondition(new ExtUser(id));
	}
	
	@Override
	public Pageable<ExtUser> findByPager(Pageable<ExtUser> pager, ExtUserCondition condition) {
		return findByPager(pager, "selectPager", "countPager", condition);
	}

	@Override
	public List<ExtUser> findList(ExtUserCondition condition) {
		Map<String, Object> params = new HashMap<String, Object>(2);
		params.put("condition", condition);
		return selectList("findList", params);
	}

	@Override
	public List<ExtUser> findRoleByUserId(ExtUser condition) {
		
		return this.selectList("findRoleByUserId", condition);
	}

	@Override
	public List<ExtUser> getUserIdByJgbh(long jgbh) {
		// TODO Auto-generated method stub
		return selectList("getUserIdByJgbh", jgbh);
	}

	@Override
	public List<ExtUser> getUserIdByRoleId(long roleid) {
		// TODO Auto-generated method stub
		return selectList("getUserIdByRoleId",roleid);
	}

}