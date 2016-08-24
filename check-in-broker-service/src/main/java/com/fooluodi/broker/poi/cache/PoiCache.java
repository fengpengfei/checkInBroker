package com.fooluodi.broker.poi.cache;

import com.fooluodi.broker.poi.po.PoiPo;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by di on 24/8/2016.
 */
public enum  PoiCache {
    INSTANCE;

    public Set<PoiPo> poiSet;

    PoiCache(){
        poiSet = new HashSet<>();
    }
}
