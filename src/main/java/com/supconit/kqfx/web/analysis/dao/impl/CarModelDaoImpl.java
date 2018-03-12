package com.supconit.kqfx.web.analysis.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import hc.orm.AbstractBasicDaoImpl;

import com.supconit.kqfx.web.analysis.dao.CarModelDao;
import com.supconit.kqfx.web.analysis.entities.CarModel;

@Repository("analysis_carmodel_dao")
public class CarModelDaoImpl extends AbstractBasicDaoImpl<CarModel, String> implements
		CarModelDao {
	
	private static final String NAMESPACE = CarModel.class.getName();
	
	@Override
	protected String getNamespace() {
		return NAMESPACE;
	}

	@Override
	public List<CarModel> AnalysisByCondition(CarModel condition) {
		
		return selectList("AnalysisByCondition", condition);
	}


}
