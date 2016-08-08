package com.fooluodi.broker.operation.log.dao;

import com.fooluodi.broker.operation.log.po.Log;

import java.util.List;

public interface LogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Log record);

    int insertSelective(Log record);

    Log selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Log record);

    int updateByPrimaryKeyWithBLOBs(Log record);

    int updateByPrimaryKey(Log record);

    List<Log> getAll();
}