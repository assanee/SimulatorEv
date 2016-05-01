package xyz.stepsecret.simulatorev.Waypiont;

import android.location.Location;
import android.os.Environment;
import android.util.Log;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import xyz.stepsecret.simulatorev.API.Flow_API;
import xyz.stepsecret.simulatorev.MainActivity;
import xyz.stepsecret.simulatorev.Model.Flow_Model;

/**
 * Created by Mylove on 25/12/2558.
 */
public class WaypiontData {

    // Size = 533 OR 0 - 532

    public static Double[] latitude;

    public static Double[] longitude;

    public static Double[] distance;

    public static int[] State;

    public static Boolean overdistance = false;

    public static Double Newlong = 0.0;

    public static Double NewLat = 0.0;

    public static CSVWriter new_writer = null;

    public static List<String[]> new_data = new ArrayList<String[]>();

    public static String nameFile = "";

    public static ArrayList<Double> latitudeNEW = new ArrayList<Double>();

    public static ArrayList<Double> longitudeNEW = new ArrayList<Double>();

    public static void GetWaypiont(final String flow)
    {

        nameFile = flow;


        final Flow_API flow_API = MainActivity.restAdapter.create(Flow_API.class);
        flow_API.Get_Flow_API(
                flow,
                new Callback<Flow_Model>() {
                    @Override
                    public void success(Flow_Model flow_Model, Response response) {

                        latitude = flow_Model.getLatitude();
                        longitude = flow_Model.getLongitude();
                        distance = flow_Model.getDistance();
                        State = flow_Model.getState();

                       // for(int i = 0 ; i < latitude.length ; i++)
                       // {
                       //  Log.e("TAG", "WaypiontData.latitude "+WaypiontData.latitude[i]);
                       // }

                        Find_new();

                    }

                    @Override
                    public void failure(RetrofitError error) {


                        GetWaypiont(flow);

                    }
                });
    }


public static void Find_new()
{
    Double dis = 5.0;
    Double disALL = 0.0;
    Double latitudeF = 0.0;
    Double longitudeF = 0.0;
    Double latitudeS = 0.0;
    Double longitudeS = 0.0;



    latitudeF = latitude[0];
    longitudeF = longitude[0];
    latitudeS = latitude[1];
    longitudeS = longitude[1];

    latitudeNEW.add(latitudeF);
    longitudeNEW.add(longitudeF);

    for(int i = 2 ; i < latitude.length ; i++)
    {
        Double tempD = calculateDistance(latitudeF,  longitudeF, latitudeS,  longitudeS);
        if(tempD > dis)
        {
            NewCoordinate(latitudeF,  longitudeF, latitudeS,  longitudeS,dis);

            latitudeNEW.add(NewLat);
            longitudeNEW.add(Newlong);

            latitudeF = NewLat;
            longitudeF = Newlong;


            i--;

            disALL = disALL + dis;
        }
        else
        {


            if(latitude.length > i+1)
            {
                //Log.e("TAG", "latitude.length : "+latitude.length+" i+1 : "+i+1);
                latitudeS = latitude[i+1];
                longitudeS = longitude[i+1];
            }



        }


    }

    writeCSV();


}

    public static void writeCSV()
    {
        try {
            File folder = new File(Environment.getExternalStorageDirectory() + "/BUSSTOP");
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdir();
            }
            if (success) {
                // Do something on success
            } else {
                // Do something else on failure
            }

            new_writer = new CSVWriter(new FileWriter(Environment.getExternalStorageDirectory() +  "/" +"BUSSTOP"+"/" + "BUS_STOP_"+nameFile+".csv"));
            new_data.add(new String[] {"number", "state", "latitude", "longitude", "distance","flow"});

            Double number = 0.0;
            int state = 0;
            new_data.add(new String[] {"0", "0", ""+latitudeNEW.get(0), ""+longitudeNEW.get(0), ""+number, nameFile});

            for (int i = 1; i < latitudeNEW.size(); i++)
            {
                number = number + calculateDistance(latitudeNEW.get(i-1),  longitudeNEW.get(i-1),latitudeNEW.get(i),  longitudeNEW.get(i));

                if( i%50 == 0)
                {
                    state++;
                    new_data.add(new String[] {""+i, ""+state, ""+latitudeNEW.get(i), ""+longitudeNEW.get(i), ""+number, nameFile});
                }
                else
                {
                    new_data.add(new String[] {""+i, ""+state, ""+latitudeNEW.get(i), ""+longitudeNEW.get(i), ""+number,nameFile});
                }

                //Log.e("TAG", "i : "+i+" latitudeNEW.get(i-1) : "+latitudeNEW.get(i-1)+" longitudeNEW.get(i-1) : "+longitudeNEW.get(i-1));
                // Log.e("TAG", "i : "+i+" latitudeNEW.get(i) : "+latitudeNEW.get(i)+" longitudeNEW.get(i-1) : "+longitudeNEW.get(i));
                // Log.e("TAG", "i : "+i+" dis : "+number);

            }
            //Log.e("TAG", " latitude.length : "+latitude.length);
            //Log.e("TAG", " dis : "+number);
            //Log.e("TAG", "disALL : "+disALL);


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        add();
    }

    public static void add()
    {
        new_writer.writeAll(new_data);
        try {
            new_writer.close();
            Log.e("TAG", "new_writer.close(); ");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        new_data.clear();
        Log.e("TAG", "write success ");
        Log.e("TAG", "latitude.length "+latitude.length);
    }

  public static Double calculateDistance(double userLat, double userLng, double venueLat, double venueLng) {

        double latDistance = Math.toRadians(userLat - venueLat);
        double lngDistance = Math.toRadians(userLng - venueLng);

        double a = Math.sin(latDistance / 2.0) * Math.sin(latDistance / 2.0)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(venueLat))
                * Math.sin(lngDistance / 2.0) * Math.sin(lngDistance / 2.0);

        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));

        return 6371000.0 * c;
    }

    public static void NewCoordinate(double Lat_F, double Long_F, double Lat_S, double Long_S, double distance)
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


}
