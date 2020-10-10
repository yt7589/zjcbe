package com.zhuanjingkj.zjcbe.repository.analysis;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhuanjingkj.zjcbe.dbengine.annotation.SingleDataSource;
import com.zhuanjingkj.zjcbe.dbengine.datasource.DBBusinessType;
import com.zhuanjingkj.zjcbe.dbengine.datasource.DBVisitType;
import com.zhuanjingkj.zjcbe.domain.entity.VehicleAnalysisHptzEntity;
import com.zhuanjingkj.zjcbe.domain.mapper.analysis.VehicleAnalysisHptzMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VehicleAnalysisHptzRepository extends ServiceImpl<VehicleAnalysisHptzMapper, VehicleAnalysisHptzEntity> {

    @SingleDataSource(businessType = DBBusinessType.BUS_LINE, visitType = DBVisitType.WRITE)
    public boolean batchAdd(List<VehicleAnalysisHptzEntity> entity) {
        return this.insertBatch(entity);
    }
}
