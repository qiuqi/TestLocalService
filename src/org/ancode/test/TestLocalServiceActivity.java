package org.ancode.test;

import org.ancode.test.LocalService.UpdateListener;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class TestLocalServiceActivity extends Activity implements UpdateListener{
	private LocalService mBoundService;
	boolean mIsBound = false;
	private ServiceConnection mConnection = new ServiceConnection(){

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			// TODO Auto-generated method stub
			Log.v("test", "onServiceConnected 1");
			mBoundService = ((LocalService.LocalBinder)service).getService();
			registerListener();
			Log.v("test", "onServiceConnected 2");
		}

		@Override
		public void onServiceDisconnected(ComponentName className) {
			// TODO Auto-generated method stub
			mBoundService = null;			
		}
		
	};
	
	void registerListener(){
		Log.v("test", "registerListener");
		mBoundService.registerListener(this);
	}
	
	void doBindService(){
		Log.v("test", "Activity doBindService 1");
		bindService(new Intent(this,LocalService.class), mConnection, Context.BIND_AUTO_CREATE);
		Log.v("test", "Activity doBindService 2");
		mIsBound = true;
	}
	
	void doUnbindService(){
		Log.v("test", "Activity doUnBindService 1");
		if(mIsBound){
			if(mBoundService != null){
				mBoundService.unregisterListener(this);
			}
			unbindService(mConnection);
			mIsBound = false;
		}
		Log.v("test", "Activity doUnBindService 2");
	}
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Log.v("test", "start activity");
        this.startService(new Intent(this, LocalService.class));
		Log.v("test", "start activity1");
        doBindService();
        Log.v("test", "start activity2");
    }
    
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    	doUnbindService();
    }

	@Override
	public void onUpdate(long value) {
		// TODO Auto-generated method stub
		Log.d("test", "Activity update once");
	}
}