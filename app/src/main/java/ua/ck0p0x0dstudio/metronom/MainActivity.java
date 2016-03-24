package ua.ck0p0x0dstudio.metronom;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.app.Notification;
import android.app.NotificationManager;

public class MainActivity extends AppCompatActivity {
    final String LOG_TAG = "myLogs";

    EditText bpm;

    CustomSeekBar csb;

    boolean bound = false;
    ServiceConnection sConn;
    Intent intent;
    MyService myService;
    int interval;
    boolean Flash=false;
    boolean Vibration=false;
    boolean Sound=false;
    boolean StartStop=false;
    boolean Indicator=false;
    NotificationManager notificationManagerInd;// = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    Notification builderInd;// = new Notification.Builder(this).build();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        csb=new CustomSeekBar(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        csb= (CustomSeekBar) findViewById(R.id.seekBar);
        setSeekControlListener();

        //final EditTextNumeric bpm = new EditTextNumeric(this);
        //bpm= (EditTextNumeric)findViewById(R.id.bpm);
        bpm= (EditText)findViewById(R.id.bpm);
        //bpm.setMaxValue(100);
        //bpm.setMinValue(0);

        intent = new Intent(this, MyService.class);
        sConn = new ServiceConnection() {

            public void onServiceConnected(ComponentName name, IBinder binder) {
                Log.d(LOG_TAG, "MainActivity onServiceConnected");
                myService = ((MyService.MyBinder) binder).getService();
                bound = true;
            }

            public void onServiceDisconnected(ComponentName name) {
                Log.d(LOG_TAG, "MainActivity onServiceDisconnected");
                bound = false;
            }
        };
    }

    private void setSeekControlListener() {
        csb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged = progress;
                bpm.setText(Integer.toString(progressChanged));
                Log.d(LOG_TAG, "onProgressChanged");
                if (StartStop == true)
                    if (progressChanged > 0) interval = myService.Interval(60000 / progressChanged);
                    else interval = myService.Interval(0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(MainActivity.this, "seek bar progress: " + progressChanged, Toast.LENGTH_SHORT)
                        .show();
                Log.d(LOG_TAG, "seek bar progress: " + progressChanged);
                bpm.setText(Integer.toString(progressChanged));

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //if(StartStop==false) {
            startService(intent);
            bindService(intent, sConn, 0);
        //}
    }



    @Override
    protected void onStop() {
        super.onStop();
        if (!bound) return;
        unbindService(sConn);
        bound = false;
        stopService(intent);
    }


    public void onToggleButtonStartStop (View button) {
        Toast.makeText(getApplicationContext(), Boolean.toString(((ToggleButton) button).isChecked()), Toast.LENGTH_SHORT).show();
        if(((ToggleButton) button).isChecked()==true) {
            StartStop=true;
            Log.d(LOG_TAG, "bpm.getText: " + Integer.parseInt(bpm.getText().toString())*600);

            if (!bound) return;
            if (Integer.parseInt(bpm.getText().toString())>0) interval = myService.Interval(60000/Integer.parseInt(bpm.getText().toString()));
            else interval = myService.Interval(0);
            //interval = myService.Interval(60000/Integer.parseInt(bpm.getText().toString()));
            //interval = myService.Interval(600);
        }
        else
        {
            StartStop=false;
            //stopService(intent);
            if (!bound) return;
            interval = myService.Interval(0);
        }
    }

    public void onToggleButtonFlash (View button) {
        Toast.makeText(getApplicationContext(), Boolean.toString(((ToggleButton) button).isChecked()),
                Toast.LENGTH_SHORT).show();
        if(((ToggleButton) button).isChecked()==true){
                Flash=true;
                myService.Module(Flash, Vibration, Sound, Indicator);
        }
        else
        {
            Flash=false;
            myService.Module(Flash, Vibration, Sound, Indicator);
        }
    }

    public void onToggleButtonVibration (View button) {
        Toast.makeText(getApplicationContext(), Boolean.toString(((ToggleButton) button).isChecked()),
                Toast.LENGTH_SHORT).show();
        if(((ToggleButton) button).isChecked()==true){
            Vibration=true;
            myService.Module(Flash, Vibration, Sound, Indicator);
        }
        else
        {
            Vibration=false;
            myService.Module(Flash, Vibration, Sound, Indicator);
        }
    }

    public void onToggleButtonSound (View button) {
        Toast.makeText(getApplicationContext(), Boolean.toString(((ToggleButton) button).isChecked()),
                Toast.LENGTH_SHORT).show();
        if(((ToggleButton) button).isChecked()==true){
            Sound=true;
            myService.Module(Flash, Vibration, Sound, Indicator);
        }
        else
        {
            Sound=false;
            myService.Module(Flash, Vibration, Sound, Indicator);
        }
    }

    public void onToggleButtonIndicator (View button) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Строим уведомление
        notificationManagerInd = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        builderInd = new Notification.Builder(this).build();
        Toast.makeText(getApplicationContext(), Boolean.toString(((ToggleButton) button).isChecked()),
                Toast.LENGTH_SHORT).show();
        if(((ToggleButton) button).isChecked()==true){
            //Indicator=true;
            //myService.Module(Flash, Vibration, Sound, Indicator);
            builderInd.flags = Notification.FLAG_SHOW_LIGHTS;
            builderInd.defaults|= Notification.DEFAULT_LIGHTS;
            notificationManagerInd.notify(2, builderInd);
        }
        else
        {
            //Indicator=false;
            //myService.Module(Flash, Vibration, Sound, Indicator);
            notificationManagerInd.cancelAll();
        }


    }
}


