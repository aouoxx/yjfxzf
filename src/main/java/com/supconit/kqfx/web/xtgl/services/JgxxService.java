package com.supconit.kqfx.web.xtgl.services;

import hc.base.domains.Pageable;
import hc.base.services.EntityBaiscOperator;

import java.util.List;
import java.util.Set;

import com.supconit.kqfx.web.xtgl.entities.Jgxx;

/**
 * 机构管理Service接口
 * 
 * @author shenxinfeng
 *
 */
public interface JgxxService extends EntityBaiscOperator<Jgxx>{
	
	String  EXTEND_MODEL_XTGL_ZHGL_JGXX_CODE = "T_XTGL_ZHGL_JGXX";
	
	/**
	 * 根据ID获取机构信息
	 * 
	 * @param id
	 * @return
	 */
	<X extends Jgxx> X getById(long id);
	
	<X extends Jgxx> Pageable<X> find(Pageable<X> pager, X condition);

	<X extends Jgxx> Pageable<X> find(Pageable<X> pager, X condition, boolean includeSub);
	
	
	/**
	 * 获取剔除虚拟根的机构列表。 默认按照ID倒序。
	 * 
	 * @return
	 */
	<X extends Jgxx> List<X> findAllWithoutVitualRoot();
	
	/**
	 * 直接子节点，平板型。
	 * 
	 * @param pid
	 * @return
	 */
	<X extends Jgxx> List<X> findByPid(long pid);
	
	/**
	 * 当前节点的全部子节点，平板型
	 * 
	 * @param pid
	 * @return
	 */
	<X extends Jgxx> List<X> findListByPid(long pid);
	
	/**
	 * 统计PID直接下级个数。
	 * 
	 * @param pid
	 * @return
	 */
	long countByPid(long pid);
	
	/**
	 * 构造一棵以id所在节点为根的一棵树。由于完整树已缓存，考虑通过B+搜索来获取子树。数据量小的话可以尝试子树缓存。
	 * 
	 * @param pid
	 * @return
	 */
	<X extends Jgxx> X getTree(Long id);
	
	/**
	 * 获取一棵完整的树。我们的一个模型只能有一个根结点，可以通过虚拟根结点来解决。
	 * 
	 * @return
	 */
	<X extends Jgxx> X getTree();
	
	/**
	 * 保存.
	 * 
	 * @param jgxx
	 */
	<X extends Jgxx> void save(X jgxx);
	
	
	void deleteById(Long id);
	
	/**
	 * @param set
	 */
	<X extends Jgxx> List<X> findByIds(Set<Long> set);
	
	/**
	 * 根据机构分类获取机构树
	 * 
	 * @param jgdjList
	 * @return
	 */
	<X extends Jgxx> X getTreeByJgdj(List<String> jgdjList);
	
	/**
	 * 构造一棵以id所在节点为根的一棵树，并且根据机构等级过滤此树下的节点
	 * 
	 * @param id
	 * @param jgdjList
	 * @return
	 */
	<X extends Jgxx> X getTreeByIdAndJgdj(Long id, List<String> jgdjList);
	
	
	/**
	 * 根据当前用户所在机构，获取机构列表
	 * 
	 * @return
	 */
	<X extends Jgxx> List<X> findJgxxByCurrentUser();
	
	/**
	 * 当前节点的全部子节点的ID,包含当前节点(如：“1，2，3”)
	 * @author hebingting
	 * @param pid 
	 * @return
	 */
	String findJgxxIdsByPid(long pid);
	
	/**
	 * 根据机构分类获取机构列表
	 * 
	 * @param jgdjList
	 * @return
	 */
	<X extends Jgxx> List<X> findByJgdjs(List<String> jgdjList);
	
	
	/**
	 * 根据机构分类获取指定节点下的机构列表(包含当前节点)
	 * 
	 * @param id
	 * @param jgdjList
	 * @return
	 */
	<X extends Jgxx> List<X> findByIdAndJgdjs(Long id, List<String> jgdjList);


	List<Jgxx> selectAllJgxx(Jgxx condition);
	
	/**
	 * 根据机构名称查询jgxx
	 * @param jgmc
	 * @param jgdj 1
	 * @return 
	 * @return
	 */
	<X extends Jgxx> X selectOneByJgmcdj(String jgmc);
	
	
}
