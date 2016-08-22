package com.fooluodi.broker.checkin.poi;

import java.math.BigDecimal;

/**
 * Created by di on 21/8/2016.
 */
public class POI {
    //维度
    private BigDecimal lat;
    //经度
    private BigDecimal lon;

    public POI(BigDecimal lat, BigDecimal lon) {
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("POI{");
        sb.append("lat=").append(lat);
        sb.append(", lon=").append(lon);
        sb.append('}');
        return sb.toString();
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLon() {
        return lon;
    }

    public void setLon(BigDecimal lon) {
        this.lon = lon;
    }
}
