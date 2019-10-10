package service.test.marees.marsservice;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class Myservice extends Service {


    public final IBinder bind = new MyBinder();
    private Handler mhandler;
    private int progress_data,maxValue;
    private boolean is_paused;

    public IBinder onBind(Intent intent) {
        return bind;

    }


    @Override
    public void onCreate() {
        super.onCreate();
        mhandler = new Handler();
        progress_data = 0;
        maxValue = 5000;
        is_paused = true;

    }

    public class MyBinder extends Binder{


        Myservice getService(){

        return Myservice.this;
        }


    }

    public void startPretendingLongrunningFakeTask(){



        final Runnable myRun = new Runnable() {
            @Override
            public void run() {

                if(progress_data >= maxValue || is_paused){


                    mhandler.removeCallbacks(this);

                    pausePretendingLongrunningFakeTask();

                }
                else{
                    progress_data += 100;
                    mhandler.postDelayed(this,100);
                }


            }
        };
mhandler.postDelayed(myRun,100);
    }


    public void pausePretendingLongrunningFakeTask(){

        is_paused = true;


    }

    public void unpausePretendingLongrunningFakeTask(){

        is_paused = false;
        startPretendingLongrunningFakeTask();

    }

    public boolean getisPause(){

        return is_paused;

    }

    public int getProgress_data(){

        return progress_data;

    }

    public int getMaxValue(){

        return maxValue;
    }



    public void resetProgress(){

        progress_data = 0;
    }

    public void onTaskRemoved(Intent back2Act){
        super.onTaskRemoved(back2Act);
        stopSelf();

    }

}
