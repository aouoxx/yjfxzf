package com.supconit.kqfx.web.analysis.services.impl;

import java.util.List;

import hc.orm.AbstractBasicOrmService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.supconit.kqfx.web.analysis.dao.CarModelDao;
import com.supconit.kqfx.web.analysis.entities.CarModel;
import com.supconit.kqfx.web.analysis.services.CarModelService;

@Service("analysis_carmodel_service")
public class CarModelServiceImpl extends AbstractBasicOrmService<CarModel, String>
		implements CarModelService {

	@Autowired
	private CarModelDao carModelDao; 
	
	@Override
	public CarModel getById(String id) {
		
		return this.carModelDao.getById(id);
	}
	
	@Override
	public void insert(CarModel entity) {
		
		this.carModelDao.insert(entity);
	}

	@Override
	public void update(CarModel entity) {
		
		this.carModelDao.update(entity);
	}

	@Override
	public void delete(CarModel entity) {
		
		this.carModelDao.delete(entity);
	}

	@Override
	public List<CarModel> AnalysisByCondition(CarModel condition) {
		
		return this.carModelDao.AnalysisByCondition(condition);
	}

}
