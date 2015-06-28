package com.example.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * @author Irfan
 *
 */
public class ConnectivityChangeReceiver extends BroadcastReceiver{

	Context mContext = null;
	private String TAG = "ConnectivityChangeReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		mContext = context;
		NetworkIOAsyncTask mObjMain;

		System.out.println("Inside Connectivity Change Broadcast Receiver: "+TAG);

		if(intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")){

			if(GenUtils.isNetworkAvailable(mContext) && SettingsMgr.getTaskRuningStatus(context)){
				Toast.makeText(mContext, "Network Available", Toast.LENGTH_SHORT).show();

				if(SettingsMgr.getTaskRuningStatus(context)){
					SettingsMgr.setTaskRunningStatus(context, false);
					mObjMain = new NetworkIOAsyncTask(context);
					if(SettingsMgr.getLastIndexRecord(context) == 0){
						mObjMain.runAsyncTask(SettingsMgr.getLastIndexRecord(context));
					}else{
						mObjMain.runAsyncTask(SettingsMgr.getLastIndexRecord(context) + 1);
					}
					Toast.makeText(mContext, "Last Index:"+SettingsMgr.getLastIndexRecord(context), Toast.LENGTH_SHORT).show();

					// Setting shared prefs variable to default values
					SettingsMgr.setLastIndexRecord(context, mObjMain.getLastExecutedIndex());
				}

			}else if(!GenUtils.isNetworkAvailable(mContext) && !SettingsMgr.getTaskRuningStatus(context)){
				Toast.makeText(mContext, "Network Unavailable", Toast.LENGTH_SHORT).show();

				mObjMain = new NetworkIOAsyncTask(context);
				if(mObjMain.isAsyncTaskRunning()){
					SettingsMgr.setTaskRunningStatus(context, true);
					SettingsMgr.setLastIndexRecord(context, mObjMain.getLastExecutedIndex());
					Toast.makeText(mContext, "Task Running :"+mObjMain.isAsyncTaskRunning(), Toast.LENGTH_SHORT).show();
				}
			}

		}
	}

}

