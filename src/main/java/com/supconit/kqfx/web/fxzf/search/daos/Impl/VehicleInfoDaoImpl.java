package com.supconit.kqfx.web.fxzf.search.daos.Impl;

import com.supconit.kqfx.web.fxzf.search.daos.VehicleInfoDao;
import com.supconit.kqfx.web.fxzf.search.entities.VehicleInfo;
import hc.base.domains.Pageable;
import hc.base.domains.Pagination;
import hc.orm.AbstractBasicDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author cjm
 * @create 2015-06-10 17:47:09
 */

@Repository
public class VehicleInfoDaoImpl extends AbstractBasicDaoImpl<VehicleInfo, String> implements VehicleInfoDao {

    private static final String NAMESPACE = VehicleInfo.class.getName();

    @Override
    protected String getNamespace() {
        return NAMESPACE;
    }

    @Override
    public List<VehicleInfo> getByPlaneNo(String planeNo) {
        return selectList("getByPlaneNo", planeNo);
    }

    @Override
    public List<VehicleInfo> findByPlaneNos(String planeNos) {
        return selectList("findByPlaneNos", planeNos);
    }

}
