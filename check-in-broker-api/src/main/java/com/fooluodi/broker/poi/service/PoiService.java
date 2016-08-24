package com.fooluodi.broker.poi.service;

import com.fooluodi.broker.poi.bo.POI;

/**
 * Created by di on 24/8/2016.
 */
public interface PoiService {
    /**
     *
     * @param poi
     */
    void addPoi(POI poi);

    /**
     * 获取随机一个poi
     * @return
     */
    POI getRandomPoi();

    /**
     * 从db中导入到缓存
     */
    void syncPoiFromDb();
}

