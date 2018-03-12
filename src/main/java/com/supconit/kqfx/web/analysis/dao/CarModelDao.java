package com.supconit.kqfx.web.analysis.dao;

import java.util.List;

import com.supconit.kqfx.web.analysis.entities.CarModel;

import hc.orm.BasicDao;

public interface CarModelDao extends BasicDao<CarModel, String> {

	List<CarModel> AnalysisByCondition(CarModel condition);

}
