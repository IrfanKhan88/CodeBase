package com.example.test;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtill {

	public static void saveStringInSharedPref (Context mContext, String Key_db, String key_sub_db, String value){
		SharedPreferences prefs = mContext.getSharedPreferences(Key_db, Context.MODE_PRIVATE); 
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(key_sub_db,value);
		editor.commit();
	}

	public static String getStringFromSharedPref (Context mContext, String Key_db, String key_sub_db){
		String value = "";
		if (mContext != null) {
			SharedPreferences prefs = mContext.getSharedPreferences(Key_db, Context.MODE_PRIVATE);
			if (prefs != null) {
				value = prefs.getString(key_sub_db, null);
			}
		}
		return value;
	}

	public static void saveIntInSharedPref (Context mContext, String Key_db, String key_sub_db, int value){
		SharedPreferences prefs = mContext.getSharedPreferences(Key_db, Context.MODE_PRIVATE); 
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(key_sub_db,value);
		editor.commit();
	}

	public static int getIntFromSharedPref (Context mContext, String Key_db, String key_sub_db){
		int value = 0;
		if (mContext != null) {
			SharedPreferences prefs = mContext.getSharedPreferences(Key_db, Context.MODE_PRIVATE);
			if (prefs != null) {
				value = prefs.getInt(key_sub_db, 0);
			}
		}
		return value;
	}

	public static void saveBooleanInSharedPref(Context mContext, String Key_db, String key_sub_db, boolean status){
		SharedPreferences prefs = mContext.getSharedPreferences(Key_db,Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = prefs.edit();
		edit.putBoolean(key_sub_db , status);
		edit.commit();
	}

	public static boolean getBooleanFromSharedPref(Context mContext, String Key_db, String key_sub_db){
		boolean status = false;
		if (mContext != null) {
			SharedPreferences prefs = mContext.getSharedPreferences(Key_db,Context.MODE_PRIVATE);
			if (prefs != null) {
				status = prefs.getBoolean(key_sub_db , false);
			}
		}
		return status;
	}

	public static void clearAllSharedPref(Context mContext,String Key_db){
		SharedPreferences prefs = mContext.getSharedPreferences(Key_db , Context.MODE_PRIVATE); 
		SharedPreferences.Editor editor = prefs.edit();
		editor.clear();
		editor.commit();
	}

}
