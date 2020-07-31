package com.example.testurl;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "idAlerm";
    private Notification notification;
    private NotificationManager manager;
    private Runnable runnable;
    private Handler handler = new Handler( );
    public static String Authentication;
    private final static String TAG = "JSON test";
    String result;
    String APIUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton getBtn = findViewById(R.id.fab);
        getBtn.setOnClickListener(getFromServer);

        runnable = new Runnable( ) {
            public void run ( ) {
                EditText et = findViewById(R.id.api_url);
                APIUrl = et.getText().toString();

                Thread thread = new Thread(multiThread);
                thread.start();
                handler.postDelayed(this,1000);
            }
        };

        handler.postDelayed(runnable,1000);
    }

    Button.OnClickListener getFromServer = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            handler.postDelayed(runnable,1000);
        }
    };

    private Runnable multiThread = new Runnable() {

        int flag;
        String upload_time;

        @Override
        public void run() {
            String url = APIUrl;
            GetServer gs = new GetServer();
            result = gs.doInBackground(url);

            //JSON
            try{
                JSONObject jsonObject = new JSONObject(result);
                flag = jsonObject.getInt("Count");
                upload_time = jsonObject.getString("Upload time");
                if(flag>0){
                    Log.d(TAG, "help");
                    // notice
                    noti();
                }
            }
            catch(JSONException e) {
                e.printStackTrace();
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView tv = findViewById(R.id.result);
                    tv.setText(Integer.toString(flag));

                    TextView ut = findViewById(R.id.time);
                    ut.setText(upload_time);
                }
            });
        }
    };

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void noti() {
        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setContentTitle("Alerm")
                .setContentText("warning")
                .setPriority(NotificationCompat.PRIORITY_MAX);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        notificationManager.notify(0, builder.build());
    }
}