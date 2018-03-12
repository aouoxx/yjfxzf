package com.supconit.kqfx.web.fxzf.msg.services;

import java.util.Date;
import java.util.List;

import hc.orm.BasicOrmService;

import com.supconit.kqfx.web.fxzf.msg.entities.Msg;

/**
 * 对告警公告信息进行相应的数据操作
 * @author gs
 *
 */
public interface MsgService extends BasicOrmService<Msg,String>{
	
	/**
	 * 告警信息
	 * @param pid 告警信息ID
	 * @param flag 告警信息标志位 0表示告警  
	 * @param updateTime 告警时间
	 * @param content 告警内容
	 * @param jgid  告警信息对应的JGID
	 */
	public void insertWarnInfo(String pid, String flag, Date updateTime, String content, String jgid);
	
	/**
	 * 公告信息
	 * @param pid 公告信息id
	 * @param flag 公告信息标志1表示公告
	 * @param updateTime 公告开始时间
	 * @param usefulDate 公告有效期
	 * @param content 公告内容
	 * @param jgid 公告jgid
	 */
	public void inserNotifyInfo(String pid, String flag, Date updateTime, Date usefulDate, String content, String jgid);
	
	
	
	public void deleteById(String id);
	
	/**
	 * 根据用户ID获取该用户弹出的消息
	 * @param id
	 * @return
	 */
	public List<Msg> getByUserId(String id);
	
	
	/**
	 * 删除有效期小于当前时间的信息
	 * @param condition
	 */
	public void deleteByUsefulDate(Msg condition);
	
	
	/**
	 * 根据用户ID和消息ID删除弹出信息
	 * @param msg
	 */
	public void deleteByUserId(Msg msg);
	

}
