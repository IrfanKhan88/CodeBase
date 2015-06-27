package com.example.test;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class GenUtils {
	
	private static String TAG = "GenUtils";
	
	/**
	 * Function to check internet connectivity
	 * */
	public static boolean isNetworkAvailable(Context mContext) {
		boolean connected = false;
		Log.d(TAG, "Checking whether internet connectivity avilable or not");
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
			if(networkInfo!=null){
				Log.d(TAG, "Network: "+networkInfo);
				if(networkInfo.isAvailable() && networkInfo.isConnected()){
					Log.d(TAG, "Network Details: "+networkInfo.isAvailable()+", "+networkInfo.isConnected());
					connected = true;
				}
			}
		} catch (Exception e) {
			System.out.println("CheckConnectivity Exception: " + e.getMessage());
			Log.d(TAG, e.toString());
		}
		Log.d(TAG, "Internet Connected: "+connected);
		return connected;
	}

}
