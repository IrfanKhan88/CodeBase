package com.example.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


/**
 * @author Irfan
 *
 */
public class MainActivity extends Activity {

	private ArrayList<String> latitudeList = new ArrayList<String>();
	private ArrayList<String> longitudeList = new ArrayList<String>();
	private ArrayList<String> locnameList = new ArrayList<String>();
	private ArrayList<String> parentList = new ArrayList<String>();

	private Button mStartButton = null;
	private Activity mActivity = null;
	private String TAG = "MainActivity";

	private static final String CONNECTIVITY_CHANGE_ACTION							= 	"android.net.conn.CONNECTIVITY_CHANGE";

	private BroadcastReceiver	mNetworkChangeReceiver;
	private NetworkIOAsyncTask mNetworkAsyncTask;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mActivity = this;
		initReceivers();
		registerReceivers();
		mStartButton = (Button) findViewById(R.id.start_button);
		mStartButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TO DO
				parseTextFile();								// Parse the text file in Raw folder
				mNetworkAsyncTask= new NetworkIOAsyncTask(locnameList, latitudeList, longitudeList, mActivity);
				mNetworkAsyncTask.runAsyncTask(0);
				//runAsyncTask(lastIndexExecuted, mActivity);				
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceivers();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Initialize receivers
	 */
	private void initReceivers() {
		Log.d(TAG, "Initializing broadcast receivers");
		mNetworkChangeReceiver = new ConnectivityChangeReceiver();
	}

	/**
	 * Register all the receivers required by the service
	 */
	private void registerReceivers() {
		Log.d(TAG, "Registering broadcast receivers");
		registerNetworkChangeReceiver();
	}

	/**
	 * Unregister all the receivers required by the service
	 */
	private void unregisterReceivers() {
		Log.d(TAG, "Unregistering broadcast receivers");
		unregisterNetworkChangeReceiver();
	}

	/**
	 * Register and Unregister api for Network Change Broadcast
	 */
	private void registerNetworkChangeReceiver() {
		IntentFilter mNetworkChangeAction = new IntentFilter();
		mNetworkChangeAction.addAction(CONNECTIVITY_CHANGE_ACTION);
		mActivity.registerReceiver(mNetworkChangeReceiver, mNetworkChangeAction);
	}

	private void unregisterNetworkChangeReceiver() {
		if (mNetworkChangeReceiver != null) {
			unregisterReceiver(mNetworkChangeReceiver);
			mNetworkChangeReceiver = null;
		}
	}


	private void parseTextFile()
	{
		parentList = readFile();
		locnameList = getLocaltionArraylist(parentList);
		latitudeList = getLatitudeArraylist(parentList);
		longitudeList = getLongitudeArraylist(parentList);

		System.out.println("This is the Parent size list " +parentList.size());
		System.out.println("This is the Location size list " +locnameList.size());
		System.out.println("This is the Latitude size list " +latitudeList.size());
		System.out.println("This is the Longitude size list " +longitudeList.size());
	}

	private ArrayList<String> readFile() {
		try{
			BufferedReader buf = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.inputlatlong)));


			String lineJustFetched = null;
			String[] wordsArray;

			while(true){
				lineJustFetched = buf.readLine();
				if(lineJustFetched == null){  
					break; 
				}else{
					wordsArray = lineJustFetched.split("\t");
					for(String each : wordsArray){
						if(!"".equals(each)){
							if(!",".contains(each))
								each = each.substring(0, each.length()-2);
							parentList.add(each);
							System.out.println(each);
						}
					}
				}
			}
			/*	System.out.println("This is the final list "+locnameList);

			for(String each : locnameList){
				System.out.println(each);
			}*/
			buf.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return parentList;
	}

	private ArrayList<String> getLocaltionArraylist(ArrayList<String> totalList){
		String locName = null;
		for(int i = 0;i<totalList.size()-2;i++)	{
			if(i == 0){
				locName = totalList.get(i);
			}else{
				try {
					locName  = totalList.get(i+2);
					i = i+2;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			locnameList.add(locName);
			System.out.println("position is "+i +"Name is "+locName);
		}	
		return locnameList;
	}

	private ArrayList<String> getLatitudeArraylist(ArrayList<String> totalList){
		String latValue = null;
		for(int i = 1;i<totalList.size()-1;i++)	{
			if(i == 1){
				latValue = totalList.get(i);
			}else{
				try {
					latValue  = totalList.get(i+2);
					i = i+2;
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			latitudeList.add(latValue);
			System.out.println("position is "+i +"Latitude is "+latValue);

		}	

		return latitudeList;

	}

	private ArrayList<String> getLongitudeArraylist(ArrayList<String> totalList){
		String longValue = null;
		for(int i = 2;i<=totalList.size()-1;i++)	{
			if(i == 2){
				longValue = totalList.get(i);
			}else{
				try {
					longValue  = totalList.get(i+2);
					i = i+2;	
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			longitudeList.add(longValue);
			System.out.println("position is "+i +"longitude is "+longValue);

		}	

		return longitudeList;

	}

}
