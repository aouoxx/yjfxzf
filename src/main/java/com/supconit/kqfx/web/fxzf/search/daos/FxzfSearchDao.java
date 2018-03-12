package com.supconit.kqfx.web.fxzf.search.daos;

import hc.base.domains.Pageable;
import hc.orm.BasicDao;

import java.util.List;

import com.supconit.kqfx.web.fxzf.search.entities.Fxzf;

public interface FxzfSearchDao extends BasicDao<Fxzf, String>  {
	
	/**
	 * 分页查询信息列表
	 * @param pager
	 * @param condition
	 * @return
	 */
	Pageable<Fxzf> findByPager(Pageable<Fxzf> pager, Fxzf condition);
	
	Pageable<Fxzf> findByPagerDetail(Pageable<Fxzf> pager, Fxzf condition);

	void batchInsert(List<Fxzf> fxzfList);
	
	/**
	 * 审核信息
	 */
	void checkFxzfMessage(Fxzf condition);
	
	/**
	 * 获取某个超限状态的数目
	 */
	int getOverLoadStatusCount(Fxzf fxzf);
	
	/**
	 * 获取所有超限状态的数目
	 */
	int getIllegalLevelCount(Fxzf fxzf);
    /**
     * 获取不同状态等级的数据
     * @param fxzf
     * @return
     */
	Integer getCountByCondition(Fxzf fxzf);
	/**
	 * 获取超载的数量
	 * @param fxzf
	 * @return
	 */
	Integer getOverLoadCount(Fxzf fxzf); 
	/**
	 * 根据ID删除对应的信息
	 * @param id
	 */
	void deleteById(String id);
	
	/**
	 * 获取没有被上传的前10条信息
	 */
	List<Fxzf> getFxzfToTransport();
	
	/**
	 * 更新传输结果
	 * @param fxzf
	 */
	void upDateFxzftransport(Fxzf fxzf);

}
