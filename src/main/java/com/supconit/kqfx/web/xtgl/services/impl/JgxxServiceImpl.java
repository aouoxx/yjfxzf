package com.supconit.kqfx.web.xtgl.services.impl;

import hc.base.domains.Pageable;
import hc.base.domains.Pagination;
import hc.base.exceptions.TreeException;
import hc.jdbc.JdbcProcessor;
import hc.modelextend.ExtendedModelProvider;
import hc.orm.search.Term;
import hc.orm.triggers.AbstractBusinessTriggerSupport;
import hc.orm.triggers.BusinessTriggerExecutor;
import hc.safety.manager.SafetyManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import jodd.util.StringUtil;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.supconit.honeycomb.business.authorization.entities.User;
import com.supconit.kqfx.web.xtgl.daos.JgxxDao;
import com.supconit.kqfx.web.xtgl.entities.ExtPerson;
import com.supconit.kqfx.web.xtgl.entities.Jgxx;
import com.supconit.kqfx.web.xtgl.services.ExtPersonService;
import com.supconit.kqfx.web.xtgl.services.JgxxService;

/**
 * 机构管理Service实现类
 * 
 * @author shenxinfeng
 *
 */
@Service
public class JgxxServiceImpl extends AbstractBusinessTriggerSupport<Jgxx> implements JgxxService {
	
	private static final String		SELECT_PRE_SQL			= "SELECT * FROM " + Jgxx.TABLE_NAME + " J ";
//	private static final String		CACHE_XTGL_ZHGL_JGXX_BY_ID		= "XTGL_ZHGL_JGXX_BY_ID";

	@Autowired
	private ExtendedModelProvider	extendedModelProvider;
	@Autowired(required = false)
	private BusinessTriggerExecutor	businessTriggerExecutor;
	
	@Autowired
	private JdbcProcessor jdbcProcessor;
	
	private String defaultOrderBy = "ORDER BY J.ID DESC";

//	@Autowired
//	private Cache cache;
	
	@Autowired
	private SafetyManager	safetyManager;
	@Autowired
	private JgxxDao	jgxxDao;
	@Autowired
	private ExtPersonService extPersonService;

	@Override
	@Transactional(readOnly = true)
	public <X extends Jgxx> X getById(long id) {
//		String key = String.valueOf(id);
//		X jgxx = this.cache.get(CACHE_XTGL_ZHGL_JGXX_BY_ID, key);
//		if (null == jgxx) {
//			jgxx = this.extendedModelProvider.get(EXTEND_MODEL_XTGL_ZHGL_JGXX_CODE, Jgxx.class, id);
//			if (null != jgxx) this.cache.put(CACHE_XTGL_ZHGL_JGXX_BY_ID, key, jgxx);
//		}
		X jgxx = this.extendedModelProvider.get(EXTEND_MODEL_XTGL_ZHGL_JGXX_CODE, Jgxx.class, id);
		return jgxx;
	}

	@Override
	@Transactional(readOnly = true)
	public <X extends Jgxx> Pageable<X> find(Pageable<X> pager, X condition) {
		Assert.notNull(pager);
		Pageable<X> p = this.extendedModelProvider.find(pager, EXTEND_MODEL_XTGL_ZHGL_JGXX_CODE, Jgxx.class, condition);
		return p;
	}

	@Override
	@Transactional(readOnly = true)
	public <X extends Jgxx> List<X> findAllWithoutVitualRoot() {
		return this.extendedModelProvider.find(EXTEND_MODEL_XTGL_ZHGL_JGXX_CODE, Jgxx.class, SELECT_PRE_SQL + " WHERE J.PID > 0 AND J.DELETED = 0 " + defaultOrderBy);
	}

	@Override
	@Transactional(readOnly = true)
	public <X extends Jgxx> List<X> findByPid(long pid) {
		return this.extendedModelProvider.find(EXTEND_MODEL_XTGL_ZHGL_JGXX_CODE, Jgxx.class, SELECT_PRE_SQL + " WHERE J.PID = ? AND J.DELETED = 0", pid);
	}
	
	@Override
	@Transactional(readOnly = true)
	public <X extends Jgxx> List<X> findListByPid(long pid) {
		List<X> jgList = new ArrayList<X>();
		Jgxx root = this.extendedModelProvider.get(EXTEND_MODEL_XTGL_ZHGL_JGXX_CODE, Jgxx.class, pid);
		if(null != root){
			if(null != root.getLft() && null != root.getRgt()){
				jgList = this.extendedModelProvider.find(EXTEND_MODEL_XTGL_ZHGL_JGXX_CODE, Jgxx.class, SELECT_PRE_SQL + " WHERE J.LFT BETWEEN ? AND ? AND J.DELETED = 0 ORDER BY J.LFT",
				root.getLft(), root.getRgt());
			}
		}
		return jgList;
	}

