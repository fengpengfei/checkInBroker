package com.fooluodi.broker.poi.service.impl;

import com.fooluodi.broker.poi.bo.POI;
import com.fooluodi.broker.poi.cache.PoiCache;
import com.fooluodi.broker.poi.dao.PoiPoMapper;
import com.fooluodi.broker.poi.po.PoiPo;
import com.fooluodi.broker.poi.service.PoiService;
import com.fooluodi.broker.util.validate.function.ValidateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;

/**
 * Created by di on 24/8/2016.
 */
@Service
public class PoiServiceImpl implements PoiService {
    private static final Logger logger = LoggerFactory.getLogger(PoiServiceImpl.class);

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

        int size = PoiCache.INSTANCE.poiSet.size();

        if (size == 0)
            return null;

        Random random = new Random();
        int i = random.nextInt(size);

        int flag = 0;
        for (PoiPo poi : PoiCache.INSTANCE.poiSet) {
            if (flag == i){
                POI result = new POI();
                BeanUtils.copyProperties(poi, result);
                logger.info("get!{}", result);
                return result;
            }
        }

        return null;
    }

    @Override
    public void syncPoiFromDb() {
        logger.info("sync poi");
        List<PoiPo> all = poiPoMapper.getALL();

        logger.info("poi sum:{}", all == null ? 0 : all.size());
        PoiCache.INSTANCE.poiSet.addAll(all);
    }
}
