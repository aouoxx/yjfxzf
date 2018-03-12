package com.supconit.kqfx.web.fxzf.msg.daos;

import java.util.List;

import hc.orm.BasicDao;

import com.supconit.kqfx.web.fxzf.msg.entities.Msg;

public interface MsgDao extends BasicDao<Msg,String> {

	public void deleteById(String id);
	
	void batchInert(List<Msg> msgs);
	
	public List<Msg> getByUserId(String id);
	
	public void deleteByUsefulDate(Msg condition);

	public void deleteByUserId(Msg msg);
	
}
