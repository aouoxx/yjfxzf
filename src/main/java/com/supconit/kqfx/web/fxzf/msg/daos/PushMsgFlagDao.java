package com.supconit.kqfx.web.fxzf.msg.daos;

import hc.orm.BasicDao;

import java.util.List;

import com.supconit.kqfx.web.fxzf.msg.entities.PushMsgFlag;

public interface PushMsgFlagDao extends BasicDao<PushMsgFlag,String> {

	List<PushMsgFlag> getByUserIds(PushMsgFlag condition);
	
	void deleteByUserId(String userid);
	
	public void deleteChannelByUserId(String userid);
}