	@Override
	@Transactional(readOnly = true)
	public long countByPid(long pid) {
		return this.jdbcProcessor.get(Long.class, "SELECT COUNT(*) FROM " + Jgxx.TABLE_NAME + " J WHERE J.PID = ? AND J.DELETED = 0", pid);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public <X extends Jgxx> X getTree(Long id) {
		if (null == id || id < 1) return null;
		X root = (X) getById(id);
		if (null == root) return null;
		// 这里采用改进的先序遍历来做
		if (null == root.getLft() || null == root.getRgt()) throw new TreeException("root has no lft or rgt.");
		return (X) buildTree(root);
	}

	@Override
	@Transactional(readOnly = true)
	public <X extends Jgxx> X getTree() {
		// 1.获取根结点ID
		List<X> roots = findByPid(Jgxx.ROOT_PID);
		if (CollectionUtils.isEmpty(roots) || roots.size() > 1) throw new TreeException("has no root or more the one root.");
		X root = roots.get(0);
		// 这里采用改进的先序遍历来做
		if (null == root.getLft() || null == root.getRgt()) throw new TreeException("root has no lft or rgt.");
		return buildTree(root);
	}

	@Override
	@Transactional
	public <X extends Jgxx> void save(X jgxx) {
		Assert.notNull(jgxx);
		if (null == jgxx.getId()) {
			// INSERT
			insert(jgxx);
		} else {
			// UPDATE
			update(jgxx);
		}

		rebuildLftAndRgt(); // 刷新左右值
	}

	@Override
	@Deprecated
	@Transactional
	public void deleteById(Long id) {
		Jgxx jgxx = new Jgxx();
		jgxx.setId(id);
		//delete(jgxx);
		jgxx.setDeleted(1);
		update(jgxx);
	}
	
	private <X extends Jgxx> X buildTree(X root) {
		// to be a tree
		List<X> list = this.extendedModelProvider.find(EXTEND_MODEL_XTGL_ZHGL_JGXX_CODE, Jgxx.class, SELECT_PRE_SQL + " WHERE J.LFT BETWEEN ? AND ? AND J.DELETED = 0 ORDER BY J.LFT",
				root.getLft(), root.getRgt());
		Stack<X> rightStack = new Stack<X>();
		for (X node : list) {
			if (!rightStack.isEmpty()) {
				while (rightStack.lastElement().getRgt() < node.getRgt()) {
					rightStack.pop();
				}
			}
			if (!rightStack.isEmpty()) {
				rightStack.lastElement().addChild(node);
			}
			rightStack.push(node);
		}
		return rightStack.firstElement();
	}
	
	private void rebuildLftAndRgt() {
		List<Long> ids = findIdsByPid(Jgxx.ROOT_PID);
		if (!CollectionUtils.isEmpty(ids)) {
			for (Long id : ids) {
				rebuildTree(id, 1L);
			}
		}
	}
	
	private List<Long> findIdsByPid(long pid) {
		return this.jdbcProcessor.find(Long.class, "SELECT J.ID AS ID FROM " + Jgxx.TABLE_NAME + " J WHERE J.PID = ? AND J.DELETED = 0", pid);
	}

	/**
	 * 递归重建
	 * 
	 * @param pid
	 * @param lft
	 * @return
	 */
	private Long rebuildTree(Long pid, Long lft) {
		Long rgt = lft + 1;
		List<Long> ids = findIdsByPid(pid);
		if (!CollectionUtils.isEmpty(ids)) {
			for (Long id : ids) {
				rgt = rebuildTree(id, rgt);
			}
		}
		this.jdbcProcessor.update("UPDATE " + Jgxx.TABLE_NAME + " SET LFT = ?, RGT = ? WHERE ID = ?", lft, rgt, pid);
		return rgt + 1;
	}
	
	/*
	 * @see hc.base.triggers.AbstractBusinessTriggerSupport#getDefaultEntityClass()
	 */
	@Override
	protected Class<?> getDefaultEntityClass() {
		return Jgxx.class;
	}
	
	/*
	 * @see hc.base.triggers.AbstractBusinessTriggerSupport#doInsert(java.lang.Object)
	 */
	@Override
	protected void doInsert(Jgxx entity) {
		this.extendedModelProvider.insert(EXTEND_MODEL_XTGL_ZHGL_JGXX_CODE, Jgxx.class, entity);
		// evict cache
//		this.evictCache(entity);
	}

	/*
	 * @see hc.base.triggers.AbstractBusinessTriggerSupport#doUpdate(java.lang.Object)
	 */
	@Override
	protected void doUpdate(Jgxx entity) {
		this.extendedModelProvider.update(EXTEND_MODEL_XTGL_ZHGL_JGXX_CODE, Jgxx.class, entity);
		// evict cache
//		this.evictCache(entity);
	}

	/*
	 * @see hc.base.triggers.AbstractBusinessTriggerSupport#doDelete(java.lang.Object)
	 */
	@Override
	protected void doDelete(Jgxx entity) {
		//this.extendedModelProvider.deleteById(EXTEND_MODEL_JGXX_CODE, Jgxx.class, entity.getId());
		entity.setDeleted(1);
		this.extendedModelProvider.update(EXTEND_MODEL_XTGL_ZHGL_JGXX_CODE, Jgxx.class, entity);
		// evict cache
//		this.evictCache(entity);
	}
	
	private <X extends Jgxx> void evictCache(X jgxx) {
		// evict cache
//		if (null != jgxx.getId()) this.cache.evict(CACHE_XTGL_ZHGL_JGXX_BY_ID, String.valueOf(jgxx.getId()));
	}

	@Override
	public <X extends Jgxx> List<X> findByIds(Set<Long> set) {
		if (CollectionUtils.isEmpty(set)) return Collections.emptyList();
		StringBuilder builder = new StringBuilder(SELECT_PRE_SQL).append(" WHERE ID IN (");
		for (Long id : set) {
			builder.append(id).append(",");
		}
		builder.deleteCharAt(builder.length() - 1);
		builder.append(")");
		return this.extendedModelProvider.find(EXTEND_MODEL_XTGL_ZHGL_JGXX_CODE, Jgxx.class, builder.toString());
	}

	@Override
	@Transactional
	public <X extends Jgxx> Pageable<X> find(Pageable<X> pager, X condition,
			boolean includeSub) {
		if (!includeSub || null == condition || null == condition.getPid()) 
			return find(pager, condition);
		Jgxx parent = this.getById(condition.getPid());
		if (null == parent) return Pagination.empty();
		condition.setPid(null);
		List<Object> params = new ArrayList<Object>();
		StringBuilder builder = new StringBuilder("SELECT * FROM T_XTGL_ZHGL_JGXX J WHERE J.LFT >= ? and J.RGT <= ? ");
		params.add(parent.getLft());
		params.add(parent.getRgt());
		Term term = condition.getTerm("J");
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
		return this.extendedModelProvider.find(pager, EXTEND_MODEL_XTGL_ZHGL_JGXX_CODE, Jgxx.class, builder.toString(), params.toArray());
	}

	@Override
	public <X extends Jgxx> X getTreeByJgdj(List<String> jgdjList) {
		// 1.获取根结点ID
		List<X> roots = findByPid(Jgxx.ROOT_PID);
		if (CollectionUtils.isEmpty(roots) || roots.size() > 1) throw new TreeException("has no root or more the one root.");
		X root = roots.get(0);
		// 这里采用改进的先序遍历来做
		if (null == root.getLft() || null == root.getRgt()) throw new TreeException("root has no lft or rgt.");
		return buildTree(root, jgdjList);
	}
	
	private <X extends Jgxx> X buildTree(X root, List<String> jgdjList) {
		String jgdjs = "";
		String jgdjStr = "";
		if(jgdjList != null && jgdjList.size() > 0){
			jgdjStr = " AND J.JGDJ in (";
			for (String jgdj : jgdjList) {
				jgdjs +="\'"+jgdj+"\',";
			}
			jgdjs = jgdjs.substring(0,jgdjs.length()-1);
			jgdjStr += jgdjs +")";
		}
		// to be a tree
		List<X> list = this.extendedModelProvider.find(EXTEND_MODEL_XTGL_ZHGL_JGXX_CODE, Jgxx.class, SELECT_PRE_SQL + " WHERE J.LFT BETWEEN ? AND ? AND J.DELETED = 0 " + jgdjStr + " ORDER BY J.LFT",
				root.getLft(), root.getRgt());
		Stack<X> rightStack = new Stack<X>();
		for (X node : list) {
			if (!rightStack.isEmpty()) {
				while (rightStack.lastElement().getRgt() < node.getRgt()) {
					rightStack.pop();
				}
			}
			if (!rightStack.isEmpty()) {
				rightStack.lastElement().addChild(node);
			}
			rightStack.push(node);
		}
		return rightStack.firstElement();
	}
	
	@Override
	public <X extends Jgxx> X getTreeByIdAndJgdj(Long id, List<String> jgdjList) {
		X root = getById(id);
		if(root == null){
			// 获取指定节点
			List<X> roots = findByPid(Jgxx.ROOT_PID);
			if (CollectionUtils.isEmpty(roots) || roots.size() > 1) throw new TreeException("has no root or more the one root.");
			root = roots.get(0);
		}
		// 这里采用改进的先序遍历来做
		if (null == root.getLft() || null == root.getRgt()) throw new TreeException("root has no lft or rgt.");
		return buildTree(root, jgdjList);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Jgxx> findJgxxByCurrentUser() {
		//获取当前用户所属机构
		User user = (User) safetyManager.getCurrentUser();
		ExtPerson extPerson = extPersonService.getById(user.getPersonId());
		Jgxx jgxx = getById(extPerson.getJgbh());
		//获取父节点
		Jgxx pJgxx = getById(jgxx.getPid());
		List<Jgxx> jgxxList = new ArrayList<Jgxx>();
		//通过判断父节点的层级来确定自己的层级
		if (Jgxx.ROOT_ID == pJgxx.getId()) {
			//表示自己是第一层,即丽水市公路局
			Jgxx rootJgxx = getTree();
			jgxxList = rootJgxx.getChildren().get(0).getChildren();
		}else if (Jgxx.ROOT_ID == pJgxx.getPid()) {
			//表示自己是第二层,即丽水市的某县
			jgxxList.add(jgxx);
		}else {
			//表示自己是其他层,即丽水市的某县下的某乡镇
			jgxxList.add(pJgxx);
		}
		return jgxxList;
	}

	@Override
	@Transactional(readOnly = true)
	public String findJgxxIdsByPid(long pid) {
		List<Jgxx> idsList = this.findListByPid(pid);
		StringBuffer sb = new StringBuffer();
		sb.append(pid);
		if(idsList.size() > 0){
			for (Jgxx jgxx : idsList) {
				sb.append(",").append(jgxx.getId());
			}
		}
		return sb.toString();
	}

	@Override
	@Transactional(readOnly = true)
	public <X extends Jgxx> List<X> findByJgdjs(List<String> jgdjList) {
		String jgdjs = "";
		String jgdjStr = "";
		if(jgdjList != null && jgdjList.size() > 0){
			jgdjStr = " AND J.JGDJ in (";
			for (String jgdj : jgdjList) {
				jgdjs +="\'"+jgdj+"\',";
			}
			jgdjs = jgdjs.substring(0,jgdjs.length()-1);
			jgdjStr += jgdjs +")";
		}
		return this.extendedModelProvider.find(EXTEND_MODEL_XTGL_ZHGL_JGXX_CODE, Jgxx.class, SELECT_PRE_SQL + " WHERE J.DELETED = 0 " + jgdjStr);
	}

	@Override
	@Transactional(readOnly = true)
	public <X extends Jgxx> List<X> findByIdAndJgdjs(Long id,
			List<String> jgdjList) {
		List<X> jgList = new ArrayList<X>();
		String jgdjs = "";
		String jgdjStr = "";
		Jgxx root = getById(id);
		if(jgdjList != null && jgdjList.size() > 0){
			jgdjStr = " AND J.JGDJ in (";
			for (String jgdj : jgdjList) {
				jgdjs +="\'"+jgdj+"\',";
			}
			jgdjs = jgdjs.substring(0,jgdjs.length()-1);
			jgdjStr += jgdjs +")";
		}
		if(null != root){
			if(null != root.getLft() && null != root.getRgt()){
				jgList = this.extendedModelProvider.find(EXTEND_MODEL_XTGL_ZHGL_JGXX_CODE, Jgxx.class, SELECT_PRE_SQL + " WHERE J.LFT BETWEEN ? AND ? AND J.DELETED = 0 "+ jgdjStr,
				root.getLft(), root.getRgt());
			}
		}
		return jgList;
	}


	@Override
	public List<Jgxx> selectAllJgxx(Jgxx condition) {
		
		return this.jgxxDao.selectAllJgxx( condition);
	}

	@Override
	public <X extends Jgxx> X selectOneByJgmcdj(String jgmc) {
		String jgmcStr = "";
		List<X> jgList = new ArrayList<X>();
		
		if(!StringUtils.isEmpty(jgmc))
		{
			jgmcStr = "AND J.JGMC LIKE \'%"+jgmc+"%\'";
		}
		String jgdj = "1";
		
		jgList = this.extendedModelProvider.find(EXTEND_MODEL_XTGL_ZHGL_JGXX_CODE, Jgxx.class, SELECT_PRE_SQL + " WHERE J.JGDJ = ? AND J.DELETED = 0 "+ jgmcStr,jgdj);
		
		if(CollectionUtils.isEmpty(jgList))
			return null;
		else
			return jgList.get(0);
	}


}
