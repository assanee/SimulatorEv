package xyz.stepsecret.simulatorev.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mylove on 14/1/2559.
 */
public class Flow_Model {

   @SerializedName("latitude")
    private Double[] latitude ;

    @SerializedName("longitude")
    private Double[] longitude ;

    @SerializedName("distance")
    private Double[] distance ;

    @SerializedName("state")
    private int[] state ;


    public Double[] getLatitude() {
        return latitude;
    }

    public Double[] getLongitude() {
        return longitude;
    }

    public Double[] getDistance() {
        return distance;
    }

    public int[] getState() {
        return state;
    }


}
