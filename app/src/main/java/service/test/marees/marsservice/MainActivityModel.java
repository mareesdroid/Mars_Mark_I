package service.test.marees.marsservice;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class MainActivityModel extends ViewModel {

   private static final String tag = "MainActivitymodel";
   private MutableLiveData<Boolean> isProgressing = new MutableLiveData<>();
   private MutableLiveData<Myservice.MyBinder> mbinder = new MutableLiveData<>();




   private ServiceConnection servconnection = new ServiceConnection() {
          @Override
          public void onServiceConnected(ComponentName name, IBinder service) {

     Myservice.MyBinder binder = (Myservice.MyBinder) service;
     mbinder.postValue(binder);

      }

      @Override
      public void onServiceDisconnected(ComponentName name) {

         mbinder.postValue(null);

      }
   };


   public LiveData<Boolean> getProgress(){

      return isProgressing;

   }

   public LiveData<Myservice.MyBinder> getBind(){


      return mbinder;
   }


   public ServiceConnection getservice(){


      return servconnection;
   }

   public void setIsUpdating(Boolean isUpdating){

      isProgressing.postValue(isUpdating);
   }


}
