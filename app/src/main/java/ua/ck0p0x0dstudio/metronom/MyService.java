package ua.ck0p0x0dstudio.metronom;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

    final String LOG_TAG = "myLogs";

    MyBinder binder = new MyBinder();

    Timer timer;
    TimerTask tTask;
    int interval;
    NotificationManager notificationManager;// = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    Notification builder, builderInd;// = new Notification.Builder(this).build();
    long[] vibrate = new long[]{0, 200, 0, 0, 0};  //vibration
    Camera cam;// = Camera.open();
    Parameters p;// = cam.getParameters();
    boolean Flash=false;
    boolean Vibration=false;
    boolean Sound=false;
    boolean Indicator=false;
    MediaPlayer mediaPlayer; //mp3
    boolean StartStop=false;


    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "MyService onCreate");
        timer = new Timer();

        if (StartStop==true) {
             Flash=false;
             Vibration=false;
             Sound=false;
             Indicator=false;
            StartStop=false;
            Log.d(LOG_TAG, "RESTART SERVICE");

        }

        if (cam == null) {
            try {
                cam = Camera.open();
                p = cam.getParameters();

            } catch (RuntimeException e) {
                Log.d(LOG_TAG, "Ошибка, невозможно запустить: ");
            }
        }


        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        builder = new Notification.Builder(this).build();
        builder.vibrate = vibrate;
        mediaPlayer = MediaPlayer.create(this, R.raw.metronom1);
        schedule();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    void schedule() {
      //         cam = Camera.open();
      //         p = cam.getParameters();

        if (tTask != null) tTask.cancel();
        if (interval > 0) {
            tTask = new TimerTask() {
                public void run() {
                    Log.d(LOG_TAG, "run");
                    if(Flash==true){
                        if (cam != null) {
                            try {
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    cam.setParameters(p);
                    cam.startPreview();
                    p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    cam.setParameters(p);
                    cam.startPreview();
                            } catch (RuntimeException e) {
                                Log.d(LOG_TAG, "Ошибка, невозможно запустить: ");
                            }
                        }
                    }

                    if(Vibration==true){
                        notificationManager.notify(1, builder);
                    }

                    if(Sound==true){
                        mediaPlayer.start();
                    }

                    if(Indicator==true) {//builder.ledARGB=Color.GREEN;
                        //builder.ledOnMS = 1;
                        //builder.ledOffMS = 0;
                        //builder.flags = Notification.FLAG_SHOW_LIGHTS;
                        //builder.defaults|= Notification.DEFAULT_LIGHTS;
                        //notificationManager.notify(1, builder);
                        }
                        else
                        {
                            //notificationManager.cancelAll();
                        }
                }
            };
            timer.schedule(tTask, 0, interval);
        }

    }

    int Interval(int gap) {
        interval = gap;
        schedule();
        return interval;
    }

    void Module(boolean FlashA, boolean VibrationA, boolean SoundA, boolean IndicatorA) {
        Flash=FlashA;
        Vibration=VibrationA;
        Sound=SoundA;
        Indicator=IndicatorA;
        schedule();
    }

    public IBinder onBind(Intent arg0) {
        Log.d(LOG_TAG, "MyService onBind");
        return binder;
    }

    class MyBinder extends Binder {
        MyService getService() {
            return MyService.this;
        }
    }
}

