package com.fooluodi.broker.worker;

import com.fooluodi.broker.poi.cache.PoiCache;
import com.fooluodi.broker.poi.service.PoiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by di on 24/8/2016.
 */
@Component
public class PoiSyncWorker {
    private static final Logger logger = LoggerFactory.getLogger(PoiSyncWorker.class);

    @Resource
    private PoiService poiService;

    public void sync() {
        long start = System.currentTimeMillis();
        logger.info("poi sync worker begin!time:{}", start);

        try {
            poiService.syncPoiFromDb();
        } catch (Exception e) {
            logger.error("sync failed", e);
        }

        logger.info("now poi list cache:{}", PoiCache.INSTANCE.poiSet);

        logger.info("done. last:{} ms", System.currentTimeMillis() - start);
    }
}
