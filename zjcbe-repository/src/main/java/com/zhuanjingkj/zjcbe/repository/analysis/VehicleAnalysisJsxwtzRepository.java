package com.zhuanjingkj.zjcbe.repository.analysis;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhuanjingkj.zjcbe.dbengine.annotation.SingleDataSource;
import com.zhuanjingkj.zjcbe.dbengine.datasource.DBBusinessType;
import com.zhuanjingkj.zjcbe.dbengine.datasource.DBVisitType;
import com.zhuanjingkj.zjcbe.domain.entity.VehicleAnalysisJsxwtzEntity;
import com.zhuanjingkj.zjcbe.domain.mapper.analysis.VehicleAnalysisJsxwtzMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VehicleAnalysisJsxwtzRepository extends ServiceImpl<VehicleAnalysisJsxwtzMapper, VehicleAnalysisJsxwtzEntity> {

    @SingleDataSource(businessType = DBBusinessType.BUS_LINE, visitType = DBVisitType.WRITE)
    public boolean batchAdd(List<VehicleAnalysisJsxwtzEntity> entity) {
        return this.insertBatch(entity);
    }
}
