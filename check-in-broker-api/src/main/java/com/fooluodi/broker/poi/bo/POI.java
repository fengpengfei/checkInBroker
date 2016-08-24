package com.fooluodi.broker.poi.bo;

import com.fooluodi.broker.util.validate.annotation.NotNull;

import java.math.BigDecimal;

/**
 * Created by di on 21/8/2016.
 */
public class POI {
    //维度
    @NotNull
    private BigDecimal lat;

    //经度
    @NotNull
    private BigDecimal lon;

    public POI(BigDecimal lat, BigDecimal lon) {
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof POI)) return false;

        POI poi = (POI) o;

        if (lat != null ? !lat.equals(poi.lat) : poi.lat != null) return false;
        return lon != null ? lon.equals(poi.lon) : poi.lon == null;

    }

    @Override
    public int hashCode() {
        int result = lat != null ? lat.hashCode() : 0;
        result = 31 * result + (lon != null ? lon.hashCode() : 0);
        return result;
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
