package com.supconit.kqfx.web.xtgl.daos;

import java.util.List;

import com.supconit.kqfx.web.xtgl.entities.Jgxx;

public interface JgxxDao extends hc.orm.BasicDao<Jgxx, Long> {
	
	/**
	 * 
	 * @param condition
	 * @return
	 */
	List<Jgxx> selectAllJgxx(Jgxx condition);

}
