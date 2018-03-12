package com.supconit.kqfx.web.fxzf.warn.daos;

import java.util.List;

import com.supconit.kqfx.web.fxzf.warn.entities.WarnInfo;

import hc.orm.BasicDao;

public interface WarnInfoDao extends BasicDao<WarnInfo, String> {

	List<WarnInfo> getAll();

	WarnInfo getByTempletTypeAndStation(WarnInfo warnInfo);

}
