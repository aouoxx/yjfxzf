package com.supconit.kqfx.web.fxzf.qbb.services;

import java.util.List;

import com.supconit.kqfx.web.fxzf.qbb.entities.QbbBfxx;
import com.supconit.kqfx.web.fxzf.warn.entities.WarnHistory;

import hc.base.domains.Pageable;
import hc.base.domains.Pagination;
import hc.orm.BasicOrmService;

public interface QbbBfxxService extends BasicOrmService<QbbBfxx, String> {

	Pageable<QbbBfxx> find(Pagination<QbbBfxx> pager, QbbBfxx condition);

	/**
	 * 发布情报板信息
	 * @param bfxxs  发布到情报板的内容
	 * @param location	情报板位置
	 * @param remainTime	情报板保留时间
	 */
	void publishToQbb(List<QbbBfxx> bfxxs, String location, Long remainTime);

	List<QbbBfxx> getByCondition(QbbBfxx condition);
	
	/**
	 * 通过情报板ID获取对应的情报板
	 * @param id
	 * @return
	 */
	QbbBfxx getByBfxxId(String id);
	
	/**
	 * 单个治超点恢复情报板信息
	 * @param remainTime 恢复时间间隔(系统初始化为0,手动发布情报板为自定义传值,自动发布为默认时间间隔)
	 * @param redis_xxnr 发布之前情报板内容(系统初始化可为空)
	 * @param location 情报板发布位置
	 * @param type 发布类型(0:告警发布情报板(自动);1:手动发布情报板)
	 */
	public void restoreLastQbb(Long remainTime, String redis_xxnr, String location, Integer type);
	
	/**
	 * 根据情报板位置、内容判断是否为告警情报板发布历史
	 * @param qbbbfxx
	 * @return
	 */
	List<QbbBfxx> IsWarnTypeHistrory(QbbBfxx qbbbfxx);
	
	/**
	 * 初始化设置恢复所有情报板内容
	 */
	void initLastQbb();
}
