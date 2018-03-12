package com.supconit.kqfx.web.fxzf.msg.daos.Impl;

import hc.orm.AbstractBasicDaoImpl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.supconit.kqfx.web.fxzf.msg.daos.PushMsgFlagDao;
import com.supconit.kqfx.web.fxzf.msg.entities.PushMsgFlag;

@Repository("tzfxzf_pushmsg_dao")
public class PushMsgFlagDaoImpl extends AbstractBasicDaoImpl<PushMsgFlag, String> implements PushMsgFlagDao {

	private static final String NAMESPACE=PushMsgFlag.class.getName();
	
	@Override
	protected String getNamespace() {
		// TODO Auto-generated method stub
		return NAMESPACE;
	}

	@Override
	public List<PushMsgFlag> getByUserIds(PushMsgFlag condition) {
		// TODO Auto-generated method stub
		return selectList("getByUserIds", condition);
	}

	@Override
	public void deleteByUserId(String userid) {
		delete("deleteByUserId", userid);
		
	}

	@Override
	public void deleteChannelByUserId(String userid) {
		delete("deleteChannelByUserId",userid);
		
	}

}
