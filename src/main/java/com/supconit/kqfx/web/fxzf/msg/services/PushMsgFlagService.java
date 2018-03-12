package com.supconit.kqfx.web.fxzf.msg.services;

import java.util.List;

import hc.orm.BasicOrmService;

import com.supconit.kqfx.web.fxzf.msg.entities.PushMsgFlag;

public interface PushMsgFlagService extends BasicOrmService<PushMsgFlag, String> {

	/**
	 * 通过ID查询对应的设备ID
	 * @param condition
	 * @return
	 */
	List<PushMsgFlag> getByUserIds(PushMsgFlag condition);
	
	/**
	 * 删除某个账户时将该账户对应标志位信息删除
	 * @param userid 
	 */
	void deleteByUserId(String userid);
	
	/**
	 * 删除用户时删除与该用户绑定的channelID记录
	 * @param userid
	 */
    void deleteChannelByUserId(String userid);
}
