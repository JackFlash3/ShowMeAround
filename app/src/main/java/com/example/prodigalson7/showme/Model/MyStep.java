package com.example.prodigalson7.showme.Model;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by ProdigaLsON7 on 03/04/2018.
 */

public class MyStep {
    LatLng start_point;
    LatLng end_point;

    public MyStep(LatLng start_point, LatLng end_point) {
        this.start_point = start_point;
        this.end_point = end_point;
    }

    public LatLng getStart_point() {
        return start_point;
    }

    public void setStart_point(LatLng start_point) {
        this.start_point = start_point;
    }

    public LatLng getEnd_point() {
        return end_point;
    }

    public void setEnd_point(LatLng end_point) {
        this.end_point = end_point;
    }
}
