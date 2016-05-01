package xyz.stepsecret.simulatorev;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import retrofit.RestAdapter;
import xyz.stepsecret.simulatorev.Waypiont.WaypiontData;

public class MainActivity extends AppCompatActivity {

    public static String API = "https://stepsecret.xyz";

    public static RestAdapter restAdapter;

    private Button btn;

    private EditText edt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.button);

        edt = (EditText) findViewById(R.id.editText);

        restAdapter = new RestAdapter.Builder()
                .setEndpoint(API).build();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String flow = edt.getText().toString();

                if(flow.length()>0)
                {
                    WaypiontData.GetWaypiont(flow);
                }



            }
        });

    }
}
