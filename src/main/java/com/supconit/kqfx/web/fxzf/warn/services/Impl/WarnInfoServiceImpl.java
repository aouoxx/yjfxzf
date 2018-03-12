package com.supconit.kqfx.web.fxzf.warn.services.Impl;

import java.util.List;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hc.orm.AbstractBasicOrmService;

import com.supconit.kqfx.web.fxzf.warn.daos.WarnInfoDao;
import com.supconit.kqfx.web.fxzf.warn.entities.WarnInfo;
import com.supconit.kqfx.web.fxzf.warn.services.WarnInfoService;

@Service("fxzf_warn_warninfo_service")
public class WarnInfoServiceImpl extends AbstractBasicOrmService<WarnInfo, String>
		implements WarnInfoService {

	@Autowired
	private WarnInfoDao warnInfoDao;
	
	@Override
	public WarnInfo getById(String arg0) {
		
		return this.warnInfoDao.getById(arg0);
	}

	@Override
	public void save(WarnInfo entity) {
		
		if(StringUtil.isEmpty(entity.getId()))
		{
			this.warnInfoDao.insert(entity);
		}else{
			this.warnInfoDao.update(entity);
		}
	}

	@Override
	public void insert(WarnInfo entity) {
		this.warnInfoDao.insert(entity);

	}

	@Override
	public void update(WarnInfo entity) {
		this.warnInfoDao.update(entity);

	}

	@Override
	public void delete(WarnInfo entity) {
		this.delete(entity);

	}

	@Override
	public List<WarnInfo> getAll() {
		
		return this.warnInfoDao.getAll();
	}

//	@Override
//	public WarnInfo getByTempletType(String templetType) {
//		
//		return this.warnInfoDao.getByTempletType(templetType);
//	}

	@Override
	public WarnInfo getByTempletTypeAndStation(WarnInfo warnInfo) {
		// TODO Auto-generated method stub
		return this.warnInfoDao.getByTempletTypeAndStation(warnInfo);
	}

}
