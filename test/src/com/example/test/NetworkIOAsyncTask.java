package com.example.test;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class NetworkIOAsyncTask {

	private String TAG = "NetworkIOAsyncTask";

	private Context mActivity;
	private static final String postURL= "http://agiotesting.appspot.com/save";
	private MyNetworkIOAsyncTask mNetworkAsyncTask = new MyNetworkIOAsyncTask();
	private static ProgressDialog progressDialog;

	private static ArrayList<String> latitudeList = new ArrayList<String>();
	private static ArrayList<String> longitudeList = new ArrayList<String>();
	private static ArrayList<String> locnameList = new ArrayList<String>();

	private static int lastIndexExecuted = 0;
	private static boolean dialogFlag = true; 

	public NetworkIOAsyncTask(Context mContext) {
		// TODO Auto-generated constructor stub
		mActivity = mContext;
	}

	public NetworkIOAsyncTask(ArrayList<String> location, ArrayList<String> latitude, 
			ArrayList<String> longitude, Context mContext){
		mActivity = mContext;
		locnameList = location;
		latitudeList = latitude;
		longitudeList = longitude;
	}





	/**
	 * Takes the starting index as parameter to 
	 * 		start sending httppost request
	 * @param indexToStartFrom
	 */
	protected void runAsyncTask(int indexToStartFrom){

		if(GenUtils.isNetworkAvailable(mActivity)){
			mNetworkAsyncTask.execute(String.valueOf(indexToStartFrom));
		}else{
			Toast.makeText(mActivity, "No Internet Connectivity", Toast.LENGTH_LONG).show();
		}

	}

	private class MyNetworkIOAsyncTask extends AsyncTask<String, Void, String>{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			if(dialogFlag){
				dialogFlag = false;
				if (android.os.Build.VERSION.SDK_INT >android.os.Build.VERSION_CODES.GINGERBREAD) {
					// only for gingerbread and newer versions
					((Activity) mActivity).invalidateOptionsMenu();
				}
				progressDialog = new ProgressDialog(mActivity);
				progressDialog.setMessage("Saving...");
				progressDialog.setIndeterminate(true);
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.setCancelable(false);
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
			latitudeList.clear();
			longitudeList.clear();
			locnameList.clear();
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
