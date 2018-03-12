package com.supconit.kqfx.web.fxzf.msg.services.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supconit.kqfx.web.fxzf.msg.daos.PushMsgFlagDao;
import com.supconit.kqfx.web.fxzf.msg.entities.PushMsgFlag;
import com.supconit.kqfx.web.fxzf.msg.services.PushMsgFlagService;

@Service("tzfxzf_pushmsg_service")
public class PushMsgFlagServiceImpl implements PushMsgFlagService {

	@Autowired
	private PushMsgFlagDao pushMsgFlagDao;
	
	
	@Override
	public PushMsgFlag getById(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(PushMsgFlag arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insert(PushMsgFlag entity) {
		pushMsgFlagDao.insert(entity);
		
	}

	@Override
	public void update(PushMsgFlag entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(PushMsgFlag entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<PushMsgFlag> getByUserIds(PushMsgFlag condition) {
		// TODO Auto-generated method stub
		return pushMsgFlagDao.getByUserIds(condition);
	}

	@Override
	public void deleteByUserId(String userid) {
		pushMsgFlagDao.deleteByUserId(userid);
		
	}

	@Override
	public void deleteChannelByUserId(String userid) {
		pushMsgFlagDao.deleteChannelByUserId(userid);
	}

}
