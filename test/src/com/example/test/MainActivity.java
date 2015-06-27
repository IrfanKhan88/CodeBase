package com.example.test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


/**
 * @author Irfan
 *
 */
public class MainActivity extends Activity {

	private static ArrayList<String> latitudeList = new ArrayList<String>();
	private static ArrayList<String> longitudeList = new ArrayList<String>();
	private static ArrayList<String> locnameList = new ArrayList<String>();
	private static ArrayList<String> parentList = new ArrayList<String>();

	private Button mStartButton = null;
	private Activity mActivity = null;
	private String TAG = "MainActivity";

	private static final String postURL= "http://agiotesting.appspot.com/save";
	private static final String CONNECTIVITY_CHANGE_ACTION							= 	"android.net.conn.CONNECTIVITY_CHANGE";

	private BroadcastReceiver	mNetworkChangeReceiver;
	private NetworkIOAsyncTask mNetworkAsyncTask = new NetworkIOAsyncTask();
	private static ProgressDialog progressDialog;

	private static int lastIndexExecuted = 0;
	private static boolean dialogFlag = true; 


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
				runAsyncTask(lastIndexExecuted, mActivity);				
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

	/**
	 * Takes the starting index as parameter to 
	 * 		start sending httppost request
	 * @param indexToStartFrom
	 */
	protected void runAsyncTask(int indexToStartFrom, Context mContext){

		if(GenUtils.isNetworkAvailable(mContext)){
			mNetworkAsyncTask.execute(String.valueOf(indexToStartFrom));
		}else{
			Toast.makeText(getApplicationContext(), "No Internet Connectivity", Toast.LENGTH_LONG).show();
		}

	}

	private class NetworkIOAsyncTask extends AsyncTask<String, Void, String>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			if(dialogFlag){
				dialogFlag = false;
				if (android.os.Build.VERSION.SDK_INT >android.os.Build.VERSION_CODES.GINGERBREAD) {
					// only for gingerbread and newer versions
					mActivity.invalidateOptionsMenu();
				}
				progressDialog = new ProgressDialog(mActivity);
				progressDialog.setMessage("Saving...");
				progressDialog.setIndeterminate(true);
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.setIndeterminate(false);
				progressDialog.setMax(100);
				progressDialog.setProgress(0);
				progressDialog.show();
			}

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				postData(params[0]);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String result){
			// TO DO
			progressDialog.dismiss();
			dialogFlag = true;
		}

		/*protected void onProgressUpdate(Integer... progress){
			progressDialog.setProgress(progress[0]);
		}*/

		public void postData(String startingIndex) throws java.lang.InterruptedException {
			int responseCode = 0, startIndex = 0;
			String urlParameters = "";
			URL url;

			if(Integer.parseInt(startingIndex) != 0){
				startIndex = Integer.parseInt(startingIndex);
			}

			for(int index = startIndex; index<locnameList.size(); index++){

				if(isCancelled()){
					return;
				}

				try {
					url = new URL(postURL);
					HttpURLConnection connec = null;

					urlParameters = "name=" + URLEncoder.encode(locnameList.get(index),"UTF-8") +
							"&latitude="+ URLEncoder.encode(latitudeList.get(index),"UTF-8") +
							"&longitude="+ URLEncoder.encode(longitudeList.get(index),"UTF-8");

					connec = (HttpURLConnection) url.openConnection();

					//add reuqest header
					connec.setRequestMethod("POST");
					connec.setFixedLengthStreamingMode(urlParameters.getBytes().length);
					connec.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

					// Send post request
					connec.setDoOutput(true);

					DataOutputStream writer;
					writer = new DataOutputStream(connec.getOutputStream());
					writer.writeBytes(urlParameters);
					writer.flush();
					writer.close();
					responseCode = connec.getResponseCode();

					//publishProgress();

					if(index<locnameList.size()){
						lastIndexExecuted = index;
					}else{
						lastIndexExecuted = 0;
					}
				}
				catch (java.net.ConnectException netExp){
					Log.e(TAG, "Network has been disconnected !");
					mNetworkAsyncTask.cancel(true);
				}
				catch(java.net.SocketException socketExp){
					Log.e(TAG, "Network has been disconnected !");
				}
				catch (ProtocolException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				catch (MalformedURLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				catch (IOException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}

				System.out.println("\nPosition  : " + index);
				System.out.println("\nSending 'POST' request to URL : " + postURL);
				System.out.println("Post parameters : " + urlParameters);
				System.out.println("Response Code : " + responseCode);
			}
		}
	}

	/**
	 * Returns whether async task has completed or is currently running
	 * @return boolean 
	 */
	protected boolean isAsyncTaskRunning(){
		boolean isRunning = false;

		if(mNetworkAsyncTask.getStatus() == AsyncTask.Status.FINISHED){
			isRunning = false;
		}else if(mNetworkAsyncTask.getStatus() == AsyncTask.Status.RUNNING){
			isRunning = false;		
		}else if(mNetworkAsyncTask.getStatus() == AsyncTask.Status.PENDING){
			isRunning = true;
		}
		return isRunning;
	}

	/**
	 * returns the lastindex upto which the httppost request has been completed
	 * @return int
	 */
	protected int getLastExecutedIndex(){
		return lastIndexExecuted;
	}

}
