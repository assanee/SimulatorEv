package xyz.stepsecret.simulatorev;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by stepsecret on 10/4/2559.
 */
public class Find {


    public Boolean overdistance = false;
    public Double Newlong = 0.0;
    public Double NewLat = 0.0;

    public void NewCoordinate(double Lat_F, double Long_F, double Lat_S, double Long_S, double distance)
    {

        Double Distancetemp = calculateDistance(Lat_F,Long_F,Lat_S,Long_S);

        if(Distancetemp > distance)
        {
            overdistance = false;

            if(Lat_F<Lat_S)
            {

                NewLat = Lat_F+(double) ((distance*(Lat_S-Lat_F))/Distancetemp);

            }
            else
            {

                NewLat = Lat_F-(double) ((distance*(Lat_F-Lat_S))/Distancetemp);

            }

            if(Long_F<Long_S)
            {

                Newlong = Long_F+(double) ((distance*(Long_S-Long_F))/Distancetemp);

            }
            else
            {

                Newlong = Long_F-(double) ((distance*(Long_F-Long_S))/Distancetemp);

            }

        }
        else
        {
            overdistance = true;


        }


    }

    public Double finddistance(double Lat_F, double Long_F, double Lat_S, double Long_S)
    {

        return calculateDistance(Lat_F,Long_F,Lat_S,Long_S);
    }

    public Double getLatitude()
    {
        return NewLat;
    }

    public Double getLongitude()
    {
        return Newlong;
    }

    Double calculateDistance(double userLat, double userLng,double venueLat, double venueLng) {

        double latDistance = Math.toRadians(userLat - venueLat);
        double lngDistance = Math.toRadians(userLng - venueLng);

        double a = Math.sin(latDistance / 2.0) * Math.sin(latDistance / 2.0)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(venueLat))
                * Math.sin(lngDistance / 2.0) * Math.sin(lngDistance / 2.0);

        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));

        return 6371000.0 * c;
    }

    public double Area(ArrayList<Double> lats, ArrayList<Double> lons)
    {
        double sum=0;
        double prevcolat=0;
        double prevaz=0;
        double colat0=0;
        double az0=0;
        for (int i=0;i<lats.size();i++)
        {
            double colat=2*Math.atan2(Math.sqrt(Math.pow(Math.sin(lats.get(i)*Math.PI/180/2), 2)+ Math.cos(lats.get(i)*Math.PI/180)*Math.pow(Math.sin(lons.get(i)*Math.PI/180/2), 2)),Math.sqrt(1-  Math.pow(Math.sin(lats.get(i)*Math.PI/180/2), 2)- Math.cos(lats.get(i)*Math.PI/180)*Math.pow(Math.sin(lons.get(i)*Math.PI/180/2), 2)));
            double az=0;
            if (lats.get(i)>=90)
            {
                az=0;
            }
            else if (lats.get(i)<=-90)
            {
                az=Math.PI;
            }
            else
            {
                az=Math.atan2(Math.cos(lats.get(i)*Math.PI/180) * Math.sin(lons.get(i)*Math.PI/180),Math.sin(lats.get(i)*Math.PI/180))% (2*Math.PI);
            }
            if(i==0)
            {
                colat0=colat;
                az0=az;
            }
            if(i>0 && i<lats.size())
            {
                sum=sum+(1-Math.cos(prevcolat  + (colat-prevcolat)/2))*Math.PI*((Math.abs(az-prevaz)/Math.PI)-2*Math.ceil(((Math.abs(az-prevaz)/Math.PI)-1)/2))* Math.signum(az-prevaz);
            }
            prevcolat=colat;
            prevaz=az;
        }
        sum=sum+(1-Math.cos(prevcolat  + (colat0-prevcolat)/2))*(az0-prevaz);
        return 5.10072E14* Math.min(Math.abs(sum)/4/Math.PI,1-Math.abs(sum)/4/Math.PI);
    }



}
