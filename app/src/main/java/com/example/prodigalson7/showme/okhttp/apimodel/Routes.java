
package com.example.prodigalson7.showme.okhttp.apimodel;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Routes {

    @SerializedName("geocoded_waypoints")
    @Expose
    private List<GeocodedWaypoint> geocodedWaypoints = null;
    @SerializedName("routes")
    @Expose
    public List<Route> routes = new ArrayList<Route>();
    @SerializedName("status")
    @Expose
    private String status;

    public List<GeocodedWaypoint> getGeocodedWaypoints() {
        return geocodedWaypoints;
    }

    public void setGeocodedWaypoints(List<GeocodedWaypoint> geocodedWaypoints) {
        this.geocodedWaypoints = geocodedWaypoints;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
