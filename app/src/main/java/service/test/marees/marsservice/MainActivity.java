package service.test.marees.marsservice;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressbar;
    Button start_bt;
    TextView textView;

    private Myservice mservice;
    private MainActivityModel mymodel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressbar = findViewById(R.id.progressBar);
        start_bt  =findViewById(R.id.start_bt);
        textView = findViewById(R.id.textView);
        mymodel = ViewModelProviders.of(this).get(MainActivityModel.class);
        setObservers();
        start_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
toggleUpdates();
            }
        });


    }
    @Override
    protected void onResume() {
        super.onResume();
        startService();
    }
    public void startService(){
        Intent goSPD = new Intent(MainActivity.this,Myservice.class);
        startService(goSPD);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            this.startForegroundService(goSPD);
//        }

        bindService();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(mymodel.getBind() != null){
            unbindService(mymodel.getservice());
        }
    }
    public void bindService(){
        Intent ninjaStorm = new Intent(this,Myservice.class);
        bindService(ninjaStorm,mymodel.getservice(),Context.BIND_AUTO_CREATE);
    }
    private void toggleUpdates(){
        if(mservice != null){
            if(mservice.getProgress_data() == mservice.getMaxValue()){
                mservice.resetProgress();
                start_bt.setText("Start");
            }
            else{
                if(mservice.getisPause()){
                    mservice.unpausePretendingLongrunningFakeTask();
                    mymodel.setIsUpdating(true);


                }
                else{
                    mservice.pausePretendingLongrunningFakeTask();
                    mymodel.setIsUpdating(false);

                }

            }

        }

    }

    private View.OnClickListener myclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private void setObservers(){

        mymodel.getBind().observe(this, new Observer<Myservice.MyBinder>() {
            @Override
            public void onChanged(@Nullable Myservice.MyBinder myBinder) {
                if(myBinder != null){
                    mservice = myBinder.getService();
                }
                else{
                    mservice = null;
                }
            }
        });
        mymodel.getProgress().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable final Boolean aBoolean) {

                final Handler handler = new Handler();
                final Runnable run = new Runnable() {
                    @Override
                    public void run() {

                        if(aBoolean){
                            if(mymodel.getBind().getValue() != null){
                                if(mservice.getProgress_data() == mservice.getMaxValue()){
                                    mymodel.setIsUpdating(false);
                                }
                                progressbar.setProgress(mservice.getProgress_data());
                                progressbar.setMax(mservice.getMaxValue());{
                                    String Progress = String.valueOf(100*mservice.getProgress_data() / mservice.getMaxValue()) +"%";
                                    textView.setText(Progress);
                                    handler.postDelayed(this,100);

                                }
                            }
                        }
                        else{
                            handler.removeCallbacks(this);
                        }
                    }
                };

                if(aBoolean){

                    start_bt.setText("Pause");
                    handler.postDelayed(run,100);
                }
                else{

                    if(mservice.getProgress_data() == mservice.getMaxValue()){
                        start_bt.setText("Restart");
                    }
                    else{

                        start_bt.setText("Start");
                    }
                }
            }
        });
    }


}
