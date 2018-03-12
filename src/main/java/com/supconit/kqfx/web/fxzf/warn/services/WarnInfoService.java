package com.supconit.kqfx.web.fxzf.warn.services;

import java.util.List;

import com.supconit.kqfx.web.fxzf.warn.entities.WarnInfo;

import hc.orm.BasicOrmService;

public interface WarnInfoService extends BasicOrmService<WarnInfo, String> {

	List<WarnInfo> getAll();

	WarnInfo getByTempletTypeAndStation(WarnInfo warnInfo);
}
