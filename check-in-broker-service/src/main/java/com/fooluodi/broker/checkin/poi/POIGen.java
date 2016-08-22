package com.fooluodi.broker.checkin.poi;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by di on 21/8/2016.
 */
public class POIGen {
    private static final Map<Double, Double> poiMaps = new HashMap();
    static {
        poiMaps.put(31.218542, 121.417997);
    }

    private static final List<POI> poiList = new ArrayList<>();
    static {
        for (Map.Entry<Double, Double> entry : poiMaps.entrySet()) {
            poiList.add(new POI(BigDecimal.valueOf(entry.getKey()), BigDecimal.valueOf(entry.getValue())));
        }
    }

    public static POI randomPoi(){
        Random random = new Random();
        int anInt = random.nextInt(poiList.size());
        return poiList.get(anInt);
    }
}
