package xyz.stepsecret.simulatorev.API;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import xyz.stepsecret.simulatorev.Model.Flow_Model;

/**
 * Created by Mylove on 14/1/2559.
 */
public interface Flow_API {

    @GET("/task_manager/v3")
    public void Get_Flow_API(@Query("flow") String flow, Callback<Flow_Model> response);

}
