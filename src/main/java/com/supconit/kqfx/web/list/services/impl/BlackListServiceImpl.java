package com.supconit.kqfx.web.list.services.impl;

import java.util.Date;
import java.util.List;

import jodd.util.StringUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hc.base.domains.AjaxMessage;
import hc.base.domains.Pageable;
import hc.orm.AbstractBasicOrmService;

import com.supconit.kqfx.web.fxzf.avro.redis.ReadRedisService;
import com.supconit.kqfx.web.fxzf.avro.redis.WriteRedisService;
import com.supconit.kqfx.web.fxzf.search.entities.IllegalTrail;
import com.supconit.kqfx.web.fxzf.search.services.IllegalTrailService;
import com.supconit.kqfx.web.list.daos.BlackListDao;
import com.supconit.kqfx.web.list.entities.BlackList;
import com.supconit.kqfx.web.list.services.BlackListService;
import com.supconit.kqfx.web.util.IDGenerator;


@Service("list_blacklist_service")
public class BlackListServiceImpl extends AbstractBasicOrmService<BlackList, String>
		implements BlackListService {

	@Autowired
	private BlackListDao	blackListDao;
	
	@Autowired
	private WriteRedisService	writeRedisService; 
	
	@Autowired
	private ReadRedisService	readRedisService;
	
	@Autowired
	private IllegalTrailService	illegalTrailService;
	
	@Override
	public BlackList getById(String arg0) {
		
		return this.blackListDao.getById(arg0);
	}

	@Override
	public void save(BlackList arg0) {
		if(StringUtil.isEmpty(arg0.getId())) 
		{
			this.blackListDao.insert(arg0);
		}else{
			this.blackListDao.update(arg0);
		}
	}

	@Override
	public void insert(BlackList entity) {
		this.blackListDao.insert(entity);
	}

	@Override
	public void update(BlackList entity) {
		this.blackListDao.update(entity);
	}

	@Override
	public void delete(BlackList entity) {
		this.blackListDao.delete(entity);
	}

	@Override
	public List<BlackList> getAll() {
		
		return this.blackListDao.getAll();
	}

	@Override
	@Transactional
	public AjaxMessage syncBlackList(String license, String plateColor) {
		//获取车辆轨迹信息获取违法次数
		IllegalTrail trail = this.illegalTrailService.findByLicenseAndColor(license, plateColor);
		if(null!=trail){
			//设置更新内容
			BlackList blackList = new BlackList();
			blackList.setLicense(license);
			blackList.setPlateColor(plateColor);
			blackList.setUpdateTime(new Date());
			blackList.setOverloadTimes(Long.valueOf(trail.getOverLoadTimes()));
			//判断黑名单是否已存在，如已存在获取ID更新，如不存在。新增黑名单至业务库、redis
			if(this.writeRedisService.RedisExistsList(license, plateColor, 0))
			{
				blackList.setId(this.readRedisService.RedisValueList(license, plateColor, 0));
				this.blackListDao.update(blackList);
			}else{
				blackList.setDeleted(0);
				blackList.setId(IDGenerator.idGenerator());
				blackList.setAddByOperatorFlag(0);
				blackList.setIllustrate("自动添加");
				this.blackListDao.insert(blackList);
				this.writeRedisService.AddListToRedis(blackList.getId(), license, plateColor, 0);
			}
			return AjaxMessage.success("更新成功");
		}else{
			return AjaxMessage.error("更新失败");
		}
		
	}

	@Override
	public Pageable<BlackList> findByPager(Pageable<BlackList> pager,
			BlackList condition) {
		return blackListDao.findByPager(pager, condition);
	}

	@Override
	public void upDateByOverLoadTimes(BlackList condition) {
		blackListDao.upDateByOverLoadTimes(condition);
		
	}

	@Override
	public void deleteById(String id) {
		blackListDao.deleteById(id);
	}

	@Override
	public int findByPlate(BlackList condition) {
		// TODO Auto-generated method stub
		return blackListDao.findByPlate(condition);
	}

	@Override
	public void batchDeleteBlackList(List<String> ids) {
		blackListDao.batchDeleteBlackList(ids);
		
	}

	@Override
	public void batchInertBlackList(List<BlackList> lists) {
		blackListDao.batchInertBlackList(lists);
	}

	@Override
	public void deleteAllNoHandler() {
		blackListDao.deleteAllNoHandler();
		
	}

	@Override
	public List<BlackList> getAllHandler() {
		// TODO Auto-generated method stub
		return blackListDao.getAllHandler();
	}

	@Override
	public void updateHandlerOverTimes(BlackList condition) {
		blackListDao.updateHandlerOverTimes(condition);
	}

	@Override
	public BlackList findByPlateAndColor(BlackList condition) {
		// TODO Auto-generated method stub
		return blackListDao.findByPlateAndColor(condition);
	}

}
