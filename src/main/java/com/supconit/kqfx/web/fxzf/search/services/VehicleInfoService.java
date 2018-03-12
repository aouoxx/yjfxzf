package com.supconit.kqfx.web.fxzf.search.services;

import com.supconit.kqfx.web.fxzf.search.entities.VehicleInfo;
import hc.base.domains.Pageable;
import hc.base.domains.Pagination;

import java.util.List;


/**
 * 车辆信息service
 *
 */
public interface VehicleInfoService extends hc.orm.BasicOrmService<VehicleInfo, String> {
   
	/**
	 * 根据车牌号获取车辆信息、业户信息
	 * @param planeNo
	 * @return
	 */
	VehicleInfo getByPlaneNo(String planeNo);

	/**
	 * 根据车牌号查询车辆信息
	 * 
	 * @param planeNos 车牌号集合，以逗号作为分割符拼接
	 * @return
	 */
	List<VehicleInfo> findByPlaneNos(String planeNos);
	
}
