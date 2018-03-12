package com.supconit.kqfx.web.analysis.services;

import java.util.List;

import com.supconit.kqfx.web.analysis.entities.CarModel;

import hc.orm.BasicOrmService;

public interface CarModelService extends BasicOrmService<CarModel, String> {
	
	/**
	 * 根据条件进行车型统计
	 * @param condition
	 * @return
	 */
	List<CarModel> AnalysisByCondition(CarModel condition);

}
