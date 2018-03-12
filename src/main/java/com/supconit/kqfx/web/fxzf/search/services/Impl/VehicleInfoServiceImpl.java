package com.supconit.kqfx.web.fxzf.search.services.Impl;

import com.supconit.kqfx.web.fxzf.search.daos.VehicleInfoDao;
import com.supconit.kqfx.web.fxzf.search.entities.VehicleInfo;
import com.supconit.kqfx.web.fxzf.search.services.VehicleInfoService;
import hc.base.domains.Pageable;
import hc.base.domains.Pagination;
import hc.orm.AbstractBasicOrmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author cjm
 * @create 2015-06-10 16:47:09
 * @since
 * 
 */
@Service
public class VehicleInfoServiceImpl extends AbstractBasicOrmService<VehicleInfo, String> implements VehicleInfoService {
	
	@Autowired
	private VehicleInfoDao vehicleInfoDao;
	
	@Override
	@Transactional
	public VehicleInfo getById(String arg0) {
		return this.vehicleInfoDao.getById(arg0);
	}

	@Override
	@Transactional
	public void insert(VehicleInfo entity) {
		this.vehicleInfoDao.insert(entity);
	}

	@Override
	@Transactional
	public void update(VehicleInfo entity) {
		this.vehicleInfoDao.update(entity);
	}

	@Override
	@Transactional
	public void delete(VehicleInfo entity) {
		this.vehicleInfoDao.delete(entity);
	}

	@Override
	public VehicleInfo getByPlaneNo(String planeNo) {
		List<VehicleInfo> list = this.vehicleInfoDao.getByPlaneNo(planeNo);
		if(!CollectionUtils.isEmpty(list))
			return list.get(0);
		else
			return null;	
	}

	@Override
	public List<VehicleInfo> findByPlaneNos(String planeNos) {
		return this.vehicleInfoDao.findByPlaneNos(planeNos);
	}
	
}
