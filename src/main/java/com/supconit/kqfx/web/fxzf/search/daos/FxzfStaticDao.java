package com.supconit.kqfx.web.fxzf.search.daos;

import hc.base.domains.Pageable;
import hc.orm.BasicDao;

import java.util.List;

import com.supconit.kqfx.web.fxzf.search.entities.FxzfStatic;

/**
 * 
 * @author JNJ
 * @time 2016年12月8日10:08:28
 *
 */
public interface FxzfStaticDao extends BasicDao<FxzfStatic, String> {

	/**
	 * 分页查询信息列表
	 * 
	 * @param pager
	 * @param condition
	 * @return
	 */
	Pageable<FxzfStatic> findByPager(Pageable<FxzfStatic> pager, FxzfStatic condition);

	Pageable<FxzfStatic> findByPagerDetail(Pageable<FxzfStatic> pager, FxzfStatic condition);

	void batchInsert(List<FxzfStatic> fxzfList);

	/**
	 * 审核信息
	 */
	void checkFxzfMessage(FxzfStatic condition);

	/**
	 * 获取某个超限状态的数目
	 */
	int getOverLoadStatusCount(FxzfStatic fxzfStatic);

	/**
	 * 获取所有超限状态的数目
	 */
	int getIllegalLevelCount(FxzfStatic fxzfStatic);

	/**
	 * 获取不同状态等级的数据
	 * 
	 * @param fxzfStatic
	 * @return
	 */
	Integer getCountByCondition(FxzfStatic fxzfStatic);

	/**
	 * 获取超载的数量
	 * 
	 * @param fxzfStatic
	 * @return
	 */
	Integer getOverLoadCount(FxzfStatic fxzfStatic);

	/**
	 * 根据ID删除对应的信息
	 * 
	 * @param id
	 */
	void deleteById(String id);

	/**
	 * 获取没有被上传的前10条信息
	 */
	List<FxzfStatic> getFxzfToTransport();

	/**
	 * 更新传输结果
	 * 
	 * @param fxzfStatic
	 */
	void upDateFxzftransport(FxzfStatic fxzfStatic);

}
