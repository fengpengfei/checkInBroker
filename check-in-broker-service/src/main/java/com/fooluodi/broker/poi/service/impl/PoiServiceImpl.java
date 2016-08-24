package com.fooluodi.broker.poi.service.impl;

import com.fooluodi.broker.poi.bo.POI;
import com.fooluodi.broker.poi.cache.PoiCache;
import com.fooluodi.broker.poi.dao.PoiPoMapper;
import com.fooluodi.broker.poi.po.PoiPo;
import com.fooluodi.broker.poi.service.PoiService;
import com.fooluodi.broker.util.validate.function.ValidateHelper;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by di on 24/8/2016.
 */
@Service
public class PoiServiceImpl implements PoiService {
    private static final Logger logger = LoggerFactory.getLogger(PoiServiceImpl.class);

    private static final POI DEFAULT_POI = new POI(
            BigDecimal.valueOf(121.417819D),
            BigDecimal.valueOf(31.218425D)
    );

    @Resource
    private PoiPoMapper poiPoMapper;

    @Override
    public void addPoi(POI poi) {
        logger.info("add poi:{}", poi);

        ValidateHelper.validate(poi);

        PoiPo poiPo = new PoiPo();
        BeanUtils.copyProperties(poi, poiPo);

        int insert = poiPoMapper.insert(poiPo);
        logger.info("result :{}", insert);
    }

    @Override
    public POI getRandomPoi() {
        logger.info("get random poi");

        ArrayList<PoiPo> poiList = new ArrayList<>(PoiCache.INSTANCE.poiSet);
        Collections.shuffle(poiList);


        if (CollectionUtils.isEmpty(poiList)) {
            return DEFAULT_POI;
        }
        PoiPo poiPo = poiList.get(0);
        logger.info("poi:{}", poiPo);

        POI poi = new POI();
        poi.setLat(poiPo.getLatitude());
        poi.setLon(poiPo.getLongitude());

        return poi;

    }

    @Override
    public void syncPoiFromDb() {
        logger.info("sync poi");
        List<PoiPo> all = poiPoMapper.getALL();

        logger.info("poi sum:{}", all == null ? 0 : all.size());
        PoiCache.INSTANCE.poiSet.addAll(all);
    }
}
