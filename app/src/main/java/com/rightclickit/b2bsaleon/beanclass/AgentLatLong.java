package com.rightclickit.b2bsaleon.beanclass;

import java.io.Serializable;

/**
 * Created by venkat on 8/9/17.
 */

public class AgentLatLong implements Serializable {
    private String agentName;
    private Double latitude;
    private Double longitude;
    private Double distance;

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "AgentLatLong{" +
                "agentName='" + agentName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", distance=" + distance +
                '}';
    }
}
