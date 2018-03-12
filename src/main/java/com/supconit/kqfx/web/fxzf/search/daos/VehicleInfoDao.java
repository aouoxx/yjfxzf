package com.supconit.kqfx.web.fxzf.search.daos;

import com.supconit.kqfx.web.fxzf.search.entities.VehicleInfo;
import hc.base.domains.Pageable;
import hc.base.domains.Pagination;
import hc.orm.BasicDao;

import java.util.List;

public interface VehicleInfoDao extends BasicDao<VehicleInfo, String> {
	
	List<VehicleInfo> getByPlaneNo(String planeNo);

	/**
	 * 根据车牌号查询车辆信息
	 * 
	 * @param planeNos 车牌号集合，以逗号作为分割符拼接
	 * @return
	 */
	List<VehicleInfo> findByPlaneNos(String planeNos);
	
}
