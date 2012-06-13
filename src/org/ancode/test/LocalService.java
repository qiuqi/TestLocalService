package org.ancode.test;

import java.util.ArrayList;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;


public class LocalService extends Service {
	
	private final ArrayList<UpdateListener> mListeners = new ArrayList<UpdateListener>();
	
	private long mTick = 0;
	private final Handler mHandler = new Handler();
	private final Runnable mTickRunnable = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			mTick ++;
			sendUpdate(mTick);
			mHandler.postDelayed(mTickRunnable, 1000);
			Log.v("test", ""+mTick);
		}
		
	};
	
	public class LocalBinder extends Binder{
		LocalService getService(){
			Log.v("test", "Binder");
			mTick = 0;
			mHandler.removeCallbacks(mTickRunnable);
			mHandler.post(mTickRunnable);
			return LocalService.this;
		}
	}
	
	private final IBinder mBinder = new LocalBinder();
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		Log.v("test", "Service onBind");
		return mBinder;
	}
	
	@Override
	public void onStart(Intent intent, int startId){
		super.onStart(intent, startId);
		Log.v("test", "Service onStart");

	}
	
	public int onStartCommand(Intent intent, int flags, int startId){
		Log.v("test", "service onStartCommand1");
		mTick = 0;
		mHandler.removeCallbacks(mTickRunnable);
		mHandler.post(mTickRunnable);
		Log.v("test", "service onStartCommand2");
		return START_STICKY;
	}
	
	public void onDestroy(){
		mHandler.removeCallbacks(mTickRunnable);
	}
	

	
	public interface UpdateListener{
		public void onUpdate(long value);
	}
	
	public void registerListener(UpdateListener listener){
		mListeners.add(listener);
	}
	
	public void unregisterListener(UpdateListener listener){
		mListeners.remove(listener);
	}
	
	private void sendUpdate(long value){
		for(int i=mListeners.size()-1;i>=0;i--){
			Log.v("test", "sendUpdate"+value);
			mListeners.get(i).onUpdate(value);
		}
	}

}
