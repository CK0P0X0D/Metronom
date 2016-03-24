package ua.ck0p0x0dstudio.metronom;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

public class MetronomService extends Service {


    public MetronomService() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate()
    {
        Toast.makeText(this, "Служба создана",
                Toast.LENGTH_SHORT).show();
        /*Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);//.TYPE_NOTIFICATION);
        mPlayer = MediaPlayer.create(this, alert);
        mPlayer.setVolume(100, 100);
        mPlayer.setLooping(false);*/





        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Строим уведомление
        Notification builder = new Notification.Builder(this).build();

        long[] vibrate = new long[]{0, 200, 0, 0, 0};  //vibration
        //builder.ledARGB=Color.GREEN;
   //     builder.ledOnMS = 1;
     //   builder.ledOffMS = 0;
       // builder.flags = Notification.FLAG_SHOW_LIGHTS;
          //builder.defaults|= Notification.DEFAULT_LIGHTS;
          //builder.defaults|= Notification.DEFAULT_ALL;
        builder.vibrate = vibrate;
        notificationManager.notify(1, builder);

        Log.d("log", "torch");
        Camera cam = Camera.open();
        Parameters p = cam.getParameters();
        p.setFlashMode(Parameters.FLASH_MODE_TORCH);
        cam.setParameters(p);
        cam.startPreview();
        p.setFlashMode(Parameters.FLASH_MODE_OFF);
        cam.setParameters(p);
        cam.startPreview();

        MediaPlayer mediaPlayer; //mp3
        mediaPlayer = MediaPlayer.create(this, R.raw.metronom1);
        mediaPlayer.start();

        try {
            TimeUnit.SECONDS.sleep(2); mediaPlayer.start();notificationManager.notify(1, builder);
            p.setFlashMode(Parameters.FLASH_MODE_TORCH);
            cam.setParameters(p);
            cam.startPreview();
            p.setFlashMode(Parameters.FLASH_MODE_OFF);
            cam.setParameters(p);
            cam.startPreview();

            TimeUnit.SECONDS.sleep(2); mediaPlayer.start();notificationManager.notify(1, builder);
            p.setFlashMode(Parameters.FLASH_MODE_TORCH);
            cam.setParameters(p);
            cam.startPreview();
            p.setFlashMode(Parameters.FLASH_MODE_OFF);
            cam.setParameters(p);
            cam.startPreview();

            TimeUnit.SECONDS.sleep(2); mediaPlayer.start();notificationManager.notify(1, builder);
            p.setFlashMode(Parameters.FLASH_MODE_TORCH);
            cam.setParameters(p);
            cam.startPreview();
            p.setFlashMode(Parameters.FLASH_MODE_OFF);
            cam.setParameters(p);
            cam.startPreview();

            TimeUnit.SECONDS.sleep(2); mediaPlayer.start();notificationManager.notify(1, builder);
            p.setFlashMode(Parameters.FLASH_MODE_TORCH);
            cam.setParameters(p);
            cam.startPreview();
            p.setFlashMode(Parameters.FLASH_MODE_OFF);
            cam.setParameters(p);
            cam.startPreview();

   //         builder = new Notification.Builder(this).build();
     //       builder.flags = Notification.DEFAULT_ALL;
       //     notificationManager.notify(1, builder);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        /*int LED_NOTIFICATION_ID = 0;
        //NotificationManager nm = ( NotificationManager ) getSystemService( NOTIFICATION_SERVICE );
        NotificationCompat.Builder notif = new NotificationCompat.Builder(this);
        notif.setLights(Color.GREEN,1,0);

        //notif.ledARGB = 0xFFffffff;
        //notif.flags = Notification.FLAG_SHOW_LIGHTS;
        //notif.ledOnMS = 1;
        //notif.ledOffMS = 1;


        //Notification notification = notif.build();
        //notification.defaults= Notification.DEFAULT_LIGHTS;

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(LED_NOTIFICATION_ID, notif);

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/


    }

    @Override
    public void onDestroy()
    {
        Toast.makeText(this, "Служба остановлена", Toast.LENGTH_SHORT).show();
        //mPlayer.stop();
    }
}
