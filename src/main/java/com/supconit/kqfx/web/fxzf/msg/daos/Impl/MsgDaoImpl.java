package com.supconit.kqfx.web.fxzf.msg.daos.Impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import hc.orm.AbstractBasicDaoImpl;

import com.supconit.kqfx.web.fxzf.msg.daos.MsgDao;
import com.supconit.kqfx.web.fxzf.msg.entities.Msg;


@Repository("taizhou_offsite_enforcement_msg_dao")
public class MsgDaoImpl extends AbstractBasicDaoImpl<Msg, String> implements MsgDao{

	
	private static final String NAMESPACE=Msg.class.getName();
	
	@Override
	protected String getNamespace() {
		// TODO Auto-generated method stub
		return NAMESPACE;
	}

	@Override
	public void batchInert(List<Msg> msgs) {
		insert("batchInert", msgs);
	}

	@Override
	public void deleteById(String id) {
		delete("deleteById", id);
		
	}

	@Override
	public List<Msg> getByUserId(String id) {
		// TODO Auto-generated method stub
		return selectList("getByUserId", id);
	}

	@Override
	public void deleteByUsefulDate(Msg condition) {
		delete("deleteByUsefulDate", condition);
		
	}

	@Override
	public void deleteByUserId(Msg msg) {
		delete("deleteByUserId",msg);
		
	}
	
	
	

}
