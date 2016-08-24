package com.fooluodi.broker.poi.dao;

import com.fooluodi.broker.poi.po.PoiPo;

import java.util.List;

public interface PoiPoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PoiPo record);

    int insertSelective(PoiPo record);

    PoiPo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PoiPo record);

    int updateByPrimaryKey(PoiPo record);

    List<PoiPo> getALL();
}