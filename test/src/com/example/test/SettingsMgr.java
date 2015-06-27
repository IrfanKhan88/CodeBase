package com.example.test;

import android.content.Context;

/**
 * @author Irfan
 *
 */
public class SettingsMgr {

	public static final String APP_PREFERENCE_DB = "app_preference";
	public static final String PREFERENCE_DB = "preference";
	public static final String TASK_RUNNING_STATUS = "taskRunningStatus";
	public static final String LAST_INDEX_EXECUTED = "lastIndex";

	
	/**
	 * @param mContext
	 * @param status
	 */
	public static void setTaskRunningStatus(Context mContext, boolean status){
		SharedPreferenceUtill.saveBooleanInSharedPref(mContext, PREFERENCE_DB, TASK_RUNNING_STATUS, status);
	}

	/**
	 * @param mContext
	 * @return
	 */
	public static boolean getTaskRuningStatus(Context mContext){
		boolean status = false;
		status = SharedPreferenceUtill.getBooleanFromSharedPref(mContext, PREFERENCE_DB, TASK_RUNNING_STATUS);
		return status;
	}

	/**
	 * @param mContext
	 * @param index last index where the app was tested for fault tolerance
	 */
	public static void setLastIndexRecord(Context mContext, int index){
		SharedPreferenceUtill.saveIntInSharedPref(mContext, PREFERENCE_DB, LAST_INDEX_EXECUTED, index);
	}

	/**
	 * @param mContext
	 * @return index - last index where the application was tested for fault tolerance 
	 */
	public static int getLastIndexRecord(Context mContext){
		int index = 0;
		index = SharedPreferenceUtill.getIntFromSharedPref(mContext, PREFERENCE_DB, LAST_INDEX_EXECUTED);
		return index;
	}


}
