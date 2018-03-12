package com.supconit.kqfx.web.fxzf.search.services.Impl;

import hc.base.domains.AjaxMessage;
import hc.base.domains.Pageable;
import hc.orm.AbstractBasicOrmService;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supconit.kqfx.web.fxzf.search.daos.IllegalTrailDao;
import com.supconit.kqfx.web.fxzf.search.entities.IllegalTrail;
import com.supconit.kqfx.web.fxzf.search.services.IllegalTrailService;
import com.supconit.kqfx.web.fxzf.warn.entities.Config;
import com.supconit.kqfx.web.fxzf.warn.services.ConfigService;
import com.supconit.kqfx.web.util.IDGenerator;
import com.supconit.kqfx.web.util.SysConstants;

@Service("taizhou_offsite_enforcement_searchIllegalTrail_service")
public class IllegalTrailServiceImpl extends AbstractBasicOrmService<IllegalTrail, String> implements IllegalTrailService {

	
	@Autowired
	private IllegalTrailDao illegalTrailDao;
	
	@Autowired
	private ConfigService configService;
	
	@Override
	public Pageable<IllegalTrail> findByPager(Pageable<IllegalTrail> pager,
			IllegalTrail condition) {
		// TODO Auto-generated method stub
		return illegalTrailDao.findByPager(pager, condition);
	}

	@Override
	public IllegalTrail getById(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insert(IllegalTrail entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(IllegalTrail entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(IllegalTrail entity) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public AjaxMessage syncIllegalTrail(String license, String plateColor) {
		try {
			IllegalTrail trail = this.findByLicenseAndColor(license, plateColor);
			if(null!=trail){
				trail.setOverLoadTimes(trail.getOverLoadTimes()+1);
				trail.setUpdateTime(new Date());
				this.illegalTrailDao.update(trail);
			}else{
				trail = new IllegalTrail();
				trail.setId(IDGenerator.idGenerator());
				trail.setLicense(license);
				trail.setPlateColor(plateColor);
				trail.setOverLoadTimes(1);
				trail.setUpdateTime(new Date());
				trail.setDeleted(0);
				this.illegalTrailDao.insert(trail);
			}
			return AjaxMessage.success("同步成功");
		} catch (Exception e) {
			e.printStackTrace();
			return AjaxMessage.error("同步失败");
		}
		
	}

	@Override
	public IllegalTrail findByLicenseAndColor(String license, String plateColor) {
		
		return this.illegalTrailDao.findByLicenseAndColor(license,plateColor);
	}

	/**
	 * 根据车牌和颜色获取违章过车轨迹实体中违章次数是否超过黑名单的阈值
	 */
	@Override
	public Boolean IsAccordBlackList(String license, String plateColor) {
		IllegalTrail trail = this.findByLicenseAndColor(license, plateColor);
		if(null!=trail){
			Config config = this.configService.getByCode(SysConstants.BLACK_LIST_OVERLOADS);
			return trail.getOverLoadTimes()>config.getValue()?true:false;
		}else{
			return false;
		}
	}

	@Override
	public List<IllegalTrail> findByOverLoadTimes(long counts) {
		// TODO Auto-generated method stub
		return illegalTrailDao.findByOverLoadTimes(counts);
	}

	@Override
	public void deleteById(String id) {
		illegalTrailDao.deleteById(id);
		
	}

}
